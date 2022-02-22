package org.summer.roast.server.net;

import io.netty.channel.Channel;

/**
 * 客户端连接信息
 */
public class Session {
    //网络连接
    private Channel channel;
    //玩家id
    private String userId;
}
