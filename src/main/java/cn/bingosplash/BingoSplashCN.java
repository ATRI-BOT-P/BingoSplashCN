package cn.bingosplash;

import cn.bingosplash.events.EventHandler;
import cn.bingosplash.messagemanagers.MessageManager;
import cn.bingosplash.ws.SplashWebSockets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = BingoSplashCN.MODID, version = BingoSplashCN.VERSION)
public final class BingoSplashCN {
    public static final String MODID = "BingoSplash";
    public static final String VERSION = "1.0.1";
    public static final MessageManager messageManager = new MessageManager();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        new Thread(() -> new SplashWebSockets().connectToWebSocket()).start();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        // TODO: Register /potion command(but not coming soon)
    }
}