package org.github.ayltai.mopub.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdNative;
import com.flurry.android.ads.FlurryAdNativeAsset;
import com.flurry.android.ads.FlurryAdNativeListener;
import com.flurry.android.ads.FlurryAdTargeting;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public class FlurryNativeAd extends BaseStaticNativeAd implements FlurryAdNativeListener {
    //region Constants

    private static final String EXTRA_STAR_RATING_IMG      = "flurry_starratingimage";
    private static final String EXTRA_APP_CATEGORY         = "flurry_appcategorytext";
    private static final String EXTRA_SEC_BRANDING_LOGO    = "flurry_brandingimage";
    private static final String CALL_TO_ACTION             = "callToAction";
    private static final String ASSET_SEC_HQ_IMAGE         = "secHqImage";
    private static final String ASSET_SEC_IMAGE            = "secImage";
    private static final String ASSET_SEC_HQ_RATING_IMG    = "secHqRatingImg";
    private static final String ASSET_SEC_HQ_BRANDING_LOGO = "secHqBrandingLogo";
    private static final String ASSET_SEC_RATING_IMG       = "secRatingImg";
    private static final String ASSET_APP_RATING           = "appRating";
    private static final String ASSET_APP_CATEGORY         = "appCategory";
    private static final String ASSET_HEADLINE             = "headline";
    private static final String ASSET_SUMMARY              = "summary";

    //endregion

    //region Variables

    private final FlurryAdTargeting targetingData = new FlurryAdTargeting();

    private FlurryAdNative nativeAd;

    //endregion

    public FlurryNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final String apiKey) {
        super(context, customEventNativeListener);

        this.nativeAd = new FlurryAdNative(context, apiKey);
    }

    @CallSuper
    @Override
    public void prepare(@NonNull final View view) {
        super.prepare(view);

        this.nativeAd.setTrackingView(view);
    }

    @CallSuper
    @Override
    public void clear(@NonNull final View view) {
        super.clear(view);

        this.nativeAd.removeTrackingView();
    }

    @CallSuper
    @Override
    public void destroy() {
        super.destroy();

        this.nativeAd.destroy();
    }

    @Override
    public void fetchAd() {
        this.nativeAd.setListener(this);
        this.nativeAd.setTargeting(this.targetingData);
        this.nativeAd.fetchAd();
    }

    @Override
    public void onFetched(final FlurryAdNative nativeAd) {
        if (nativeAd != null) {
            this.nativeAd = nativeAd;

            final FlurryAdNativeAsset coverImageAsset = this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_HQ_IMAGE);
            if (coverImageAsset != null && !TextUtils.isEmpty(coverImageAsset.getValue())) this.setMainImageUrl(coverImageAsset.getValue());

            final FlurryAdNativeAsset iconImageAsset  = this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_IMAGE);
            if (iconImageAsset != null && !TextUtils.isEmpty(iconImageAsset.getValue())) this.setIconImageUrl(iconImageAsset.getValue());

            this.setTitle(this.nativeAd.getAsset(FlurryNativeAd.ASSET_HEADLINE).getValue());
            this.setText(this.nativeAd.getAsset(FlurryNativeAd.ASSET_SUMMARY).getValue());

            this.addExtra(FlurryNativeAd.EXTRA_SEC_BRANDING_LOGO, this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_HQ_BRANDING_LOGO).getValue());

            if (this.isAppInstallAd()) {
                // App rating image URL may be null
                final FlurryAdNativeAsset ratingHqImageAsset = this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_HQ_RATING_IMG);
                if (ratingHqImageAsset != null && !TextUtils.isEmpty(ratingHqImageAsset.getValue())) {
                    this.addExtra(FlurryNativeAd.EXTRA_STAR_RATING_IMG, ratingHqImageAsset.getValue());
                } else {
                    final FlurryAdNativeAsset ratingImageAsset = this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_RATING_IMG);
                    if (ratingImageAsset != null && !TextUtils.isEmpty(ratingImageAsset.getValue())) this.addExtra(FlurryNativeAd.EXTRA_STAR_RATING_IMG, ratingImageAsset.getValue());
                }

                final FlurryAdNativeAsset appCategoryAsset = this.nativeAd.getAsset(FlurryNativeAd.ASSET_APP_CATEGORY);
                if (appCategoryAsset != null) this.addExtra(FlurryNativeAd.EXTRA_APP_CATEGORY, appCategoryAsset.getValue());

                final FlurryAdNativeAsset appRatingAsset = this.nativeAd.getAsset(FlurryNativeAd.ASSET_APP_RATING);
                if(appRatingAsset != null) this.setStarRating(FlurryNativeAd.getStarRatingValue(appRatingAsset.getValue()));
            }

            final FlurryAdNativeAsset ctaAsset = this.nativeAd.getAsset(FlurryNativeAd.CALL_TO_ACTION);
            if (ctaAsset != null) this.setCallToAction(ctaAsset.getValue());

            this.setImpressionMinTimeViewed(BaseStaticNativeAd.IMPRESSION_MIN_TIME);

            final List<String> imageUrls = this.getImageUrls();
            if (!imageUrls.isEmpty()) this.preCacheImages(this.getImageUrls());

            this.getCustomEventNativeListener().onNativeAdLoaded(this);
        }
    }

    @Override
    public void onImpressionLogged(final FlurryAdNative flurryAdNative) {
        this.notifyAdImpressed();
    }

    @Override
    public void onClicked(final FlurryAdNative flurryAdNative) {
        this.notifyAdClicked();
    }

    @Override
    public void onShowFullscreen(final FlurryAdNative flurryAdNative) {
    }

    @Override
    public void onCloseFullscreen(final FlurryAdNative flurryAdNative) {
    }

    @Override
    public void onExpanded(final FlurryAdNative flurryAdNative) {
    }

    @Override
    public void onCollapsed(final FlurryAdNative flurryAdNative) {
    }

    @Override
    public void onAppExit(final FlurryAdNative flurryAdNative) {
    }

    @Override
    public void onError(final FlurryAdNative nativeAd, final FlurryAdErrorType adErrorType, final int errorCode) {
        switch (adErrorType) {
            case FETCH:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                break;

            case CLICK:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
                break;

            case RENDER:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.NATIVE_RENDERER_CONFIGURATION_ERROR);
                break;

            default:
                this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                break;
        }
    }

    /**
     * Returns {@code true} if the fetched native ad is an app-install ad; otherwise, {@code false}.
     * @return {@code true} if the fetched native ad is an app-install ad; otherwise, {@code false}.
     */
    public boolean isAppInstallAd() {
        return this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_RATING_IMG) != null || this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_HQ_RATING_IMG) != null || this.nativeAd.getAsset(ASSET_APP_CATEGORY) != null;
    }

    /**
     * Returns a {@link FlurryAdTargeting} instance for ad targeting customization.
     * @return A {@link FlurryAdTargeting} instance for ad targeting customization.
     */
    @NonNull
    public FlurryAdTargeting getTargetingData() {
        return this.targetingData;
    }

    @NonNull
    private List<String> getImageUrls() {
        final List<String> imageUrls = new ArrayList<>(2);

        if (!TextUtils.isEmpty(this.getMainImageUrl())) imageUrls.add(this.getMainImageUrl());
        if (!TextUtils.isEmpty(this.getIconImageUrl())) imageUrls.add(this.getIconImageUrl());

        return imageUrls;
    }

    @Nullable
    private static Double getStarRatingValue(@Nullable final String appRatingString) {
        // App rating String should be of the form X/Y. E.g. 80/100
        if (appRatingString != null) {
            final String[] ratingParts = appRatingString.split("/");

            if (ratingParts.length == 2) {
                try {
                    return (double)Integer.parseInt(ratingParts[0]) / Integer.parseInt(ratingParts[1]) * BaseStaticNativeAd.MAX_STAR_RATING;
                } catch (final NumberFormatException e) {
                    Log.w(FlurryNativeAd.class.getName(), e.getMessage(), e);
                }
            }
        }

        return null;
    }
}
