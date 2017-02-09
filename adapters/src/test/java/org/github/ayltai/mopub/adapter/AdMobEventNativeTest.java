package org.github.ayltai.mopub.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;
import org.mockito.Mockito;

import com.mopub.nativeads.CustomEventNative;

public final class AdMobEventNativeTest extends BaseEventNativeTest {
    @Test
    public void whenLoadNativeAd_thenFetchAd() {
        final AdMobNativeAd    nativeAd    = Mockito.mock(AdMobNativeAd.class);
        final AdMobEventNative eventNative = Mockito.spy(new AdMobEventNative() {
            @Override
            protected BaseStaticNativeAd createNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @Nullable final String apiKey, @Nullable final String adUnitId) {
                return nativeAd;
            }
        });

        Mockito.doReturn(true).when(eventNative).validateApiKey(Mockito.anyString());
        Mockito.doReturn(true).when(eventNative).validateAdUnitId(Mockito.anyString());
        Mockito.doReturn(true).when(eventNative).validateServerExtras(Mockito.<String, String>anyMap());
        Mockito.doReturn(nativeAd).when(eventNative).createNativeAd(Mockito.any(Context.class), Mockito.any(CustomEventNative.CustomEventNativeListener.class), Mockito.anyString(), Mockito.anyString());

        eventNative.loadNativeAd(null, Mockito.mock(CustomEventNative.CustomEventNativeListener.class), BaseEventNativeTest.MOCK_LOCAL_EXTRAS, BaseEventNativeTest.MOCK_SERVER_EXTRAS);

        Mockito.verify(eventNative, Mockito.times(1)).onLoadNativeAd(nativeAd);
        Mockito.verify(nativeAd, Mockito.times(1)).fetchAd();
    }
}
