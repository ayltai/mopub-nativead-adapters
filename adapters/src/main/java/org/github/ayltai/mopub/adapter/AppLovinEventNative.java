package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public class AppLovinEventNative extends BaseEventNative<AppLovinNativeAd> {
    @Override
    public void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String apiKey = serverExtras.get(BaseEventNative.KEY_API_KEY);

        if (this.validateApiKey(apiKey)) {
            final AppLovinNativeAd nativeAd = this.createNativeAd(context, customEventNativeListener, apiKey, null);

            this.onLoadNativeAd(nativeAd);

            nativeAd.fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }

    @Override
    protected AppLovinNativeAd createNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @Nullable final String apiKey, @Nullable final String adUnitId) {
        return new AppLovinNativeAd(context, customEventNativeListener, apiKey);
    }
}
