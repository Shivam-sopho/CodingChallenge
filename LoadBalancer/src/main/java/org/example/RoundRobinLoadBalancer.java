package org.example;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinLoadBalancer implements LoadBalancer {
    private final List<BackendServer> servers = new CopyOnWriteArrayList<>();
    private AtomicInteger index = new AtomicInteger(0);

    @Override
    public BackendServer getNextServer() {
        if (servers.isEmpty()) {
            return null;
        }
        int i = index.getAndUpdate(n -> (n + 1) % servers.size());
        return servers.get(i);
    }

    @Override
    public void addServer(BackendServer server) {
        servers.add(server);
    }

    @Override
    public void removeServer(BackendServer server) {
        servers.remove(server);
    }

    @Override
    public List<BackendServer> getServers() {
        return servers;
    }
}

