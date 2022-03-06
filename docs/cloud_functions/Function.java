package com.example;

import com.google.cloud.functions.HttpFunction;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.logging.Logger;

public class Example implements HttpFunction {
    private static final Logger logger = Logger.getLogger(Example.class.getName());

    private static final Gson gson = new Gson();

    @Override
    public void service(com.google.cloud.functions.HttpRequest request, com.google.cloud.functions.HttpResponse response)
            throws IOException, URISyntaxException, InterruptedException {
        String key = request.getFirstQueryParameter("key").orElse("");

        HttpRequest googleMapsRequest = HttpRequest.newBuilder()
                .uri(new URI("https://maps.googleapis.com/maps/api/distancematrix/json?destinations=06065430&origins=90010311&key=AIzaSyADqGFdnIn4flWazYBtDR12db0K1b6_8Ag"))
                .GET()
                .build();

        HttpResponse googleMapsresponse = HttpClient.newBuilder().build().send(googleMapsRequest, HttpResponse.BodyHandlers.ofString());

        var writer = new PrintWriter(response.getWriter());
        writer.print(googleMapsresponse.body().toString());
    }
}