package org.example;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestHandler extends AbstractHandler {
    private final LoadBalancer loadBalancer;
    private final OkHttpClient client = new OkHttpClient();

    public RequestHandler(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public void handle(String s, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {

        BackendServer server = loadBalancer.getNextServer();
        if (server == null || !server.isHealthy()) {
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            response.getWriter().write("No backend servers available");
            baseRequest.setHandled(true);
            return;
        }

        Request proxyRequest = new Request.Builder()
                .url(server.getUrl() + s)
                .build();
        try (Response proxyResponse = client.newCall(proxyRequest).execute()) {
            response.setStatus(proxyResponse.code());
            assert proxyResponse.body() != null;
            response.getWriter().write(proxyResponse.body().toString());
        }
        baseRequest.setHandled(true);
    }
}

