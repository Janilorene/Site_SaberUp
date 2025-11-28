package controller;

import com.google.gson.Gson;
import service.AzureOCRService;
import service.GeminiService;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import java.io.InputStream;
import java.util.Map;

public class AnaliseController {
    private AzureOCRService ocrService = new AzureOCRService();
    private GeminiService geminiService = new GeminiService();
    private Gson gson = new Gson();

    public Object analisar(Request req, Response res) {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement(System.getProperty("java.io.tmpdir")));
        res.type("application/json");

        try {
            String enunciado = req.queryParams("enunciado");
            String respAlunoTexto = req.queryParams("respostaAluno");
            String respCorretaTexto = req.queryParams("respostaCorreta");

            javax.servlet.http.Part filePart = null;
            try { filePart = req.raw().getPart("imagem"); } catch (Exception ignored) {}
            
            boolean temImagem = (filePart != null && filePart.getSize() > 0);
            String feedback;
            String textoLido = "";

            if (temImagem) {
                try (InputStream is = filePart.getInputStream()) {
                    byte[] bytes = is.readAllBytes();
                    textoLido = ocrService.extrairTexto(bytes);
                    
                    if (textoLido != null) {
                        feedback = geminiService.corrigirQuestao(textoLido, enunciado);
                    } else {
                        feedback = "Não consegui ler a imagem. " + geminiService.explicarErro(enunciado, respAlunoTexto, respCorretaTexto);
                    }
                }
            } else {
                feedback = geminiService.explicarErro(enunciado, respAlunoTexto, respCorretaTexto);
            }

            return gson.toJson(Map.of(
                "temImagem", temImagem,
                "texto_ocr", textoLido != null ? textoLido : "",
                "feedback", feedback
            ));

        } catch (Exception e) {
            e.printStackTrace();
            res.status(500);
            return gson.toJson(Map.of("erro", e.getMessage()));
        }
    }
}