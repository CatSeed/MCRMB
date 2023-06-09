# MCRMB v1.1.0 反混淆

## 简介
我使用非常老旧的版本`MCRMB1.1.0`插件进行了反混淆，
但很遗憾我并没有为`PlaceholderAPI`和`ScoreboardStats`插件添加支持。
反混淆它只是为了让其他开发者更好地理解MCRMB的内部工作原理，
并且创建更多与之相关的扩展插件。

## 项目独特之处
该项目同时也在我古老的`1.6.4种子猫MC`服务器上运行，
这个服务器在`Java 7`环境下工作。
因此，在进行反混淆和重新编写时，
我使用了`比较老旧的语言特性和API`。

## 支持范围
本插件理论上支持Minecraft服务器版本从`1.6.4`到最新版本，并且兼容`Java 7`及其以上的任意版本。

## 什么是MCRMB？
[MCRMB](https://www.mcrmb.com)是面向Minecraft捐助行为的支付解决方案平台，自2014年开始运营至今。

## 插件指令

### 玩家指令
- `/mcrmb`：查询帮助信息
- `/mcrmb cx`：查询你自己的最后 5 条交易记录，包括赞助和消费
- `/mcrmb ck <卡号>`：查询对应的充值卡充值状态
- `/mcrmb cz <类型> <卡号> <卡密>`：卡密充值
- `/mcrmb money`：查询余额、累计赞助和消费
### 管理员指令
- `/mcrmbadmin` （已废弃，请使用 `/mcrmb admin`）
- `/mcrmb admin`：管理员指令，查看可用指令列表
- `/mcrmb setup <sid> <key>`：快速设置 Sid 和 Key
- `/mcrmb give <玩家名> <点券数>`：给予玩家点券(手动加款)
- `/mcrmb take <玩家名> <点券数>`：减少玩家点券(手动扣款)
- `/mcrmb set <玩家名> <点券数>`：设置玩家点券
- `/mcrmb admin reload`：重载插件配置
- `/mcrmb admin xf <玩家名> <金额> <用途>`：强制消费指令，需手动发奖励
- `/mcrmb admin money <玩家名>`：查询玩家的余额信息
- `/mcrmb test`：测试网络连接

## 插件权限
- `Mcrmb.admin`: 允许使用管理员指令

注意：管理员指令需要 `OP` 或对应的权限才能使用。
