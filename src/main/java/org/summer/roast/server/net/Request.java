package org.summer.roast.server.net;

import io.netty.channel.ChannelHandlerContext;
import org.summer.roast.protocol.RemoteObject;

import java.rmi.Remote;
import java.util.Map;

/**
 * 封装一个请求，提供回消息等便捷的方法
 */
public class Request {
    private final ChannelHandlerContext ctx;
    private final RemoteObject remoteObject;
    private final RemoteObjectHandler remoteObjectHandler;

    public Request(ChannelHandlerContext ctx, RemoteObject remoteObject, RemoteObjectHandler remoteObjectHandler) {
        this.ctx = ctx;
        this.remoteObject = remoteObject;
        this.remoteObjectHandler = remoteObjectHandler;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public RemoteObject getRemoteObject() {
        return remoteObject;
    }

    public void replyOk(Map<String, Object> data) {
        remoteObjectHandler.send(ctx, RemoteObject.okResponse(remoteObject.getClientRequestId(), data));
    }
}
