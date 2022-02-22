package org.summer.roast.protocol;

import com.alibaba.fastjson.JSON;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class JSONRemoteObjectCodec implements RemoteObjectCodec {
    @Override
    public byte[] encode(RemoteObject remoteObject) {
        byte[] data = JSON.toJSONString(remoteObject).getBytes(StandardCharsets.UTF_8);
        ByteBuffer buffer = ByteBuffer.allocate(data.length + 4);
        buffer.putInt(data.length);
        buffer.put(data);
        buffer.flip();
        byte[] result = new byte[buffer.remaining()];
        buffer.get(result);
        return result;
    }

    @Override
    public RemoteObject decode(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int length = buffer.getInt();
        byte[] data = new byte[length];
        buffer.get(data);
        return JSON.parseObject(new String(data, StandardCharsets.UTF_8), RemoteObject.class);
    }

}
