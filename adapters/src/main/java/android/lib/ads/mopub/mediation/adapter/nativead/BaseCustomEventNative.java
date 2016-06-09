package android.lib.ads.mopub.mediation.adapter.nativead;

import java.util.Map;

import android.text.TextUtils;

import com.mopub.nativeads.CustomEventNative;

public abstract class BaseCustomEventNative extends CustomEventNative {
    private static final String NAME_API_KEY    = "apiKey";
    private static final String NAME_AD_UNIT_AD = "adUnitId";

    protected String getApiKeyName() {
        return BaseCustomEventNative.NAME_API_KEY;
    }

    protected String getAdUnitIdName() {
        return BaseCustomEventNative.NAME_AD_UNIT_AD;
    }

    protected boolean validateExtras(final Map<String, String> serverExtras) {
        final String apiKey   = serverExtras.get(this.getApiKeyName());
        final String adUnitId = serverExtras.get(this.getAdUnitIdName());

        return !TextUtils.isEmpty(apiKey) && !TextUtils.isEmpty(adUnitId);
    }
}
