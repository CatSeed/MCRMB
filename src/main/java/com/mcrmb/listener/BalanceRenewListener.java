package com.mcrmb.listener;

import com.mcrmb.task.BalanceRenewTask;
import com.mcrmb.Mcrmb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * 监听玩家加入事件，并在异步线程中执行余额更新任务
 */
public class BalanceRenewListener implements Listener {

    /**
     * 当玩家加入服务器时触发该事件处理方法
     *
     * @param event 玩家加入事件
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player != null) {
            // 运行异步任务，传入当前事件对象
            Mcrmb.runTaskAsync(new BalanceRenewTask(event));
        }
    }
}
