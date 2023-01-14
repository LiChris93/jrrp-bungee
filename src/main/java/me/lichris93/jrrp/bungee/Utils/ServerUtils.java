package me.lichris93.jrrp.bungee.Utils;
import me.lichris93.jrrp.bungee.values;


public class ServerUtils {
    public static void runAsync(Runnable runnable) {
        values.ins.getProxy().getScheduler().runAsync(values.ins, runnable);//异步
    }
}
