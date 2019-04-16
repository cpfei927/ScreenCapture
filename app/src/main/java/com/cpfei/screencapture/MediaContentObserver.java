package com.cpfei.screencapture;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;


/**
 * Created by cpfei on 2019/4/16
 * Description:
 * <uses-permission android:name="MediaStore.Images.Media.INTERNAL_CONTENT_URI"/>
 * <uses-permission android:name="MediaStore.Images.Media.EXTERNAL_CONTENT_URI"/>
 */
public class MediaContentObserver extends ContentObserver {

    private static final String TAG = MediaContentObserver.class.getName();

    private Context context;
    private OnScreenShotListener listener;

    /** 读取媒体数据库时需要读取的列 */
    private static final String[] MEDIA_PROJECTIONS = {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
    };

    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap"
    };

    public interface OnScreenShotListener {
        void onScreenShot(String path);
    }

    public MediaContentObserver(Context context, Handler handler, OnScreenShotListener listener) {
        super(handler);
        this.context = context;
        this.listener = listener;
    }


    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        handleMediaContentChange(uri);
    }

    private void handleMediaContentChange(Uri contentUri) {
        Cursor cursor = null;
        try {
            // 数据改变时查询数据库中最后加入的一条数据
            cursor = context.getContentResolver().query(
                    contentUri, MEDIA_PROJECTIONS, null, null, MediaStore.Images.ImageColumns.DATE_ADDED + " desc " + "limit 1");
            if (cursor == null) {
                return;
            }
            if (!cursor.moveToFirst()) {
                return;
            }
            // 获取各列的索引
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);
            // 获取行数据
            String data = cursor.getString(dataIndex);
            long dateTaken = cursor.getLong(dateTakenIndex);
            // 处理获取到的第一行数据
            handleMediaRowData(data, dateTaken);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * 处理监听到的资源
     *
     */
    private void handleMediaRowData(String data, long dateTaken) {
        if (checkScreenShot(data, dateTaken)) {
            Log.d(TAG, data + " " + dateTaken);
            if (listener != null) {
                listener.onScreenShot(data);
            }
        } else {
            Log.d(TAG, "Not screenshot event");
        }
    }

    /**
     * 判断是否是截屏
     */
    private boolean checkScreenShot(String data, long dateTaken) {
        data = data.toLowerCase();
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (String keyWork : KEYWORDS) {
            if (data.contains(keyWork)) {
                return true;
            }
        }
        return false;
    }


}
