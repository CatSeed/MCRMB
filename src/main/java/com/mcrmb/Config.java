package com.mcrmb;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * 配置类
 * 用于读取和解析插件配置文件
 */
public class Config {
    private static String sid;
    private static String key;
    private static boolean logApi;
    private static boolean renewOnJoin;
    private static int cacheTimeout;
    private static int whitelist;
    private static boolean opModify;
    private static String command;
    private static String prefix;
    private static String point;
    private static String help;


    /**
     * 从配置文件中读取并解析配置
     */
    public static void loadConfig() {
        Mcrmb mcrmb = Mcrmb.getInstance();
        mcrmb.saveDefaultConfig();
        File configFile = new File(mcrmb.getDataFolder(), "config.yml");
        FileConfiguration config = mcrmb.getConfig();
        sid = config.getString("sid", "Your SID");
        key = config.getString("key", "Your KEY");
        logApi = config.getBoolean("logapi");
        renewOnJoin = config.getBoolean("renew_on_join", true);
        cacheTimeout = config.getInt("cache_timeout");
        whitelist = config.getInt("whitelist", 0);
        opModify = config.getBoolean("op_modify", false);
        command = config.getString("command");
        prefix = ChatColor.translateAlternateColorCodes('&', config.getString("prefix"));
        point = ChatColor.translateAlternateColorCodes('&', config.getString("point"));
        help = ChatColor.translateAlternateColorCodes('&', config.getString("help"));
    }

    /**
     * 重新加载配置文件
     */
    public static void reloadConfig() {
        Mcrmb mcrmb = Mcrmb.getInstance();
        mcrmb.reloadConfig();
        loadConfig();
    }

    /**
     * 存储SID和KEY到插件配置文件中
     *
     * @param sid 用户输入的SID
     * @param key 用户输入的KEY
     */
    public static void storeSidAndKey(String sid, String key) {
        Mcrmb mcrmb = Mcrmb.getInstance();
        FileConfiguration config = mcrmb.getConfig();
        Config.sid = sid;
        Config.key = key;
        config.set("sid", sid);
        config.set("key", key);
        mcrmb.saveConfig();
        mcrmb.reloadConfig();
    }

    public static String sid() {
        return sid;
    }

    public static String key() {
        return key;
    }

    public static boolean logApi() {
        return logApi;
    }

    public static boolean renewOnJoin() {
        return renewOnJoin;
    }

    public static int cacheTimeout() {
        return cacheTimeout;
    }

    public static int whitelist() {
        return whitelist;
    }

    public static boolean opModify() {
        return opModify;
    }

    public static String command() {
        return command;
    }

    public static String prefix() {
        return prefix;
    }

    public static String point() {
        return point;
    }

    public static String help() {
        return help;
    }


}