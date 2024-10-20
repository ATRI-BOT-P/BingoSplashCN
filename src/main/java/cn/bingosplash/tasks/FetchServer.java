package cn.bingosplash.tasks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import javax.websocket.*;
import java.net.URI;
import java.util.Timer;
import java.util.TimerTask;

@ClientEndpoint
public class FetchServer {
    public static Session session;
    private static String lastMessage = "";

    public static String getLastMessage() {
        return lastMessage;
    }

    public synchronized static void setLastMessage(String lastMessage) {
        FetchServer.lastMessage = lastMessage;
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WS连接成功");
    }

    @OnMessage
    public void onMessage(String message) {
        if (Minecraft.getMinecraft().thePlayer != null) {
            JsonObject gson = new Gson().fromJson(message, JsonElement.class).getAsJsonObject();
            if (gson.get("status").getAsInt() == 200) {
                if (gson.get("type").getAsString().equals("splash") && gson.get("content").getAsString() != null) {
                    lastMessage = "§6§l提醒: §e" + gson.get("content").getAsString();
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§dBingo§5Splash§cCN§7] [WS] §a接收到BingoSplash提醒消息: §e" + gson.get("content").getAsString()));
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        Minecraft.getMinecraft().thePlayer.playSound(
                                "random.orb", 1.0F, 1.0F
                        );
                    });
                    return;
                }
                if (gson.get("content").getAsString() != null) {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§dBingo§5Splash§cCN§7] [WS] §a接收到其他消息(无法解析,可能是你版本过低与服务端不同步): §e" + gson.get("content").getAsString()));
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        reconnect();
        System.out.println("WS断开连接, 断开理由: " + closeReason.toString());
    }

    public void connectToWebSocket() {
        try {
            session = ContainerProvider.getWebSocketContainer().connectToServer(FetchServer.class, URI.create("wss://ws.meownya.asia/api"));
        } catch (Exception e) {
            reconnect();
            e.printStackTrace();
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                reconnect();
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

    public void reconnect() {
        if (session == null || !session.isOpen()) {
            connectToWebSocket();
        }
    }
}