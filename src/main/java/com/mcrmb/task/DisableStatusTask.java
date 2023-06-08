package com.mcrmb.task;

import com.mcrmb.ApiConnection;
import com.mcrmb.Config;
import com.mcrmb.Mcrmb;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * DisableStatusTask是一个Bukkit任务，用于向Mcrmb的API发送一条禁用插件的状态信息。
 */
public class DisableStatusTask extends BukkitRunnable {

    @Override
    public void run() {
        try {
            // 构造禁用状态请求URL
            String disableStatusUrl = Mcrmb.API + "Status?sid=" + Config.sid() + "&do=disable&pl=mcrmb&ver=" + Mcrmb.getInstance().getDescription().getVersion();
            // 发起API请求
            new ApiConnection().getApiResponse(disableStatusUrl);
            Mcrmb.loggerInfo("插件已停止，感谢使用www.mcrmb.com");
        } catch (Exception ignored) {
            // 忽略异常，避免影响服务器正常运行
        }
    }
}