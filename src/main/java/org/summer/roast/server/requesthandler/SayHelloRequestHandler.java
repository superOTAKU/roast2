package org.summer.roast.server.requesthandler;

import lombok.extern.slf4j.Slf4j;
import org.summer.roast.server.net.Request;
import org.summer.roast.server.net.RequestHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SayHelloRequestHandler implements RequestHandler {

    @Override
    public void handle(Request request) {
        log.info("from client {}", request.getRemoteObject().getData());
        Map<String, Object> data = new HashMap<>();
        data.put("data", "hello, " + request.getRemoteObject().getData().get("name"));
        request.replyOk(data);
    }

}
