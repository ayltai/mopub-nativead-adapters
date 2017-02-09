package org.github.ayltai.mopub.adapter;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

@RunWith(JUnit4.class)
public final class FacebookEventNativeTest extends BaseEventNativeTest<FacebookEventNative, FacebookNativeAd> {
    @Override
    public FacebookEventNative createCustomEventNative() {
        return new FacebookEventNative();
    }

    @Override
    public FacebookNativeAd createNativeAd() {
        return Mockito.mock(FacebookNativeAd.class);
    }
}
