package cn.bingosplash.events;

import cn.bingosplash.BingoSplashCN;
import cn.bingosplash.loggers.BSLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public final class RenderGameOverlay {
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || BingoSplashCN.titleManager.getLastMessage() == null || mc.thePlayer == null) {
            return;
        }
        FontRenderer fontRenderer = mc.fontRendererObj;
        String message = BingoSplashCN.titleManager.getLastMessage();
        mc.fontRendererObj.drawString(message, (event.resolution.getScaledWidth() - fontRenderer.getStringWidth(message)) / 2, 66, 0xFFFFFF);
        // 等待5秒消失字符串, 按照DragPrio的ctjs写法
        new Thread(() -> {
            try {
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                BSLogger.severe("Stop thread catch: " + e.getMessage());
            }
            BingoSplashCN.titleManager.setLastMessage(null);
        }).start();
    }
}