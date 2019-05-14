package com.hust_twj.zademo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.style.ImageSpan;

/**
 * Created by twj on 2019/5/14.
 * 媒体工具（视频等）
 */

public class MediaUtils {

    int mVideoWidth, mVideoHeight;
    int videoRotation;
    /**
     * 获取本地视频的宽高、旋转方向
     * @param videoPath
     */
    private void playLocalVideo(String videoPath) {
        MediaMetadataRetriever mRetriever = new MediaMetadataRetriever();
        mRetriever.setDataSource(videoPath);

        String height = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
        String width = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度
        String rotation = mRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION);

        if (width != null && !width.isEmpty()) {
            mVideoWidth = Integer.valueOf(width);
        }

        if (height != null && !height.isEmpty()) {
            mVideoHeight = Integer.valueOf(height);
        }

        if (rotation != null && !rotation.isEmpty()) {
            videoRotation = Integer.parseInt(rotation);
        }

        if ((videoRotation + 360) % 360 == 90 ||
                (videoRotation + 360) % 360 == 270) {
            mVideoWidth = mVideoWidth + mVideoHeight;
            mVideoHeight = mVideoWidth - mVideoHeight;
            mVideoWidth = mVideoWidth - mVideoHeight;
        }
        LogUtils.e("twj124", mVideoWidth + " " + mVideoHeight + "  " + videoRotation) ;
    }
}
