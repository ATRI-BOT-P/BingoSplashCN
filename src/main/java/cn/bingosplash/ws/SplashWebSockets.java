package cn.bingosplash.ws;

import cn.bingosplash.handlers.MessageHandler;
import cn.bingosplash.loggers.BSLogger;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
public final class SplashWebSockets {
    public static Session session;
    // session赋值似乎有延迟, 所以用自己的变量以判断是否连接, 防止出现重复连接 :skull:
    public static boolean isConnect = false;

    @OnOpen
    public void onOpen(Session session) {
        BSLogger.info("WS连接成功");
    }

    @OnMessage
    public void onMessage(String message) {
        MessageHandler.handlerContent(message);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        isConnect = false;
        connectToWebSocket();
        BSLogger.warning("WS断开连接尝试重连, 断开理由: " + closeReason.toString());
    }

    public void connectToWebSocket() {
        if (!isConnect) {
            isConnect = true;
            BSLogger.info("尝试连接WS");
            try {
                session = ContainerProvider.getWebSocketContainer().connectToServer(SplashWebSockets.class, URI.create("wss://ws.meownya.asia/api"));
            } catch (Exception e) {
                isConnect = false;
                BSLogger.severe("WS连接失败: " + e.getMessage());
            }
        }
    }

    public void disconnect() {
        if (isConnect || session != null || session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                BSLogger.severe("WS断开连接失败:" + e.getMessage());
            }
            session = null;
            isConnect = false;
            BSLogger.info("WS断开连接成功");
        }
    }
}