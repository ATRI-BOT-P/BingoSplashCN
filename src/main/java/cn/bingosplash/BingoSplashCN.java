package cn.bingosplash;

import cn.bingosplash.events.RenderGameOverlay;
import cn.bingosplash.titlemanagers.TitleManager;
import cn.bingosplash.ws.SplashWebSockets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.util.Timer;
import java.util.TimerTask;

@Mod(modid = BingoSplashCN.MODID, version = BingoSplashCN.VERSION)
public final class BingoSplashCN {
    public static final String MODID = "BingoSplash";
    public static final String VERSION = "1.0.5";
    public static final TitleManager titleManager = new TitleManager();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new RenderGameOverlay());
        // 不明原因有时候会断开, 暂时创建一个task判断重连
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Thread(() -> new SplashWebSockets().connectToWebSocket()).start();
            }
        }, 10000, 10000);
    }
}