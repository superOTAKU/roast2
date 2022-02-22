package org.summer.roast.server.net;

import java.util.HashMap;
import java.util.Map;

public class DefaultRequestHandlerRegistry implements RequestHandlerRegistry {
    private final Map<Integer, RequestHandler> requestHandlerMap = new HashMap<>();

    @Override
    public void add(int requestCode, RequestHandler requestHandler) {
        requestHandlerMap.put(requestCode, requestHandler);
    }

    @Override
    public RequestHandler get(int requestCode) {
        return requestHandlerMap.get(requestCode);
    }

}
