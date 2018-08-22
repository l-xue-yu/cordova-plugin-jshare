# cordova-plugin-jshare
___
用于分享的cordova插件，目前只支持安卓。<br>
## 功能特性<br>
___
微信的好友分享，朋友圈分享，收藏和微博分享在安卓手机上正常分享。<br>
## 插件依赖<br>
___
cordova-plugin-jshare插件依赖[极光](https://www.jiguang.cn/)的一个核心插件[cordova-plugin-jcore](https://github.com/jpush/cordova-plugin-jcore)。所以需要先安装cordova-plugin-jcore插件。cordova-plugin-jshare的子插件包括：<br>
1. [cordova-plugin-jshare-wechat](https://github.com/l-xue-yu/cordova-plugin-jshare-wechat)
2. [cordova-plugin-jshare-qq](https://github.com/l-xue-yu/cordova-plugin-jshare-qq)
3. [cordova-plugin-jshare-weibo](https://github.com/l-xue-yu/cordova-plugin-jshare-weibo)
4. [cordova-plugin-jshare-facebook](https://github.com/l-xue-yu/cordova-plugin-jshare-facebook)
5. [cordova-plugin-jshare-twitter](https://github.com/l-xue-yu/cordova-plugin-jshare-twitter)<br>
## 插件安装<br>
___
建议写一个批处理文件来安装所有的父插件和子插件。如下：<br>
@echo off<br>
call cordova plugin add cordova-plugin-jcore<br>
call cordova plugin add cordova-plugin-jshare --variable JPUSH_APPKEY=value --variable JPUSH_CHANNEL=default<br>
call cordova plugin add cordova-plugin-jshare-wechat --variable  WECHAT_APPID=value --variable WECHAT_APPSECRET=value<br>
call cordova plugin add cordova-plugin-jshare-qq --variable  QQ_APPID=value --variable QQ_APPKEY=value<br>
call cordova plugin add cordova-plugin-jshare-weibo --variable  WEIBO_APPKEY=value --variable WEIBO_APPSECRET=value --variable WEIBO_REDIRECT_URL=value<br>
call cordova plugin add cordova-plugin-jshare-facebook --variable  FACEBOOK_APPID=value --variable FACEBOOK_APPNAME=value<br>
call cordova plugin add cordova-plugin-jshare-twitter --variable  TWITTER_CONSUMERKEY=value --variable TWITTER_CONSUMERSECRET=value<br>
### 注意<br>
* 不建议更改插件的安装顺序。
* JPUSH_APPKEY的值必须有
## 使用<br>
html:<br>
```html
<a (click)="testWechat()">测试微信好友网页分享</a><br>
```
ts:<br>
```Javascript
testWechat() {
    // 是否安装微信
    window.JShare.isClientValid('Wechat', (res) => {console.log(res + "微信已安装")}, (reason) => { console.log(reason + '微信未安装')
    });
    //分享
    window.JShare.share('Wechat', 3,
      {
        title: "Title", text: "Text", url: "https://www.baidu.com",
        image_url: "http://www.shijieditu.net/ditu/allimg/170829/1-1FR9235I0501.jpg"
      },
      () => {
        console.log('成功');
      },
      (reason) => {
        console.log(reason + '失败分享');
      });
  }
```
