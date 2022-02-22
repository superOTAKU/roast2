package org.summer.roast.client;

import cn.hutool.json.JSONObject;
import com.google.inject.Guice;
import lombok.extern.slf4j.Slf4j;
import org.summer.roast.client.net.ClientBootstrap;
import org.summer.roast.protocol.RemoteObject;
import org.summer.roast.protocol.RequestCode;

@Slf4j
public class ClientMain {

    public static void main(String[] args) {
        ClientBootstrap clientBootstrap = Guice.createInjector(new ClientModule()).getInstance(ClientBootstrap.class);
        clientBootstrap.start();
        JSONObject data = new JSONObject();
        data.set("name", "Jack");
        RemoteObject response = clientBootstrap.sendForResponse(RequestCode.SAY_HELLO, data);
        log.info("response: {}", response.getData());
    }

}
