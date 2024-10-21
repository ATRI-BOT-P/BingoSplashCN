package cn.bingosplash.ws;

import cn.bingosplash.handlers.WSHandler;

import javax.websocket.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

@ClientEndpoint
public final class SplashWebSockets {
    public static Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WS连接成功");
    }

    @OnMessage
    public void onMessage(String message) {
        WSHandler.handlerContent(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        connectToWebSocket();
        System.out.println("WS断开连接尝试重连, 断开理由: " + closeReason.toString());
    }

    public void connectToWebSocket() {
        if (session == null || !session.isOpen()) {
            try {
                session = ContainerProvider.getWebSocketContainer().connectToServer(SplashWebSockets.class, URI.create("wss://ws.meownya.asia/api"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                connectToWebSocket();
            }
        }, 5000, 5000);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (session != null && session.isOpen()) {
                    session.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }
}