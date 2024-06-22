package com.alura.literalura;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Consulta {

    public ApiResponse buscarDados(String termoBusca) {

        // Criar o HttpClient
//        HttpClient client = HttpClient.newHttpClient();
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();

//        String url = "https://gutendex.com/books?" + termoBusca;
        String url = termoBusca;

        // Criar a requisição
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = null;
        try {
            // Enviar a requisição e receber a resposta
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e.getMessage());
        }

        // Verificar o status da resposta
        int statusCode = response.statusCode();
        System.out.println("Status Code: " + statusCode);

        if (statusCode != 200) {
            throw new RuntimeException("Failed to fetch data. HTTP status code: " + statusCode);
        }

        // Obter o corpo da resposta
        String responseBody = response.body();
        System.out.println("Response Body: " + responseBody);

//        JsonParser parser = new JsonParser();
//        JsonObject jsonObject = parser.parse(response.body()).getAsJsonObject();
//        return jsonObject;

        //Passar a resposta JSOn usando Jackson
        ObjectMapper objectMapper = new ObjectMapper();
        ApiResponse apiResponse = null;
        try{
            apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        return apiResponse;
    }

}
