package cn.bingosplash;

import cn.bingosplash.events.ClientTick;
import cn.bingosplash.events.RenderGameOverlay;
import cn.bingosplash.loggers.BSLogger;
import cn.bingosplash.titlemanagers.TitleManager;
import cn.bingosplash.ws.SplashWebSockets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

import java.util.Timer;
import java.util.TimerTask;

@Mod(modid = BingoSplashCN.MODID, version = BingoSplashCN.VERSION)
public final class BingoSplashCN {
    public static final String MODID = "BingoSplash";
    public static final String VERSION = "1.0.7";
    public static final TitleManager titleManager = new TitleManager();
    public static SplashWebSockets splashWebSockets = new SplashWebSockets();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        BSLogger.info("Loading");
        MinecraftForge.EVENT_BUS.register(new RenderGameOverlay());
        MinecraftForge.EVENT_BUS.register(new ClientTick());
        // 不明原因有时候会断开, 暂时创建一个task判断重连
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                new Thread(() -> splashWebSockets.connectToWebSocket()).start();
            }
        }, 10000, 10000);
    }

    @Mod.EventHandler
    public void stop(FMLServerStoppingEvent event) {
        splashWebSockets.disconnect();
        BSLogger.info("cya~");
    }
}