package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;

import com.mopub.nativeads.NativeErrorCode;

public class AppLovinEventNative extends BaseEventNative<AppLovinNativeAd> {
    @Override
    protected void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String apiKey = serverExtras.get(BaseEventNative.KEY_API_KEY);

        if (this.validateApiKey(apiKey)) {
            final AppLovinNativeAd nativeAd = new AppLovinNativeAd(context, customEventNativeListener, apiKey);

            this.onLoadNativeAd(nativeAd);

            nativeAd.fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }
}
