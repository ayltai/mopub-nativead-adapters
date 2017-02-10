package org.github.ayltai.mopub.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public abstract class AdMobNativeAd extends BaseStaticNativeAd implements NativeAppInstallAd.OnAppInstallAdLoadedListener, NativeContentAd.OnContentAdLoadedListener {
    //region Variables

    private final AdRequest.Builder       adRequestBuilder       = new AdRequest.Builder();
    private final NativeAdOptions.Builder nativeAdOptionsBuilder = new NativeAdOptions.Builder();
    private final String                  adUnitId;

    private NativeAppInstallAd nativeAppInstallAd;
    private NativeContentAd    nativeContentAd;

    //endregion

    protected AdMobNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final String adUnitId) {
        super(context.getApplicationContext(), customEventNativeListener);

        this.adUnitId = adUnitId;
    }

    @CallSuper
    @Override
    public void prepare(@NonNull final View view) {
        super.prepare(view);

        if (this.nativeAppInstallAd != null && view instanceof NativeAppInstallAdView) {
            final NativeAppInstallAdView adView = (NativeAppInstallAdView)view;

            this.prepare(adView, this.nativeAppInstallAd);

            adView.setNativeAd(this.nativeAppInstallAd);

            this.notifyAdImpressed();
        } else if (this.nativeContentAd != null && view instanceof NativeContentAdView) {
            final NativeContentAdView adView = (NativeContentAdView)view;

            this.prepare(adView, this.nativeContentAd);

            adView.setNativeAd(this.nativeContentAd);

            this.notifyAdImpressed();
        }
    }

    /**
     * To render ad view properly, implementation is expected to call {@link NativeAppInstallAdView#setHeadlineView(View)}, {@link NativeAppInstallAdView#setBodyView(View)}, {@link NativeAppInstallAdView#setCallToActionView(View)}, {@link NativeAppInstallAdView#setImageView(View)}, {@link NativeAppInstallAdView#setIconView(View)}, and/or {@link NativeAppInstallAdView#setStarRatingView(View)} methods,
     * and render the ad view text/images using {@link NativeAppInstallAd#getHeadline()}, {@link NativeAppInstallAd#getBody()}, {@link NativeAppInstallAd#getCallToAction()}, {@link NativeAppInstallAd#getImages()}, {@link NativeAppInstallAd#getIcon()}, and/or {@link NativeAppInstallAd#getStarRating()} methods.
     * @param adView The native ad view to render the ad.
     * @param nativeAd The native ad instance used to render the ad view.
     */
    protected abstract void prepare(@NonNull NativeAppInstallAdView adView, @NonNull NativeAppInstallAd nativeAd);

    /**
     * To render ad view properly, implementation is expected to call {@link NativeContentAdView#setHeadlineView(View)}, {@link NativeContentAdView#setBodyView(View)}, {@link NativeContentAdView#setCallToActionView(View)}, and/or {@link NativeContentAdView#setImageView(View)} methods,
     * and render the ad view text/images using {@link NativeContentAd#getHeadline()}, {@link NativeContentAd#getBody()}, {@link NativeContentAd#getCallToAction()}, and/or {@link NativeContentAd#getImages()} methods.
     * @param adView The native ad view to render the ad.
     * @param nativeAd The native ad instance used to render the ad view.
     */
    protected abstract void prepare(@NonNull NativeContentAdView adView, @NonNull NativeContentAd nativeAd);

    @Override
    public void clear(@NonNull final View view) {
        super.clear(view);

        if (view instanceof NativeAdView) ((NativeAdView)view).destroy();
    }

    @CallSuper
    @Override
    public void destroy() {
        super.destroy();

        if (this.nativeAppInstallAd != null) this.nativeAppInstallAd.destroy();
        if (this.nativeContentAd != null) this.nativeContentAd.destroy();
    }

    @Override
    public void fetchAd() {
        new AdLoader.Builder(this.getContext(), this.adUnitId)
            .forAppInstallAd(AdMobNativeAd.this)
            .forContentAd(AdMobNativeAd.this)
            .withNativeAdOptions(this.getNativeAdOptionsBuilder().build())
            .withAdListener(new AdListener() {
                @Override
                public void onAdOpened() {
                    AdMobNativeAd.this.notifyAdClicked();
                }

                @Override
                public void onAdFailedToLoad(final int errorCode) {
                    AdMobNativeAd.this.onAdFailedToLoad(errorCode);
                }
            })
            .build()
            .loadAd(this.getAdRequestBuilder().build());
    }

    public NativeAd getNativeAd() {
        return this.nativeAppInstallAd == null ? this.nativeContentAd : this.nativeAppInstallAd;
    }

    @Override
    public void onAppInstallAdLoaded(final NativeAppInstallAd nativeAd) {
        this.nativeAppInstallAd = nativeAd;

        this.render(this.nativeAppInstallAd);

        this.setImpressionMinTimeViewed(BaseStaticNativeAd.IMPRESSION_MIN_TIME);
    }

    @Override
    public void onContentAdLoaded(final NativeContentAd nativeAd) {
        this.nativeContentAd = nativeAd;

        this.render(this.nativeContentAd);

        this.setImpressionMinTimeViewed(BaseStaticNativeAd.IMPRESSION_MIN_TIME);
    }

    protected void onAdFailedToLoad(final int errorCode) {
        switch (errorCode) {
            case AdRequest.ERROR_CODE_NO_FILL:
                AdMobNativeAd.this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.NETWORK_NO_FILL);
                break;

            case AdRequest.ERROR_CODE_NETWORK_ERROR:
                AdMobNativeAd.this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.CONNECTION_ERROR);
                break;

            case AdRequest.ERROR_CODE_INVALID_REQUEST:
                AdMobNativeAd.this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.INVALID_REQUEST_URL);
                break;

            case AdRequest.ERROR_CODE_INTERNAL_ERROR:
                AdMobNativeAd.this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.SERVER_ERROR_RESPONSE_CODE);
                break;

            default:
                AdMobNativeAd.this.getCustomEventNativeListener().onNativeAdFailed(NativeErrorCode.UNEXPECTED_RESPONSE_CODE);
                break;
        }
    }

    /**
     * Returns an {@link com.google.android.gms.ads.AdRequest.Builder} instance to let you custom ad requests.
     * <p>For details on how to customize it, please refer to <a href="https://firebase.google.com/docs/reference/android/com/google/android/gms/ads/AdRequest">this guide</a></p>
     * @return An {@link com.google.android.gms.ads.AdRequest.Builder} instance to let you custom ad requests.
     */
    @NonNull
    public AdRequest.Builder getAdRequestBuilder() {
        return this.adRequestBuilder;
    }

    /**
     * Returns a {@link com.google.android.gms.ads.formats.NativeAdOptions.Builder} instance to let you customize ad requests.
     * <p>For details on how to customize it, please refer to <a href="https://firebase.google.com/docs/admob/android/native-advanced">this guide</a>.</p>
     * @return A {@link com.google.android.gms.ads.formats.NativeAdOptions.Builder} instance to let you customize ad requests.
     */
    @NonNull
    public NativeAdOptions.Builder getNativeAdOptionsBuilder() {
        return this.nativeAdOptionsBuilder;
    }

    private void render(@NonNull final NativeAppInstallAd nativeAd) {
        this.setHeadline(nativeAd);
        this.setBody(nativeAd);
        this.setCallToAction(nativeAd);
        this.setStarRating(nativeAd);

        final List<String> imageUrls = new ArrayList<>();

        if (nativeAd.getIcon() != null && nativeAd.getIcon().getUri() != null) {
            final String imageUrl = nativeAd.getIcon().getUri().toString();
            this.setIconImageUrl(imageUrl);
            imageUrls.add(imageUrl);
        }

        if (nativeAd.getImages() != null && nativeAd.getImages().size() >= 1 && nativeAd.getImages().get(0).getUri() != null) {
            final String imageUrl = nativeAd.getImages().get(0).getUri().toString();
            this.setMainImageUrl(imageUrl);
            imageUrls.add(imageUrl);
        }

        this.preCacheImages(imageUrls);
    }

    private void render(@NonNull final NativeContentAd nativeAd) {
        this.setHeadline(nativeAd);
        this.setBody(nativeAd);
        this.setCallToAction(nativeAd);

        final List<String> imageUrls = new ArrayList<>();

        if (nativeAd.getLogo() != null && nativeAd.getLogo().getUri() != null) {
            final String imageUrl = nativeAd.getLogo().getUri().toString();
            this.setIconImageUrl(imageUrl);
            imageUrls.add(imageUrl);
        }

        if (nativeAd.getImages() != null && nativeAd.getImages().size() >= 1 && nativeAd.getImages().get(0).getUri() != null) {
            final String imageUrl = nativeAd.getImages().get(0).getUri().toString();
            this.setMainImageUrl(imageUrl);
            imageUrls.add(imageUrl);
        }

        this.preCacheImages(imageUrls);
    }

    private void setHeadline(@NonNull final NativeAppInstallAd nativeAd) {
        if (!TextUtils.isEmpty(nativeAd.getHeadline())) this.setTitle(nativeAd.getHeadline().toString());
    }

    private void setHeadline(@NonNull final NativeContentAd nativeAd) {
        if (!TextUtils.isEmpty(nativeAd.getHeadline())) this.setTitle(nativeAd.getHeadline().toString());
    }

    private void setBody(@NonNull final NativeAppInstallAd nativeAd) {
        if (!TextUtils.isEmpty(nativeAd.getBody())) this.setText(nativeAd.getBody().toString());
    }

    private void setBody(@NonNull final NativeContentAd nativeAd) {
        if (!TextUtils.isEmpty(nativeAd.getBody())) this.setText(nativeAd.getBody().toString());
    }

    private void setCallToAction(@NonNull final NativeAppInstallAd nativeAd) {
        if (!TextUtils.isEmpty(nativeAd.getCallToAction())) this.setCallToAction(nativeAd.getCallToAction().toString());
    }

    private void setCallToAction(@NonNull final NativeContentAd nativeAd) {
        if (!TextUtils.isEmpty(nativeAd.getCallToAction())) this.setCallToAction(nativeAd.getCallToAction().toString());
    }

    private void setStarRating(@NonNull final NativeAppInstallAd nativeAd) {
        if (nativeAd.getStarRating() != null && nativeAd.getStarRating() > 0) this.setStarRating(nativeAd.getStarRating());
    }
}
