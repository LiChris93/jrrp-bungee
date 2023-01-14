package me.lichris93.jrrp.bungee;

import me.dreamvoid.miraimc.api.MiraiBot;
import me.dreamvoid.miraimc.bungee.event.message.passive.MiraiGroupMessageEvent;
import me.lichris93.jrrp.bungee.Utils.ServerUtils;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.NotNull;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import static me.lichris93.jrrp.bungee.values.*;
public class groupMsg implements Listener {
    @EventHandler
    public void onSend(@NotNull MiraiGroupMessageEvent e) {
        if (e.getGroupID() != qqgroup) {
            return;
        }//如果不是来自配置中的群就跳出
        if (e.getMessage().equals(jrrpmes)) {//处理.jrrp
            jrrpMes(e);
        }
        if (e.getMessage().equals(jrrpclear)) {//处理.jrrp-clear
           jrrpClear(e);
        }
        if (e.getMessage().equals(sendmap)) {//处理.jrrp-send-map
            sendMap(e);
        }
    }

    public boolean hasPermission(long qqNum) {
        for (String s : list) {
            if (Long.toString(qqNum).equals(s)) {
                return true;
            }
        }
        return false;

    }

    public void send(String text) {
        ServerUtils.runAsync(() ->
            MiraiBot.getBot(qqbot).getGroup(qqgroup).sendMessage(text)
        );
    }
    public void jrrpMes(@NotNull MiraiGroupMessageEvent e){
        if (Time.get(e.getSenderID()) == null) {// 判断hashmap中是否存在发送者，没有则发送消息并存储

            Date now = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyy 年 MM 月 dd 日");// 格式化当天的日期

            String num = Integer.toString(new Random().nextInt(101));
            send(getsucceedmes.replace("%sendername%", e.getSenderName()).replace("%rpnum%", num));// 发送消息
            String[] temp = {f.format(now), num};
            Time.put(e.getSenderID(), temp);// 放置到hashmap中
        } else {// 如果hashmap中存在
            Date now = new Date();
            SimpleDateFormat f = new SimpleDateFormat("yyyy 年 MM 月 dd 日");
            if (!f.format(now).equals(Time.get(e.getSenderID())[0])) {// 如果不是当天再次发送
                String num = Integer.toString(new Random().nextInt(101));
                send(getsucceedmes.replace("%sendername%", e.getSenderName()).replace("%rpnum%", num));// 发送消息
                String[] temp = {f.format(now), num};
                Time.put(e.getSenderID(), temp);// 重置时间
            } else {// 如果是当天再次发送
                send(getfailmes.replace("%sendername%", e.getSenderName())
                        .replace("%rpnum%", Time.get(e.getSenderID())[1]));// 发送还在冷却中消息
            }
        }
    }
    public void jrrpClear(@NotNull MiraiGroupMessageEvent e){
        if (hasPermission(e.getSenderID())) {
            Time.clear();
            send("HashMap Cleared");
        } else {
            send("你没有权限");
        }
    }
    public void sendMap(@NotNull MiraiGroupMessageEvent e){
        if (hasPermission(e.getSenderID())) {
            StringBuilder result = new StringBuilder("{");
            for (Map.Entry<Long, String[]> entry : Time.entrySet()) {
                result.append(entry.getKey()).append("=[").append(entry.getValue()[0]).append(",").append(entry.getValue()[1]).append("]");
            }
            result.append("}");
            send(result.toString());
        } else {
            send("你没有权限");
        }
    }
    
}

