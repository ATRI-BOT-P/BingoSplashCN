package cn.bingosplash.events;

import cn.bingosplash.BingoSplashCN;
import cn.bingosplash.updater.CheckUpdate;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class ClientTick {
    @SubscribeEvent
    public void onClientTicket(TickEvent.ClientTickEvent event) {
        // Check update
        if (Minecraft.getMinecraft().thePlayer != null && !CheckUpdate.alreadyCheck) {
            CheckUpdate.alreadyCheck = true;
            new Thread(() -> {
                String ghVersion = CheckUpdate.StartCheck();
                if (ghVersion != null && !ghVersion.startsWith("v")) {
                    return;
                }
                if (ghVersion != null) {
                    ghVersion = ghVersion.replaceFirst("v", "");
                }
                if (ghVersion != null && ghVersion.equals(BingoSplashCN.VERSION)) {
                    return;
                }
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                        new ChatComponentText("§7[§dBingo§5Splash§cCN§7] §an发现新版本: §chttps://github.com/ATRI-BOT-P/BingoSplashCN/releases/tag/v" + ghVersion));
            }).start();
        }
    }
}
