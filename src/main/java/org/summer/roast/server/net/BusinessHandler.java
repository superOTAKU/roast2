package org.summer.roast.server.net;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.summer.roast.protocol.RemoteObject;


@ChannelHandler.Sharable
@Slf4j
public class BusinessHandler extends SimpleChannelInboundHandler<RemoteObject> {
    @Inject
    private RemoteObjectHandler remoteObjectHandler;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemoteObject remoteObject) {
        remoteObjectHandler.receive(ctx, remoteObject);
    }
}
