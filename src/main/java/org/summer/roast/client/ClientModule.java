package org.summer.roast.client;

import com.google.inject.AbstractModule;
import org.summer.roast.protocol.JSONRemoteObjectCodec;
import org.summer.roast.protocol.RemoteObjectCodec;

public class ClientModule extends AbstractModule {

    static ClientConfig provideClientConfig() {
        ClientConfig config = new ClientConfig();
        config.setHost("localhost");
        config.setPort(80);
        return config;
    }

    @Override
    protected void configure() {
        bind(RemoteObjectCodec.class).to(JSONRemoteObjectCodec.class);
    }
}
