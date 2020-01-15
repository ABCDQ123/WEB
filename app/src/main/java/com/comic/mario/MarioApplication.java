package com.comic.mario;

import android.app.Application;
import android.os.Environment;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MarioApplication extends Application {

    public static final String WebFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Mario/WebElement";

    private static final String UMENG_KEY = "5e1e7c560cafb2aeff000494";
    private static final String UMENG_CHANNEL = "UMENG_CHANNEL";

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                .build();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, okHttpClient)
                .setDiskCacheEnabled(true)
                .build();
        Fresco.initialize(this, config);

        UMConfigure.init(this, UMENG_KEY, UMENG_CHANNEL, UMConfigure.DEVICE_TYPE_PHONE, null);
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    }
}
