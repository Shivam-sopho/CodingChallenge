package org.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpHealthCheaker implements HealthChecker {
    private final OkHttpClient client = new OkHttpClient();

    @Override
    public void checkHealth(BackendServer server) {
        Request request = new Request.Builder().url(server.getUrl()).build();
        try (Response response = client.newCall(request).execute()) {
            server.setHealthy(response.isSuccessful());
        } catch (IOException e) {
            server.setHealthy(false);
        }
    }
}

