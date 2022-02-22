package org.summer.roast.protocol;

/**
 * 把编解码抽象出来
 */
public interface RemoteObjectCodec {

    byte[] encode(RemoteObject remoteObject);

    RemoteObject decode(byte[] bytes);

}
