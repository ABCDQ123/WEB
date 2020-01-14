package com.comic.mario.util;

import android.util.Log;

public class LogUtil {

    private static final String tag = "LogUtil";

    public static void loge(String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;
        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize) {// 长度小于等于限制直接打印
            Log.e(tag, msg);
        } else {
            while (msg.length() > segmentSize) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize);
                msg = msg.replace(logContent, "");
                Log.e(tag, ":" + logContent);
            }
            Log.e(tag, ":" + msg);
        }
    }

}
