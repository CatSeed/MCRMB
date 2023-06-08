package com.mcrmb;

import com.mcrmb.util.DateUtil;
import com.mcrmb.util.EncryptionUtil;
import com.mcrmb.util.Optional;
import com.mcrmb.util.json.JSONArray;
import com.mcrmb.util.json.JSONObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.net.URLEncoder;
import java.util.List;

/**
 * PayApi类是一个与虚拟货币API交互的工具类，提供了查看余额、充值扣款以及购买物品等功能。
 */
public class PayApi {
    public static ApiConnection apiConnection = new ApiConnection();

    /**
     * 查看指定玩家的余额
     *
     * @param player 玩家用户名
     * @return 玩家余额
     */
    public static int look(String player) {
        Mcrmb.loggerInfo("跳过缓存强制刷新玩家 " + player + " 余额");
        ApiConnection.PlayerDataRequest playerDataReq = ApiConnection.PlayerDataRequest.newInstance(player);
        String name = playerDataReq.getName();
        String sign = playerDataReq.getSign();
        String timeStamp = playerDataReq.getTimeStamp();
        String sid = Config.sid();
        String apiUrl = Mcrmb.API + "CheckMoney?sign=" + sign + "&sid=" + sid + "&wname=" + name + "&time=" + timeStamp;
        Optional<List<Object>> resultOptional = apiConnection.parseApiResponse(apiUrl);
        if (resultOptional.isPresent()) {
            List<Object> result = resultOptional.get();
            if (result.get(0).equals("101")) {
                JSONArray resultArray = (JSONArray) result.get(2);
                JSONObject resultObject = resultArray.getJSONObject(0);
                Mcrmb.balances.put(player, resultObject.getString("money"));
                return Integer.parseInt(resultObject.getString("money"));
            }
        }
        return 0;
    }

    /**
     * 查询指定玩家的总充值金额
     *
     * @param player 玩家用户名
     * @return 玩家总充值金额
     */
    public static int allCharge(String player) {
        ApiConnection.PlayerDataRequest playerDataReq = ApiConnection.PlayerDataRequest.newInstance(player);
        String name = playerDataReq.getName();
        String sign = playerDataReq.getSign();
        String timeStamp = playerDataReq.getTimeStamp();
        String sid = Config.sid();
        String apiUrl = Mcrmb.API + "CheckMoney?sign=" + sign + "&sid=" + sid + "&wname=" + name + "&time=" + timeStamp;
        Optional<List<Object>> resultOptional = apiConnection.parseApiResponse(apiUrl);
        if (resultOptional.isPresent()) {
            List<Object> result = resultOptional.get();
            if (result.get(0).equals("101")) {
                JSONArray resultArray = (JSONArray) result.get(2);
                JSONObject resultObject = resultArray.getJSONObject(0);
                Mcrmb.balances.put(player, resultObject.getString("money"));
                return Integer.parseInt(resultObject.getString("allcharge"));
            }
        }
        return 0;
    }

    /**
     * 查询指定玩家的总消费金额
     *
     * @param player 玩家用户名
     * @return 玩家总消费金额
     */
    public static int allPay(String player) {
        ApiConnection.PlayerDataRequest playerDataReq = ApiConnection.PlayerDataRequest.newInstance(player);
        String name = playerDataReq.getName();
        String sign = playerDataReq.getSign();
        String timeStamp = playerDataReq.getTimeStamp();
        String sid = Config.sid();
        String apiUrl = Mcrmb.API + "CheckMoney?sign=" + sign + "&sid=" + sid + "&wname=" + name + "&time=" + timeStamp;
        Optional<List<Object>> resultOptional = apiConnection.parseApiResponse(apiUrl);
        if (resultOptional.isPresent()) {
            List<Object> result = resultOptional.get();
            if (result.get(0).equals("101")) {
                JSONArray resultArray = (JSONArray) result.get(2);
                JSONObject resultObject = resultArray.getJSONObject(0);
                Mcrmb.balances.put(player, resultObject.getString("money"));
                return Integer.parseInt(resultObject.getString("allpay"));
            }
        }
        return 0;
    }

    /**
     * 手动操作玩家余额，可以为玩家加款、扣款或重设点券余额
     *
     * @param player          玩家用户名
     * @param transactionType 操作类型：1表示加款，2表示扣款，3表示重设
     * @param amount          操作金额，加款和扣款为正负数，重设为新的余额
     * @param reason          操作原因，会记录在操作日志中
     * @return 操作是否成功
     */
    public static boolean manual(String player, int transactionType, String amount, String reason) {
        try {
            player = player.toLowerCase();
            String timeStamp = DateUtil.getTimeStamp();
            String key = Config.key();
            String sid = Config.sid();
            String sign = EncryptionUtil.encryptString(sid + player + transactionType + URLEncoder.encode(reason, "UTF-8") + amount + timeStamp + key);
            if (Config.logApi()) {
                Mcrmb.loggerInfo("发起手动" + (transactionType == 3 ? "重设点券余额" : (transactionType == 1 ? "加款" : "扣款")) + "请求:Pay?sign=" + sign + "&sid=" + sid + "&wname=" + player + "&type=" + transactionType + "&text=" + URLEncoder.encode(reason, "UTF-8") + "&money=" + amount + "&time=" + timeStamp);
            }
            String apiUrl = Mcrmb.API + "Manual?sign=" + sign + "&sid=" + sid + "&wname=" + player + "&type=" + transactionType + "&text=" + URLEncoder.encode(reason, "UTF-8") + "&money=" + amount + "&time=" + timeStamp;
            Optional<List<Object>> resultOptional = apiConnection.parseApiResponse(apiUrl);
            if (resultOptional.isPresent()) {
                List<Object> result = resultOptional.get();
                if (result.get(0) != null) {
                    Mcrmb.loggerInfo("加款扣款接口： 为玩家 " + player + " " + (transactionType == 3 ? "重设点券余额" : (transactionType == 1 ? "加款" : "扣款")) + amount + Config.point() + ", 原因: " + reason + ", 处理结果: " + result.get(1));
                    if (!result.get(0).equals("201") && !result.get(0).equals("101")) {
                        return false;
                    } else {
                        if (transactionType == 1) {
                            Mcrmb.balances.put(player, String.valueOf(Integer.parseInt(Mcrmb.balances.get(player)) + Integer.parseInt(amount)));
                        } else if (transactionType == 2) {
                            Mcrmb.balances.put(player, String.valueOf(Integer.parseInt(Mcrmb.balances.get(player)) - Integer.parseInt(amount)));
                        } else {
                            Mcrmb.balances.put(player, amount);
                        }

                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Mcrmb.loggerSevere("访问接口异常 " + e.getMessage());
        }
        return false;
    }

    /**
     * 为指定玩家购买物品
     *
     * @param playerName 玩家用户名
     * @param itemId 物品ID
     * @param amount 购买数量
     * @param broadcast 购买成功时是否全服公告
     * @return 购买是否成功
     */
    public static boolean pay(String playerName, String amount, String itemId, boolean broadcast) {
        try {
            Player player = Bukkit.getPlayer(playerName);
            playerName = playerName.toLowerCase();
            String timeStamp = DateUtil.getTimeStamp();
            String sid = Config.sid();
            String key = Config.key();
            String sign = EncryptionUtil.encryptString(sid + playerName + URLEncoder.encode(itemId, "UTF-8") + amount + timeStamp + key);
            if (Config.logApi()) {
                Mcrmb.loggerInfo("发起接口请求:Pay?sign=" + sign + "&sid=" + sid + "&wname=" + playerName + "&use=" + URLEncoder.encode(itemId, "UTF-8") + "&money=" + amount + "&time=" + timeStamp);
            }

            List<Object> result = apiConnection.parseApiResponse(Mcrmb.API + "Pay?sign=" + sign + "&sid=" + sid + "&wname=" + playerName + "&use=" + URLEncoder.encode(itemId, "UTF-8") + "&money=" + amount + "&time=" + timeStamp).orElse(null);

            if (result == null) {
                return false;
            }

            String resultCode = (String) result.get(0);
            switch (resultCode) {
                case "101": {
                    if (broadcast) {
                        Bukkit.broadcastMessage(Config.prefix() + "玩家" + playerName + "成功购买了【" + itemId + "】");
                    }

                    JSONObject resultObject = ((JSONArray) result.get(2)).getJSONObject(0);
                    String money = resultObject.getString("money") + Config.point();
                    Mcrmb.balances.put(playerName, money);

                    String message = Config.prefix() + "您成功消费了" + amount + Config.point() + "用于" + itemId + ",您的余额为" + money;
                    if (player != null) {
                        player.sendMessage(message);
                    }
                    Mcrmb.loggerInfo(playerName + "消费了" + amount + Config.point() + "用于" + itemId + ",玩家余额为" + money);
                    return true;
                }
                case "102": {
                    JSONObject resultObject = ((JSONArray) result.get(2)).getJSONObject(0);
                    String need = resultObject.getString("need") + Config.point();
                    String money = resultObject.getString("money") + Config.point();

                    String message = Config.prefix() + "您的消费失败: 花费" + amount + Config.point() + "用于" + itemId + ",您的余额为" + money + ",还需要" + need;
                    if (player != null) {
                        player.sendMessage(message);
                    }
                    Mcrmb.loggerInfo(playerName + "消费失败," + amount + Config.point() + "用于" + itemId + ",玩家余额为" + money + ",还需要" + need);
                    return false;
                }
                case "103": {
                    String message = Config.prefix() + "您的消费失败: 花费" + amount + Config.point() + "用于" + itemId + ",您未开户,请用/" + Config.command() + " cz 命令充值" + amount + Config.point();
                    if (player != null) {
                        player.sendMessage(message);
                    }
                    Mcrmb.loggerInfo(playerName + "消费失败," + amount + Config.point() + "用于" + itemId + ",玩家未开户");
                    return false;
                }
            }
            return false;

        } catch (Exception e) {
            Player player = Bukkit.getPlayer(playerName);
            String errorMessage = "支付过程发生错误，请汇报服主，错误信息：" + e.getMessage();
            if (player != null) {
                player.sendMessage(errorMessage);
            }
            Mcrmb.loggerInfo(errorMessage);
            e.printStackTrace();
            return false;
        }
    }


}
