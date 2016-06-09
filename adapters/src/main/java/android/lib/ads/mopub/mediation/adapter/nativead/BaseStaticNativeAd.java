package android.lib.ads.mopub.mediation.adapter.nativead;

import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.NativeImageHelper;
import com.mopub.nativeads.StaticNativeAd;

public abstract class BaseStaticNativeAd extends StaticNativeAd {
    protected static final double MIN_STAR_RATING = 0;
    protected static final double MAX_STAR_RATING = 5;

    protected static final int IMPRESSION_MIN_TIME = 1000;

    protected final Context                                     context;
    protected final CustomEventNative.CustomEventNativeListener customEventNativeListener;

    protected BaseStaticNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener) {
        this.context                   = context;
        this.customEventNativeListener = customEventNativeListener;
    }

    public abstract void fetchAd();

    protected void preCacheImages(final List<String> imageUrls) {
        NativeImageHelper.preCacheImages(this.context, imageUrls, new NativeImageHelper.ImageListener() {
            @Override
            public void onImagesCached() {
                BaseStaticNativeAd.this.customEventNativeListener.onNativeAdLoaded(BaseStaticNativeAd.this);
            }

            @Override
            public void onImagesFailedToCache(final NativeErrorCode errorCode) {
                BaseStaticNativeAd.this.customEventNativeListener.onNativeAdFailed(errorCode);
            }
        });
    }
}
