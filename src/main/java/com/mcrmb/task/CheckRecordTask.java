package com.mcrmb.task;

import com.mcrmb.ApiConnection;
import com.mcrmb.Config;
import com.mcrmb.Mcrmb;
import com.mcrmb.util.DateUtil;
import com.mcrmb.util.Optional;
import com.mcrmb.util.json.JSONArray;
import com.mcrmb.util.json.JSONObject;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.List;

/**
 * 通过发送 API 请求来检查玩家的交易记录。
 */
public class CheckRecordTask implements Runnable {

    private final CommandSender sender;
    private final String senderName;

    public CheckRecordTask(CommandSender sender) {
        this.sender = sender;
        this.senderName = this.sender.getName();
    }

    @Override
    public void run() {
        // 如果开启了 debug 模式，则打印调试信息
        ApiConnection.PlayerDataRequest playerDataRequest = ApiConnection.PlayerDataRequest.newInstance(senderName);
        String timeStamp = playerDataRequest.getTimeStamp();
        String sid = Config.sid();
        String sign = playerDataRequest.getSign();
        String name = playerDataRequest.getName();
        if (Config.logApi()) {
            Mcrmb.loggerInfo(MessageFormat.format("玩家{0}发起接口请求:CheckRecord?sign={1}&sid={2}&wname={3}&time={4}", name, sign, sid, name, timeStamp));
        }
        // 发送 API 请求以检查玩家的交易记录
        ApiConnection apiConnection = new ApiConnection();
        Optional<List<Object>> resultOptional = apiConnection.parseApiResponse(Mcrmb.API + "CheckRecord?sign=" + sign + "&sid=" + sid + "&wname=" + name + "&time=" + timeStamp);

        // 显示响应结果给玩家
        sender.sendMessage("§b===================");
        if (resultOptional.isPresent()) {
            List<Object> result = resultOptional.get();
            if (result.size() > 2) {
                JSONArray jsonArray = (JSONArray) result.get(2);
                StringBuilder message = new StringBuilder(Config.prefix() + "为您查询到以下流水记录：\n" +
                        "【时间】           【金额】 【说明】\n");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    message.append("【")
                            .append(DateUtil.convertTimestampToDate(jsonObject.getString("date")))
                            .append("】【")
                            .append(jsonObject.getString("money"))
                            .append("】【")
                            .append(jsonObject.getString("text"))
                            .append("】\n");
                }
                message.append("§b仅显示最新5条，如需查询所有记录，请登陆 http://www.mcrmb.com/");
                sender.sendMessage(message.toString());

            } else if (!result.get(0).equals("404") && !result.get(0).equals("444")) {

                sender.sendMessage(Config.prefix() + result.get(1).toString());
            } else {

                sender.sendMessage(Config.prefix() + "操作失败，建议您联系服主咨询！");
                Mcrmb.loggerWarning(result.get(1).toString());

            }

        }


        sender.sendMessage("§b===================");
    }
}
