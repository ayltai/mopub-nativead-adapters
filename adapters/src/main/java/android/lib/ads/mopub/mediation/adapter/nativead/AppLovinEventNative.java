package android.lib.ads.mopub.mediation.adapter.nativead;

import java.util.Map;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.mopub.nativeads.CustomEventNative;

public class AppLovinEventNative extends BaseCustomEventNative {
    @Override
    protected void loadNativeAd(@NonNull final Activity activity,
                                @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener,
                                @NonNull final Map<String, Object> localExtras,
                                @NonNull final Map<String, String> serverExtras) {
        new AppLovinNativeAd(activity, customEventNativeListener).fetchAd();
    }
}
