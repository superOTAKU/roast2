package org.summer.roast.client.net;

import cn.hutool.core.lang.Snowflake;
import com.google.inject.Inject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.summer.roast.client.ClientConfig;
import org.summer.roast.common.net.MessageCodec;
import org.summer.roast.protocol.RemoteObject;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ClientBootstrap {
    @Inject
    private ResponseHandler responseHandler;
    @Inject
    private MessageCodec messageCodec;
    @Inject
    private ResponseFutureHolder responseFutureHolder;
    private NioEventLoopGroup group;
    private Channel channel;
    private final AtomicReference<ClientState> state = new AtomicReference<>(ClientState.INIT);
    @Inject
    private ClientConfig config;
    private final AtomicInteger requestIdGenerator = new AtomicInteger();

    public void start() {
        if (!state.compareAndSet(ClientState.INIT, ClientState.CONNECTING)) {
            throw new IllegalStateException();
        }
        group = new NioEventLoopGroup(1);
        Bootstrap bootstrap = new Bootstrap();
        ChannelFuture connectFuture = bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
                        p.addLast(messageCodec, responseHandler);
                    }
                })
                .connect(config.getHost(), config.getPort())
                .syncUninterruptibly();
        channel = connectFuture.channel();
        log.info("connect to server {}:{}", config.getHost(), config.getPort());
        state.set(ClientState.CONNECTED);
    }

    public RemoteObject sendForResponse(int requestCode, Map<String, Object> data) {
        RemoteObject request = RemoteObject.request(requestCode, requestIdGenerator.incrementAndGet(), data);
        CompletableFuture<RemoteObject> responseFuture = responseFutureHolder.addAndGet(request.getClientRequestId());
        channel.writeAndFlush(request);
        try {
            return responseFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
