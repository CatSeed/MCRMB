package com.mcrmb;


import com.mcrmb.util.EncryptionUtil;
import com.mcrmb.util.Optional;
import com.mcrmb.util.json.JSONObject;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * ApiConnection类表示与API端点的连接，并提供了从API获取响应和将其解析为对象列表的方法。
 */
public class ApiConnection {

    /**
     * 发送API请求，并返回API响应。
     *
     * @param apiUrl API请求的URL。
     * @return API响应字符串。
     * @throws IOException 如果发生I/O错误，则抛出IOException异常。
     */
    public String getApiResponse(String apiUrl) throws IOException {
        StringBuilder responseBuilder = new StringBuilder();
        URL url = new URL(apiUrl);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                responseBuilder.append(line);
            }
        }
        return responseBuilder.toString();
    }

    /**
     * 解析API响应为一个包含三个元素的对象列表。如果解析失败或者获取API响应失败，则返回Optional.empty()。
     *
     * @param apiUrl API请求的URL。
     * @return 一个包含三个元素的对象列表，如果解析失败或者获取API响应失败，则返回Optional.empty()。
     */
    public Optional<List<Object>> parseApiResponse(String apiUrl) {
        String response = null;
        try {
            response = this.getApiResponse(apiUrl);
        } catch (Exception e) {
            Mcrmb.loggerSevere("插件连接接口过程出现异常！");
            Mcrmb.loggerSevere("异常原因: " + e.getMessage());
            e.printStackTrace();
        }
        if (response != null) {
            JSONObject jsonObject = new JSONObject(response);
            List<Object> result = new ArrayList<>();
            result.add(jsonObject.get("code"));
            result.add(jsonObject.get("msg"));
            result.add(jsonObject.get("data"));
            return Optional.of(result);

        } else {
            return Optional.empty();
        }
    }

    /**
     * 玩家数据请求类，用于向游戏API发送玩家数据请求一些参数的包装
     */
    public static class PlayerDataRequest {

        public static PlayerDataRequest newInstance(String senderName) {
            return new PlayerDataRequest(senderName);
        }

        public static PlayerDataRequest newInstance(Player player) {
            return new PlayerDataRequest(player.getName());
        }

        public static PlayerDataRequest newInstance(CommandSender sender) {
            return new PlayerDataRequest(sender.getName());
        }

        public PlayerDataRequest(String senderName) {
            this.senderName = senderName.toLowerCase();
            this.timeStamp = String.valueOf(System.currentTimeMillis() / 1000L);
            this.sign = generatePlayerSign();
        }

        public PlayerDataRequest(Player player) {
            this(player.getName());
        }

        public PlayerDataRequest(CommandSender sender) {
            this(sender.getName());
        }

        private final String timeStamp;
        private final String senderName;
        private final String sign;

        private String generatePlayerSign() {
            String sid = Config.sid();
            String key = Config.key();
            return EncryptionUtil.encryptString(sid + senderName + timeStamp + key);
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public String getName() {
            return senderName;
        }

        public String getSign() {
            return sign;
        }
    }


}
