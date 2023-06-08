package com.mcrmb;

import com.mcrmb.task.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.text.MessageFormat;

/**
 * 用户指令和管理员指令处理类
 **/
public class Commands {


    public static void execute(final CommandSender sender, String label, String[] args) {

        if (label.equalsIgnoreCase(Config.command())) {

            // 游戏帮助
            if (args.length == 0) {
                sender.sendMessage(Config.help().replace("<br>", "\n").replace("{command}", Config.command()));
                return;
            }

            // 查询你自己的最后5条交易记录,包括赞助和消费
            if (args[0].equalsIgnoreCase("cx")) {
                try {
                    sender.sendMessage(Config.prefix() + "§a指令已成功发送！  请耐心等待。");
                    Mcrmb.runTaskAsync(new CheckRecordTask(sender));
                } catch (Exception e) {
                    sender.sendMessage(Config.prefix() + "§c操作失败，请联系服主！");
                    Mcrmb.loggerSevere("访问接口异常 " + e.getMessage());
                }
                return;
            }

            // 查询对应的充值卡充值状态
            if (args[0].equalsIgnoreCase("ck")) {
                try {
                    sender.sendMessage(Config.prefix() + "§a指令已成功发送！请耐心等待。");
                    Mcrmb.runTaskAsync(new CheckCardTask(sender, args));
                } catch (Exception e) {
                    sender.sendMessage(Config.prefix() + "§c操作失败，请联系服主！");
                    Mcrmb.loggerSevere("访问接口异常 " + e.getMessage());
                }
                return;
            }

            // 卡密充值
            if (args[0].equalsIgnoreCase("cz")) {
                try {
                    if (args.length == 4) {

                        String cardName = args[1].toLowerCase(), cardNumber = args[2], cardPassword = args[3];

                        if (Mcrmb.cardNames.contains(cardName)) {

                            int numLength = Integer.parseInt(Mcrmb.cardInfoJson.getJSONObject(cardName).get("num").toString());
                            int pwdLength = Integer.parseInt(Mcrmb.cardInfoJson.getJSONObject(cardName).get("pwd").toString());
                            if (numLength > 0 && cardNumber.length() != numLength) {
                                sender.sendMessage(Config.prefix() + "【" + Mcrmb.cardInfoJson.getJSONObject(cardName).get("name").toString() + "】卡号必须为【" + numLength + "位】, 您输入了【" + cardNumber.length() + "位】.");
                                return;
                            }

                            if (pwdLength > 0 && cardPassword.length() != pwdLength) {
                                sender.sendMessage(Config.prefix() + "【" + Mcrmb.cardInfoJson.getJSONObject(cardName).get("name").toString() + "】密码必须为【" + pwdLength + "位】, 您输入了【" + cardPassword.length() + "位】.");
                                return;
                            }

                            String cardId = Mcrmb.cardInfoJson.getJSONObject(cardName).get("id").toString();
                            sender.sendMessage(Config.prefix() + "§a指令已发送，请耐心等待。");
                            Mcrmb.runTaskAsync(new ChargeTask(sender, cardNumber, cardPassword, cardId));
                        } else {
                            sender.sendMessage(Config.prefix() + "§c卡类型不存在！支持的卡种和卡密长度如下：\n§b" + Mcrmb.czHelp);
                        }
                        return;

                    }

                    String message = MessageFormat.format("{0}/{1} cz <类型> <卡号> <卡密>\n{2}注意: 卡号和密码不要有空格\n{3}支持的卡种和卡密长度:\n§b{4}",
                            Config.prefix(), Config.command(), Config.prefix(), Config.prefix(), Mcrmb.czHelp);
                    sender.sendMessage(message);
                } catch (Exception e) {
                    sender.sendMessage(Config.prefix() + "§c操作失败，请联系服主！");
                    Mcrmb.loggerSevere("访问接口异常 " + e.getMessage());
                }
                return;
            }

            // 查询余额 累计赞助 消费
            if (args[0].equalsIgnoreCase("money")) {
                try {
                    sender.sendMessage(Config.prefix() + "§a指令已成功发送！请耐心等待。");
                    Mcrmb.runTaskAsync(new CheckMoneyTask(sender));

                } catch (Exception e) {
                    sender.sendMessage(Config.prefix() + "§c操作失败，请联系服主！");
                    Mcrmb.loggerSevere("访问接口异常 " + e.getMessage());
                }
                return;
            }


            // 管理员指令
            if (args[0].equalsIgnoreCase("admin") && sender.hasPermission("Mcrmb.admin")) {
                if (args.length == 1) {
                    sender.sendMessage(Config.prefix() + "管理员指令:");
                    sender.sendMessage(Config.prefix() + "/" + Config.command() + " setup <sid> <key> 快速设置Sid和Key.");
                    sender.sendMessage(Config.prefix() + "/" + Config.command() + " give <玩家名> <点券> 给予玩家点券(手工加款).");
                    sender.sendMessage(Config.prefix() + "/" + Config.command() + " take <玩家名> <点券> 减少玩家点券(手工扣款).");
                    sender.sendMessage(Config.prefix() + "/" + Config.command() + " set <玩家名> <点券> 设置玩家点券.");
                    sender.sendMessage(Config.prefix() + "/" + Config.command() + " admin reload 重载插件配置.");
                    sender.sendMessage(Config.prefix() + "/" + Config.command() + " admin xf <玩家名> <金额> <用途> 强制消费指令,需手动发奖励哦.");
                    sender.sendMessage(Config.prefix() + "/" + Config.command() + " admin money <玩家名> 查询玩家的余额信息.");
                    sender.sendMessage(Config.prefix() + "当前服务器SID: " + Config.sid());
                    sender.sendMessage(Config.prefix() + "当前服务器key: " + Config.key() + "(请勿外泄!)");
                    sender.sendMessage(Config.prefix() + "当前单位: " + Config.point());
                    return;
                }

                if (args[1].equalsIgnoreCase("xf")) {
                    if (args.length == 5) {
                        String targetPlayerName = args[2];
                        String amount = args[3];
                        String reason = args[4];
                        sender.sendMessage(Config.prefix() + "§a指令已成功发送！请耐心等待。");
                        Mcrmb.runTaskAsync(new PaymentTask(sender, targetPlayerName, amount, reason));
                        return;
                    }
                    sender.sendMessage(Config.prefix() + "参数不足，示例：/" + Config.command() + " admin xf <玩家名> <金额> <用途>");
                    return;
                }


                if (args[1].equalsIgnoreCase("money")) {
                    try {
                        if (args.length == 3) {
                            sender.sendMessage(Config.prefix() + "§a指令已成功发送！请耐心等待。");
                            Mcrmb.runTaskAsync(new RetrievePlayerMoneyTask(sender, args[2]));
                            return;
                        }

                        sender.sendMessage(Config.prefix() + "参数数量不足，示例：/" + Config.command() + " admin money <玩家名>");
                    } catch (Exception e) {
                        sender.sendMessage(Config.prefix() + "操作失败，建议您联系服主咨询！");
                        Mcrmb.loggerInfo("访问接口异常 " + e.getMessage());
                    }
                    return;
                }

                if (args[1].equalsIgnoreCase("reload")) {
                    Config.reloadConfig();
                    if (Config.opModify()) {
                        Mcrmb.loggerInfo("■■■■■■■■■■■■■■■■");
                        Mcrmb.loggerInfo("服务器指令操作玩家点券：已开启");
                        Mcrmb.loggerInfo("■■■■■■■■■■■■■■■■");
                    }
                    sender.sendMessage("[MCRMB] 已经重新载入配置!");
                    Mcrmb.loggerInfo("已经重新载入配置!");
                }
            } else if (args[0].equalsIgnoreCase("setup") && sender.hasPermission("Mcrmb.admin")) {
                if (args.length == 3) {
                    Config.storeSidAndKey(args[1], args[2]);
                    sender.sendMessage("[MCRMB] Sid已经设为" + args[1] + ", Key设为" + args[2] + ", 现在为您重新载入!");
                    Mcrmb.loggerInfo("Sid已经设为" + args[1] + ", Key设为" + args[2] + ", 现在为重新载入MCRMB核心插件!");
                    Mcrmb.getInstance().getCardTypes();
                } else {
                    sender.sendMessage("§c请输入/b setup <sid> <key> , sid和key请到平台上获取！");
                }
            } else if (args[0].equalsIgnoreCase("take") && sender.hasPermission("Mcrmb.admin")) {
                if (Config.opModify()) {
                    if (args.length == 3) {
                        if (args[2].matches("^[0-9]*$")) {
                            final String targetPlayer = args[1];
                            final String amount = args[2];
                            Mcrmb.runTaskAsync(new Runnable() {
                                @Override
                                public void run() {
                                    String reason = sender.getName() + "扣款" + amount;
                                    sender.sendMessage("§b==============================================================");
                                    sender.sendMessage(Config.prefix() + "扣款操作已执行，扣款返回结果：" + PayApi.manual(targetPlayer, 2, amount, reason));
                                    sender.sendMessage("§b==============================================================");
                                }
                            });

                        } else {
                            sender.sendMessage(Config.prefix() + "点券数量必须为数字噢~");
                        }
                    } else {
                        sender.sendMessage("§c请输入/b take <玩家名> <点券数>");
                    }
                } else {
                    sender.sendMessage("§cOP操作点券功能未开启！请在config.yml文件中添加“op_modify: true”以启用。");
                }
            } else if (args[0].equalsIgnoreCase("give") && sender.hasPermission("Mcrmb.admin")) {
                if (Config.opModify()) {
                    if (args.length == 3) {
                        if (args[2].matches("^[0-9]*$")) {
                            final String targetPlayer = args[1];
                            final String amount = args[2];
                            Mcrmb.runTaskAsync(new Runnable() {
                                @Override
                                public void run() {
                                    String reason = sender.getName() + "加款" + amount;
                                    sender.sendMessage("§b==============================================================");
                                    sender.sendMessage(Config.prefix() + "加款操作已执行，加款返回结果：" + PayApi.manual(targetPlayer, 1, amount, reason));
                                    sender.sendMessage("§b==============================================================");
                                }
                            });
                        } else {
                            sender.sendMessage(Config.prefix() + "点券数量必须为数字");
                        }
                    } else {
                        sender.sendMessage("§c请输入/b give <玩家名> <点券数>");
                    }
                } else {
                    sender.sendMessage("§cOP操作点券功能未开启！请在config.yml文件中添加“op_modify: true”以启用。");
                }
            } else if (args[0].equalsIgnoreCase("set") && sender.hasPermission("Mcrmb.admin")) {
                if (Config.opModify()) {
                    if (args.length == 3) {
                        if (args[2].matches("^[0-9]*$")) {
                            final String amount = args[2];
                            final String targetPlayer = args[1];
                            Mcrmb.runTaskAsync(new Runnable() {
                                @Override
                                public void run() {

                                    String reason = sender.getName() + "重设点券" + amount;
                                    sender.sendMessage("§b==============================================================");
                                    sender.sendMessage(Config.prefix() + "重设点券操作已执行，重设点券返回结果：" + PayApi.manual(targetPlayer, 3, amount, reason));
                                    sender.sendMessage("§b==============================================================");

                                }
                            });
                        } else {
                            sender.sendMessage(Config.prefix() + "点券数量必须为数字");
                        }
                    } else {
                        sender.sendMessage("§c请输入/b set <玩家名> <点券数>");
                    }
                } else {
                    sender.sendMessage("§cOP操作点券功能未开启！请在config.yml文件中添加“op_modify: true”以启用。");
                }
            } else if (args[0].equalsIgnoreCase("test") && sender.hasPermission("Mcrmb.admin")) {
                Mcrmb.runTaskAsync(new NetworkTestTask(sender, args));
            } else {
                Bukkit.getPluginManager().callEvent(new CmdEvent(args, args[0], sender));
            }
        } else if (label.equalsIgnoreCase(Config.command() + "admin")) {
            sender.sendMessage(Config.prefix() + "命令 /" + Config.command() + "admin 已更改为=> /" + Config.command() + " admin");
        }

    }


}
