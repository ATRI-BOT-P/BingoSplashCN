package cn.bingosplash.handlers;

import cn.bingosplash.BingoSplashCN;
import cn.bingosplash.datas.ContentType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public final class WSHandler {
    // 返回bool, false说明没被显示
    public static boolean handlerContent(String content) {
        // 判断是否在游戏
        if (Minecraft.getMinecraft().thePlayer != null) {
            ContentType contentType = new ContentType(content);
            if (contentType.Status != null && contentType.Status != 200) {
                System.out.println("WS返回错误: " + contentType.Status + " " + contentType.Type + " " + contentType.Content);
                return false;
            }
            if (contentType.Content == null) {
                System.out.println("WS返回空内容");
                return false;
            }
            if (contentType.Type.equals("splash")) {
                BingoSplashCN.messageManager.setLastMessage("§6收到一条 §d§lBingo §5§lSplash §6提醒");
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§d§lBingo §5§lSplash §a-> " + contentType.Content));
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    Minecraft.getMinecraft().thePlayer.playSound(
                            "random.orb", 1.0F, 1.0F
                    );
                });
                return true;
            }
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§7[§dBingo§5Splash§cCN§7] §a-> 接收到无法解析内容: " + contentType.Content));
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
