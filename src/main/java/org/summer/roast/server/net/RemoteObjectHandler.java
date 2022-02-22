package org.summer.roast.server.net;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import org.summer.roast.protocol.RemoteObject;

/**
 * 封装RemoteObject的处理过程
 *
 * 对于请求，这里执行请求的处理；对于响应，这里主要是回消息给客户端
 *
 * 封装的目的：是否将请求派发到线程池处理，以及是否1个user对于1个线程处理，可以延迟到这里决定
 *
 */
public interface RemoteObjectHandler {

    /**
     * 接收请求
     */
    void receive(ChannelHandlerContext ctx, RemoteObject remoteObject);

    /**
     * 回消息给客户端
     */
    void send(ChannelHandlerContext ctx, RemoteObject remoteObject);

}
