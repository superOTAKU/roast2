package org.summer.roast.server.net;

public interface RequestHandlerRegistry {

    void add(int requestCode, RequestHandler requestHandler);

    RequestHandler get(int requestCode);

}
