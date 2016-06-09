package android.lib.ads.mopub.mediation.adapter.nativead;

import java.util.Map;

import android.app.Activity;
import android.lib.ads.mopub.mediation.adapter.BuildConfig;
import android.support.annotation.NonNull;

import com.flurry.android.FlurryAgent;
import com.flurry.android.ads.FlurryAdNative;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public class FlurryEventNative extends BaseCustomEventNative {
    @Override
    protected void loadNativeAd(@NonNull final Activity activity,
                                @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener,
                                @NonNull final Map<String, Object> localExtras,
                                @NonNull final Map<String, String> serverExtras) {
        final String apiKey;
        final String adSpaceName;

        if (this.validateExtras(serverExtras)) {
            apiKey      = serverExtras.get(this.getApiKeyName());
            adSpaceName = serverExtras.get(this.getAdUnitIdName());

            new FlurryAgent.Builder()
                .withLogEnabled(BuildConfig.DEBUG)
                .build(activity, apiKey);

            new FlurryNativeAd(activity, customEventNativeListener, new FlurryAdNative(activity, adSpaceName)).fetchAd();
        } else {
            customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
        }
    }
}
