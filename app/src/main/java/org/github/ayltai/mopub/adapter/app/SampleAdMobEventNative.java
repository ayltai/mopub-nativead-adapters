package org.github.ayltai.mopub.adapter.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.github.ayltai.mopub.adapter.AdMobEventNative;

import com.mopub.nativeads.CustomEventNative;

public class SampleAdMobEventNative extends AdMobEventNative<SampleAdMobNativeAd> {
    @Override
    protected SampleAdMobNativeAd createNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @Nullable final String apiKey, @Nullable final String adUnitId) {
        return new SampleAdMobNativeAd(context, customEventNativeListener, adUnitId);
    }
}
