# WeTeBot

WeChatBot , 集成了定时任务的聊天机器人

## Getting Started

### 运行环境
* JDK 8以上
* 由于基于quartz做了持久化的任务,所以需要数据库自持,这里我选择了MySQL(首次运行需要配置MySQL)
* WeTeBot还集成了天气提醒任务,这里选择了[和风天气](https://www.heweather.com/)提供天气服务(需要自己去注册一个免费账号然后将key填入api.weather.key)
* 另外还集成了腾讯提供的[聊天机器人](https://ai.qq.com/product/nlpchat.shtml)接口(需自行去注册免费账号,将相关参数填入api.chat)
### java -jar 启动后会在终端输出登录二维码,扫描登录即可

### 提醒任务设置
![提醒任务命令](https://github.com/ChaunceyXCX/WeTeBot/blob/master/images/%E6%8F%90%E9%86%92.png)
### 天气任务设置
![天气任务命令](https://github.com/ChaunceyXCX/WeTeBot/blob/master/images/%E5%A4%A9%E6%B0%94.png)


## TODO

-[ ] 接入telegram
-[ ] 微信其它接口处理
    - [ ] 语音消息等,目前主要是文本消息处理
    - [x] 但是本项目是基于itchat4j做的相关接口都在**WeChatApi.java**里面定义,可以直接使用


## 已知问题
* 收到提醒命令后脑残忘了用正则解析,自己手动分割字符串实现的所以会有你n多bug,请严格按照样例发送, ps: 所有命令字符必须用半角字符
* 因为用的微信网页版接口,之前自测会在一个月后被微信官方强制下线,暂时没有做热登录,
## 警告
使用任何微信机器人，包括WeTeBot，都有可能导致您的账号被禁用。不对此负责，请谨慎使用。

## 感谢
WeTeBot 是受到 [jeeves](https://github.com/kanjielu/jeeves) and [itchat4j](https://https://github.com/yaphone/itchat4j.com/Urinx/WeixinBot) 的启发而创建的。

## 问题反馈
有任何bug或者疑问，请使用 [Github Issues](https://github.com/ChaunceyXCX/WeTeBot/issues).

## 协议

[MIT](LICENSE)
