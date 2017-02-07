package org.github.ayltai.mopub.adapter;

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;
import com.mopub.nativeads.StaticNativeAd;

public abstract class BaseStaticNativeAd extends StaticNativeAd {
    //region Constants

    protected static final double MIN_STAR_RATING     = 0;
    protected static final double MAX_STAR_RATING     = 5;
    protected static final int    IMPRESSION_MIN_TIME = 1000;

    //endregion

    private final NativeImageHelper.ImageListener imageListener = new NativeImageHelper.ImageListener() {
        @Override
        public void onImagesCached() {
            BaseStaticNativeAd.this.customEventNativeListener.onNativeAdLoaded(BaseStaticNativeAd.this);
        }

        @Override
        public void onImagesFailedToCache(final NativeErrorCode errorCode) {
            BaseStaticNativeAd.this.customEventNativeListener.onNativeAdFailed(errorCode);
        }
    };

    //region Variables

    private final Context                                     context;
    private final CustomEventNative.CustomEventNativeListener customEventNativeListener;

    //endregion

    protected BaseStaticNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener) {
        this.context                   = context;
        this.customEventNativeListener = customEventNativeListener;
    }

    /**
     * Initiates an ad request.
     */
    public abstract void fetchAd();

     /**
     * Helper method to pre-cache ad images.
     * @param imageUrls A list of images to cache.
     */
    protected void preCacheImages(@NonNull final List<String> imageUrls) {
        NativeImageHelper.preCacheImages(this.context, imageUrls, this.imageListener);
    }

    //region Properties

    protected Context getContext() {
        return this.context;
    }

    protected CustomEventNative.CustomEventNativeListener getCustomEventNativeListener() {
        return this.customEventNativeListener;
    }

    //endregion
}
