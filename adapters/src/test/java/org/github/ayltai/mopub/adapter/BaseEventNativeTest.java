package org.github.ayltai.mopub.adapter;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.support.annotation.CallSuper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import com.mopub.nativeads.CustomEventNative;

@RunWith(JUnit4.class)
public abstract class BaseEventNativeTest<E extends BaseEventNative<A>, A extends BaseStaticNativeAd> {
    protected static final Map<String, Object> MOCK_LOCAL_EXTRAS  = new HashMap<>();
    protected static final Map<String, String> MOCK_SERVER_EXTRAS = new HashMap<>();

    protected static final String MOCK_API_KEY    = "123";
    protected static final String MOCK_AD_UNIT_ID = "abc";

    private E eventNative;
    private A nativeAd;

    protected E getEventNative() {
        return this.eventNative;
    }

    protected A getNativeAd() {
        return this.nativeAd;
    }

    @CallSuper
    @Before
    public void setUp() {
        BaseEventNativeTest.MOCK_SERVER_EXTRAS.clear();
        BaseEventNativeTest.MOCK_SERVER_EXTRAS.put(BaseEventNative.KEY_API_KEY, BaseEventNativeTest.MOCK_API_KEY);
        BaseEventNativeTest.MOCK_SERVER_EXTRAS.put(BaseEventNative.KEY_AD_UNIT_ID, BaseEventNativeTest.MOCK_AD_UNIT_ID);

        this.nativeAd    = this.createNativeAd();
        this.eventNative = Mockito.spy(this.createCustomEventNative());
    }

    @Test
    public void whenLoadNativeAd_thenFetchAd() {
        Mockito.doReturn(true).when(this.eventNative).validateApiKey(Mockito.anyString());
        Mockito.doReturn(true).when(this.eventNative).validateAdUnitId(Mockito.anyString());
        Mockito.doReturn(true).when(this.eventNative).validateServerExtras(Mockito.<String, String>anyMap());
        Mockito.doReturn(this.nativeAd).when(this.eventNative).createNativeAd(Mockito.any(Context.class), Mockito.any(CustomEventNative.CustomEventNativeListener.class), Mockito.anyString(), Mockito.anyString());

        this.eventNative.loadNativeAd(null, Mockito.mock(CustomEventNative.CustomEventNativeListener.class), BaseEventNativeTest.MOCK_LOCAL_EXTRAS, BaseEventNativeTest.MOCK_SERVER_EXTRAS);

        Mockito.verify(this.eventNative, Mockito.times(1)).onLoadNativeAd(this.nativeAd);
        Mockito.verify(this.nativeAd, Mockito.times(1)).fetchAd();
    }

    public abstract E createCustomEventNative();

    public abstract A createNativeAd();
}
