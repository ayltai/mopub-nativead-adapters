package org.github.ayltai.mopub.adapter.app;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

public final class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        if (!LeakCanary.isInAnalyzerProcess(this)) LeakCanary.install(this);
    }
}
