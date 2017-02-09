package org.github.ayltai.mopub.adapter;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public final class AppLovinEventNativeTest extends BaseEventNativeTest<AppLovinEventNative, AppLovinNativeAd> {
    @Override
    public AppLovinEventNative createCustomEventNative() {
        return new AppLovinEventNative();
    }

    @Override
    public AppLovinNativeAd createNativeAd() {
        return Mockito.mock(AppLovinNativeAd.class);
    }
}
