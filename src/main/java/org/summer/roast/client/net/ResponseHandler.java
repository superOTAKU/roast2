package org.summer.roast.client.net;

import com.google.inject.Inject;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.summer.roast.protocol.RemoteObject;
import org.summer.roast.protocol.RemoteObjectType;

@ChannelHandler.Sharable
@Slf4j
public class ResponseHandler extends SimpleChannelInboundHandler<RemoteObject> {
    @Inject
    private ResponseFutureHolder responseFutureHolder;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RemoteObject msg) throws Exception {
        if (msg.getType() == RemoteObjectType.RESPONSE) {
            responseFutureHolder.completeFuture(msg);
        } else {
            log.info("receive non RESPONSE type object from server, type: {}", msg.getType());
        }
    }

}
