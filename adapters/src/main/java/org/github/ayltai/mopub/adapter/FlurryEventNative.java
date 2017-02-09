package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.flurry.android.FlurryAgent;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public class FlurryEventNative extends BaseEventNative<FlurryNativeAd> {
    @Override
    public void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras) {
        final String apiKey = serverExtras.get(BaseEventNative.KEY_API_KEY);

        if (this.validateApiKey(apiKey)) {
            this.init(context, apiKey);

            final FlurryNativeAd nativeAd = this.createNativeAd(context, customEventNativeListener, apiKey, null);

            this.onLoadNativeAd(nativeAd);

            nativeAd.fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }

    @Override
    protected FlurryNativeAd createNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @Nullable final String apiKey, @Nullable final String adUnitId) {
        return new FlurryNativeAd(context, customEventNativeListener, apiKey);
    }

    @VisibleForTesting
    /* private static */ void init(@NonNull final Context context, @NonNull final String apiKey) {
        new FlurryAgent.Builder()
            .withLogEnabled(BuildConfig.DEBUG)
            .withLogLevel(Log.VERBOSE)
            .build(context, apiKey);
    }
}
