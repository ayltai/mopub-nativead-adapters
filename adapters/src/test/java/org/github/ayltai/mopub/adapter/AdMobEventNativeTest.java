package org.github.ayltai.mopub.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.mockito.Mockito;

public final class AdMobEventNativeTest extends BaseEventNativeTest<AdMobEventNativeTest.MockAdMobEventNative, AdMobNativeAd> {
    @Override
    public AdMobEventNativeTest.MockAdMobEventNative createCustomEventNative() {
        return new MockAdMobEventNative();
    }

    @Override
    public AdMobNativeAd createNativeAd() {
        return Mockito.mock(AdMobNativeAd.class);
    }

    public class MockAdMobEventNative extends AdMobEventNative<AdMobNativeAd> {
        @Override
        protected AdMobNativeAd createNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @Nullable final String apiKey, @Nullable final String adUnitId) {
            return AdMobEventNativeTest.this.getNativeAd();
        }
    }
}
