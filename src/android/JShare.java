package com.zgwit.jshare;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.webkit.URLUtil;

import java.lang.Exception;
import java.util.List;
import java.util.HashMap;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatformConfig;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.android.api.AuthListener;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.model.BaseResponseInfo;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class echoes a string called from JavaScript.
 */
public class JShare extends CordovaPlugin {

    //微信图片最大上传大小
    public static final int MAX_THUMBNAIL_SIZE = 320;


    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (action.equals("setDebugMode")) {
                this.setDebugMode(args);
            } else if (action.equals("init")) {
                this.init();
            } else if (action.equals("getPlatformList")) {
                this.getPlatformList(callbackContext);
            } else if (action.equals("isClientValid")) {
                this.isClientValid(args, callbackContext);
            } else if (action.equals("share")) {
                this.share(args, callbackContext);
            } else if (action.equals("isSupportAuthorize")) {
                this.isSupportAuthorize(args, callbackContext);
            } else if (action.equals("isAuthorize")) {
                this.isAuthorize(args, callbackContext);
            } else if (action.equals("removeAuthorize")) {
                this.removeAuthorize(args, callbackContext);
            } else if (action.equals("authorize")) {
                this.authorize(args, callbackContext);
            } else if (action.equals("getUserInfo")) {
                this.getUserInfo(args, callbackContext);
            } else {
                return false;
            }
            return true;
        } catch (Exception ex) {
            callbackContext.error(ex.getMessage());
            return true;
        }
    }

    private void setDebugMode(JSONArray params) throws JSONException{
        boolean enable = params.getBoolean(0);
        JShareInterface.setDebugMode(enable);
    }

    private void getPlatformList(CallbackContext callbackContext) {
        List<String> list = JShareInterface.getPlatformList();
        callbackContext.success(new JSONArray(list));
    }

    private void isClientValid(JSONArray params, CallbackContext callbackContext) throws JSONException{
        String platform = params.getString(0);
        boolean valid = JShareInterface.isClientValid(platform);
        if (valid) callbackContext.success("软件支持");
        else callbackContext.error("invalid");
    }

    private void init(){
        PlatformConfig config = new PlatformConfig();

        //微信配置
        String wechatAppId = preferences.getString("WECHAT_APPID", null);
        String wechatAppSecret = preferences.getString("WECHAT_APPSECRET", null);
        if (wechatAppId != null && wechatAppSecret != null)
            config.setWechat(wechatAppId, wechatAppSecret);

        //QQ配置
        String qqAppId = preferences.getString("QQ_APPID", null);
        String qqAppKey = preferences.getString("QQ_APPKEY", null);
        if (qqAppId != null && qqAppKey != null)
            config.setQQ(qqAppId, qqAppKey);

        //微博配置
        String weiboAppKey = preferences.getString("WEIBO_APPKEY", null);
        String weiboAppSecret = preferences.getString("WEIBO_APPSECRET", null);
        String weiboRedirectUrl = preferences.getString("WEIBO_REDIRECT_URL", null);
        if (weiboAppKey != null && weiboAppSecret != null)
            config.setSinaWeibo(weiboAppKey, weiboAppSecret, weiboRedirectUrl);

        //Facebook配置
        String facebookAppId = preferences.getString("FACEBOOK_APPID", null);
        String facebookAppName = preferences.getString("FACEBOOK_APPNAME", null);
        if (facebookAppId != null && facebookAppName != null)
            config.setFacebook(facebookAppId, facebookAppName);

        //Twitter配置
        String twitterConsumerKey = preferences.getString("TWITTER_CONSUMERKEY", null);
        String twitterConsumerSecret = preferences.getString("TWITTER_CONSUMERSECRET", null);
        if (twitterConsumerKey != null && twitterConsumerSecret != null)
            config.setTwitter(twitterConsumerKey, twitterConsumerSecret);


        JShareInterface.init(webView.getContext(), config);
    }

     /**
      * 微信分享准备，将分享的各个参数写进sp
      *
      *@param int type 分享类型
      *@parm JSONObject param 分享内容的JSONObject
      * @return File
      * @throws JSONException
      */
    private ShareParams prepareWechat(int type, JSONObject param) throws JSONException{
        ShareParams sp = new ShareParams();
        sp.setShareType(type);

        switch (type) {
            case Platform.SHARE_TEXT:
                sp.setText(param.getString("text"));
                break;
            case Platform.SHARE_IMAGE:
                 Bitmap Wechatbitmap = Util.getBitmap(param.getString("image_url"),webView.getContext(),MAX_THUMBNAIL_SIZE);
                sp.setImageData(Wechatbitmap);
                break;
            case Platform.SHARE_WEBPAGE:
                if (param.has("title"))
                sp.setTitle(param.getString("title"));
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                if(param.has("image_url")){
                  Bitmap bitmap = Util.getBitmap(param.getString("image_url"),webView.getContext(),MAX_THUMBNAIL_SIZE);
                  sp.setImageData(bitmap);
                }
                //sp.setImageUrl(param.getString("image_url"));
                break;
            case Platform.SHARE_MUSIC:
                if (param.has("title"))
                sp.setTitle(param.getString("title"));
                if (param.has("text"))
                sp.setText(param.getString("text"));
                if (param.has("url"))
                sp.setUrl(param.getString("url"));
                sp.setMusicUrl(param.getString("music_url"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                //sp.setImageUrl(param.getString("image_url"));
                break;
            case Platform.SHARE_VIDEO:
                if (param.has("title"))
                sp.setTitle(param.getString("title"));
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                break;
            case Platform.SHARE_FILE:
                sp.setFilePath(param.getString("file_path"));
                break;
            default:
                break;
        }
        return sp;
    }

    private ShareParams prepareQQ(int type, JSONObject param) throws JSONException{
        ShareParams sp = new ShareParams();
        sp.setShareType(type);

        switch (type) {
            case Platform.SHARE_IMAGE:
                sp.setImagePath(param.getString("image_path"));
                break;
            case Platform.SHARE_WEBPAGE:
                if (param.has("title"))
                sp.setTitle(param.getString("title"));
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                if (param.has("image_url"))
                sp.setImageUrl(param.getString("image_url"));
                break;
            case Platform.SHARE_MUSIC:
                if (param.has("title"))
                sp.setTitle(param.getString("title"));
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                sp.setMusicUrl(param.getString("music_url"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                if (param.has("image_url"))
                sp.setImageUrl(param.getString("image_url"));
                break;
            default:
                break;
        }
        return sp;
    }

    private ShareParams prepareQZone(int type, JSONObject param) throws JSONException{
        ShareParams sp = new ShareParams();
        sp.setShareType(type);

        switch (type) {
            case Platform.SHARE_TEXT:
                sp.setText(param.getString("text"));
                break;
            case Platform.SHARE_IMAGE:
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                if (param.has("image_url"))
                sp.setImageUrl(param.getString("image_url"));
                break;
            case Platform.SHARE_WEBPAGE:
                sp.setTitle(param.getString("title"));
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                if (param.has("image_url"))
                sp.setImageUrl(param.getString("image_url"));
                break;
            case Platform.SHARE_MUSIC:
                sp.setTitle(param.getString("title"));
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                if (param.has("music_url"))
                sp.setMusicUrl(param.getString("music_url"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                if (param.has("image_url"))
                sp.setImageUrl(param.getString("image_url"));
                break;
            case Platform.SHARE_VIDEO:
                sp.setVideoPath(param.getString("video_path"));
                break;
            default:
                break;
        }
        return sp;
    }

    private ShareParams prepareSinaWeibo(int type, JSONObject param) throws JSONException{
        ShareParams sp = new ShareParams();
        sp.setShareType(type);

        switch (type) {
            case Platform.SHARE_TEXT:
                sp.setText(param.getString("text"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                break;
            case Platform.SHARE_IMAGE:
                if (param.has("text"))
                sp.setText(param.getString("text"));
                 Bitmap bitmap = Util.getBitmap(param.getString("image_url"),webView.getContext(),MAX_THUMBNAIL_SIZE);
                 sp.setImageData(bitmap);
                //sp.setImagePath(param.getString("image_path"));
                break;
            case Platform.SHARE_WEBPAGE:
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                if (param.has("image_url")){
                  Bitmap bitmapSinaWeiboImage = Util.getBitmap(param.getString("image_url"),webView.getContext(),MAX_THUMBNAIL_SIZE);
                  sp.setImageData(bitmapSinaWeiboImage);
                }
                //sp.setImagePath(param.getString("image_path"));
                //sp.setImageUrl(param.getString("image_url"));
                break;
            default:
                break;
        }
        return sp;
    }


    private ShareParams prepareSinaWeiboMessage(int type, JSONObject param) throws JSONException{
        ShareParams sp = new ShareParams();
        sp.setShareType(type);
         
        switch (type) {
            case Platform.SHARE_WEBPAGE:
                if (param.has("title"))
                sp.setTitle(param.getString("title"));
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                if (param.has("image_path"))
                sp.setImagePath(param.getString("image_path"));
                if (param.has("image_url")){
                   Bitmap bitmapSinaWeiboMessageImage = Util.getBitmap(param.getString("image_url"),webView.getContext(),MAX_THUMBNAIL_SIZE);
                  sp.setImageData(bitmapSinaWeiboMessageImage);
                }
                //sp.setImageUrl(param.getString("image_url"));
                break;
            default:
                break;
        }
        return sp;
    }


    private ShareParams prepareFacebook(int type, JSONObject param) throws JSONException{
        ShareParams sp = new ShareParams();
        sp.setShareType(type);

        switch (type) {
            case Platform.SHARE_IMAGE:
                sp.setImagePath(param.getString("image_path"));
                break;
            case Platform.SHARE_WEBPAGE:
                sp.setUrl(param.getString("url"));
                if (param.has("quote"))
                sp.setImagePath(param.getString("quote"));
                //sp.setImageUrl(param.getString("image_url"));
                break;
            case Platform.SHARE_VIDEO:
                sp.setVideoPath(param.getString("video_path"));
                break;
            default:
                break;
        }
        return sp;
    }

    private ShareParams prepareTwitter(int type, JSONObject param) throws JSONException{
        ShareParams sp = new ShareParams();
        sp.setShareType(type);

        switch (type) {
            case Platform.SHARE_TEXT:
                sp.setText(param.getString("text"));
                break;
            case Platform.SHARE_IMAGE:
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                sp.setImagePath(param.getString("image_path"));
                break;
            case Platform.SHARE_WEBPAGE:
                sp.setUrl(param.getString("url"));
                if (param.has("text"))
                sp.setText(param.getString("text"));
                break;
            case Platform.SHARE_VIDEO:
                if (param.has("text"))
                sp.setText(param.getString("text"));
                sp.setUrl(param.getString("url"));
                sp.setVideoPath(param.getString("video_path"));
                break;
            default:
                break;
        }
        return sp;
    }

    /**
    * 分享
    *
    *@parm JSONArray params 分享的JSONArray
    *@parm CallbackContext callbackContext 描述回调运行的上下文。
    * @return
    * @throws JSONException
    */
    private void share(JSONArray params, CallbackContext callbackContext) throws JSONException{
        ShareParams sp = null;

        String platform = params.getString(0);
        int type = params.getInt(1);
        JSONObject param = params.getJSONObject(2);

        switch (platform) {
            case "Wechat":
            case "WechatMoments":
            case "WechatFavorite":
                sp = prepareWechat(type, param);
                break;
            case "QQ":
                sp =  prepareQQ(type, param);
                break;
            case "QZone":
                sp =  prepareQZone(type, param);
                break;
            case "SinaWeibo":
                sp = prepareSinaWeibo(type, param);
                break;
            case "SinaWeiboMessage":
                sp=prepareSinaWeiboMessage(type,param);
            case "FaceBook":
            case "FbMessager":
                sp = prepareFacebook(type, param);
                break;
            case "Twitter":
                sp = prepareTwitter(type, param);
                break;
            default:
                break;
        }

        JShareInterface.share(platform, sp, new PlatActionListener(){
            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> args){
                callbackContext.success("分享成功");
            }
            @Override
            public void onError(Platform platform, int action, int paramInt2, Throwable throwable){
            //错误堆栈转成字符串
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            throwable.printStackTrace(pw);
            String msg=sw.toString();
                callbackContext.error("错误堆栈 ："+msg+",简短描述："+"极光错误码："+paramInt2);
            }
            @Override
            public void onCancel(Platform platform, int action){
                callbackContext.error("取消分享");
            }
        });

    }

    private void isSupportAuthorize(JSONArray params, CallbackContext callbackContext) throws JSONException{
        String platform = params.getString(0);
        boolean support = JShareInterface.isSupportAuthorize(platform);
        if (support) callbackContext.success();
        else callbackContext.error("不支持");
    }

    private void isAuthorize(JSONArray params, CallbackContext callbackContext) throws JSONException {
        String platform = params.getString(0);
        boolean authorized = JShareInterface.isAuthorize(platform);
        if (authorized) callbackContext.success();
        else callbackContext.error("未授权");
    }

    private void removeAuthorize(JSONArray params, CallbackContext callbackContext) throws JSONException {
        String platform = params.getString(0);
        JShareInterface.removeAuthorize(platform, new AuthListener() {
            @Override
            public void onComplete(Platform platform, int action, BaseResponseInfo info){
                callbackContext.success();
            }
            @Override
            public void onError(Platform platform, int action, int j, Throwable throwable){
                callbackContext.error(throwable.getMessage());
            }
            @Override
            public void onCancel(Platform platform, int action){
                callbackContext.error("取消");
            }
        });
    }

    private void authorize(JSONArray params, CallbackContext callbackContext) throws JSONException {
        String platform = params.getString(0);
        JShareInterface.authorize(platform, new AuthListener() {
            @Override
            public void onComplete(Platform platform, int action, BaseResponseInfo info){
                callbackContext.success(info.getOriginData());
            }
            @Override
            public void onError(Platform platform, int action, int j, Throwable throwable){
                callbackContext.error(throwable.getMessage());
            }
            @Override
            public void onCancel(Platform platform, int action){
                callbackContext.error("取消");
            }
        });
    }

    private void getUserInfo(JSONArray params, CallbackContext callbackContext) throws JSONException {
        String platform = params.getString(0);
        JShareInterface.getUserInfo(platform, new AuthListener() {
            @Override
            public void onComplete(Platform platform, int action, BaseResponseInfo info){
                callbackContext.success(info.getOriginData());
            }
            @Override
            public void onError(Platform platform, int action, int j, Throwable throwable){
                callbackContext.error(throwable.getMessage());
            }
            @Override
            public void onCancel(Platform platform, int action){
                callbackContext.error("取消");
            }
        });
    }

    
}
