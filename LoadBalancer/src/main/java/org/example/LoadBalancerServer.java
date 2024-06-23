package org.example;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LoadBalancerServer {
    public static void main(String[] args) throws Exception {
        LoadBalancer loadBalancer = new RoundRobinLoadBalancer();
        loadBalancer.addServer(new BackendServer("http://localhost:8080"));
        loadBalancer.addServer(new BackendServer("http://localhost:8081"));

        HealthChecker healthChecker = new HttpHealthCheaker();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> loadBalancer.getServers().forEach(healthChecker::checkHealth), 0, 10, TimeUnit.SECONDS);

        Server server = new Server(80);
        ServletContextHandler handler = new ServletContextHandler();
        handler.setHandler(new RequestHandler(loadBalancer));
        server.setHandler(handler);

        server.start();
        server.join();
    }
}

