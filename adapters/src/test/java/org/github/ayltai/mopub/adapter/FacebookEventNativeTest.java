package org.github.ayltai.mopub.adapter;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.mopub.nativeads.CustomEventNative;

@RunWith(JUnit4.class)
public final class FacebookEventNativeTest extends BaseEventNativeTest {
    @Test
    public void whenLoadNativeAd_thenFetchAd() {
        final FacebookNativeAd    nativeAd    = Mockito.mock(FacebookNativeAd.class);
        final FacebookEventNative eventNative = Mockito.spy(new FacebookEventNative());

        Mockito.doReturn(true).when(eventNative).validateApiKey(Mockito.anyString());
        Mockito.doReturn(true).when(eventNative).validateAdUnitId(Mockito.anyString());
        Mockito.doReturn(true).when(eventNative).validateServerExtras(Mockito.<String, String>anyMap());
        Mockito.doReturn(nativeAd).when(eventNative).createNativeAd(Mockito.any(Context.class), Mockito.any(CustomEventNative.CustomEventNativeListener.class), Mockito.anyString(), Mockito.anyString());

        eventNative.loadNativeAd(null, Mockito.mock(CustomEventNative.CustomEventNativeListener.class), BaseEventNativeTest.MOCK_LOCAL_EXTRAS, BaseEventNativeTest.MOCK_SERVER_EXTRAS);

        Mockito.verify(eventNative, Mockito.times(1)).onLoadNativeAd(nativeAd);
        Mockito.verify(nativeAd, Mockito.times(1)).fetchAd();
    }
}
