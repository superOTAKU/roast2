package org.summer.roast.server.net;

import cn.hutool.core.thread.NamedThreadFactory;
import com.google.inject.Inject;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.summer.roast.protocol.ErrorCode;
import org.summer.roast.protocol.RemoteObject;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 完全异步的消息处理，同一个用户的请求将乱序交由业务线程池并发地进行处理，因此特别要注意线程安全问题
 */
@Slf4j
public class AsyncRemoteObjectHandler implements RemoteObjectHandler {
    private ExecutorService requestService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2,
            new NamedThreadFactory("request-", true));
    private ExecutorService responseService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
            new NamedThreadFactory("response-", true));
    @Inject
    private RequestHandlerRegistry requestHandlerRegistry;

    private class RequestRunner implements Runnable {
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
                RequestHandler requestHandler = requestHandlerRegistry.get(remoteObject.getCode());
                if (requestHandler == null) {
                    send(ctx, RemoteObject.errorResponse(remoteObject.getClientRequestId(), ErrorCode.REQUEST_HANDLER_NOT_FOUND));
                    return;
                }
                requestHandler.handle(new Request(ctx, remoteObject, AsyncRemoteObjectHandler.this));
            } catch (Exception e) {
                log.error("channel {} process msg {} fail", ctx.channel(), remoteObject.getCode(), e);
            }
        }
    }

    @Override
    public void receive(ChannelHandlerContext ctx, RemoteObject remoteObject) {
        //TODO 对于这种长连接来说，首包的处理很关键，比如首包可能要登录
        requestService.execute(new RequestRunner(ctx, remoteObject));
    }

    @Override
    public void send(ChannelHandlerContext ctx, RemoteObject remoteObject) {
        //回消息给客户端
        responseService.execute(() -> ctx.writeAndFlush(remoteObject));
    }

}
