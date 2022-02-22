package org.summer.roast.server.net;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SessionHandler extends ChannelInboundHandlerAdapter {
    @Inject
    private SessionManager sessionManager;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        sessionManager.connected(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        sessionManager.disConnected(ctx);
    }
}
