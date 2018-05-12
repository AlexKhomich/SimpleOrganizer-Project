package com.cdg.alex.simpleorganizer.fragments;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdg.alex.simpleorganizer.R;
import com.cdg.alex.simpleorganizer.alarm_list_view.AlarmSettingsLoader;
import com.cdg.alex.simpleorganizer.alarm_list_view.AlarmsAdapter;
import com.cdg.alex.simpleorganizer.service.JsonParser;
import com.cdg.alex.simpleorganizer.settings_builder.JsonSettingsStringBuilder;
import com.cdg.alex.simpleorganizer.utils.AlarmParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AlarmFragment extends Fragment {

    private View view;
    private RecyclerView alarmLayout;
    private FloatingActionButton fab;
    private AlarmsAdapter alarmFeedAdapter;
    private List<AlarmSettingsLoader> dataList;
    private static int elementOfAlarm = 0;
    private final String ALARM_NAME = "alarm_";
    private String id = "alarm_";
    private String defaultSong = "android.resource://com.cdg.alex.simpleorganizer/";
    private final int PERMISSION_REQUEST_CODE = 1;
    private AlarmParser alarmParser;

    private boolean isMonday = false;
    private boolean isTuesday = false;
    private boolean isWednesday = false;
    private boolean isThursday = false;
    private boolean isFriday = false;
    private boolean isSaturday = false;
    private boolean isSunday = false;


    public static AlarmFragment newInstance(int num) {
        AlarmFragment alarmFragment = new AlarmFragment();
        Bundle args = new Bundle();
        alarmFragment.setArguments(args);
        return alarmFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmParser = new AlarmParser();
        dataList = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = JsonParser.Companion.getNumberOfAlarms(getContext());
                for (int i = 0; i < count; i++) {
                    dataList.add(new AlarmSettingsLoader(alarmParser.getTime(getContext()), alarmParser.getSwitchState(getContext()), alarmParser.mondayState(getContext()),
                            alarmParser.tuesdayState(getContext()), alarmParser.wednesdayState(getContext()), alarmParser.thursdayState(getContext()), alarmParser.fridayState(getContext()),
                            alarmParser.saturdayState(getContext()), alarmParser.sundayState(getContext()), alarmParser.checkPeriod(getContext()), alarmParser.getRingtone(getContext()), alarmParser.getPeriod(getContext()),
                            alarmParser.getAlarmId(getContext()), alarmParser.getSoundPath(getContext())));
                    elementOfAlarm++;
                }
            }
        }).start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_alarm_rec, container, false);
        alarmLayout = (RecyclerView) view.findViewById(R.id.alarmRecyclerView);

        alarmFeedAdapter = new AlarmsAdapter(getContext(), dataList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        alarmLayout.setLayoutManager(mLayoutManager);
        alarmLayout.setItemAnimator(new DefaultItemAnimator());
        alarmLayout.setAdapter(alarmFeedAdapter);

        accessPermission();

        fab = (FloatingActionButton) view.findViewById(R.id.fabAddAlarm);

        alarmLayout.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy == 0) {
                    fab.show();
                    fab.animate().scaleX(1).scaleY(1).setDuration(150).start();
                }
                if(dy > 0) {
                    fab.animate().scaleX(0).scaleY(0).setDuration(150).start();
                    fab.hide();
                } else if(dy < 0) {
                    fab.show();
                    fab.animate().scaleX(1).scaleY(1).setDuration(150).start();
                }
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlarmsAdderTask alarmsAdderTask = new AlarmsAdderTask();
                alarmsAdderTask.execute();

            }
        });

        return view;
    }

    private void accessPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(super.getActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onDestroyView() {
        elementOfAlarm = 0;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Map<String, String> alarmMap = AlarmsAdapter.getAlarmSettingsMap();
        if (alarmMap != null) {
            for (Map.Entry<String, String> entry : alarmMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                editor.putString(key, value);
                editor.apply();
            }
        }

        alarmLayout.clearOnScrollListeners();
        super.onDestroyView();
    }

    public static int getElementOfAlarm() {
        return elementOfAlarm;
    }

    private String witchDay() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
        return sdf.format(date);
    }

    public boolean isMonday() {
        return isMonday;
    }

    public void setMonday(boolean monday) {
        isMonday = monday;
    }

    public boolean isTuesday() {
        return isTuesday;
    }

    public void setTuesday(boolean tuesday) {
        isTuesday = tuesday;
    }

    public boolean isWednesday() {
        return isWednesday;
    }

    public void setWednesday(boolean wednesday) {
        isWednesday = wednesday;
    }

    public boolean isThursday() {
        return isThursday;
    }

    public void setThursday(boolean thursday) {
        isThursday = thursday;
    }

    public boolean isFriday() {
        return isFriday;
    }

    public void setFriday(boolean friday) {
        isFriday = friday;
    }

    public boolean isSaturday() {
        return isSaturday;
    }

    public void setSaturday(boolean saturday) {
        isSaturday = saturday;
    }

    public boolean isSunday() {
        return isSunday;
    }

    public void setSunday(boolean sunday) {
        isSunday = sunday;
    }


    public class AlarmsAdderTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            int result = 0;
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
            Map<String, ?> alarmMap = JsonParser.Companion.getSharedPrefs(getContext());
            for (Map.Entry<String, ?> entry : alarmMap.entrySet()) {
                String value = entry.getValue().toString();
                String idAlarm;
                String[] stringNumber = null;

                try {
                    JSONObject jsonObject = new JSONObject(value);
                    JSONArray jsonArray = jsonObject.getJSONArray("settings");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.getJSONObject(i);
                        idAlarm = jObj.getString("id");
                        stringNumber = idAlarm.split("_");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                assert stringNumber != null;
                int temp = Integer.parseInt(stringNumber[1]);
                if (result == 0 || temp > result) {
                    result = temp;
                } else if (temp < result) {
                    break;
                }
            }

            int numberOfAlarms = JsonParser.Companion.getNumberOfAlarms(getContext());
            numberOfAlarms++;
            result++;
            id = ALARM_NAME + result;

            switch (witchDay()) {
                case "Mon":
                    setMonday(true);
                    break;
                case "Tue":
                    setTuesday(true);
                    break;
                case "Wed":
                    setWednesday(true);
                    break;
                case "Thu":
                    setThursday(true);
                    break;
                case "Fri":
                    setFriday(true);
                    break;
                case "Sat":
                    setSaturday(true);
                    break;
                case "Sun":
                    setSunday(true);
            }

            dataList.add(new AlarmSettingsLoader("8:00",
                    true,
                    isMonday(),
                    isTuesday(),
                    isWednesday(),
                    isThursday(),
                    isFriday(),
                    isSaturday(),
                    isSunday(),
                    false,
                    "Default",
                    "2/2",
                    id,
                    defaultSong));

            AlarmSettingsLoader alarmSettingsLoader = dataList.get(numberOfAlarms - 1);

            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(alarmSettingsLoader.getTimeTextView())
                    .setPeriod(alarmSettingsLoader.getSetPeriodView()).setRingtone(alarmSettingsLoader.getSetRingtoneView()).setOnOfSwitch(alarmSettingsLoader.isOnOfSwitch())
                    .setMonday(alarmSettingsLoader.isMonday()).setTuesday(alarmSettingsLoader.isTuesday()).setWednesday(alarmSettingsLoader.isWednesday())
                    .setThursday(alarmSettingsLoader.isThursday()).setFriday(alarmSettingsLoader.isFriday()).setSaturday(alarmSettingsLoader.isSaturday())
                    .setSunday(alarmSettingsLoader.isSunday()).setCheckPeriod(alarmSettingsLoader.isCheckPeriod()).setAlarmId(id)
                    .setSoundPath(defaultSong).build();
            String json = jsonSettingsStringBuilder.toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(id, json);
            editor.apply();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            alarmFeedAdapter.notifyDataSetChanged();
            Snackbar.make(view, "New alarm has been added!", Snackbar.LENGTH_SHORT).show();
        }
    }
}
