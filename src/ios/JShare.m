/********* JShare.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "JSHAREService.h"
#import "JShare.h"

@interface JShare ()
  // Member variables go here.


- (void)coolMethod:(CDVInvokedUrlCommand*)command;
//- (void)isClientValid:(NSArray*)platform;
//客户端安装测试
void isClientValid(CDVInvokedUrlCommand * platform);
//分享
- (void)share:(CDVInvokedUrlCommand *)parms;
//设置微信网页分享内容
+ (JSHAREMessage *)prepareWechat:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform;
//设置微博网页分享内容
+ (JSHAREMessage *)prepareWeibo:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform;
//设置QQ网页分享内容
+ (JSHAREMessage *)prepareQQ:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform;
//设置QQ空间网页分享内容
+ (JSHAREMessage *)prepareQzone:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform;
//检查授权信息是否过期
+ (BOOL)isPlatformAuth:(JSHAREPlatform)platform;
//获取用户信息
void getUserInfo(CDVInvokedUrlCommand * platform);
@end

@implementation JShare


- (void)coolMethod:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    NSString* echo = [command.arguments objectAtIndex:0];

    if (echo != nil && [echo length] > 0) {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:echo];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
//判断软件是否存在
-(void)isClientValid:(CDVInvokedUrlCommand *)platform
{
    //获取软件名称
    NSArray *arguments=platform.arguments;
    NSString *platformName=arguments[0];
    
    CDVPluginResult* validResult=nil;
    //判断软件是否已经安装
    //因为object c 不支持switch 用string 先用if else
    if([platformName isEqualToString: @"Wechat"]){
        if([JSHAREService isWeChatInstalled]){
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"微信已经安装callback"];
        }else{
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"微信未安装callback"];
            }
    }else if([platformName isEqualToString: @"SinaWeiBo"]){
        if([JSHAREService isSinaWeiBoInstalled]){
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"微博已经安装callback"];
        }else{
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"微博未安装callback"];
        }
    }else if([platformName isEqualToString: @"QQ"]){
        if([JSHAREService isQQInstalled]){
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"QQ已经安装callback"];
        }else{
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"QQ未安装callback"];
        }
    }else if([platformName isEqualToString: @"Facebook"]){
        if([JSHAREService isFacebookInstalled]){
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Facebook已经安装callback"];
        }else{
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Facebook未安装callback"];
        }
    }else if([platformName isEqualToString: @"Twitter"]){
        if([JSHAREService isTwitterInstalled]){
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"Twitter已经安装callback"];
        }else{
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"Twitter未安装callback"];
        }
    }
    [self.commandDelegate sendPluginResult:validResult callbackId:platform.callbackId];

}

//分享
- (void)share:(CDVInvokedUrlCommand *)shareParms
{
    //获取分享参数
    NSArray *arguments=shareParms.arguments;
    
    //获取分享软件
    NSString *platformName=arguments[0];//字符串
    //获取分享类型
    int type=[arguments[1] intValue];//数值int
    //获取分享内容
    NSMutableDictionary * parmDic = [NSMutableDictionary dictionary];//键值对字典
    parmDic=arguments[2];
    
    //确定分享软件的下标
    NSInteger platformIndex;
    NSArray *platformlist=[NSArray arrayWithObjects:
     @"Wechat",@"WechatMoments",@"WechatFavorite",@"QQ",@"QZone",@"SinaWeibo",@"SinaWeiboMessage",@"Facebook",@"FbMessenger",@"Twitter",nil];
    for (int i=0; i<platformlist.count; i++) {
        if([platformlist[i] isEqualToString: platformName])
        {
            platformIndex=i+1;
        }
    }
    
    JSHAREMessage *message = [JSHAREMessage message];
            switch (platformIndex) {
                case 1:
                    message=[JShare prepareWechat:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)JSHAREPlatformWechatSession];
                    break;
                case 2:
                    message=[JShare prepareWechat:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)JSHAREPlatformWechatTimeLine];
                    break;
                case 3:
                    message=[JShare prepareWechat:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)JSHAREPlatformWechatFavourite];
                    break;
                case 4:
                    message=[JShare prepareQQ:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)JSHAREPlatformWechatFavourite];
                    break;
                case 5:
                    message=[JShare prepareQzone:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)JSHAREPlatformWechatFavourite];
                    
                    break;
                case 5:
                    break;
                case 6:
                    message = [JShare prepareWeibo:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)JSHAREPlatformSinaWeibo];
                    break;
                case 7:
                    message = [JShare prepareWeibo:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)JSHAREPlatformSinaWeiboContact];
                    break;
                case 8:
                    break;
                default:
                    break;
            }
    
    [JSHAREService share:message handler:^(JSHAREState state, NSError *error) {
        NSLog(@"分享回调");
        NSLog(@"error code :%@",error);
    }];
    
}

//设置微信分享内容
+ (JSHAREMessage *)prepareWechat:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform
{
    
    JSHAREMessage *message = [JSHAREMessage message];
    switch (type) {
        case JSHARELink:
        {
            message.mediaType = JSHARELink;
            message.url=[parmDic objectForKey:@"url"];
            message.text = [parmDic objectForKey:@"text"];
            message.title = [parmDic objectForKey:@"title"];
            message.platform = platform;
            NSString *imageURL = [parmDic objectForKey:@"image_url"];
            NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:imageURL]];
            message.image = imageData;
            break;
        }
        default:
            break;
    }
    
    return message;
}

//设置微博网页分享内容
+ (JSHAREMessage *)prepareWeibo:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform
{
    
    JSHAREMessage *message = [JSHAREMessage message];
    switch (type) {
        case JSHARELink:
        {
            message.mediaType = JSHARELink;
            message.url=[parmDic objectForKey:@"url"];
            message.text = [parmDic objectForKey:@"text"];
            message.title = [parmDic objectForKey:@"title"];
            message.platform = platform;
            NSString *imageURL = [parmDic objectForKey:@"image_url"];
            NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:imageURL]];
            message.image = imageData;
            break;
        }
        default:
            break;
    }
    
    return message;
}

//设置QQ网页分享内容
+ (JSHAREMessage *)prepareQQ:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform
{
    
    JSHAREMessage *message = [JSHAREMessage message];
    switch (type) {
        case JSHARELink:
        {
            message.mediaType = JSHARELink;
            message.url=[parmDic objectForKey:@"url"];
            message.text = [parmDic objectForKey:@"text"];
            message.title = [parmDic objectForKey:@"title"];
            message.platform = platform;
            NSString *imageURL = [parmDic objectForKey:@"image_url"];
            NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:imageURL]];
            message.image = imageData;
            break;
        }
        default:
            break;
    }
    
    return message;
}
//设置QQ网页分享内容
+ (JSHAREMessage *)prepareQzone:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform
{
    
    JSHAREMessage *message = [JSHAREMessage message];
    switch (type) {
        case JSHARELink:
        {
            message.mediaType = JSHARELink;
            message.url=[parmDic objectForKey:@"url"];
            message.text = [parmDic objectForKey:@"text"];
            message.title = [parmDic objectForKey:@"title"];
            message.platform = platform;
            NSString *imageURL = [parmDic objectForKey:@"image_url"];
            NSData *imageData = [NSData dataWithContentsOfURL:[NSURL URLWithString:imageURL]];
            message.image = imageData;
            break;
        }
        default:
            break;
    }
    
    return message;
}
BOOL isOauth = [JSHAREService isPlatformAuth:JSHAREPlatformQQ];
//获取用户信息

- (void) getUserInfo:(CDVInvokedUrlCommand *)platform
{
    //获取软件名称
    NSArray *arguments=platform.arguments;
    NSString *platformName=arguments[0];
    //返回信息
    __block CDVPluginResult* validResult=nil;
    
    [JSHAREService getSocialUserInfo:platformName handler:^(JSHARESocialUserInfo *userInfo, NSError *error) {
        NSString *alertMessage;
        NSString *title;
        if (error) {
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsString:@"失败，无法获取用户信息"];
        }else{
            title = userInfo.name;
            alertMessage = [NSString stringWithFormat:@"昵称: %@\n 头像链接: %@\n 性别: %@\n",userInfo.name,userInfo.iconurl,userInfo.gender == 1? @"男" : @"女"];
            validResult=[CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:alertMessage];
        }
        UIAlertView *Alert = [[UIAlertView alloc] initWithTitle:title message:alertMessage delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
        dispatch_async(dispatch_get_main_queue(), ^{
            [Alert show];
        });
        
        
    }];
    [self.commandDelegate sendPluginResult:validResult callbackId:platform.callbackId];
}
@end
