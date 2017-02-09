package org.github.ayltai.mopub.adapter.app;

import java.util.EnumSet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;

public final class MainActivity extends Activity implements MoPubNative.MoPubNativeNetworkListener, NativeAd.MoPubNativeEventListener {
    private MoPubNative moPubNative;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        this.initMoPub("TODO");

        this.setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Fresco.shutDown();

        if (this.moPubNative != null) this.moPubNative.destroy();
    }

    @Override
    public void onNativeLoad(final NativeAd nativeAd) {
        nativeAd.setMoPubNativeEventListener(this);

        // TODO
    }

    @Override
    public void onNativeFail(final NativeErrorCode errorCode) {
        // TODO
    }

    @Override
    public void onImpression(final View view) {
        // TODO
    }

    @Override
    public void onClick(final View view) {
        // TODO
    }

    private void initMoPub(@NonNull final String adUnitId) {
        this.moPubNative = new MoPubNative(this, adUnitId, this);
        this.moPubNative.registerAdRenderer(new MoPubStaticNativeAdRenderer(new ViewBinder.Builder(R.layout.view_native_ad)
            .titleId(R.id.ad_title)
            .textId(R.id.ad_body)
            .mainImageId(R.id.ad_image)
            .callToActionId(R.id.ad_call_to_action)
            .build()));
    }

    private void requestAd() {
        if (this.moPubNative != null) this.moPubNative.makeRequest(new RequestParameters.Builder()
            .desiredAssets(EnumSet.of(
                RequestParameters.NativeAdAsset.TITLE,
                RequestParameters.NativeAdAsset.TEXT,
                RequestParameters.NativeAdAsset.MAIN_IMAGE,
                RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT))
            .build());
    }
}
