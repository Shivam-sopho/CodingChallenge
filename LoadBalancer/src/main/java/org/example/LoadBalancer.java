package org.example;


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public interface LoadBalancer {
    BackendServer getNextServer();
    void addServer(BackendServer server);
    void removeServer(BackendServer server);
    List<BackendServer> getServers();


}
