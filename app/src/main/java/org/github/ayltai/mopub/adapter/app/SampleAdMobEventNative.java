package org.github.ayltai.mopub.adapter.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.github.ayltai.mopub.adapter.AdMobEventNative;

public class SampleAdMobEventNative extends AdMobEventNative<SampleAdMobNativeAd> {
    @Override
    protected SampleAdMobNativeAd createNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @Nullable final String apiKey, @Nullable final String adUnitId) {
        return new SampleAdMobNativeAd(context, customEventNativeListener, adUnitId);
    }
}
