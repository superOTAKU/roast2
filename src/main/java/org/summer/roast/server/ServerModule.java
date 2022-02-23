package org.summer.roast.server;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.summer.roast.protocol.JSONRemoteObjectCodec;
import org.summer.roast.protocol.RemoteObjectCodec;
import org.summer.roast.protocol.RequestCode;
import org.summer.roast.server.net.*;
import org.summer.roast.server.requesthandler.SayHelloRequestHandler;

public class ServerModule extends AbstractModule {

    @Provides
    ServerConfig provideServerConfig() {
        ServerConfig config = new ServerConfig();
        config.setHost("0.0.0.0");
        config.setPort(80);
        return config;
    }

    @Provides
    DefaultRequestHandlerRegistry provideRequestHandlerRegistry() {
        DefaultRequestHandlerRegistry requestHandlerRegistry = new DefaultRequestHandlerRegistry();
        requestHandlerRegistry.add(RequestCode.SAY_HELLO, provideSayHelloRequestHandler());
        return requestHandlerRegistry;
    }

    @Provides
    SayHelloRequestHandler provideSayHelloRequestHandler() {
        return new SayHelloRequestHandler();
    }

    @Override
    protected void configure() {
        bind(RemoteObjectCodec.class).to(JSONRemoteObjectCodec.class);
        bind(SessionManager.class).to(DefaultSessionManager.class);
        bind(RequestHandlerRegistry.class).to(DefaultRequestHandlerRegistry.class);
        bind(RemoteObjectHandler.class).to(AsyncRemoteObjectHandler.class);
    }
}
