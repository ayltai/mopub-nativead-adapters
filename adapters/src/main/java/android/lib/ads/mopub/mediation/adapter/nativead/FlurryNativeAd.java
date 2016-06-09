package android.lib.ads.mopub.mediation.adapter.nativead;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.flurry.android.ads.FlurryAdErrorType;
import com.flurry.android.ads.FlurryAdNative;
import com.flurry.android.ads.FlurryAdNativeAsset;
import com.flurry.android.ads.FlurryAdNativeListener;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public class FlurryNativeAd extends BaseStaticNativeAd implements FlurryAdNativeListener {
    //region Constants

    public static final String EXTRA_STAR_RATING_IMG   = "flurry_starratingimage";
    public static final String EXTRA_APP_CATEGORY      = "flurry_appcategorytext";
    public static final String EXTRA_SEC_BRANDING_LOGO = "flurry_brandingimage";

    private static final String CALL_TO_ACTION = "callToAction";

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

    protected FlurryAdNative nativeAd;

    public FlurryNativeAd(@NonNull final Context context,
                          @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener,
                          @NonNull final FlurryAdNative adNative) {
        super(context, customEventNativeListener);
        this.nativeAd = adNative;
    }

    @Override
    public void prepare(@NonNull final View view) {
        super.prepare(view);
        this.nativeAd.setTrackingView(view);
    }

    @Override
    public void clear(@NonNull final View view) {
        super.clear(view);
        this.nativeAd.removeTrackingView();
    }

    @Override
    public void destroy() {
        super.destroy();
        this.nativeAd.destroy();
    }

    @Override
    public void fetchAd() {
        if (this.context != null) {
            this.nativeAd.setListener(this);
            this.nativeAd.fetchAd();
        }
    }

    @Override
    public void onFetched(final FlurryAdNative adNative) {
        if (adNative != null) {
            this.nativeAd = adNative;

            final FlurryAdNativeAsset coverImageAsset = this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_HQ_IMAGE);
            if (coverImageAsset != null && !TextUtils.isEmpty(coverImageAsset.getValue())) {
                this.setMainImageUrl(coverImageAsset.getValue());
            }

            final FlurryAdNativeAsset iconImageAsset  = this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_IMAGE);
            if (iconImageAsset != null && !TextUtils.isEmpty(iconImageAsset.getValue())) {
                this.setIconImageUrl(iconImageAsset.getValue());
            }

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
                    if (ratingImageAsset != null && !TextUtils.isEmpty(ratingImageAsset.getValue())) {
                        this.addExtra(FlurryNativeAd.EXTRA_STAR_RATING_IMG, ratingImageAsset.getValue());
                    }
                }

                final FlurryAdNativeAsset appCategoryAsset = this.nativeAd.getAsset(FlurryNativeAd.ASSET_APP_CATEGORY);
                if (appCategoryAsset != null) {
                    this.addExtra(FlurryNativeAd.EXTRA_APP_CATEGORY, appCategoryAsset.getValue());
                }

                final FlurryAdNativeAsset appRatingAsset = this.nativeAd.getAsset(FlurryNativeAd.ASSET_APP_RATING);
                if(appRatingAsset != null) {
                    this.setStarRating(FlurryNativeAd.getStarRatingValue(appRatingAsset.getValue()));
                }
            }

            final FlurryAdNativeAsset ctaAsset = this.nativeAd.getAsset(FlurryNativeAd.CALL_TO_ACTION);
            if(ctaAsset != null){
                this.setCallToAction(ctaAsset.getValue());
            }

            this.setImpressionMinTimeViewed(BaseStaticNativeAd.IMPRESSION_MIN_TIME);

            if (this.getImageUrls() == null || this.getImageUrls().isEmpty()) {
                this.customEventNativeListener.onNativeAdLoaded(this);
            } else {
                this.preCacheImages(this.getImageUrls());
            }
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
    public void onError(final FlurryAdNative adNative, final FlurryAdErrorType adErrorType, final int errorCode) {
        switch (adErrorType) {
            case FETCH:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                break;

            case CLICK:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_ADAPTER_CONFIGURATION_ERROR);
                break;

            case RENDER:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.NATIVE_RENDERER_CONFIGURATION_ERROR);
                break;

            default:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                break;
        }
    }

    protected boolean isAppInstallAd() {
        return this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_RATING_IMG) != null || this.nativeAd.getAsset(FlurryNativeAd.ASSET_SEC_HQ_RATING_IMG) != null || this.nativeAd.getAsset(FlurryNativeAd.ASSET_APP_CATEGORY) != null;
    }

    private List<String> getImageUrls() {
        final List<String> imageUrls = new ArrayList<>(2);

        final String mainImageUrl = this.getMainImageUrl();
        if (mainImageUrl != null) {
            imageUrls.add(this.getMainImageUrl());
        }

        final String iconUrl = this.getIconImageUrl();
        if (iconUrl != null) {
            imageUrls.add(this.getIconImageUrl());
        }

        return imageUrls;
    }

    private static Double getStarRatingValue(@Nullable final String appRatingString) {
        // App rating String should be of the form X/Y. E.g. 80/100
        Double rating = null;

        if (appRatingString != null) {
            final String[] ratingParts = appRatingString.split("/");

            if (ratingParts.length == 2) {
                try {
                    rating = (double)Integer.parseInt(ratingParts[0]) / Integer.parseInt(ratingParts[1]) * BaseStaticNativeAd.MAX_STAR_RATING;
                } catch (final NumberFormatException e) {
                    Log.d(FlurryNativeAd.class.getName(), e.getMessage(), e);
                }
            }
        }

        return rating;
    }
}
