package org.summer.roast.server.net;

import com.google.inject.Inject;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;
import org.summer.roast.common.net.MessageCodec;
import org.summer.roast.server.ServerConfig;
import org.summer.roast.server.ServerState;

import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class NettyServerBootstrap {
    @Inject
    private MessageCodec messageCodec;
    @Inject
    private SessionHandler sessionHandler;
    @Inject
    private BusinessHandler businessHandler;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private final AtomicReference<ServerState> state = new AtomicReference<>(ServerState.INIT);
    @Inject
    private ServerConfig config;
    private Channel channel;

    public void start() {
        if (!state.compareAndSet(ServerState.INIT, ServerState.STARTING)) {
            throw new IllegalStateException();
        }
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup(4);
        ServerBootstrap bootstrap = new ServerBootstrap();
        ChannelFuture bindFuture = bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4));
                        p.addLast(messageCodec)
                                .addLast(sessionHandler)
                                .addLast(businessHandler);
                    }
                })
                .bind(config.getHost(), config.getPort())
                .syncUninterruptibly();
        channel = bindFuture.channel();
        log.info("server started at {}:{}", config.getHost(), config.getPort());
        state.set(ServerState.RUNNING);
    }

}
