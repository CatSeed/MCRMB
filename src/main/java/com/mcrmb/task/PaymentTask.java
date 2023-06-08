package com.mcrmb.task;

import com.mcrmb.Config;
import com.mcrmb.PayApi;
import org.bukkit.command.CommandSender;

/**
 * 通过 PayApi 执行付款的任务
 **/
public class PaymentTask implements Runnable {

    private final CommandSender sender;
    private final String targetPlayerName;
    private final String amount;
    private final String reason;

    public PaymentTask(CommandSender sender, String targetPlayerName, String amount, String reason) {
        this.sender = sender;
        this.targetPlayerName = targetPlayerName;
        this.amount = amount;
        this.reason = reason;
    }

    @Override
    public void run() {
        boolean result = PayApi.pay(targetPlayerName, amount, reason, false);
        sender.sendMessage("§b===================");
        sender.sendMessage(Config.prefix() + "接口方式消费操作已执行，扣除返回结果：" + result + "！");
        sender.sendMessage("§b===================");
    }
}
