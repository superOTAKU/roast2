package org.summer.roast.client;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.summer.roast.client.net.ResponseFutureHolder;
import org.summer.roast.protocol.JSONRemoteObjectCodec;
import org.summer.roast.protocol.RemoteObjectCodec;

public class ClientModule extends AbstractModule {

    @Provides
    ClientConfig provideClientConfig() {
        ClientConfig config = new ClientConfig();
        config.setHost("localhost");
        config.setPort(80);
        return config;
    }

    @Override
    protected void configure() {
        bind(RemoteObjectCodec.class).to(JSONRemoteObjectCodec.class);
        bind(ResponseFutureHolder.class).toInstance(new ResponseFutureHolder());
    }
}
