package org.summer.roast.client.net;

import lombok.extern.slf4j.Slf4j;
import org.summer.roast.protocol.RemoteObject;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ResponseFutureHolder {
    private final Map<Integer, CompletableFuture<RemoteObject>> responseFutureMap = new ConcurrentHashMap<>();

    public CompletableFuture<RemoteObject> addAndGet(int clientRequestId) {
        CompletableFuture<RemoteObject> future = new CompletableFuture<>();
        responseFutureMap.put(clientRequestId, future);
        return future;
    }

    public void completeFuture(RemoteObject response) {
        CompletableFuture<RemoteObject> future = responseFutureMap.remove(response.getClientRequestId());
        if (future == null) {
            log.warn("response future not found for requestId {}", response.getClientRequestId());
            return;
        }
        future.complete(response);
    }

}
