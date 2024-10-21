package cn.bingosplash.ws;

import cn.bingosplash.handlers.MessageHandler;

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
        MessageHandler.handlerContent(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        new Thread(() -> new SplashWebSockets().connectToWebSocket()).start();
        System.out.println("WS断开连接尝试重连, 断开理由: " + closeReason.toString());
    }

    public void connectToWebSocket() {
        if (session == null || !session.isOpen()) {
            try {
                session = ContainerProvider.getWebSocketContainer().connectToServer(SplashWebSockets.class, URI.create("wss://ws.meownya.asia/api"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    new Thread(() -> new SplashWebSockets().connectToWebSocket()).start();
                }
            }, 10000, 10000);
        }
    }
}