package org.github.ayltai.mopub.adapter;

import android.content.Context;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public final class FlurryEventNativeTest extends BaseEventNativeTest<FlurryEventNative, FlurryNativeAd> {
    @Override
    public FlurryEventNative createCustomEventNative() {
        return new FlurryEventNative();
    }

    @Override
    public FlurryNativeAd createNativeAd() {
        return Mockito.mock(FlurryNativeAd.class);
    }

    @Override
    @Before
    public void setUp() {
        super.setUp();

        Mockito.doNothing().when(this.getEventNative()).init(Mockito.any(Context.class), Mockito.anyString());
    }
}
