package org.github.ayltai.mopub.adapter.app;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

final class NativeAdViewHolder extends RecyclerView.ViewHolder {
    public static final int AD_TYPE_NORMAL            = 0;
    public static final int AD_TYPE_ADMOB_APP_INSTALL = 1;
    public static final int AD_TYPE_ADMOB_CONTENT     = 2;

    final ViewGroup adContainer;

    public NativeAdViewHolder(final View itemView) {
        super(itemView);

        this.adContainer = (ViewGroup)itemView.findViewById(R.id.native_ad_container);
    }

    public void setAdType(final int adType) {
        this.adContainer.removeAllViews();

        int viewId = 0;

        if (adType == NativeAdViewHolder.AD_TYPE_NORMAL) {
            viewId = R.layout.view_native_ad;
        } else if (adType == NativeAdViewHolder.AD_TYPE_ADMOB_APP_INSTALL) {
            viewId = R.layout.view_app_install_ad;
        } else if (adType == NativeAdViewHolder.AD_TYPE_ADMOB_CONTENT) {
            viewId = R.layout.view_content_ad;
        }

        if (viewId > 0) LayoutInflater.from(this.adContainer.getContext()).inflate(viewId, this.adContainer, true);
    }
}
