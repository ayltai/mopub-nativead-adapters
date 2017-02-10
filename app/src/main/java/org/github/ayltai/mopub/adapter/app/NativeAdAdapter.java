package org.github.ayltai.mopub.adapter.app;

import java.util.List;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import org.github.ayltai.mopub.adapter.AdMobNativeAd;

import com.google.android.gms.ads.formats.NativeAdView;
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeContentAd;

import com.mopub.nativeads.NativeAd;
import com.mopub.nativeads.StaticNativeAd;

final class NativeAdAdapter extends RecyclerView.Adapter<NativeAdViewHolder> implements View.OnAttachStateChangeListener {
    // The ad industry standard aspect ratio is 1.91 although it is ignored by some ad networks
    private static final float AD_IMAGE_ASPECT_RATIO = 1.91f;

    private static final int ID_ADMOB_APP_INSTALL_AD_VIEW = R.id.admob_app_install_ad_view;
    private static final int ID_ADMOB_CONTENT_AD_VIEW     = R.id.admob_content_ad_view;
    private static final int ID_ADMOB_NORMAL_AD_VIEW      = R.id.ad_container;

    private final List<NativeAd> nativeAds;

    public NativeAdAdapter(@NonNull final List<NativeAd> nativeAds) {
        this.nativeAds = nativeAds;
    }

    @Override
    public int getItemCount() {
        return this.nativeAds.size();
    }

    @Override
    public NativeAdViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new NativeAdViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_native_ad_container, parent, false));
    }

    @Override
    public void onBindViewHolder(final NativeAdViewHolder holder, final int position) {
        final NativeAd nativeAd = this.nativeAds.get(position);

        if (nativeAd != null) {
            holder.itemView.addOnAttachStateChangeListener(this);

            // Not sure what the previous ad type is, so just clear all of them
            NativeAdAdapter.clear(nativeAd, holder, NativeAdAdapter.ID_ADMOB_APP_INSTALL_AD_VIEW);
            NativeAdAdapter.clear(nativeAd, holder, NativeAdAdapter.ID_ADMOB_CONTENT_AD_VIEW);
            NativeAdAdapter.clear(nativeAd, holder, NativeAdAdapter.ID_ADMOB_NORMAL_AD_VIEW);

            final StaticNativeAd staticNativeAd = (StaticNativeAd)nativeAd.getBaseNativeAd();

            int viewId = 0;

            // AdMob native ads need special handling
            if (staticNativeAd instanceof AdMobNativeAd) {
                final com.google.android.gms.ads.formats.NativeAd ad = ((AdMobNativeAd)staticNativeAd).getNativeAd();

                if (ad instanceof NativeAppInstallAd) {
                    holder.setAdType(NativeAdViewHolder.AD_TYPE_ADMOB_APP_INSTALL);

                    viewId = NativeAdAdapter.ID_ADMOB_APP_INSTALL_AD_VIEW;
                } else if (ad instanceof NativeContentAd) {
                    holder.setAdType(NativeAdViewHolder.AD_TYPE_ADMOB_CONTENT);

                    viewId = NativeAdAdapter.ID_ADMOB_CONTENT_AD_VIEW;
                } else {
                    Log.w(this.getClass().getSimpleName(), String.format("Unexpected AdMob native ad type: %s", ad.getClass()));
                }
            } else {
                holder.setAdType(NativeAdViewHolder.AD_TYPE_NORMAL);

                viewId = NativeAdAdapter.ID_ADMOB_NORMAL_AD_VIEW;
            }

            if (viewId > 0) {
                nativeAd.renderAdView(holder.adContainer.findViewById(viewId));
                nativeAd.prepare(holder.adContainer.findViewById(viewId));
            }

            NativeAdAdapter.resizeAdImage(holder, R.id.ad_image);
        }
    }

    @Override
    public void onViewAttachedToWindow(final View view) {
    }

    @Override
    public void onViewDetachedFromWindow(final View view) {
        view.removeOnAttachStateChangeListener(this);

        NativeAdAdapter.destroy(view, NativeAdAdapter.ID_ADMOB_APP_INSTALL_AD_VIEW);
        NativeAdAdapter.destroy(view, NativeAdAdapter.ID_ADMOB_CONTENT_AD_VIEW);
    }

    private static void clear(@NonNull final NativeAd nativeAd, @NonNull final NativeAdViewHolder holder, @IdRes final int viewId) {
        final View view = holder.adContainer.findViewById(viewId);

        if (view != null) nativeAd.clear(view);
    }

    private static void destroy(@NonNull final View view, @IdRes final int viewId) {
        final View adView = view.findViewById(viewId);

        if (adView instanceof NativeAdView) ((NativeAdView)adView).destroy();
    }

    private static void resizeAdImage(@NonNull final NativeAdViewHolder holder, @IdRes final int viewId) {
        final View view = holder.adContainer.findViewById(viewId);

        if (view != null) {
            final DisplayMetrics metrics = new DisplayMetrics();
            ((WindowManager)holder.adContainer.getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

            final ViewGroup.LayoutParams params = view.getLayoutParams();
            params.width = metrics.widthPixels;
            params.height = (int)(metrics.widthPixels / NativeAdAdapter.AD_IMAGE_ASPECT_RATIO + 0.5f);

            view.setLayoutParams(params);
        }
    }
}
