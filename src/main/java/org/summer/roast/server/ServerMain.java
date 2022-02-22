package org.summer.roast.server;

import com.google.inject.Guice;
import org.summer.roast.server.net.NettyServerBootstrap;

public class ServerMain {

    public static void main(String[] args) {
        NettyServerBootstrap bootstrap = Guice.createInjector(new ServerModule()).getInstance(NettyServerBootstrap.class);
        bootstrap.start();
    }

}
