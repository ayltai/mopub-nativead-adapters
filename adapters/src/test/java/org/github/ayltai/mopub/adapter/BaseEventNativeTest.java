package org.github.ayltai.mopub.adapter;

import java.util.HashMap;
import java.util.Map;

import android.support.annotation.CallSuper;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BaseEventNativeTest {
    protected static final Map<String, Object> MOCK_LOCAL_EXTRAS  = new HashMap<>();
    protected static final Map<String, String> MOCK_SERVER_EXTRAS = new HashMap<>();

    protected static final String MOCK_API_KEY    = "123";
    protected static final String MOCK_AD_UNIT_ID = "abc";

    @CallSuper
    @Before
    public void setUp() {
        BaseEventNativeTest.MOCK_SERVER_EXTRAS.clear();
        BaseEventNativeTest.MOCK_SERVER_EXTRAS.put(BaseEventNative.KEY_API_KEY, BaseEventNativeTest.MOCK_API_KEY);
        BaseEventNativeTest.MOCK_SERVER_EXTRAS.put(BaseEventNative.KEY_AD_UNIT_ID, BaseEventNativeTest.MOCK_AD_UNIT_ID);
    }
}
