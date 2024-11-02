package cn.bingosplash.commands;

import cn.bingosplash.BingoSplashCN;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;


public class ReconnectWS extends CommandBase {
    @Override
    public String getCommandName() {
        return "reconnectws";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/reconnectws";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("§e重连中"));
        new Thread(() -> BingoSplashCN.splashWebSockets.connectToWebSocket()).start();
        // 防止出现标题消不掉的bug, 使用此命令以重置标题
        BingoSplashCN.titleManager.setLastMessage(null);
    }
}