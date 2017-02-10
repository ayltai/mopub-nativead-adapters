package org.github.ayltai.mopub.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.applovin.nativeAds.AppLovinNativeAdLoadListener;
import com.applovin.sdk.AppLovinErrorCodes;
import com.applovin.sdk.AppLovinPostbackListener;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkSettings;
import com.applovin.sdk.AppLovinTargetingData;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public class AppLovinNativeAd extends BaseStaticNativeAd implements AppLovinNativeAdLoadListener {
    private final AppLovinSdk appLovinSdk;

    private com.applovin.nativeAds.AppLovinNativeAd nativeAd;

    public AppLovinNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final String apiKey) {
        super(context, customEventNativeListener);

        AppLovinSdk.initializeSdk(context);

        this.appLovinSdk = AppLovinSdk.getInstance(apiKey, this.getSettings(), context);
    }

    @Override
    public void recordImpression(@NonNull final View view) {
        super.recordImpression(view);

        if (this.nativeAd != null && !TextUtils.isEmpty(this.nativeAd.getImpressionTrackingUrl())) {
            this.appLovinSdk.getPostbackService().dispatchPostbackAsync(this.nativeAd.getImpressionTrackingUrl(), new AppLovinPostbackListener() {
                @Override
                public void onPostbackSuccess(final String url) {
                    AppLovinNativeAd.this.notifyAdImpressed();
                }

                @Override
                public void onPostbackFailure(final String url, final int errorCode) {
                    Log.w(this.getClass().getSimpleName(), String.format("Post back to %s failed\nError code = %d", url, errorCode));
                }
            });
        }
    }

    @Override
    public void handleClick(@NonNull final View view) {
        super.handleClick(view);

        if (this.nativeAd != null && !TextUtils.isEmpty(this.nativeAd.getClickUrl())) {
            this.nativeAd.launchClickTarget(this.getContext());

            this.notifyAdClicked();
        }
    }

    @Override
    public void fetchAd() {
        this.appLovinSdk.getNativeAdService().loadNativeAds(1, this);
    }

    @Override
    public void onNativeAdsLoaded(final List list) {
        if (!list.isEmpty()) {
            this.nativeAd = (com.applovin.nativeAds.AppLovinNativeAd)list.get(0);

            if (!TextUtils.isEmpty(this.nativeAd.getTitle())) this.setTitle(this.nativeAd.getTitle());
            if (!TextUtils.isEmpty(this.nativeAd.getDescriptionText())) this.setText(this.nativeAd.getDescriptionText());
            if (!TextUtils.isEmpty(this.nativeAd.getCtaText())) this.setCallToAction(this.nativeAd.getCtaText());
            if (this.nativeAd.getStarRating() > 0) this.setStarRating((double)this.nativeAd.getStarRating());
            if (!TextUtils.isEmpty(this.nativeAd.getClickUrl())) this.setClickDestinationUrl(this.nativeAd.getClickUrl());

            final List<String> imageUrls = this.getImageUrls();
            if (!imageUrls.isEmpty()) this.preCacheImages(imageUrls);

            this.setImpressionMinTimeViewed(BaseStaticNativeAd.IMPRESSION_MIN_TIME);
            this.notifyAdImpressed();
        }
    }

    @Override
    public void onNativeAdsFailedToLoad(final int errorCode) {
        switch (errorCode) {
            case AppLovinErrorCodes.FETCH_AD_TIMEOUT:
            case AppLovinErrorCodes.INCENTIVIZED_SERVER_TIMEOUT:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.NETWORK_TIMEOUT);
                break;

            case AppLovinErrorCodes.NO_NETWORK:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.CONNECTION_ERROR);
                break;

            case AppLovinErrorCodes.INCENTIVIZED_UNKNOWN_SERVER_ERROR:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.SERVER_ERROR_RESPONSE_CODE);
                break;

            case AppLovinErrorCodes.INVALID_URL:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.INVALID_REQUEST_URL);
                break;

            case AppLovinErrorCodes.NO_FILL:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                break;

            default:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                break;
        }
    }

    /**
     * Returns an {@link AppLovinTargetingData} instance for ad targeting customization.
     * @return An {@link AppLovinTargetingData} instance for ad targeting customization.
     */
    @NonNull
    public AppLovinTargetingData getTargetingData() {
        return this.appLovinSdk.getTargetingData();
    }

    /**
     * Returns the settings used to initialize AppLovin SDK.
     * @return The settings used to initialize AppLovin SDK.
     */
    @NonNull
    protected AppLovinSdkSettings getSettings() {
        final AppLovinSdkSettings settings = new AppLovinSdkSettings();

        settings.setTestAdsEnabled(BuildConfig.DEBUG);
        settings.setVerboseLogging(BuildConfig.DEBUG);

        return settings;
    }

    @NonNull
    private List<String> getImageUrls() {
        final List<String> imageUrls = new ArrayList<>(2);

        if (!TextUtils.isEmpty(this.nativeAd.getIconUrl())) {
            this.setIconImageUrl(this.nativeAd.getIconUrl());
            imageUrls.add(this.nativeAd.getIconUrl());
        }

        if (!TextUtils.isEmpty(this.nativeAd.getImageUrl())) {
            this.setMainImageUrl(this.nativeAd.getImageUrl());
            imageUrls.add(this.nativeAd.getImageUrl());
        }

        return imageUrls;
    }
}
