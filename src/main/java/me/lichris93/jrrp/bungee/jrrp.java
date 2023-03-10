package me.lichris93.jrrp.bungee;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Arrays;

import static me.lichris93.jrrp.bungee.values.*;
public class jrrp extends Plugin{


    @Override
    public void onEnable() {
        long loadTime = System.currentTimeMillis();
        createDefaultFolder();
        ins = this;
        if (this.getProxy().getPluginManager().getPlugin("MiraiMC") == null) {
            warn("NO MIRAIMC, DISABLE");
            return;
        }
        saveDefaultFile("config.yml");
        try {
            config = getYAMLProvider("config.yml");
        } catch (IOException e) {
            warn("READ FILE FAILED,DISABLE");
            e.printStackTrace();
            return;
        }
        regCommand();
        regEvent();
        loadConfig();
        info("jrrp 加载完成！——By LiChris93[" + (System.currentTimeMillis() - loadTime) + "ms]");
    }
    public void info(String msg) {
        this.getLogger().info(msg);
    }
    public void warn(String msg) {
        this.getLogger().warning(msg);
    }
    public void createDefaultFolder() {
        if (!this.getDataFolder().exists()) {
            if (this.getDataFolder().mkdir()) {
                info("Create plugin data folder successfully");
            } else {
                warn("Unable to create plugin data folder [Unknown reason]");
            }
        }
    }
    public void saveDefaultFile(String name) {
        File file = new File(this.getDataFolder(), name);
        /* Create file */
        if (!file.exists()) {
            try (InputStream in = this.getResourceAsStream(name)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public Configuration getYAMLProvider(String name) throws IOException {
        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(this.getDataFolder(), name));
    }
    public static void saveYAMLProvider(Configuration yamlProvider, String name) throws IOException {
        ConfigurationProvider.getProvider(YamlConfiguration.class).save(yamlProvider,
                new File(ins.getDataFolder(), name));
    }
    public void regCommand(){
        try {
            this.getProxy().getPluginManager().registerCommand(this, new gameCommand());
            info("命令执行器注册完成");
        }catch(Exception e){
            warn("jrrp加载失败！原因：命令执行器注册失败");
        }
    }
    public void regEvent(){
        try {
            this.getProxy().getPluginManager().registerListener(this, new groupMsg());
            info("群消息监听事件注册完成");
        }catch (Exception e){
            warn("jrrp加载失败！原因：群消息监听事件注册失败");
        }
    }
    public void loadConfig(){
        try {
            qqBot = config.getLong("bot");
            qqGroup.addAll(config.getLongList("group"));
            admin = config.getString("admin");
            jrrpMes = config.getString("lang.jrrpmes");
            version = config.getString("version");
            jrrpClear = config.getString("lang.jrrpclear");
            sendMap = config.getString("lang.sendmap");
            getFailMes = config.getString("lang.getfailmes");
            getSucceedMes = config.getString("lang.getsucceedmes");
            sendRank = config.getString("lang.sendrank");
            if (admin.contains(",")) {
                String[] temp = admin.split(",");
                list.addAll(Arrays.asList(temp));
            } else {
                list.add(admin);
            }
            info("config读取完成");
        }catch (Exception e){
            warn("jrrp加载失败！原因：config读取失败");
        }
    }
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull TextComponent tc(String str){
        return new TextComponent(str);
    }
    public static boolean hasAdminPermission(long qqNum) {
        for (String s : list) {
            if (Long.toString(qqNum).equals(s)) {
                return true;
            }
        }
        return false;
    }
}
