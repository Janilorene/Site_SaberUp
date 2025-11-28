package service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GeminiService {

    private static String getApiKey() {
        String key = System.getenv("GEMINI_API_KEY");
        
        if (key == null || key.isEmpty()) {
            return "CHAVE_NAO_CONFIGURADA"; 
        }
        return key;
    }

    private static final String URL_BASE = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=";

    private final Gson gson = new Gson();
    private final HttpClient client = HttpClient.newHttpClient();

    public String corrigirQuestao(String textoAluno, String enunciadoOriginal) {
        String prompt = "Atue como um professor de matemática. \n" +
                        "Enunciado da questão: " + enunciadoOriginal + "\n" +
                        "Resolução do aluno (lida via OCR): \n" + textoAluno + "\n\n" +
                        "Tarefa: 1) Identifique se a resposta está correta. " +
                        "2) Se estiver errada, explique o erro. " +
                        "3) Seja breve e didático.";

        return enviarParaGemini(prompt);
    }

    public String explicarErro(String enunciado, String respostaAluno, String respostaCorreta) {
        String prompt = "Atue como um tutor de matemática. \n" +
                        "Questão: " + enunciado + "\n" +
                        "O aluno marcou a alternativa: " + respostaAluno + "\n" +
                        "A alternativa correta é: " + respostaCorreta + "\n" +
                        "Tarefa: Explique brevemente (máximo 2 frases) por que a alternativa do aluno está certa ou errada.";

        return enviarParaGemini(prompt);
    }

    private String enviarParaGemini(String prompt) {
        try {
            JsonObject textPart = new JsonObject();
            textPart.addProperty("text", prompt);

            JsonArray parts = new JsonArray();
            parts.add(textPart);

            JsonObject content = new JsonObject();
            content.add("parts", parts);

            JsonArray contents = new JsonArray();
            contents.add(content);

            JsonObject body = new JsonObject();
            body.add("contents", contents);

            String jsonBody = gson.toJson(body);

            String urlCompleta = URL_BASE + getApiKey();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlCompleta))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                System.err.println("ERRO NA API GEMINI (Status " + response.statusCode() + "):");
                System.err.println(response.body());
                return "Erro ao consultar a inteligência artificial.";
            }

            JsonObject respJson = gson.fromJson(response.body(), JsonObject.class);
            
            if (respJson.has("candidates") && respJson.getAsJsonArray("candidates").size() > 0) {
                return respJson.getAsJsonArray("candidates")
                        .get(0).getAsJsonObject()
                        .getAsJsonObject("content")
                        .getAsJsonArray("parts")
                        .get(0).getAsJsonObject()
                        .get("text").getAsString();
            } else {
                return "A IA não retornou nenhuma resposta de texto.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erro interno no servidor: " + e.getMessage();
        }
    }
}