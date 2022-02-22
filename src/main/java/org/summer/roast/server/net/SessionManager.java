package org.summer.roast.server.net;

import io.netty.channel.ChannelHandlerContext;

public interface SessionManager {

    void connected(ChannelHandlerContext ctx);

    void disConnected(ChannelHandlerContext ctx);

}
