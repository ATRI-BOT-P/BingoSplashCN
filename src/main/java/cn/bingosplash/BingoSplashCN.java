package cn.bingosplash;

import cn.bingosplash.events.EventHandler;
import cn.bingosplash.tasks.FetchServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = BingoSplashCN.MODID, version = BingoSplashCN.VERSION)
public class BingoSplashCN {
    public static final String MODID = "BingoSplash";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        new Thread(() -> new FetchServer().connectToWebSocket()).start();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {

    }
}