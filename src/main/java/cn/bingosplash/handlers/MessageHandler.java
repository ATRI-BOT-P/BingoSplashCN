package cn.bingosplash.handlers;

import cn.bingosplash.BingoSplashCN;
import cn.bingosplash.datas.ContentType;
import cn.bingosplash.loggers.BSLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public final class MessageHandler {
    // 返回bool, false说明没被显示
    public static boolean handlerContent(String content) {
        // 判断是否在游戏
        if (Minecraft.getMinecraft().thePlayer != null) {
            ContentType contentType = new ContentType(content);
            if (contentType.Status != null && contentType.Status != 200) {
                BSLogger.severe("WS返回错误: " + contentType.Status + " " + contentType.Type + " " + contentType.Content);
                return false;
            }
            if (contentType.Content == null) {
                BSLogger.warning("WS返回空内容");
                return false;
            }
            // 解析splash消息
            if (contentType.Type.equals("splash")) {
                BingoSplashCN.titleManager.setLastMessage("§6收到一条 §d§lBingo §5§lSplash §6提醒");
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§d§lBingo §5§lSplash §a-> " + contentType.Content));
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Minecraft.getMinecraft().thePlayer.playSound(
                            "random.orb", 1.0F, 1.0F
                    );
                });
                return true;
            }
            // 后端提醒消息/或其他类型
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
            Minecraft.getMinecraft().addScheduledTask(() -> {
                Minecraft.getMinecraft().thePlayer.playSound(
                        "random.orb", 1.0F, 1.0F
                );
            });
            return true;
        }
        return false;
    }
}
