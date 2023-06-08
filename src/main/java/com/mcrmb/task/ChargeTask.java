package com.mcrmb.task;

import com.mcrmb.ApiConnection;
import com.mcrmb.CardService;
import com.mcrmb.Config;
import com.mcrmb.Mcrmb;
import com.mcrmb.util.Optional;
import com.mcrmb.util.json.JSONArray;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;
import java.util.List;

/**
 * 处理代币充值请求的任务，使用参数向 API 发送请求，更新玩家账户余额并通知结果
 **/
public class ChargeTask implements Runnable {

    private final ApiConnection apiConnection = new ApiConnection();
    private final CommandSender sender;

    private final String cardNumber;
    private final String cardPassword;
    private final String cardId;

    public ChargeTask(CommandSender sender, String cardNumber, String cardPassword, String cardId) {
        this.sender = sender;
        this.cardNumber = cardNumber;
        this.cardPassword = cardPassword;
        this.cardId = cardId;
    }

    @Override
    public void run() {
        ApiConnection.PlayerDataRequest playerDataReq = ApiConnection.PlayerDataRequest.newInstance(sender);
        String sign = playerDataReq.getSign();
        String timeStamp = playerDataReq.getTimeStamp();
        String name = playerDataReq.getName();
        String sid = Config.sid();


        if (Config.logApi()) {
            String message = MessageFormat.format("玩家{0}发起接口请求:Charge?sign={1}&sid={2}&wname={3}&ctype={4}&cnum={5}&cpwd={6}&time={7}",
                    name, sign, sid, name, cardId, cardNumber, cardPassword, timeStamp);
            Mcrmb.loggerInfo(message);
        }

        String apiUrl = MessageFormat.format("{0}Charge?sign={1}&sid={2}&wname={3}&ctype={4}&cnum={5}&cpwd={6}&time={7}",
                Mcrmb.API, sign, sid, name, cardId, cardNumber, cardPassword, timeStamp);

        Optional<List<Object>> resultOptional = apiConnection.parseApiResponse(apiUrl);


        sender.sendMessage("§b===================");
        if (resultOptional.isPresent()) {
            List<Object> result = resultOptional.get();
            String responseCode = result.get(0).toString();
            String responseMessage = result.get(1).toString();
            if (responseCode.equals("555")) {
                sender.sendMessage(Config.prefix() + "提交接口错误：" + responseMessage);
            } else if (!responseCode.equals("404") && !responseCode.equals("444")) {
                if (responseCode.equals("101")) {
                    sender.sendMessage(Config.prefix() + "开户并提交卡密成功！\n" + Config.prefix() + "输入/" + Config.command() + " ck查询结果，或输入/" + Config.command() + " money查询余额");
                } else if (responseCode.equals("201")) {
                    sender.sendMessage(Config.prefix() + "卡密提交成功！\n" + Config.prefix() + "输入/" + Config.command() + " ck查询结果，或输入/" + Config.command() + " money查询余额");
                } else {

                    switch (responseCode) {
                        case "102": {
                            JSONArray cardInfoArray = (JSONArray) result.get(2);
                            sender.sendMessage(Config.prefix() + "开户成功，但卡密提交失败，请检查！\n" + Config.prefix() + "原因:" + CardService.getInstance().getCardStatus(cardInfoArray.getJSONObject(0).get("wmsg").toString()));
                            break;
                        }
                        case "202": {

                            JSONArray cardInfoArray = (JSONArray) result.get(2);
                            sender.sendMessage(Config.prefix() + "卡密提交失败，请检查原因：" + CardService.getInstance().getCardStatus(cardInfoArray.getJSONObject(0).get("wmsg").toString()));
                            break;
                        }
                        case "105":
                            sender.sendMessage(Config.prefix() + "卡号和密码需为数字！");
                            break;
                    }
                }
            } else {
                sender.sendMessage(Config.prefix() + "操作失败，请联系服主！");
                Mcrmb.loggerSevere(responseMessage);
            }
        }
        sender.sendMessage("§b===================");


    }
}
