package cn.bingosplash;

import cn.bingosplash.events.EventHandler;
import cn.bingosplash.titlemanagers.TitleManager;
import cn.bingosplash.ws.SplashWebSockets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = BingoSplashCN.MODID, version = BingoSplashCN.VERSION)
public final class BingoSplashCN {
    public static final String MODID = "BingoSplash";
    public static final String VERSION = "1.0.3";
    public static final TitleManager titleManager = new TitleManager();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        new Thread(() -> new SplashWebSockets().connectToWebSocket()).start();
    }
}