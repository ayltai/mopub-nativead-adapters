package org.github.ayltai.mopub.adapter;

import java.util.Map;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

import com.mopub.nativeads.CustomEventNative;

public abstract class BaseEventNative<T extends BaseStaticNativeAd> extends CustomEventNative {
    //region Constants

    protected static final String KEY_API_KEY    = "apiKey";
    protected static final String KEY_AD_UNIT_ID = "adUnitId";

    //endregion

    protected BaseEventNative() {
    }

    @VisibleForTesting
    @Override
    public abstract void loadNativeAd(@NonNull final Context context, @NonNull final CustomEventNativeListener customEventNativeListener, @NonNull final Map<String, Object> localExtras, @NonNull final Map<String, String> serverExtras);

    protected boolean validateServerExtras(@NonNull final Map<String, String> serverExtras) {
        return this.validateApiKey(serverExtras.get(BaseEventNative.KEY_API_KEY)) && this.validateAdUnitId(serverExtras.get(BaseEventNative.KEY_AD_UNIT_ID));
    }

    protected boolean validateApiKey(@Nullable final String apiKey) {
        return !TextUtils.isEmpty(apiKey);
    }

    protected boolean validateAdUnitId(@Nullable final String adUnitId) {
        return !TextUtils.isEmpty(adUnitId);
    }

    /**
     * This method is called before {@link BaseStaticNativeAd#fetchAd()}. Ad request customization, e.g. ad targeting, can be done here.
     * @param nativeAd A {@link BaseStaticNativeAd} instance to let you customize ad requests.
     */
    protected void onLoadNativeAd(@NonNull final T nativeAd) {
    }

    protected abstract T createNativeAd(@NonNull Context context, @NonNull CustomEventNative.CustomEventNativeListener customEventNativeListener, @Nullable String apiKey, @Nullable String adUnitId);
}
