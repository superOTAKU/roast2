package org.summer.roast.server.net;

import io.netty.channel.ChannelHandlerContext;
import org.summer.roast.protocol.RemoteObject;

/**
 * 封装一个请求，提供回消息等便捷的方法
 */
public class Request {
    private final ChannelHandlerContext ctx;
    private final RemoteObject remoteObject;

    public Request(ChannelHandlerContext ctx, RemoteObject remoteObject) {
        this.ctx = ctx;
        this.remoteObject = remoteObject;
    }

}
