package com.zgwit.jshare;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.webkit.URLUtil;

import org.apache.cordova.CallbackContext;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Util {

    

    //新建存储图片的目录
    public static File getCacheFolder(Context context) {
        File cacheDir = null;
        //获取外部存储状态并判断是否可以读取
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //在app外部存储目录下新建cache文件夹实例
            cacheDir = new File(Environment.getExternalStorageDirectory(), "cache");
            if (!cacheDir.isDirectory()) {
                cacheDir.mkdirs();
            }
        }

        if(!cacheDir.isDirectory()) {
            cacheDir = context.getCacheDir(); //get system cache folder
        }

        return cacheDir;
    }

      /**
       * 将文件下载到新建的目录下
       *
       *@param context
       *@parm String url 图片的网址
       *@parm Context context webView,getContext(),用来新建app的外部存储空间
       *@return File
       *@throws IOException
       */
    public static File downloadAndCacheFile(Context context, String url) {
        URL fileURL = null;
        //callbackContext.error("进入downloadAndCacheFile，url:"+url);
        try {
            fileURL = new URL(url);

            Log.e("TAG", String.format("Start downloading file at %s.", url));

            HttpURLConnection connection = (HttpURLConnection) fileURL.openConnection();
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("TAG", String.format("Failed to download file from %s, response code: %d.", url, connection.getResponseCode()));
                 //callbackContext.error("Failed to download file,url"+url);
                return null;
            }

            InputStream inputStream = connection.getInputStream();//返回InputStream表示数据的内容(返回的是inputStream)

            File cacheDir = getCacheFolder(context);//获取新建的外部存储空间文件夹的实例对象
            File cacheFile = new File(cacheDir, url.substring(url.lastIndexOf("/") + 1));//从cachDir后加上图片文件名获取新的文件实例
            FileOutputStream outputStream = new FileOutputStream(cacheFile);//创建文件输出流以写入由指定File对象表示的文件。

            byte buffer[] = new byte[4096];
            int dataSize;
            // 将数据写入cachFile
            while ((dataSize = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, dataSize);
            }
            outputStream.close();

            Log.e("TAG", String.format("File was downloaded and saved at %s.", cacheFile.getAbsolutePath()));

            return cacheFile;//返回下载的文件对象实例
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //将问图片转成bitmap并压缩成新的bitmap

    public static Bitmap getBitmap(String image_data,Context context,int maxSize){

      Bitmap bmp=null;
      InputStream inputStream = getFileInputStream(image_data,context);//获取下载的文件图片输入流

      // decode it
      // @TODO make sure the image is not too big, or it will cause out of memory
      BitmapFactory.Options options = new BitmapFactory.Options();
      bmp = BitmapFactory.decodeStream(inputStream, null, options);

      // scale
      if (maxSize > 0 && (options.outWidth > maxSize || options.outHeight > maxSize)) {

          Log.e("TAG", String.format("Before scale, width : %d x height: %d, max allowed size: %d.beginByteCount:%d, AllocationByteCountsize:%d",
                  options.outWidth, options.outHeight, maxSize,bmp.getByteCount(),bmp.getAllocationByteCount()));
          int width = 0;
          int height = 0;
          if (options.outWidth > options.outHeight) {
              width = maxSize;
              height = width * options.outHeight / options.outWidth;//为了保持原图片的宽高比
          } else {
              height = maxSize;
              width = height * options.outWidth / options.outHeight;
          }
          Bitmap scaled = Bitmap.createScaledBitmap(bmp, width, height, true);
          int scaleSize=scaled.getAllocationByteCount();
           Log.e("TAG", String.format("after scaled  width: %d x height: %d,  beginByteCount:%d, AllocationByteCountsize:%d.",
                            scaled.getWidth(), scaled.getHeight(), scaled.getByteCount(),scaleSize));


          bmp = scaled;
      }
      return bmp;

    }

    //将图片文件转成输入流
    public static InputStream getFileInputStream(String image_data,Context context){
        try{
          InputStream inputStream = null;
          if(URLUtil.isHttpUrl(image_data) || URLUtil.isHttpsUrl(image_data)){
            File file =downloadAndCacheFile(context, image_data);//将从网址下载的图片的File实例对象给file

            if(file==null){
             Log.e("TAG", String.format("Failed to download file from %s", image_data ));
              return null;
            }
            inputStream = new FileInputStream(file);//返回文件输入流，（准备转成bitmap）
          }


        return inputStream;

        }catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
     }


}
