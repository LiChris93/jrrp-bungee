package me.lichris93.jrrp.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import static me.lichris93.jrrp.bungee.values.*;

public class gameCommand extends Command {


    public gameCommand() {
        super("jrrp");
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (!commandSender.hasPermission("jrrp.admin")) {
            commandSender.sendMessage("你没有权限");
            return;
        }
        if (args.length == 1 && args[0].equals("help")) {
            showHelp(commandSender);
        } else if (args.length == 2 && args[0].equals("addadmin")) {
            addAdmin(commandSender,args[1]);
        } else if (args.length == 2 && args[0].equals("deladmin")) {
            delAdmin(commandSender,args[1]);
        } else if (args.length == 1 && args[0].equals("reload")) {
            reloadConfigYml(commandSender);
        } else if (args.length == 2 && args[0].equals("isadmin")) {
            isAdmin(commandSender,args[1]);
        } else {
            commandSender.sendMessage("§ajrrp v" + version + "正在这个服务器上运行, 使用 /jrrp help 来获取帮助");
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
    public void saveConfig() throws IOException {
        jrrp.saveYAMLProvider(config,"config.yml");
    }
    public void showHelp(@NotNull CommandSender commandSender){
        commandSender.sendMessage("§a--------------[ jrrp ]--------------");
        commandSender.sendMessage("§a/jrrp help              显示本帮助信息");
        commandSender.sendMessage("§a/jrrp reload                 重载配置");
        commandSender.sendMessage("§a/jrrp addadmin <qqid>          加管理");
        commandSender.sendMessage("§a/jrrp deladmin <qqid>          减管理");
        commandSender.sendMessage("§a/jrrp isadmin <qqid>    判断是否是管理");
        commandSender.sendMessage("§a----------[ By LiChris93 ]-----------");
    }
    public void addAdmin(CommandSender commandSender, @NotNull String qqNum){
        if (qqNum.matches("[1-9][0-9]{4,14}")) {
            list.add(qqNum);

            StringBuilder temp = new StringBuilder();
            if (list.size() > 1) {
                for (String s : list) {
                    temp.append(s).append(",");
                }
                temp.deleteCharAt(temp.length()-1);//去掉结尾逗号
            } else {
                temp = new StringBuilder((String) list.toArray()[0]);
            }
            config.set("admin", temp.toString());
            try {
                saveConfig();
                commandSender.sendMessage("§a添加完成!" + qqNum);
            } catch (IOException e) {
                ins.warn(e.toString());
                commandSender.sendMessage("§c添加失败，详细信息查看控制台");
            }

        } else {
            commandSender.sendMessage("§c不正确的QQ号！");
        }
    }
    public void reloadConfigYml(CommandSender commandSender){
        try {
            config = ins.getYAMLProvider("config.yml");
            qqbot = config.getLong("bot");
            qqgroup = config.getLong("group");
            admin = config.getString("admin");
            jrrpmes = config.getString("lang.jrrpmes");
            version = config.getString("version");
            jrrpclear = config.getString("lang.jrrpclear");
            sendmap = config.getString("lang.sendmap");
            getfailmes = config.getString("lang.getfailmes");
            getsucceedmes = config.getString("lang.getsucceedmes");
            if (admin.contains(",")) {
                String[] temp = admin.split(",");
                for (String i : temp) {
                    if (i.matches("[1-9][0-9]{4,14}")) {
                        list.add(i);
                    } else {
                        ins.warn(i + "不是有效的qq号");
                    }
                }
            } else {
                if (admin.matches("[1-9][0-9]{4,14}")) {
                    list.add(admin);
                } else {
                    ins.warn(admin + "不是有效的qq号");
                }
            }
            commandSender.sendMessage("§aconfig重载完成");

        } catch (Exception e) {
            ins.info(e.toString());

            commandSender.sendMessage("§cconfig重载失败，详细信息查看控制台");

        }        }

    public void isAdmin(CommandSender commandSender,String qqNum){
        if (hasPermission(Long.parseLong(qqNum))) {
            commandSender.sendMessage("§a该用户是管理");
        } else {
            commandSender.sendMessage("§c该用户不是管理");

        }
    }
    public void delAdmin(CommandSender commandSender,String qqNum){
        if (hasPermission(Long.parseLong(qqNum))) {
            list.remove(qqNum);
            StringBuilder temp = new StringBuilder();
            if (list.size() > 1) {
                for (String s : list) {
                    temp.append(s).append(",");
                }
                temp.deleteCharAt(temp.length()-1);//去掉结尾逗号
            } else {
                temp = new StringBuilder((String) list.toArray()[0]);
            }
            config.set("admin", temp.toString());
            try {
                saveConfig();
                commandSender.sendMessage("§a移除完成!" + qqNum);
            } catch (IOException e) {
                ins.warn(e.toString());
                commandSender.sendMessage("§c移除失败，详细信息查看控制台");
            }
        } else if (!qqNum.matches("[1-9][0-9]{4,14}")) {
            commandSender.sendMessage("§c不正确的QQ号！");
        } else {
            commandSender.sendMessage("§c该用户不是管理！");
        }
    }
}
