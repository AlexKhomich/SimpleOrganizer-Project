package com.cdg.alex.simpleorganizer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdg.alex.simpleorganizer.settings_list_view.SettingsAdapter;
import com.cdg.alex.simpleorganizer.settings_list_view.SettingsLoader;

import java.util.Map;

/**
 * Created by alex on 26/10/16.
 */

public class SettingsFragment extends Fragment {
    private View view;
    private SettingsAdapter settingsAdapter;
    private RecyclerView settingsLayout;
    private SettingsLoader settingsLoader;
    private final String SILENCE_AFTER = "silence_after";
    private final String TIME_OF_SNOOZE = "time_of_snooze";
    private final String VIBRATE = "vibrate";
    private final String VOLUME = "volume";

    public SettingsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        String silenceAfter = sharedPreferences.getString(SILENCE_AFTER, "1 min");
        String timeOfSnooze = sharedPreferences.getString(TIME_OF_SNOOZE, "1 min");
        boolean isVibrate = Boolean.parseBoolean(sharedPreferences.getString(VIBRATE, "false"));
        int volumeValue = Integer.parseInt(sharedPreferences.getString(VOLUME, "30"));

        settingsLoader = new SettingsLoader(silenceAfter, timeOfSnooze, isVibrate, volumeValue);

        settingsLayout = (RecyclerView) view.findViewById(R.id.settingsView);


        settingsAdapter = new SettingsAdapter(getContext(), settingsLoader);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        settingsLayout.setLayoutManager(mLayoutManager);
        settingsLayout.setItemAnimator(new DefaultItemAnimator());
        settingsLayout.setAdapter(settingsAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Map<String, String> settingsMap;
        settingsMap = SettingsAdapter.getCacheSettings();
        for(Map.Entry<String, String> entry: settingsMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            editor.putString(key, value);
        }

        editor.apply();
    }
}
