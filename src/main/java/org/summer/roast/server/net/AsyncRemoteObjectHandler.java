package org.summer.roast.server.net;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.summer.roast.protocol.RemoteObject;

import java.util.concurrent.ExecutorService;

/**
 * 完全异步的消息处理，同一个用户的请求将乱序交由业务线程池并发地进行处理，因此特别要注意线程安全问题
 */
public class AsyncRemoteObjectHandler implements RemoteObjectHandler {
    private ExecutorService requestService;
    private ExecutorService responseService;

    @Slf4j
    private static class RequestRunner implements Runnable {
        final ChannelHandlerContext ctx;
        final RemoteObject remoteObject;
        RequestRunner(ChannelHandlerContext ctx, RemoteObject remoteObject) {
            this.ctx = ctx;
            this.remoteObject = remoteObject;
        }

        @Override
        public void run() {
            try {
                //将请求分发给业务逻辑层
            } catch (Exception e) {
                log.error("channel {} process msg {} fail", ctx.channel(), remoteObject.getCode(), e);
            }
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, RemoteObject remoteObject) {
        requestService.execute(new RequestRunner(ctx, remoteObject));
    }

    @Override
    public void send(ChannelHandlerContext ctx, RemoteObject remoteObject) {
        //回消息给客户端
        responseService.execute(() -> ctx.writeAndFlush(remoteObject));
    }

}
