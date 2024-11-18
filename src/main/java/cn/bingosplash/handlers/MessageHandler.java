package cn.bingosplash.handlers;

import cn.bingosplash.BingoSplashCN;
import cn.bingosplash.datas.ContentType;
import cn.bingosplash.loggers.BSLogger;
import cn.bingosplash.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public final class MessageHandler {
    // 返回bool, false说明没被显示
    public static boolean handlerContent(String content) {
        ContentType contentType = new ContentType(content);
        // debug
        BSLogger.info("WS Content: " + contentType.Status + " " + contentType.Type + " " + contentType.Content);
        // 判断是否在游戏
        if (Minecraft.getMinecraft().thePlayer != null) {
            if (contentType.Status != null && contentType.Status != 200) {
                // 重复
                BSLogger.severe("WS Error: " + contentType.Status + " " + contentType.Type + " " + contentType.Content);
                return false;
            }
            if (contentType.Content == null) {
                BSLogger.warning("WS Return empty content");
                return false;
            }
            // 解析splash消息, 后端返回消息会包含SplashID, 防止Splash消息重复, 我感觉重复的概率挺小的了, 发送的内容也不会很大
            if (contentType.Type.equals("splash") && !Utils.splashIDSet.contains(contentType)) {
                Utils.splashIDSet.add(contentType);
                BingoSplashCN.titleManager.setLastMessage("§6收到一条 §d§lBingo §5§lSplash §6提醒");
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§d§lBingo §5§lSplash §a-> " + contentType.Content));
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Minecraft.getMinecraft().thePlayer.playSound(
                            "random.orb", 1.0F, 1.0F
                    );
                });
                new Thread(() -> {
                    try {
                        Thread.sleep(60 * 1000);
                    } catch (Exception e) {
                        BSLogger.severe("Stop thread catch: " + e.getMessage());
                    }
                    Utils.splashIDSet.remove(contentType);
                    }).start();
                return true;
            }
            // 后端提醒消息/或其他类型, 固定前缀防止出现伪造消息漏洞
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
