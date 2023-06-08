package com.mcrmb.task;

import com.mcrmb.ApiConnection;
import com.mcrmb.Config;
import com.mcrmb.Mcrmb;
import com.mcrmb.util.EncryptionUtil;
import com.mcrmb.util.json.JSONObject;

import java.text.MessageFormat;

/**
 * 用于获取服务器允许使用的卡种类。
 */
public class CardTypeFetcherTask implements Runnable {

    @Override
    public void run() {
        String sid = Config.sid(), key = Config.key();
        if (!sid.equalsIgnoreCase("Your SID") && !key.equalsIgnoreCase("Your KEY")) {
            Mcrmb.loggerInfo("开始获取您的服务器允许使用的卡种类!");
            String timestamp = String.valueOf(System.currentTimeMillis() / 1000L);
            String sign = EncryptionUtil.encryptString(sid + "Console" + timestamp + key);
            try {
                String apiUrl = MessageFormat.format("{0}CardTypes?sign={1}&sid={2}&wname=Console&time={3}", Mcrmb.API, sign, sid, timestamp);
                String result = new ApiConnection().getApiResponse(apiUrl);
                JSONObject response = new JSONObject(result);
                switch (response.get("code").toString()) {
                    case "101":
                        try {
                            Mcrmb.cardInfoJson = response.getJSONArray("data").getJSONObject(0);
                            Mcrmb.cardNames = Mcrmb.cardInfoJson.keySet();
                            Mcrmb.loggerInfo("从您的服务器设置共获取到 " + Mcrmb.cardNames.size() + " 种支持卡密：");
                            Mcrmb.czHelp = "";
                            for (String cardName : Mcrmb.cardNames) {
                                JSONObject cardJsonObject = Mcrmb.cardInfoJson.getJSONObject(cardName);
                                Mcrmb.czHelp += MessageFormat.format(" ※ 类型：【{0}】 {1} 卡号{2}位 密码{3}位 比例：{4}%\n", cardName, cardJsonObject.get("name").toString(), cardJsonObject.get("num").toString(), cardJsonObject.get("pwd").toString(), cardJsonObject.get("rate").toString());
                                Mcrmb.loggerInfo(MessageFormat.format("【代码：{0}】{1} 卡号长度：{2} 密码长度：{3} 折算比例：{4}%", cardName, cardJsonObject.get("name").toString(), cardJsonObject.get("num").toString(), cardJsonObject.get("pwd").toString(), cardJsonObject.get("rate").toString()));
                            }
                        } catch (Exception e) {
                            Mcrmb.loggerInfo("获取卡种失败（1），请联系MCRMB管理员！" + e.getCause());
                        }
                        break;
                    case "102":
                        Mcrmb.czHelp = "该服务器没有开通任何卡密渠道！请咨询服主！";
                        Mcrmb.loggerInfo("从您的服务器设置共获取到 0 种支持卡密渠道！");
                        break;
                    case "444":
                        Mcrmb.czHelp = "该服务器配置错误！请咨询服主！";
                        Mcrmb.loggerInfo("§c签名错误！§r您当前sid=" + sid + ", key=" + key + ", 请检查是否正确, 用/b setup <sid> <key> 可以重设.");
                        break;
                    default:
                        Mcrmb.czHelp = "该服务器配置错误！请咨询服主！";
                        Mcrmb.loggerInfo("获取卡种失败（2），请联系MCRMB管理员！");
                        break;
                }


            } catch (Exception e) {
                Mcrmb.loggerInfo("§c获取卡种超时！§r请打/b test检查服务器网络连接！或联系主机商、MCRMB管理员！" + e.getCause());
            }
        } else {
            Mcrmb.loggerInfo("§e您服务器的Sid或Key还未设置！请用/b setup <sid> <key> 进行设置！");
        }
    }

}
