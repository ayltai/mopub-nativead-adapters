package android.lib.ads.mopub.mediation.adapter.nativead;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.applovin.nativeAds.AppLovinNativeAdLoadListener;
import com.applovin.sdk.AppLovinErrorCodes;
import com.applovin.sdk.AppLovinPostbackListener;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkUtils;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public class AppLovinNativeAd extends BaseStaticNativeAd implements AppLovinNativeAdLoadListener {
    private com.applovin.nativeAds.AppLovinNativeAd nativeAd;

    public AppLovinNativeAd(@NonNull final Context context,
                            @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener) {
        super(context, customEventNativeListener);
    }

    @Override
    public void recordImpression(@NonNull final View view) {
        super.recordImpression(view);

        if (this.nativeAd != null && !TextUtils.isEmpty(this.nativeAd.getImpressionTrackingUrl())) {
            AppLovinSdk.getInstance(this.context)
                .getPostbackService()
                .dispatchPostbackAsync(this.nativeAd.getImpressionTrackingUrl(), new AppLovinPostbackListener() {
                    @Override
                    public void onPostbackSuccess(final String url) {
                        AppLovinNativeAd.this.notifyAdImpressed();
                    }

                    @Override
                    public void onPostbackFailure(final String url, final int errorCode) {
                    }
                });
        }
    }

    @Override
    public void handleClick(@NonNull final View view) {
        super.handleClick(view);

        if (this.nativeAd != null && !TextUtils.isEmpty(this.nativeAd.getClickUrl())) {
            AppLovinSdkUtils.openUrl(this.context, this.nativeAd.getClickUrl(), AppLovinSdk.getInstance(this.context));
            AppLovinNativeAd.this.notifyAdClicked();
        }
    }

    @Override
    public void fetchAd() {
        AppLovinSdk.getInstance(this.context)
            .getNativeAdService()
            .loadNativeAds(1, this);
    }

    @Override
    public void onNativeAdsLoaded(final List list) {
        final List<com.applovin.nativeAds.AppLovinNativeAd> nativeAds = (List<com.applovin.nativeAds.AppLovinNativeAd>)list;

        if (!nativeAds.isEmpty()) {
            this.notifyAdImpressed();

            this.nativeAd = nativeAds.get(0);

            if (!TextUtils.isEmpty(this.nativeAd.getTitle())) {
                this.setTitle(this.nativeAd.getTitle());
            }

            if (!TextUtils.isEmpty(this.nativeAd.getDescriptionText())) {
                this.setText(this.nativeAd.getDescriptionText());
            }

            if (!TextUtils.isEmpty(this.nativeAd.getCtaText())) {
                this.setCallToAction(this.nativeAd.getCtaText());
            }

            if (this.nativeAd.getStarRating() > 0) {
                this.setStarRating((double)this.nativeAd.getStarRating());
            }

            if (!TextUtils.isEmpty(this.nativeAd.getClickUrl())) {
                this.setClickDestinationUrl(this.nativeAd.getClickUrl());
            }

            this.setImpressionMinTimeViewed(BaseStaticNativeAd.IMPRESSION_MIN_TIME);

            final List<String> imageUrls = new ArrayList<>();

            if (!TextUtils.isEmpty(this.nativeAd.getIconUrl())) {
                this.setIconImageUrl(this.nativeAd.getIconUrl());
                imageUrls.add(this.nativeAd.getIconUrl());
            }

            if (!TextUtils.isEmpty(this.nativeAd.getImageUrl())) {
                this.setMainImageUrl(this.nativeAd.getImageUrl());
                imageUrls.add(this.nativeAd.getImageUrl());
            }

            this.preCacheImages(imageUrls);
        }
    }

    @Override
    public void onNativeAdsFailedToLoad(final int errorCode) {
        switch (errorCode) {
            case AppLovinErrorCodes.FETCH_AD_TIMEOUT:
            case AppLovinErrorCodes.INCENTIVIZED_SERVER_TIMEOUT:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_TIMEOUT);
                break;

            case AppLovinErrorCodes.NO_NETWORK:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.CONNECTION_ERROR);
                break;

            case AppLovinErrorCodes.INCENTIVIZED_UNKNOWN_SERVER_ERROR:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.SERVER_ERROR_RESPONSE_CODE);
                break;

            case AppLovinErrorCodes.INVALID_URL:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.INVALID_REQUEST_URL);
                break;

            case AppLovinErrorCodes.NO_FILL:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                break;

            default:
                this.customEventNativeListener.onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
                break;
        }
    }
}
