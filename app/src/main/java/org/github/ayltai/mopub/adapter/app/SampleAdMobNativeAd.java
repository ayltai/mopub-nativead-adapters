package org.github.ayltai.mopub.adapter.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.TextView;

import org.github.ayltai.mopub.adapter.AdMobNativeAd;

import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import com.mopub.nativeads.CustomEventNative;

public final class SampleAdMobNativeAd extends AdMobNativeAd {
    public SampleAdMobNativeAd(@NonNull final Context context, @NonNull final CustomEventNative.CustomEventNativeListener customEventNativeListener, @NonNull final String adUnitId) {
        super(context, customEventNativeListener, adUnitId);
    }

    @Override
    protected void prepare(@NonNull final NativeAppInstallAdView adView, @NonNull final NativeAppInstallAd nativeAd) {
        final TextView  adTitle        = (TextView)adView.findViewById(R.id.ad_title);
        final TextView  adBody         = (TextView)adView.findViewById(R.id.ad_body);
        final ImageView adImage        = (ImageView)adView.findViewById(R.id.ad_image);
        final TextView  adCallToAction = (TextView)adView.findViewById(R.id.ad_call_to_action);

        adView.setHeadlineView(adTitle);
        adView.setBodyView(adBody);
        adView.setImageView(adImage);
        adView.setCallToActionView(adCallToAction);
    }

    @Override
    protected void prepare(@NonNull final NativeContentAdView adView, @NonNull final NativeContentAd nativeAd) {
        final TextView  adTitle        = (TextView)adView.findViewById(R.id.ad_title);
        final TextView  adBody         = (TextView)adView.findViewById(R.id.ad_body);
        final ImageView adImage        = (ImageView)adView.findViewById(R.id.ad_image);
        final TextView  adCallToAction = (TextView)adView.findViewById(R.id.ad_call_to_action);

        adView.setHeadlineView(adTitle);
        adView.setBodyView(adBody);
        adView.setImageView(adImage);
        adView.setCallToActionView(adCallToAction);
    }
}
