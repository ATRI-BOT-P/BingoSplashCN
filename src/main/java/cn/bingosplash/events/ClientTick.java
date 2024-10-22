package cn.bingosplash.events;

import cn.bingosplash.BingoSplashCN;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import updater.CheckUpdate;

public final class ClientTick {
    @SubscribeEvent
    public void onClientTicket(TickEvent.ClientTickEvent event) {
        // CheckUpdate
        if (Minecraft.getMinecraft().thePlayer != null && !CheckUpdate.alreadyCheck) {
            CheckUpdate.alreadyCheck = true;
            new Thread(() -> {
                String ghVersion = CheckUpdate.CheckUpdate();
                if (!ghVersion.startsWith("v")) {
                    return;
                }
                ghVersion = ghVersion.replaceFirst("v", "");
                if (ghVersion.equals(BingoSplashCN.VERSION)) {
                    return;
                }
                Minecraft.getMinecraft().thePlayer.addChatMessage(
                        new ChatComponentText("§7[§dBingo§5Splash§cCN§7] §a发现新版本: §cv" + ghVersion));
            }).start();
        }
    }
}
