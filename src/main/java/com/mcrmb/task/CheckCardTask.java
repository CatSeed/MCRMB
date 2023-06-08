package com.mcrmb.task;

import com.mcrmb.*;
import com.mcrmb.util.DateUtil;
import com.mcrmb.util.Optional;
import com.mcrmb.util.json.JSONArray;
import com.mcrmb.util.json.JSONObject;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.List;

/**
 * 用于查询充值卡信息的任务。
 */
public class CheckCardTask implements Runnable {
    private final CommandSender sender;
    private final String[] args;

    private final ApiConnection apiConnection = new ApiConnection();

    public CheckCardTask(CommandSender sender, String[] args) {
        this.sender = sender;
        this.args = args;

    }

    @Override
    public void run() {
        ApiConnection.PlayerDataRequest playerDataReq = ApiConnection.PlayerDataRequest.newInstance(sender);
        String sid = Config.sid();
        Optional<List<Object>> resultOptional;
        String playerName = playerDataReq.getName();
        String sign = playerDataReq.getSign();
        String timeStamp = playerDataReq.getTimeStamp();

        if (args.length > 1) {
            // 有卡号的情况下...
            String cardNumber = args[1];
            if (Config.logApi()) {
                String message = MessageFormat.format(
                        "玩家{0}发起接口请求:CheckCard?sign={1}&sid={2}&wname={3}&cnum={4}&time={5}",
                        playerName, sign, sid, playerName, cardNumber, timeStamp);
                Mcrmb.loggerInfo(message);
            }

            String apiUrl = MessageFormat.format(
                    "{0}CheckCard?sign={1}&sid={2}&wname={3}&cnum={4}&time={5}",
                    Mcrmb.API, sign, sid, playerName, cardNumber, timeStamp);
            resultOptional = apiConnection.parseApiResponse(apiUrl);
        } else {
            // 当没有输入卡号的时候，自动查询最后5条卡密记录
            if (Config.logApi()) {
                String message = MessageFormat.format(
                        "玩家{0}发起接口请求:CheckCard?sign={1}&sid={2}&wname={3}&time={4}",
                        playerName, sign, sid, playerName, timeStamp);
                Mcrmb.loggerInfo(message);
            }

            String apiUrl = MessageFormat.format(
                    "{0}CheckCard?sign={1}&sid={2}&wname={3}&time={4}",
                    Mcrmb.API, sign, sid, playerName, timeStamp);
            resultOptional = apiConnection.parseApiResponse(apiUrl);
        }

        sender.sendMessage("§b===================");

        if (resultOptional.isPresent()) {
            List<Object> result = resultOptional.get();

            if (result.size() > 2) {
                JSONArray cardRecords = (JSONArray) result.get(2);
                StringBuilder message = new StringBuilder(Config.prefix() + "以下是查询到的充值卡记录：\n【时间】       【卡号】       【成功金额】       【状态】\n");

                for (int i = 0; i < cardRecords.length(); i++) {
                    JSONObject cardRecord = cardRecords.getJSONObject(i);
                    message.append(MessageFormat.format("【{0}】【{1}】【{2}】【{3}】\n",
                            DateUtil.convertTimestampToDate(cardRecord.get("date").toString()),
                            cardRecord.get("num"),
                            cardRecord.get("money"),
                            CardService.getInstance().getCardStatus(cardRecord.get("status").toString())));
                }

                sender.sendMessage(message + "§b仅显示最新5条，需查询所有记录请登陆 http://www.mcrmb.com/");

            } else if (!result.get(0).equals("404") && !result.get(0).equals("444")) {
                sender.sendMessage(Config.prefix() + "查不到记录,该卡未提交过或玩家未曾充值！");
            } else {
                sender.sendMessage(Config.prefix() + "操作失败，请联系服主咨询！");
                Mcrmb.loggerSevere(result.get(1).toString());
            }
        }

        sender.sendMessage("§b===================");
    }

}
