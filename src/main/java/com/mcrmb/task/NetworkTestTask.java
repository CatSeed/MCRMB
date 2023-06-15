package com.mcrmb.task;

import com.mcrmb.ApiConnection;
import com.mcrmb.Config;
import com.mcrmb.Mcrmb;
import org.bukkit.command.CommandSender;

/**
 * 网络测试
 **/
public class NetworkTestTask implements Runnable {
    private final String[] args;
    private final ApiConnection apiConnection;
    private final CommandSender sender;

    public NetworkTestTask(CommandSender sender, String[] args) {
        this.args = args;
        this.apiConnection = new ApiConnection();
        this.sender = sender;

    }

    @Override
    public void run() {
        if (args.length == 2) {
            String url = args[1];
            try {
                apiConnection.getApiResponse(url);
                sender.sendMessage("§a自定义测试【" + url + "】： 正常");
            } catch (Exception e) {
                sender.sendMessage("§c自定义测试【" + url + "】： 异常 " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            sender.sendMessage("§e网络测试中，共3个结果，请耐心等待。");

            try {
                apiConnection.getApiResponse("http://www.baidu.com");
                sender.sendMessage("§a测试连接百度： 正常");
            } catch (Exception e) {
                sender.sendMessage("§c测试连接百度： 异常 " + e.getMessage());
                e.printStackTrace();
            }
            String sid = Config.sid();

            try {
                apiConnection.getApiResponse(Mcrmb.API + "Status?sid=" + sid + "&do=enable&pl=mcrmb&ver=" + Mcrmb.getInstance().getDescription().getVersion());
                sender.sendMessage("§a测试当前接口： 正常");
            } catch (Exception e) {
                sender.sendMessage("§c测试当前接口： 异常 " + e.getMessage());
                e.printStackTrace();
            }

            try {
                apiConnection.getApiResponse("http://www.mcrmb.com/Api/Status?sid=" + sid + "&do=enable&pl=mcrmb&ver=" + Mcrmb.getInstance().getDescription().getVersion());
                sender.sendMessage("§a测试旧接口： 正常");
            } catch (Exception e) {
                sender.sendMessage("§c测试旧接口： 异常 " + e.getMessage());
                e.printStackTrace();
            }

            try {
                apiConnection.getApiResponse("http://www.bue.cc");
                sender.sendMessage("§a附加测试1： 正常");
            } catch (Exception e) {
                sender.sendMessage("§c附加测试1： 异常 " + e.getMessage());
                e.printStackTrace();
            }

            try {
                apiConnection.getApiResponse("http://fnob.net");
                sender.sendMessage("§a附加测试2： 正常");
            } catch (Exception e) {
                sender.sendMessage("§c附加测试2： 异常 " + e.getMessage());
                e.printStackTrace();
            }

            sender.sendMessage("§e测试结束，请截图发给Mcrmb技术，谢谢支持！");
        }

    }
}
