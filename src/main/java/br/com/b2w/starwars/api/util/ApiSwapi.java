package br.com.b2w.starwars.api.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApiSwapi {

    public ApiSwapi() {
    }

    public JsonObject solicitarRequisicao(String nome) throws Exception {
        HttpGet httpGet;
        if (nome == null) {
            httpGet = new HttpGet("https://swapi.co/api/planets" +  "/");
        } else {
            httpGet = new HttpGet("https://swapi.co/api/planets"  + "/?search=" + nome);
        }
        return montarRequisicao(httpGet);
    }

    private JsonObject montarRequisicao(HttpGet getRequest) throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().build();
        getRequest.addHeader("accept", "application/json");
        HttpResponse response = httpClient.execute(getRequest);

        if (response.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("Falha : HTTP error code : "
                    + response.getStatusLine().getStatusCode());
        }

        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader((response.getEntity().getContent())));

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }

        JsonObject jsonObject = deserialize(stringBuilder.toString());
        bufferedReader.close();

        return jsonObject;
    }

    private JsonObject deserialize(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JsonObject.class);
    }

    public JsonObject criarRequisicao(String uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        return montarRequisicao(httpGet);
    }
}
