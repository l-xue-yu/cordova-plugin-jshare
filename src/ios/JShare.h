//
//  JShare.h
//  高球魔镜
//
//  Created by  on 2018/9/17.
//

#ifndef JShare_h
#define JShare_h
#import "JSHAREService.h"

#endif /* JShare_h */

@interface JShare : CDVPlugin

- (void)isClientValid:(CDVInvokedUrlCommand *)platform;
- (void)share:(CDVInvokedUrlCommand *)parms;
//设置微信网页分享内容
+ (JSHAREMessage *)prepareWechat:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform;
//设置微博网页分享内容
+ (JSHAREMessage *)prepareWeibo:(NSMutableDictionary *)parmDic num2:(int)type num3:(NSUInteger)platform;
@end
