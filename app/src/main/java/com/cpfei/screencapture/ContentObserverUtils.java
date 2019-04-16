package com.cpfei.screencapture;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;

/**
 * Created by cpfei on 2019/4/16
 * Description:
 * 监听屏幕截图
 */
public class ContentObserverUtils {

    private Context context;

    /** 内部存储器内容观察者 */
    private ContentObserver mInternalObserver;

    /** 外部存储器内容观察者 */
    private ContentObserver mExternalObserver;

    public ContentObserverUtils(Context context, MediaContentObserver.OnScreenShotListener listener) {
        this(context, listener, null);
    }

    public ContentObserverUtils(Context context, MediaContentObserver.OnScreenShotListener listener, Handler handler) {
        this.context = context;
        //指定子线程处理，不指定handler 回调发生在注册时线程
//        HandlerThread mHandlerThread = new HandlerThread("Screenshot_Observer");
//        mHandlerThread.start();
//        Handler mHandler = new Handler(mHandlerThread.getLooper());
        // 初始化
        mInternalObserver = new MediaContentObserver(context, handler, listener);
        mExternalObserver = new MediaContentObserver(context, handler, listener);
    }


    /**
     * 注册
     */
    public void register() {
        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, false, mInternalObserver);
        context.getContentResolver().registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false, mExternalObserver);
    }

    /**
     * 页面关闭时调用解除注册
     */
    public void unregiest() {
        context.getContentResolver().unregisterContentObserver(mInternalObserver);
        context.getContentResolver().unregisterContentObserver(mExternalObserver);
    }



}
