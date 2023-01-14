package me.lichris93.jrrp.bungee.Utils;

import me.dreamvoid.miraimc.api.bot.MiraiGroup;

public class MiraiUtils {
    public static void sendMsgAsync(MiraiGroup group, String message) {
        ServerUtils.runAsync(() -> {
            group.sendMessage(message);
        });
    }
}
