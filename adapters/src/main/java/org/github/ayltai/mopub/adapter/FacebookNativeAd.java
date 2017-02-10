package org.github.ayltai.mopub.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.ImpressionListener;
import com.facebook.ads.NativeAd;
import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public class FacebookNativeAd extends BaseStaticNativeAd implements AdListener, ImpressionListener {
    private static final String SOCIAL_CONTEXT_FOR_AD = "socialContextForAd";

    private NativeAd nativeAd;

    public FacebookNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final String adUnitId) {
        super(context.getApplicationContext(), customEventNativeListener);

        this.nativeAd = new NativeAd(this.getContext(), adUnitId);
    }

    @CallSuper
    @Override
    public void prepare(@NonNull final View view) {
        super.prepare(view);

        this.nativeAd.registerViewForInteraction(view);
    }

    @CallSuper
    @Override
    public void clear(@NonNull final View view) {
        super.clear(view);

        this.nativeAd.unregisterView();
    }

    @CallSuper
    @Override
    public void destroy() {
        super.destroy();

        this.nativeAd.destroy();
    }

    @Override
    public void fetchAd() {
        this.nativeAd.setAdListener(this);
        this.nativeAd.setImpressionListener(this);
        this.nativeAd.loadAd();
    }

    @Override
    public void onAdLoaded(final Ad ad) {
        // This identity check is from Facebook's Native API sample code:
        // https://developers.facebook.com/docs/audience-network/android/native-api
        if (!this.nativeAd.equals(ad) || !this.nativeAd.isAdLoaded()) {
            this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.NETWORK_INVALID_STATE);
            return;
        }

        this.setTitle(this.nativeAd.getAdTitle());
        this.setText(this.nativeAd.getAdBody());

        final NativeAd.Image coverImage = this.nativeAd.getAdCoverImage();
        if (coverImage != null) this.setMainImageUrl(coverImage.getUrl());

        final NativeAd.Image icon = this.nativeAd.getAdIcon();
        if (icon != null) this.setIconImageUrl(icon.getUrl());

        this.setCallToAction(this.nativeAd.getAdCallToAction());
        this.setStarRating(FacebookNativeAd.getStarRatingValue(this.nativeAd.getAdStarRating()));

        this.addExtra(FacebookNativeAd.SOCIAL_CONTEXT_FOR_AD, this.nativeAd.getAdSocialContext());

        final NativeAd.Image adChoicesIconImage = this.nativeAd.getAdChoicesIcon();
        if (adChoicesIconImage != null) {
            this.setPrivacyInformationIconImageUrl(adChoicesIconImage.getUrl());
            this.setPrivacyInformationIconClickThroughUrl(this.nativeAd.getAdChoicesLinkUrl());
        }

        this.preCacheImages(this.getImageUrls());

        this.setImpressionMinTimeViewed(BaseStaticNativeAd.IMPRESSION_MIN_TIME);
    }

    @Override
    public void onError(final Ad ad, final AdError adError) {
        if (adError == null) {
            this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.UNSPECIFIED);
        } else {
            switch (adError.getErrorCode()) {
                case AdError.NO_FILL_ERROR_CODE:
                    this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                    break;

                case AdError.NETWORK_ERROR_CODE:
                    this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.CONNECTION_ERROR);
                    break;

                case AdError.SERVER_ERROR_CODE:
                    this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.SERVER_ERROR_RESPONSE_CODE);
                    break;

                default:
                    this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.UNEXPECTED_RESPONSE_CODE);
                    break;
            }
        }
    }

    @Override
    public void onAdClicked(final Ad ad) {
        this.notifyAdClicked();
    }

    @Override
    public void onLoggingImpression(final Ad ad) {
        this.notifyAdImpressed();
    }

    @NonNull
    private List<String> getImageUrls() {
        final List<String> imageUrls = new ArrayList<>();

        if (!TextUtils.isEmpty(this.getMainImageUrl())) imageUrls.add(this.getMainImageUrl());
        if (!TextUtils.isEmpty(this.getIconImageUrl())) imageUrls.add(this.getIconImageUrl());
        if (!TextUtils.isEmpty(this.getPrivacyInformationIconImageUrl())) imageUrls.add(this.getPrivacyInformationIconImageUrl());

        return imageUrls;
    }

    @Nullable
    private static Double getStarRatingValue(final NativeAd.Rating rating) {
        if (rating == null) return null;

        return BaseStaticNativeAd.MAX_STAR_RATING * rating.getValue() / rating.getScale();
    }
}
