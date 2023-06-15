package com.mcrmb.task;


import com.mcrmb.ApiConnection;
import com.mcrmb.Config;
import com.mcrmb.Mcrmb;
import com.mcrmb.util.Optional;
import com.mcrmb.util.json.JSONArray;
import com.mcrmb.util.json.JSONObject;
import org.bukkit.command.CommandSender;


import java.text.MessageFormat;
import java.util.List;

/**
 * 查询余额的任务
 **/
public class CheckMoneyTask implements Runnable {

    private final CommandSender sender;
    private final ApiConnection apiConnection;

    public CheckMoneyTask(CommandSender sender) {
        this.sender = sender;
        apiConnection = new ApiConnection();

    }

    @Override
    public void run() {
        ApiConnection.PlayerDataRequest playerDataReq = ApiConnection.PlayerDataRequest.newInstance(sender);
        String sign = playerDataReq.getSign();
        String name = playerDataReq.getName();
        String timeStamp = playerDataReq.getTimeStamp();
        String sid = Config.sid();

        if (Config.logApi()) {
            String message = MessageFormat.format("玩家{0}发起接口请求:CheckMoney?sign={1}&sid={2}&wname={3}&time={4}",
                    name, sign, sid, name, timeStamp);
            Mcrmb.loggerInfo(message);
        }

        Optional<List<Object>> resultOptional = apiConnection.parseApiResponse(Mcrmb.API + "CheckMoney?sign=" + sign + "&sid=" + sid + "&wname=" + name + "&time=" + timeStamp);
        sender.sendMessage("§b===================");

        if (resultOptional.isPresent()) {
            List<Object> result = resultOptional.get();
            if (result.get(0).equals("101")) {
                JSONArray infoArray = (JSONArray) result.get(2);
                JSONObject infoObject = infoArray.getJSONObject(0);
                sender.sendMessage(Config.prefix() + "玩家: " + name + "\n" + Config.prefix() + "您的余额: " + infoObject.get("money") + Config.point() + "\n" + Config.prefix() + "历史消费: " + infoObject.get("allpay") + Config.point() + "\n" + Config.prefix() + "历史充值: " + infoObject.get("allcharge") + Config.point());
                Mcrmb.setPlayerBalance(name, infoObject.get("money").toString());
            } else if (result.get(0).equals("102")) {
                sender.sendMessage(Config.prefix() + "您尚未开户,请提交充值卡以便自动开户!");
            }

        }

        sender.sendMessage("§b===================");
    }
}
