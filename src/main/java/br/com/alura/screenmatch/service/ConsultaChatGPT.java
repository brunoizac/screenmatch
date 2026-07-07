package br.com.alura.screenmatch.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ConsultaChatGPT {
    public static String obterTraducao(String texto) {
        try {
            // Codifica o texto da sinopse de forma segura
            String textoCodificado = URLEncoder.encode(texto, StandardCharsets.UTF_8);

            // CORREÇÃO AQUI: Codificamos também o par de idiomas "en|pt-br" para evitar o erro de caractere ilegal
            String langpairCodificado = URLEncoder.encode("en|pt-br", StandardCharsets.UTF_8);

            // Monta a URL com os parâmetros totalmente protegidos e válidos
            String url = "https://api.mymemory.translated.net/get?q=" + textoCodificado + "&langpair=" + langpairCodificado;

            // Cria o cliente HTTP do Java moderno
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            // Envia a requisição
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Usa o ObjectMapper do Jackson para ler a resposta JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.body());

            // Pega o texto traduzido de dentro da estrutura do JSON deles
            String traducao = rootNode.path("responseData").path("translatedText").asText();

            return traducao.trim();

        } catch (Exception e) {
            // Caso dê algum erro na rede, ele retorna a sinopse original em inglês para não quebrar o programa
            System.out.println("Erro ao traduzir: " + e.getMessage());
            return texto;
        }
    }
}