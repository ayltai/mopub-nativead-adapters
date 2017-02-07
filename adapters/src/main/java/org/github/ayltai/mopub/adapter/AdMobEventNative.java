package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mopub.nativeads.NativeErrorCode;

public class AdMobEventNative extends BaseEventNative<AdMobNativeAd> {
    @Override
    protected void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String adUnitId = serverExtras.get(BaseEventNative.KEY_AD_UNIT_ID);

        if (this.validateAdUnitId(adUnitId)) {
            final AdMobNativeAd nativeAd = new AdMobNativeAd(context, customEventNativeListener, adUnitId);

            this.onLoadNativeAd(nativeAd);

            nativeAd.fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }
}
