package com.comic.mario.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.comic.mario.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.umeng.analytics.MobclickAgent;

import me.relex.photodraweeview.PhotoDraweeView;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class ReadView extends FrameLayout {

    private Context context;
    private int index;
    private int count;
    private PhotoDraweeView imageView;

    public void cancel(){
    }

    public ReadView(@NonNull Context context, int index, int count) {
        super(context);
        this.context = context;
        this.index = index;
        this.count = count;
        init();
    }

    private void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic_read, null);
        imageView = view.findViewById(R.id.pv_item_comic_read);
        GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .setProgressBarImage(new ProgressBarDrawable())
                .setFailureImage(R.drawable.failimage)
                .setFailureImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                .build();
        imageView.setHierarchy(hierarchy);
        addView(view);
    }

    public void loadImage(String url) {
        if (((Activity) context).isDestroyed())
            return;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setProgressiveRenderingEnabled(true)
                .build();
        PipelineDraweeControllerBuilder controller = Fresco
                .newDraweeControllerBuilder()
                .setImageRequest(request)
                .setTapToRetryEnabled(true)
                .setOldController(imageView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null || imageView == null) {
                    return;
                }
                imageView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        imageView.setController(controller.build());
    }


    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }
}
