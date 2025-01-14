package cn.bingosplash.commands;

import cn.bingosplash.ws.SplashWebSockets;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;


public class WStatus extends CommandBase {
    @Override
    public String getCommandName() {
        return "wstatus";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/wstatus";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        String msg = "§6WS状态:\n";
        msg += SplashWebSockets.session != null ?
                "§e已成功连接到服务端: §7" + SplashWebSockets.session.getId() + "\n" :
                "§c尚未连接到服务端, 最后的断开理由: §7" + SplashWebSockets.lastDisconnectMsg + "\n";
        msg += SplashWebSockets.isConnect ?
                "正在尝试连接中, 最后的断开理由: §7" + SplashWebSockets.lastDisconnectMsg + "\n" :
                "尚未开始尝试连接, 最后的断开理由: §7" + SplashWebSockets.lastDisconnectMsg + "\n";
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
    }
}