package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mopub.nativeads.NativeErrorCode;

public class FacebookEventNative extends BaseEventNative<FacebookNativeAd> {
    @Override
    protected void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String adUnitId = serverExtras.get(BaseEventNative.KEY_AD_UNIT_ID);

        if (this.validateAdUnitId(adUnitId)) {
            final FacebookNativeAd nativeAd = new FacebookNativeAd(context, customEventNativeListener, adUnitId);

            this.onLoadNativeAd(nativeAd);

            nativeAd.fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }

    /**
     * You may customize ad requests here.
     * @param nativeAd A {@link FacebookNativeAd} instance to let you customize ad requests.
     */
    @Override
    protected void onLoadNativeAd(@NonNull final FacebookNativeAd nativeAd) {
        super.onLoadNativeAd(nativeAd);
    }
}
