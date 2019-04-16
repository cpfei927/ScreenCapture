package com.cpfei.screencapture;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by cpfei on 2019/4/16
 * Description:
 */
public class ScreenShotUtils {

    /**
     * 截取WebView内容
     * 保存图片需要权限，如下：
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     * <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
     *
     * @param webView
     * @return 保存截图路径
     * 在5.0版本后，在webview初始化前添加如下代码，保证截取整个webview，
     * 但是牺牲性能（5.0后对webview做了优化，只加载显示区域的内容）
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
     *      WebView.enableSlowWholeDocumentDraw();
     * }
     */
    public static String screenWeb(WebView webView) {
        Picture snapShot = webView.capturePicture();
        if (snapShot != null && snapShot.getWidth() > 0 && snapShot.getHeight() > 0) {
            Bitmap bitmap = Bitmap.createBitmap(snapShot.getWidth(), snapShot.getHeight(), Bitmap.Config.ARGB_8888);
            //设置相应的图片质量
            Canvas canvas = new Canvas(bitmap);
            snapShot.draw(canvas);
            return saveImage(bitmap);
        }
        return null;
    }

    public static String saveImage(Bitmap bitmap) {
        //将截取的图片保存到本地
        try {
            String dirPath = Environment.getExternalStorageDirectory() + "/testpic/app";
            File appFile = new File(dirPath);
            if (!appFile.exists() && !appFile.isDirectory()) {
                appFile.mkdirs();
            }
            String fileName = dirPath + "img_" + System.currentTimeMillis() + ".jpg";
            FileOutputStream fos = new FileOutputStream(fileName);
            //设置保存本地图片质量
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fos);
            fos.flush();
            fos.close();
            return fileName;
        } catch (Exception e) {
            Log.d("Tag", e.getMessage());
        }
        return null;
    }


    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap captureWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, 0, dm.widthPixels, dm.heightPixels);
        view.destroyDrawingCache();
        return ret;
    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity activity
     * @return Bitmap
     */
    public static Bitmap captureWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int statusBarHeight = getStatusBarHeight(activity);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Bitmap ret = Bitmap.createBitmap(bmp, 0, statusBarHeight, dm.widthPixels, dm.heightPixels - statusBarHeight);
        view.destroyDrawingCache();
        return ret;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

}
