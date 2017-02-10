package org.github.ayltai.mopub.adapter.app;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mopub.nativeads.MoPubNative;
import com.mopub.nativeads.MoPubStaticNativeAdRenderer;
import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.NativeErrorCode;
import com.mopub.nativeads.RequestParameters;
import com.mopub.nativeads.ViewBinder;

public final class MainActivity extends Activity implements MoPubNative.MoPubNativeNetworkListener, NativeAd.MoPubNativeEventListener {
    private static final int MAX_ITEMS = 10;

    private final List<NativeAd> nativeAds = new ArrayList<>();

    private MoPubNative     moPubNative;
    private NativeAdAdapter adapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        this.initMoPub("TODO");

        this.adapter = new NativeAdAdapter(this.nativeAds);

        final RecyclerView recyclerView = (RecyclerView)LayoutInflater.from(this).inflate(R.layout.activity_main, (ViewGroup)this.findViewById(android.R.id.content), false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(this.adapter);

        this.setContentView(recyclerView);

        this.requestAd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Fresco.shutDown();

        for (final NativeAd nativeAd : this.nativeAds) nativeAd.destroy();

        if (this.moPubNative != null) this.moPubNative.destroy();
    }

    @Override
    public void onNativeLoad(final NativeAd nativeAd) {
        Log.i(this.getClass().getSimpleName(), "Native ad loaded");

        nativeAd.setMoPubNativeEventListener(this);

        synchronized (this.nativeAds) {
            this.nativeAds.add(nativeAd);
            this.adapter.notifyItemInserted(this.nativeAds.size() - 1);
        }

        if (this.nativeAds.size() < MainActivity.MAX_ITEMS) this.requestAd();
    }

    @Override
    public void onNativeFail(final NativeErrorCode errorCode) {
        Log.w(this.getClass().getSimpleName(), errorCode.toString());
    }

    @Override
    public void onImpression(final View view) {
        // Ad impression has been recorded on MoPub and the corresponding ad network
    }

    @Override
    public void onClick(final View view) {
        // Ad click has been recorded on MoPub and the corresponding ad network
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
