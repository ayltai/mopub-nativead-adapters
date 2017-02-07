package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.mopub.nativeads.NativeErrorCode;

public class FlurryEventNative extends BaseEventNative<FlurryNativeAd> {
    @Override
    protected void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String apiKey = serverExtras.get(BaseEventNative.KEY_API_KEY);

        if (this.validateApiKey(apiKey)) {
            new FlurryAgent.Builder()
                .withLogEnabled(BuildConfig.DEBUG)
                .withLogLevel(Log.VERBOSE)
                .build(context, apiKey);

            new FlurryNativeAd(context, customEventNativeListener, apiKey).fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }

    /**
     * You may customize ad requests here.
     * @param nativeAd A {@link FlurryNativeAd} instance to let you customize ad requests.
     */
    @Override
    protected void onLoadNativeAd(@NonNull final FlurryNativeAd nativeAd) {
        super.onLoadNativeAd(nativeAd);
    }
}
