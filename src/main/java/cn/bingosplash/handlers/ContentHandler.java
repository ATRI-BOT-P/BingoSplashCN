package cn.bingosplash.handlers;

import cn.bingosplash.BingoSplashCN;
import cn.bingosplash.datas.ContentType;
import cn.bingosplash.loggers.BSLogger;
import cn.bingosplash.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.concurrent.CompletableFuture;

public final class ContentHandler {
    // 返回bool, false说明没被显示
    public static boolean contentHandler(String content) {
        ContentType contentType = new ContentType(content);
        // debug
        BSLogger.info("WS Content: " + contentType.Status + " " + contentType.Type + " " + contentType.Content);
        // 判断是否在游戏
        if (Minecraft.getMinecraft().thePlayer != null) {
            if (contentType.Status != null && contentType.Status != 200) {
                // 重复
                BSLogger.severe("WS return error: " + contentType.Status + " " + contentType.Type + " " + contentType.Content);
                return false;
            }
            if (contentType.Content == null) {
                BSLogger.warning("WS return empty content");
                return false;
            }
            // 解析splash消息, 后端返回消息会包含SplashID
            // 防止服务端端返回消息重复
            if (contentType.Type.equals("splash") && !Utils.splashIDSet.contains(contentType)) {
                Utils.splashIDSet.add(contentType);
                BingoSplashCN.titleManager.setLastMessage("§r§l-> §6收到一条 §d§lBingo §5§lSplash §6提醒 §r§l<-");
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§d§lBingo §5§lSplash §a-> " + contentType.Content));
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Minecraft.getMinecraft().thePlayer.playSound(
                            "random.orb", 1.5F, 1.2F
                    );
                });
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(5 * 1000);
                    } catch (InterruptedException e) {
                        BSLogger.severe("Stop thread catch: " + e.getMessage());
                    } finally {
                        Utils.splashIDSet.remove(contentType);
                    }
                }).exceptionally(e -> {
                    BSLogger.severe("Error occurred: " + e.getMessage());
                    return null;
                });
            }
            // 服务端提醒消息/或其他类型
            if (contentType.Type.equals("msg")) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§dBingo§5Splash§cCN§7] §a-> " + contentType.Content));
                return true;
            }
            // 解析error消息
            if (contentType.Type.equals("error")) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§dBingo§5Splash§cCN§7] §a-> 捕获到错误 " + contentType.Content));
                return true;
            }
            // 没有被前面解析的消息
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§dBingo§5Splash§cCN§7] §a-> 无法解析 " + contentType.Type + ": " + contentType.Content));
            return true;
        }
        return false;
    }
}
