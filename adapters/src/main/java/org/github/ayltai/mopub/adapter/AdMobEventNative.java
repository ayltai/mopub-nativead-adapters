package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public abstract class AdMobEventNative<T extends AdMobNativeAd> extends BaseEventNative<T> {
    @Override
    protected void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String adUnitId = serverExtras.get(BaseEventNative.KEY_AD_UNIT_ID);

        if (this.validateAdUnitId(adUnitId)) {
            final T nativeAd = this.createAdMobNativeAd(context, customEventNativeListener, adUnitId);

            this.onLoadNativeAd(nativeAd);

            nativeAd.fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }

    /**
     * Returns a new instance of a concrete {@link AdMobNativeAd} sub-class that implements {@link AdMobNativeAd#prepare(NativeAppInstallAdView, NativeAppInstallAd)} and {@link AdMobNativeAd#prepare(NativeContentAdView, NativeContentAd)}.
     * @param context The application context.
     * @param customEventNativeListener Callbacks to be invoked by MoPub SDK.
     * @param adUnitId The unit ID of the AdMob native ad.
     * @return A concrete sub-class of {@link AdMobNativeAd}.
     */
    protected abstract T createAdMobNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final String adUnitId);
}
