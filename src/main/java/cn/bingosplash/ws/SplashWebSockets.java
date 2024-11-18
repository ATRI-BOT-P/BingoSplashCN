package cn.bingosplash.ws;

import cn.bingosplash.BingoSplashCN;
import cn.bingosplash.handlers.MessageHandler;
import cn.bingosplash.loggers.BSLogger;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@ClientEndpoint
public final class SplashWebSockets extends Endpoint {
    public static Session session;
    // session赋值似乎有延迟, 所以用自己的变量以判断是否连接, 防止出现重复连接 :skull:
    public static boolean isConnect = false;

    public static String lastDisconnectMsg = null;

    @OnOpen
    public void onOpen(Session session) {
        BSLogger.info("WS Connect");
    }

    @OnMessage
    public void onMessage(String message) {
        MessageHandler.handlerContent(message);
    }

    @Override
    public void onOpen(Session session, EndpointConfig config) {
        BSLogger.info("WS Connect opened with session ID: " + session.getId());
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        isConnect = false;
        connectToWebSocket();
        lastDisconnectMsg = "Reason: " + closeReason.getReasonPhrase() + " Code: " + closeReason.getCloseCode();
        BSLogger.warning("WS Disconnected, Reason: " + closeReason.getReasonPhrase() + " Code: " + closeReason.getCloseCode());
    }

    public void connectToWebSocket() {
        if (!isConnect) {
            isConnect = true;
            BSLogger.info("Trying connect WS");

            try {
                ClientEndpointConfig.Configurator configurator = new ClientEndpointConfig.Configurator() {
                    @Override
                    public void beforeRequest(Map<String, List<String>> headers) {
                        // 发送版本号, 兼容旧版本
                        headers.put("BingoSplashCN", Collections.singletonList(BingoSplashCN.VERSION));
                        headers.put("version", Collections.singletonList(BingoSplashCN.VERSION));
                    }
                };

                ClientEndpointConfig clientConfig = ClientEndpointConfig.Builder.create().configurator(configurator).build();
                WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                session = container.connectToServer(SplashWebSockets.class, clientConfig, URI.create("wss://ws.meownya.asia/api"));
            } catch (Exception e) {
                isConnect = false;
                BSLogger.severe("WS Connect failed: " + e.getMessage());
            }
        }
    }

    public void disconnect() {
        if (isConnect || session != null || session.isOpen()) {
            try {
                session.close();
            } catch (IOException e) {
                BSLogger.severe("WS Disconnect failed:" + e.getMessage());
            }
            session = null;
            isConnect = false;
            lastDisconnectMsg = "主动断开";
            BSLogger.info("WS Disconnected");
        }
    }
}