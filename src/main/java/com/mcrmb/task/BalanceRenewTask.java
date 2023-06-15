package com.mcrmb.task;

import com.mcrmb.ApiConnection;
import com.mcrmb.Config;
import com.mcrmb.Mcrmb;
import com.mcrmb.util.Optional;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.event.player.PlayerJoinEvent;

import java.text.MessageFormat;
import java.util.List;

/**
 * 用于玩家进服时更新用户余额。
 * 当配置文件中的 whitelist 大于0时，将会设置进入服务器的门槛，玩家需要确保他们的账户中有足够的点券余额
 */
public class BalanceRenewTask implements Runnable {

    private final PlayerJoinEvent event;

    public BalanceRenewTask(PlayerJoinEvent event) {
        this.event = event;
    }

    public void run() {

        ApiConnection.PlayerDataRequest playerDataRequest = ApiConnection.PlayerDataRequest.newInstance(event.getPlayer());
        String playerName = playerDataRequest.getName();
        String sid = Config.sid();
        String key = Config.key();
        String timestamp = playerDataRequest.getTimeStamp();
        String sign = playerDataRequest.getSign();

        if (Config.logApi()) {
            Mcrmb.loggerInfo("入服查询 " + playerName + " 余额:CheckMoney?sign=" + sign + "&sid=" + sid + "&wname=" + playerName + "&time=" + timestamp);
        }

        String apiUrl = MessageFormat.format("{0}CheckMoney?sign={1}&sid={2}&wname={3}&time={4}", Mcrmb.API, sign, sid, playerName, timestamp);
        Optional<List<Object>> resultOptional = new ApiConnection().parseApiResponse(apiUrl);
        if (resultOptional.isPresent()) {
            List<Object> result = resultOptional.get();

            if (result.get(0).equals("101")) {
                JsonArray data = (JsonArray) result.get(2);
                JsonObject moneyJsonObject = data.get(0).getAsJsonObject();
                Mcrmb.setPlayerBalance(playerName, moneyJsonObject.get("money").getAsString());
            }

        }

        int requiredBalance = Config.whitelist();
        float currentBalance = Float.parseFloat(Mcrmb.getPlayerBalance(playerName));
        if (requiredBalance > 0 && currentBalance - requiredBalance < 0.0F) {
            String kickMessage = "§b很抱歉，本服务器要求您的点券余额有 %d 以上方能加入，您当前的点券余额为 %.2f，请到 http://www.mcrmb.com/%d 进行充值后再加入服务器~";
            event.getPlayer().kickPlayer(MessageFormat.format(kickMessage, requiredBalance, currentBalance, sid));
        }

    }
}