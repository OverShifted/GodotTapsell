package com.overshifted.godottapsell;

import java.util.Set;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

import ir.tapsell.plus.AdRequestCallback;
import ir.tapsell.plus.AdShowListener;
import ir.tapsell.plus.TapsellPlus;
import ir.tapsell.plus.TapsellPlusBannerType;
import ir.tapsell.plus.TapsellPlusInitListener;
import ir.tapsell.plus.model.AdNetworkError;
import ir.tapsell.plus.model.AdNetworks;
import ir.tapsell.plus.model.TapsellPlusAdModel;
import ir.tapsell.plus.model.TapsellPlusErrorModel;

public class GodotTapsell extends GodotPlugin {

    private Activity m_activity;

    public GodotTapsell(Godot godot) {
        super(godot);
        m_activity = getActivity();
    }

    @NonNull
    public String getPluginName() {
        return "GodotTapsell";
    }

    @NonNull
    public Set<SignalInfo> getPluginSignals() {
        ArraySet<SignalInfo> signals = new ArraySet<>();

        signals.add(new SignalInfo("init_success"));
        signals.add(new SignalInfo("init_failed", String.class, String.class));

        signals.add(new SignalInfo("banner_ad_request_response", String.class, String.class));
        signals.add(new SignalInfo("banner_ad_request_error", String.class, String.class));

        signals.add(new SignalInfo("banner_ad_opened", String.class));
        signals.add(new SignalInfo("banner_ad_closed", String.class));
        signals.add(new SignalInfo("banner_ad_rewarded", String.class));
        signals.add(new SignalInfo("banner_ad_show_error", String.class, String.class));

        signals.add(new SignalInfo("video_ad_request_response", String.class, String.class));
        signals.add(new SignalInfo("video_ad_request_error", String.class, String.class));

        signals.add(new SignalInfo("video_ad_opened", String.class));
        signals.add(new SignalInfo("video_ad_closed", String.class));
        signals.add(new SignalInfo("video_ad_rewarded", String.class));
        signals.add(new SignalInfo("video_ad_show_error", String.class, String.class));

        return signals;
    }

    @UsedByGodot
    public void init(String key, boolean p_override_gdrp) {
        TapsellPlus.initialize(m_activity, key, new TapsellPlusInitListener() {
            @Override
            public void onInitializeSuccess(AdNetworks adNetworks) {
                emitSignal("init_success");
            }

            @Override
            public void onInitializeFailed(AdNetworks adNetworks, AdNetworkError adNetworkError) {
                emitSignal("init_failed", adNetworkError.getErrorDomain(), adNetworkError.getErrorMessage());
            }
        });

        if (p_override_gdrp)
            TapsellPlus.setGDPRConsent(m_activity, true);
    }

    @UsedByGodot
    public void create_banner_frame(int width, int height, int gravity) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
                FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(width, height, gravity);
                FrameLayout fl = new FrameLayout(m_activity);
                fl.setLayoutParams(lp);
                fl.setId(R.id.ad_banner);
                m_activity.addContentView((View) fl, (ViewGroup.LayoutParams) lp);
            }
        });
    }

    @UsedByGodot
    public void request_banner_ad(String p_zone_id, int p_banner_type) {
        TapsellPlusBannerType tapsell_banner_type = TapsellPlusBannerType.BANNER_320x50;

        switch(p_banner_type) {
            case 0: tapsell_banner_type = TapsellPlusBannerType.BANNER_320x50; break;
            case 1: tapsell_banner_type = TapsellPlusBannerType.BANNER_320x90; break;
            case 2: tapsell_banner_type = TapsellPlusBannerType.BANNER_320x100; break;
            case 3: tapsell_banner_type = TapsellPlusBannerType.BANNER_250x250; break;
            case 4: tapsell_banner_type = TapsellPlusBannerType.BANNER_300x250; break;
            case 5: tapsell_banner_type = TapsellPlusBannerType.BANNER_468x60; break;
            case 6: tapsell_banner_type = TapsellPlusBannerType.BANNER_728x90; break;
            case 7: tapsell_banner_type = TapsellPlusBannerType.BANNER_160x600; break;
        }

        TapsellPlus.requestStandardBannerAd(m_activity, p_zone_id, tapsell_banner_type, new AdRequestCallback() {
            @Override
            public void response(TapsellPlusAdModel tapsellPlusAdModel) {
                super.response(tapsellPlusAdModel);
                emitSignal("banner_ad_request_response", p_zone_id, tapsellPlusAdModel.getResponseId());
            }

            @Override
            public void error(String s) {
                super.error(s);
                emitSignal("banner_ad_request_error", p_zone_id, s);
            }
        });
    }

    @UsedByGodot
    public void show_banner_ad(String p_id) {
        TapsellPlus.showStandardBannerAd(m_activity, p_id, m_activity.findViewById(R.id.ad_banner), new AdShowListener() {
            @Override
            public void onOpened(TapsellPlusAdModel tapsellPlusAdModel) {
                super.onOpened(tapsellPlusAdModel);
                emitSignal("banner_ad_opened", tapsellPlusAdModel.getResponseId());
            }

            @Override
            public void onClosed(TapsellPlusAdModel tapsellPlusAdModel) {
                super.onClosed(tapsellPlusAdModel);
                emitSignal("banner_ad_closed", tapsellPlusAdModel.getResponseId());
            }

            @Override
            public void onRewarded(TapsellPlusAdModel tapsellPlusAdModel) {
                super.onRewarded(tapsellPlusAdModel);
                emitSignal("banner_ad_rewarded", tapsellPlusAdModel.getResponseId());
            }

            @Override
            public void onError(TapsellPlusErrorModel tapsellPlusErrorModel) {
                super.onError(tapsellPlusErrorModel);
                emitSignal("banner_ad_show_error", tapsellPlusErrorModel.getResponseId(), tapsellPlusErrorModel.getErrorMessage());
            }
        });
    }

    @UsedByGodot
    public void request_video_ad(String p_zone_id) {
        TapsellPlus.requestRewardedVideoAd(m_activity, p_zone_id, new AdRequestCallback() {
            @Override
            public void response(TapsellPlusAdModel tapsellPlusAdModel) {
                super.response(tapsellPlusAdModel);
                emitSignal("video_ad_request_response", p_zone_id, tapsellPlusAdModel.getResponseId());
            }

            @Override
            public void error(String s) {
                super.error(s);
                emitSignal("video_ad_request_error", p_zone_id, s);
            }
        });
    }

    @UsedByGodot
    public void show_video_ad(String p_id) {
        TapsellPlus.showRewardedVideoAd(m_activity, p_id, new AdShowListener() {
            @Override
            public void onOpened(TapsellPlusAdModel tapsellPlusAdModel) {
                super.onOpened(tapsellPlusAdModel);
                emitSignal("video_ad_opened", tapsellPlusAdModel.getResponseId());
            }

            @Override
            public void onClosed(TapsellPlusAdModel tapsellPlusAdModel) {
                super.onClosed(tapsellPlusAdModel);
                emitSignal("video_ad_closed", tapsellPlusAdModel.getResponseId());
            }

            @Override
            public void onRewarded(TapsellPlusAdModel tapsellPlusAdModel) {
                super.onRewarded(tapsellPlusAdModel);
                emitSignal("video_ad_rewarded", tapsellPlusAdModel.getResponseId());
            }

            @Override
            public void onError(TapsellPlusErrorModel tapsellPlusErrorModel) {
                super.onError(tapsellPlusErrorModel);
                emitSignal("video_ad_show_error", tapsellPlusErrorModel.getResponseId(), tapsellPlusErrorModel.getErrorMessage());
            }
        });
    }
}
