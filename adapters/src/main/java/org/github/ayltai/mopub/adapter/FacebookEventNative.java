package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mopub.nativeads.NativeErrorCode;

public class FacebookEventNative extends BaseEventNative<FacebookNativeAd> {
    @Override
    public void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String adUnitId = serverExtras.get(BaseEventNative.KEY_AD_UNIT_ID);

        if (this.validateAdUnitId(adUnitId)) {
            final FacebookNativeAd nativeAd = this.createNativeAd(context, customEventNativeListener, null, adUnitId);

            this.onLoadNativeAd(nativeAd);

            nativeAd.fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }

    @Override
    protected FacebookNativeAd createNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @Nullable final String apiKey, @Nullable final String adUnitId) {
        return new FacebookNativeAd(context, customEventNativeListener, adUnitId);
    }
}
