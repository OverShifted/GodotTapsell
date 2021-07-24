package com.overshifted.godottapsell;

import android.app.Activity;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellAdRequestListener;
import ir.tapsell.sdk.TapsellAdRequestOptions;
import ir.tapsell.sdk.TapsellAdShowListener;
import ir.tapsell.sdk.TapsellShowOptions;
import java.util.Set;
import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.SignalInfo;
import org.godotengine.godot.plugin.UsedByGodot;

public class GodotTapsell extends GodotPlugin {

    private Activity m_activity;
    private String m_zone_id;
    private String m_ad_id;

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
        ArraySet<SignalInfo> arraySet = new ArraySet<SignalInfo>();

        arraySet.add(new SignalInfo("request_ad_on_ad_available", String.class));
        arraySet.add(new SignalInfo("request_ad_on_error", String.class));
        arraySet.add(new SignalInfo("show_ad_on_opened"));
        arraySet.add(new SignalInfo("show_ad_on_closed"));
        arraySet.add(new SignalInfo("show_ad_on_error", String.class));
        arraySet.add(new SignalInfo("show_ad_on_rewarded", boolean.class));

        return arraySet;
    }

    @UsedByGodot
    public void init(String key) {
        Tapsell.initialize(m_activity.getApplication(), key);
    }

    @UsedByGodot
    public void request_ad(String p_zone_id) {
        m_zone_id = p_zone_id;
        Tapsell.requestAd(m_activity.getApplicationContext(), m_zone_id, new TapsellAdRequestOptions(),
            new TapsellAdRequestListener() {
                public void onAdAvailable(String ad_id) {
                    m_ad_id = ad_id;
                    emitSignal("request_ad_on_ad_available", ad_id);
                    Toast.makeText(m_activity, "requestAd: " + ad_id, Toast.LENGTH_LONG).show();
                }

                public void onError(String message) {
                    m_ad_id = "";
                    emitSignal("request_ad_on_error", message);
                    Toast.makeText(m_activity, "requestAd error: " + message, Toast.LENGTH_LONG).show();
                }
            }
        );
    }

    @UsedByGodot
    public void show_ad() {
        Tapsell.showAd(m_activity.getApplicationContext(), m_zone_id, m_ad_id, new TapsellShowOptions(),
            new TapsellAdShowListener() {
                public void onOpened() {
                    emitSignal("show_ad_on_opened");
                    Toast.makeText(m_activity, "Open", Toast.LENGTH_LONG).show();
                }

                public void onClosed() {
                    emitSignal("show_ad_on_closed");
                    Toast.makeText(m_activity, "Close", Toast.LENGTH_LONG).show();
                }

                public void onError(String message) {
                    emitSignal("show_ad_on_error", message);
                    Toast.makeText(m_activity, "showAd error: " + message, Toast.LENGTH_LONG).show();
                }

                public void onRewarded(boolean completed) {
                    emitSignal("show_ad_on_rewarded", completed);
                    Toast.makeText(m_activity, "rewarded: " + completed, Toast.LENGTH_LONG).show();
                }
            }
        );

        m_ad_id = "";
    }
}