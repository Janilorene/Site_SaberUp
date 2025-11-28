package service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AzureOCRService {

    private static String getAzureKey() {
        String key = System.getenv("AZURE_KEY");
        if (key == null || key.isEmpty()) {
            return "CHAVE_NAO_CONFIGURADA";
        }
        return key;
    }

    private static String getAzureEndpoint() {
        String endpoint = System.getenv("AZURE_ENDPOINT");
        if (endpoint == null || endpoint.isEmpty()) {
            return "ENDPOINT_NAO_CONFIGURADO";
        }
        return endpoint.endsWith("/") ? endpoint : endpoint + "/";
    }

    private final HttpClient client;
    private final Gson gson;

    public AzureOCRService() {
        this.client = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public String extrairTexto(byte[] imagemBytes) {
        try {
            String endpoint = getAzureEndpoint();
            String url = endpoint + "vision/v3.2/read/analyze";
            
            String key = getAzureKey();

            // 1. Enviar Imagem
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Ocp-Apim-Subscription-Key", key)
                .header("Content-Type", "application/octet-stream")
                .POST(HttpRequest.BodyPublishers.ofByteArray(imagemBytes))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 202) {
                System.err.println("Erro Azure (Envio): " + response.body());
                return null;
            }

            // 2. Pegar a URL de operação 
            String operationLocation = response.headers().firstValue("Operation-Location").orElse(null);
            
            if (operationLocation == null) return null;

            return aguardarResultado(operationLocation, key);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String aguardarResultado(String url, String key) throws Exception {
        int tentativas = 0;
        while (tentativas < 10) {
            Thread.sleep(1000); 

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Ocp-Apim-Subscription-Key", key)
                .GET()
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JsonObject json = gson.fromJson(response.body(), JsonObject.class);
            
            String status = "";
            if (json.has("status")) {
                status = json.get("status").getAsString();
            }

            if ("succeeded".equals(status)) {
                return parsearTexto(json);
            } else if ("failed".equals(status)) {
                return null;
            }
            tentativas++;
        }
        return null; 
    }

    private String parsearTexto(JsonObject json) {
        StringBuilder textoCompleto = new StringBuilder();
        try {
            JsonObject analyzeResult = json.getAsJsonObject("analyzeResult");
            JsonArray readResults = analyzeResult.getAsJsonArray("readResults");

            for (JsonElement page : readResults) {
                JsonArray lines = page.getAsJsonObject().getAsJsonArray("lines");
                for (JsonElement line : lines) {
                    textoCompleto.append(line.getAsJsonObject().get("text").getAsString()).append("\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return textoCompleto.toString();
    }
}