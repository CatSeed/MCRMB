package com.mcrmb.task;

import com.mcrmb.ApiConnection;
import com.mcrmb.Config;
import com.mcrmb.Mcrmb;
import com.mcrmb.util.Optional;
import com.mcrmb.util.json.JSONArray;
import com.mcrmb.util.json.JSONObject;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * 用于从API连接中获取指定玩家的余额、历史消费和历史充值数据，并将结果发送给命令发送者。
 * 如果返回结果不为空，则解析返回结果并将相关数据发送给命令发送者。
 * 如果返回结果为空，则提示玩家尚未开户。
 */
public class RetrievePlayerMoneyTask implements Runnable {
    private final ApiConnection apiConnection;
    private final CommandSender sender;
    private final String targetPlayerName;

    public RetrievePlayerMoneyTask(CommandSender sender, String targetPlayerName) {
        this.sender = sender;
        this.targetPlayerName = targetPlayerName;
        this.apiConnection = new ApiConnection();
    }

    @Override
    public void run() {
        ApiConnection.PlayerDataRequest playerDataReq = ApiConnection.PlayerDataRequest.newInstance(targetPlayerName);
        String name = playerDataReq.getName();
        String timeStamp = playerDataReq.getTimeStamp();
        String sign = playerDataReq.getSign();
        String sid = Config.sid();
        if (Config.logApi()) {
            Mcrmb.loggerInfo("发起接口请求:CheckMoney?sign=" + sign + "&sid=" + sid + "&wname=" + name + "&time=" + timeStamp);
        }

        Optional<List<Object>> resultOptional = apiConnection.parseApiResponse(Mcrmb.API + "CheckMoney?sign=" + sign + "&sid=" + sid + "&wname=" + name + "&time=" + timeStamp);
        sender.sendMessage("§b==============================================================");
        if (resultOptional.isPresent()) {
            List<Object> result = resultOptional.get();
            if (result.get(0).equals("101")) {
                JSONArray var2 = (JSONArray) result.get(2);
                JSONObject var3 = var2.getJSONObject(0);
                sender.sendMessage(Config.prefix() + "玩家: " + targetPlayerName);
                sender.sendMessage(Config.prefix() + "余额: " + var3.get("money") + Config.point());
                sender.sendMessage(Config.prefix() + "历史消费: " + var3.get("allpay") + Config.point());
                sender.sendMessage(Config.prefix() + "历史充值: " + var3.get("allcharge") + Config.point());
            } else if (result.get(0).equals("102")) {
                sender.sendMessage(Config.prefix() + "该玩家" + targetPlayerName + "尚未开户!");
            }

        }

        sender.sendMessage("§b==============================================================");
    }
}

