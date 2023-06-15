package com.mcrmb;

import com.mcrmb.listener.BalanceRenewListener;
import com.mcrmb.task.CardTypeFetcherTask;
import com.mcrmb.task.DisableStatusTask;
import com.mcrmb.util.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Mcrmb extends JavaPlugin {

    @Deprecated
    public static Mcrmb plugin;
    private static Mcrmb instance;

    public static final String API = "http://api.mcrmb.com/Api/";
    public static JSONObject cardInfoJson;
    public static Set<String> cardNames;
    public static String czHelp;

    @Deprecated
    public static Map<String, String> balances = new HashMap<>();

    /**
     * 获取特定玩家的余额信息。
     *
     * @param playerName 玩家名称
     * @return 玩家余额，如果不存在则为0
     */
    public static String getPlayerBalance(String playerName) {
        playerName = playerName.toLowerCase();
        String result = balances.get(playerName);
        return result == null ? "0" : result;
    }

    /**
     * 更新或设置特定玩家的余额信息。
     *
     * @param playerName 玩家名称
     * @param money      余额
     */
    public static void setPlayerBalance(String playerName, String money) {
        playerName = playerName.toLowerCase();
        balances.put(playerName, money);

    }

    public static Mcrmb getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        plugin = this;
        Config.loadConfig();

        try {
            new ApiConnection().getApiResponse(API + "Status?sid=" + Config.sid() + "&do=enable&pl=mcrmb&ver=" + instance.getDescription().getVersion());
        } catch (Exception e) {
            loggerSevere("插件无法连接接口，请检查服务器是否能访问 www.mcrmb.com。对于面板服问题，请向您的服务商咨询。");
        }

        getCardTypes();

        PluginManager pluginManager = getServer().getPluginManager();

        if (Config.renewOnJoin()) {
            pluginManager.registerEvents(new BalanceRenewListener(), this);
            loggerInfo("玩家进服时查询余额：已开启");
        } else if (Config.whitelist() > 0) {
            pluginManager.registerEvents(new BalanceRenewListener(), this);
            loggerInfo("白名单限制已开启，玩家必须有" + Config.whitelist() + "点券方能进服。");
        }

        if (Config.opModify()) {
            loggerInfo("服务器指令操作玩家点券：已开启");
        }


    }

    @Override
    public void onDisable() {
        try {
            new DisableStatusTask().runTaskAsynchronously(Mcrmb.getInstance());
        } catch (Exception e) {
            Mcrmb.loggerInfo("插件已停止，感谢使用www.mcrmb.com");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Commands.execute(sender, label, args);
        return true;
    }

    /**
     * 异步获取服务器允许的卡密类型列表
     */
    public void getCardTypes() {
        runTaskAsync(new CardTypeFetcherTask());
    }

    /**
     * 异步执行一个任务
     *
     * @param runnable 要执行的任务
     */
    public static void runTaskAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(instance, runnable);
    }

    /**
     * 同步执行一个任务
     *
     * @param runnable 要执行的任务
     */
    public static void runTask(Runnable runnable) {
        Bukkit.getScheduler().runTask(instance, runnable);
    }

    /**
     * 在日志中打印信息级别的消息
     *
     * @param message 要打印的消息
     */
    public static void loggerInfo(String message) {
        Bukkit.getLogger().info(MessageFormat.format("[Mcrmb] {0}", message));
    }

    /**
     * 在日志中打印警告级别的消息
     *
     * @param message 要打印的消息
     */
    public static void loggerWarning(String message) {
        Bukkit.getLogger().warning(MessageFormat.format("[Mcrmb] {0}", message));
    }

    /**
     * 在日志中打印严重级别的消息
     *
     * @param message 要打印的消息
     */
    public static void loggerSevere(String message) {
        Bukkit.getLogger().warning(MessageFormat.format("[Mcrmb] {0}", message));
    }
}
