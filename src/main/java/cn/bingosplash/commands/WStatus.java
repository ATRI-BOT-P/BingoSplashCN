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
        if (SplashWebSockets.session != null) {
            msg += "已成功连接到服务端: " + SplashWebSockets.session.getId() + "\n";
        } else {
            msg += "尚未连接到服务端, 最后的断开理由" + SplashWebSockets.lastDisconnectMsg + "\n";
        }
        if (SplashWebSockets.isConnect) {
            msg += "正在尝试连接中, 最后的断开理由" + SplashWebSockets.lastDisconnectMsg + "\n";
        } else {
            msg += "尚未开始尝试连接, 最后的断开理由" + SplashWebSockets.lastDisconnectMsg + "\n";
        }
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(msg));
    }
}