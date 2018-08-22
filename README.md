# cordova-plugin-jshare
用于分享的安卓cordova插件，目前支持微信，微博。<br>
#功能特性
微信的好友分享，朋友圈分享，收藏和微博分享在安卓手机上正常分享。
#插件依赖
cordova-plugin-jshare插件依赖[极光](https://www.jiguang.cn/)的一个核心插件[cordova-plugin-jcore](https://github.com/jpush/cordova-plugin-jcore)。所以需要先安装cordova-plugin-jcore插件。cordova-plugin-jshare的子插件包括：
1. [cordova-plugin-jshare-wechat](https://github.com/l-xue-yu/cordova-plugin-jshare-wechat)
2. [cordova-plugin-jshare-qq](https://github.com/l-xue-yu/cordova-plugin-jshare-qq)
3. [cordova-plugin-jshare-weibo](https://github.com/l-xue-yu/cordova-plugin-jshare-weibo)
4. [cordova-plugin-jshare-facebook](https://github.com/l-xue-yu/cordova-plugin-jshare-facebook)
5. [cordova-plugin-jshare-twitter](https://github.com/l-xue-yu/cordova-plugin-jshare-twitter)
#插件安装
建议写一个批处理文件来安装所有的插件。如下：
@echo off
call cordova plugin add cordova-plugin-jcore
call cordova plugin add cordova-plugin-jshare --variable JPUSH_APPKEY=value --variable JPUSH_CHANNEL=default
call cordova plugin add cordova-plugin-jshare-wechat --variable  WECHAT_APPID=value --variable WECHAT_APPSECRET=value
call cordova plugin add cordova-plugin-jshare-qq --variable  QQ_APPID=value --variable QQ_APPKEY=value
call cordova plugin add cordova-plugin-jshare-weibo --variable  WEIBO_APPKEY=value --variable WEIBO_APPSECRET=value --variable WEIBO_REDIRECT_URL=value
call cordova plugin add cordova-plugin-jshare-facebook --variable  FACEBOOK_APPID=value --variable FACEBOOK_APPNAME=value
call cordova plugin add cordova-plugin-jshare-twitter --variable  TWITTER_CONSUMERKEY=value --variable TWITTER_CONSUMERSECRET=value
###注意
1. 不建议更改插件的安装顺序。
2. JPUSH_APPKEY的值必须有
