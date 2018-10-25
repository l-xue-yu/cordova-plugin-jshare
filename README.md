# cordova-plugin-jshare
___
<<<<<<< HEAD
用于分享的cordova插件，目前只支持安卓。<br>
## 功能特性<br>
___
微信的好友分享，朋友圈分享，收藏，微博分享和微信登录。<br>
=======
用于分享的cordova插件，支持安卓和ios分享。<br>
## 功能特性<br>
___
微信的好友分享，朋友圈分享，收藏和微博分享在安卓手机和苹果手机正常分享（ios现在只写了web分享，以后应该会添加其它的分享类型。）。<br>
>>>>>>> 998086430edbbd25c87268ad48d89afdb28793c7
## 插件依赖<br>
___
因为cordova-plugin-jshare插件依赖[极光](https://www.jiguang.cn/)的一个核心插件[cordova-plugin-jcore](https://github.com/jpush/cordova-plugin-jcore)。所以需要先安装cordova-plugin-jcore插件。cordova-plugin-jshare的子插件包括：<br>
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
```javascript
declare var window:any;
//初始化极光分享在app.component.ts的constructor里
 window.JShare.init();
```

html（使用了ionic框架）:<br>
```html
<a (click)="testWechat()">测试微信好友网页分享</a><br>
<a (click)="testWechatMoments()">测试微信朋友圈网页分享</a><br>
<a (click)="testWechatFavorite()">测试微信收藏网页分享</a><br>
```
ts:<br>
```Javascript
//分享开始
//全局变量window
declare var window:any;

testWechat() {
    // 是否安装微信
    window.JShare.isClientValid('Wechat', (res) => {console.log(res + "微信已安装")}, (reason) => { console.log(reason + '微信未安装')
    });
    //微信好友网页分享
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
  //微信朋友圈网页分享
 testWechatMoments() {
    window.JShare.share('WechatMoments', 3,
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
  //微信收藏网页
  testWechatFavorite() {
    window.JShare.share('WechatFavorite', 3,
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
//分享结束
```
在AppDelegate.m中进行初始化和设置回调，微信回调必须有Appsecript

```Object c
//微信登录，初始化和回调开始
@implementation AppDelegate
```

ios jshare初始化和回调<br>
程序ionic cordova build ios（编译ios） 后，在AppDelegate.m文件中进行jshare的初始化和回调。
先在头部引入jshare-ios-1.6.0.a的头文件
```Object C

#import "JSHAREService.h"
```
然后在didFinishLaunchingWithOptions钩子中进行初始化，handleOpenURL进行回调设置
```Object C
//初始化
- (BOOL)application:(UIApplication*)application didFinishLaunchingWithOptions:(NSDictionary*)launchOptions
{
    self.viewController = [[MainViewController alloc] init];
    //极光分享初始化
    JSHARELaunchConfig *config = [[JSHARELaunchConfig alloc] init];
    config.appKey = @"valueappkey";
    config.SinaWeiboAppKey = @"valueappkey";
    config.SinaWeiboAppSecret = @"valuesrcret";
    config.SinaRedirectUri = @"value";
    config.QQAppId = @"valueappkey";
    config.QQAppKey = @"valueappkey";
    config.WeChatAppId = @"valueappkey";
    config.WeChatAppSecret = @"value";
    config.FacebookAppID = @"valueappkey";
    config.FacebookDisplayName = @"valueappkey";
    config.TwitterConsumerKey = @"valueappkey";
    config.TwitterConsumerSecret = @"valueappkey";
    config.JChatProAuth = @"valueappkey";
    [JSHAREService setupWithConfig:config];
    [JSHAREService setDebug:YES];
    
    return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

//回调低版本
//回调
- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url{
    [JSHAREService handleOpenUrl:url];
    return YES;
}

//回调高版本
- (BOOL)application:(UIApplication *)application openURL:(NSURL *)url options:(nonnull NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options{
    [JSHAREService handleOpenUrl:url];
    return YES;
}
@end
//微信登录，初始化和回调结束
```
```Javscript
//牵扯到js的回调嵌套，希望提出问题，给更好的方法来调用。
weixin_authorie(cb) {
    if (this.platform.is('android')) {
      window.JShare.isAuthorize('Wechat', ()=>cb(), err => {
        window.JShare.authorize('Wechat', ()=>cb(), err => cb(err));
      });
    } else if (this.platform.is('ios')) {
      cb();
    } else {
      //error
      cb('未支持的平台');
    }
  }

  //微信登录
  login_weixin() {
    this.weixin_authorie(err => {
      if (err) {
        this.common.createAlert('授权失败', err).present();
        return;
      }
      window.JShare.getUserInfo('Wechat', (userinfo) => {
        //获取微信用户数据成功
        console.log(userinfo);
        this.weixin_auth = this.platform.is('android') ? JSON.parse(userinfo) : userinfo;

      }, (error) => {
        alert("获取用户信息错误", error).present();
      });

    });
  }

```
分享的软件，分享的类型，分享的JSONArray参数，请查看JShare.js。<br>
## 调用层级关系
>页面的ts
>>www目录下JShare.js
>>>src目录
>>>>android
>>>>>JShare.java
>>>>>Util.java
## 可能遇到的问题
1. 微信等软件的开发者账号下的软件ID和KEY没有写对。
2. 软件没有读写手机存储空间的权限。<br>
## 其它<br>
* 图片的分享，默认下载后转bitmap，然后压缩，因为微信的32K图片分享要求。如果你想修改图片下载相关的代码请查看Util.java。
* 如果遇到不明显的问题，请打包安装到手机后，使用Android studio的logcat来查看具体的错误描述。比如参数出错时，极光会有错误码。当然你也可以自己添加log。
