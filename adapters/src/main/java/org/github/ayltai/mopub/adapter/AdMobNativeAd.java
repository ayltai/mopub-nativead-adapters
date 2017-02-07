package org.github.ayltai.mopub.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import com.mopub.nativeads.CustomEventNative;
import com.mopub.nativeads.NativeErrorCode;

public class AdMobNativeAd extends BaseStaticNativeAd implements NativeAppInstallAd.OnAppInstallAdLoadedListener, NativeContentAd.OnContentAdLoadedListener {
    //region Variables

    private final AdRequest.Builder       adRequestBuilder       = new AdRequest.Builder();
    private final NativeAdOptions.Builder nativeAdOptionsBuilder = new NativeAdOptions.Builder();
    private final String                  adUnitId;

    private NativeAppInstallAd nativeAppInstallAd;
    private NativeContentAd    nativeContentAd;

    //endregion

    public AdMobNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final String adUnitId) {
        super(context.getApplicationContext(), customEventNativeListener);

        this.adUnitId = adUnitId;
    }

    @Override
    public void prepare(@NonNull final View view) {
        super.prepare(view);

        if (this.nativeAppInstallAd != null && view instanceof NativeAppInstallAdView) {
            ((NativeAppInstallAdView)view).setNativeAd(this.nativeAppInstallAd);

            this.notifyAdImpressed();
        } else if (this.nativeContentAd != null && view instanceof NativeContentAdView) {
            ((NativeContentAdView)view).setNativeAd(this.nativeContentAd);

            this.notifyAdImpressed();
        }
    }

    @Override
    public void clear(@NonNull final View view) {
        super.clear(view);

        if (view instanceof NativeAdView) ((NativeAdView)view).destroy();
    }

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

    @Override
    public void onAppInstallAdLoaded(final NativeAppInstallAd nativeAd) {
        this.nativeAppInstallAd = nativeAd;

        this.render(this.nativeAppInstallAd);

        this.setImpressionMinTimeViewed(BaseStaticNativeAd.IMPRESSION_MIN_TIME);

        this.getCustomEventNativeListener().onNativeAdLoaded(this);
    }

    @Override
    public void onContentAdLoaded(final NativeContentAd nativeAd) {
        this.nativeContentAd = nativeAd;

        this.render(this.nativeContentAd);

        this.setImpressionMinTimeViewed(BaseStaticNativeAd.IMPRESSION_MIN_TIME);

        this.getCustomEventNativeListener().onNativeAdLoaded(this);
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

    private void render(final NativeAppInstallAd nativeAd) {
        if (!TextUtils.isEmpty(nativeAd.getHeadline())) this.setTitle(nativeAd.getHeadline().toString());
        if (!TextUtils.isEmpty(nativeAd.getBody())) this.setText(nativeAd.getBody().toString());
        if (!TextUtils.isEmpty(nativeAd.getCallToAction())) this.setCallToAction(nativeAd.getCallToAction().toString());

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

        if (nativeAd.getStarRating() != null && nativeAd.getStarRating() > 0) this.setStarRating(nativeAd.getStarRating());
    }

    private void render(final NativeContentAd nativeAd) {
        if (!TextUtils.isEmpty(nativeAd.getHeadline())) this.setTitle(nativeAd.getHeadline().toString());
        if (!TextUtils.isEmpty(nativeAd.getBody())) this.setText(nativeAd.getBody().toString());
        if (!TextUtils.isEmpty(nativeAd.getCallToAction())) this.setCallToAction(nativeAd.getCallToAction().toString());

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
}
