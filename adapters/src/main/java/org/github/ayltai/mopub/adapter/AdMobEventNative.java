package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public abstract class AdMobEventNative<T extends AdMobNativeAd> extends BaseEventNative<T> {
    @Override
    protected void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String adUnitId = serverExtras.get(BaseEventNative.KEY_AD_UNIT_ID);

        if (this.validateAdUnitId(adUnitId)) {
            final T nativeAd = this.createNativeAd(context, customEventNativeListener, null, adUnitId);

            this.onLoadNativeAd(nativeAd);

            nativeAd.fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }
}
