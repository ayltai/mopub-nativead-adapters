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
            new AppLovinNativeAd(context, customEventNativeListener, apiKey).fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }

    /**
     * You may customize ad requests here.
     * @param nativeAd An {@link AppLovinNativeAd} instance to let you customize ad requests.
     */
    @Override
    protected void onLoadNativeAd(@NonNull final AppLovinNativeAd nativeAd) {
        super.onLoadNativeAd(nativeAd);
    }
}
