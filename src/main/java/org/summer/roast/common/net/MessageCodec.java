package org.summer.roast.common.net;

import com.google.inject.Inject;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.summer.roast.protocol.RemoteObject;
import org.summer.roast.protocol.RemoteObjectCodec;

import java.util.List;

/**
 * 消息编解码逻辑，委托到我们的自定义接口，以适配不同的网络层框架
 */
public class MessageCodec extends MessageToMessageCodec<ByteBuf, RemoteObject> {
    @Inject
    private RemoteObjectCodec remoteObjectCodec;

    @Override
    protected void encode(ChannelHandlerContext ctx, RemoteObject msg, List<Object> out) {
        byte[] data = remoteObjectCodec.encode(msg);
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(data);
        out.add(buffer);

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        byte[] data = new byte[msg.readableBytes()];
        msg.readBytes(data);
        RemoteObject remoteObject = remoteObjectCodec.decode(data);
        out.add(remoteObject);
    }

}
