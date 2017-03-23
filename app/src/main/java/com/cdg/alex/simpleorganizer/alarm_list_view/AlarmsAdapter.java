package com.cdg.alex.simpleorganizer.alarm_list_view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.cdg.alex.simpleorganizer.R;
import com.cdg.alex.simpleorganizer.service.JsonParser;
import com.cdg.alex.simpleorganizer.settings_builder.JsonSettingsStringBuilder;
import com.cdg.alex.simpleorganizer.utils.PeriodSetter;
import com.cdg.alex.simpleorganizer.utils.PeriodSetterBuilder;
import com.cdg.alex.simpleorganizer.utils.SoundPickerDialog;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class AlarmsAdapter extends RecyclerView.Adapter<AlarmsAdapter.AlarmsSettingsLoaderHolder> {

    private Context context;
    private List<AlarmSettingsLoader> data;
    private static int mHour = 8;
    private static int mMinute = 0;
    private TimePickerDialog.OnTimeSetListener callbackDialog;
    private AlarmSettingsLoader alarmSettingsLoader;
    private String json;
    private String alarmId = "";
    private static HashMap<String, String> alarmSettingsMap = new HashMap<>(); //кэш для хранения настроек после внесения каких-либо изменений
    private ExecutorService service = Executors.newFixedThreadPool(2);

    public static class AlarmsSettingsLoaderHolder extends RecyclerView.ViewHolder {

        TextView timeTextView;
        Switch onOfSwitch;
        ToggleButton monday;
        ToggleButton tuesday;
        ToggleButton wednesday;
        ToggleButton thursday;
        ToggleButton friday;
        ToggleButton saturday;
        ToggleButton sunday;
        ImageButton deleteButton;
        ImageButton ringtoneButton;
        TextView setRingtoneView;
        TextView setPeriodView;
        ImageButton settingsButton;
        CheckBox checkPeriod;

        public AlarmsSettingsLoaderHolder(View itemView) {
            super(itemView);
            timeTextView = (TextView) itemView.findViewById(R.id.timeTextView);
            onOfSwitch = (Switch) itemView.findViewById(R.id.onOfSwitch);
            monday = (ToggleButton) itemView.findViewById(R.id.monday);
            tuesday = (ToggleButton) itemView.findViewById(R.id.tuesday);
            wednesday = (ToggleButton) itemView.findViewById(R.id.wednesday);
            thursday = (ToggleButton) itemView.findViewById(R.id.thursday);
            friday = (ToggleButton) itemView.findViewById(R.id.friday);
            saturday = (ToggleButton) itemView.findViewById(R.id.saturday);
            sunday = (ToggleButton) itemView.findViewById(R.id.sunday);
            checkPeriod = (CheckBox) itemView.findViewById(R.id.checkPeriod);
            deleteButton = (ImageButton) itemView.findViewById(R.id.deleteButton);
            ringtoneButton = (ImageButton) itemView.findViewById(R.id.ringtoneButton);
            setRingtoneView = (TextView) itemView.findViewById(R.id.setRingtoneView);
            setPeriodView = (TextView) itemView.findViewById(R.id.setPeriodView);
            settingsButton = (ImageButton) itemView.findViewById(R.id.settingsButton);

        }

    }

    public AlarmsAdapter(Context context, List<AlarmSettingsLoader> data) {

        this.data = data;
        this.context = context;

    }

    public static HashMap<String, String> getAlarmSettingsMap() {
        return alarmSettingsMap;
    }

    @Override
    public AlarmsSettingsLoaderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_row_card, parent, false);
        return new AlarmsSettingsLoaderHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final AlarmsSettingsLoaderHolder holder, final int position) {

        alarmSettingsLoader = data.get(holder.getAdapterPosition());
        holder.timeTextView.setText(alarmSettingsLoader.getTimeTextView());
        holder.onOfSwitch.setChecked(alarmSettingsLoader.isOnOfSwitch());
        holder.monday.setChecked(alarmSettingsLoader.isMonday());
        holder.tuesday.setChecked(alarmSettingsLoader.isTuesday());
        holder.wednesday.setChecked(alarmSettingsLoader.isWednesday());
        holder.thursday.setChecked(alarmSettingsLoader.isThursday());
        holder.friday.setChecked(alarmSettingsLoader.isFriday());
        holder.saturday.setChecked(alarmSettingsLoader.isSaturday());
        holder.sunday.setChecked(alarmSettingsLoader.isSunday());
        holder.checkPeriod.setChecked(alarmSettingsLoader.isCheckPeriod());
        holder.setRingtoneView.setText(alarmSettingsLoader.getSetRingtoneView());
        holder.setPeriodView.setText(alarmSettingsLoader.getSetPeriodView());

        //проверка на установленный чекбокс
        if (!(holder.checkPeriod.isChecked())) {
            holder.setPeriodView.setTextColor(context.getResources().getColor(R.color.color_not_active_text));
            holder.settingsButton.setImageDrawable(context.getDrawable(R.drawable.settings_not_active));
        } else {
            holder.setPeriodView.setTextColor(context.getResources().getColor(R.color.colorPrimaryText));
            holder.settingsButton.setImageDrawable(context.getDrawable(R.drawable.settings));
        }

//        on/off switch
        holder.onOfSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean temp = holder.onOfSwitch.isChecked();
                alarmSettingsLoader.setOnOfSwitch(temp);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                        Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                        String value;
                        for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                            value = entry.getValue().toString();
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    alarmId = jObj.getString("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (alarmId.equals(sharedPrefsAlarmId)) {
                                JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                        .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                        .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                        .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                        .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                        .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                json = jsonSettingsStringBuilder.toString();

                                break;
                            }
                        }
                        alarmSettingsMap.put(sharedPrefsAlarmId, json);
                    }
                });
            }
        });

//        monday
        holder.monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean temp = holder.monday.isChecked();
                alarmSettingsLoader.setMonday(temp);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                        Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                        String value;
                        for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                            value = entry.getValue().toString();
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    alarmId = jObj.getString("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (alarmId.equals(sharedPrefsAlarmId)) {

                                JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                        .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                        .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                        .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                        .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                        .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                json = jsonSettingsStringBuilder.toString();

                                break;
                            }
                        }
                        alarmSettingsMap.put(sharedPrefsAlarmId, json);
                    }
                });
            }
        });

//        tuesday
        holder.tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean temp = holder.tuesday.isChecked();
                alarmSettingsLoader.setTuesday(temp);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                        Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                        String value;
                        for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                            value = entry.getValue().toString();
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    alarmId = jObj.getString("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (alarmId.equals(sharedPrefsAlarmId)) {

                                JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                        .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                        .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                        .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                        .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                        .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                json = jsonSettingsStringBuilder.toString();

                                break;
                            }
                        }
                        alarmSettingsMap.put(sharedPrefsAlarmId, json);
                    }
                });

            }
        });

//        wednesday
        holder.wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean temp = holder.wednesday.isChecked();
                alarmSettingsLoader.setWednesday(temp);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                        Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                        String value;
                        for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                            value = entry.getValue().toString();
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    alarmId = jObj.getString("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (alarmId.equals(sharedPrefsAlarmId)) {

                                JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                        .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                        .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                        .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                        .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                        .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                json = jsonSettingsStringBuilder.toString();

                                break;
                            }
                        }
                        alarmSettingsMap.put(sharedPrefsAlarmId, json);
                    }
                });

            }
        });

        //        thursday
        holder.thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean temp = holder.thursday.isChecked();
                alarmSettingsLoader.setThursday(temp);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                        Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                        String value;
                        for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                            value = entry.getValue().toString();
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    alarmId = jObj.getString("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (alarmId.equals(sharedPrefsAlarmId)) {

                                JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                        .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                        .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                        .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                        .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                        .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                json = jsonSettingsStringBuilder.toString();

                                break;
                            }
                        }
                        alarmSettingsMap.put(sharedPrefsAlarmId, json);
                    }
                });

            }
        });

        //        friday
        holder.friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean temp = holder.friday.isChecked();
                alarmSettingsLoader.setFriday(temp);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                        Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                        String value;
                        for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                            value = entry.getValue().toString();
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    alarmId = jObj.getString("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (alarmId.equals(sharedPrefsAlarmId)) {

                                JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                        .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                        .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                        .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                        .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                        .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                json = jsonSettingsStringBuilder.toString();

                                break;
                            }
                        }
                        alarmSettingsMap.put(sharedPrefsAlarmId, json);
                    }
                });

            }
        });

        //        saturday
        holder.saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean temp = holder.saturday.isChecked();
                alarmSettingsLoader.setSaturday(temp);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                        Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                        String value;
                        for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                            value = entry.getValue().toString();
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    alarmId = jObj.getString("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (alarmId.equals(sharedPrefsAlarmId)) {

                                JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                        .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                        .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                        .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                        .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                        .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                json = jsonSettingsStringBuilder.toString();

                                break;
                            }
                        }
                        alarmSettingsMap.put(sharedPrefsAlarmId, json);
                    }
                });

            }
        });

        //        sunday
        holder.sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean temp = holder.sunday.isChecked();
                alarmSettingsLoader.setSunday(temp);
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                        Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                        String value;
                        for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                            value = entry.getValue().toString();
                            try {
                                JSONObject jsonObject = new JSONObject(value);
                                JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                    alarmId = jObj.getString("id");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (alarmId.equals(sharedPrefsAlarmId)) {

                                JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                        .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                        .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                        .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                        .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                        .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                json = jsonSettingsStringBuilder.toString();

                                break;
                            }
                        }
                        alarmSettingsMap.put(sharedPrefsAlarmId, json);
                    }
                });

            }
        });

//        test function!!!!!!!!!
        holder.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PeriodSetterBuilder periodSetterBuilder = new PeriodSetterBuilder();
                periodSetterBuilder.buildNewWeek(context);
            }
        });

        holder.checkPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean temp = holder.checkPeriod.isChecked();
                alarmSettingsLoader.setCheckPeriod(temp);
                if (holder.checkPeriod.isChecked()) {
                    holder.setPeriodView.setTextColor(context.getResources().getColor(R.color.colorPrimaryText));
                    holder.settingsButton.setImageDrawable(context.getDrawable(R.drawable.settings));

                    holder.monday.setChecked(false);
                    holder.tuesday.setChecked(false);
                    holder.wednesday.setChecked(false);
                    holder.thursday.setChecked(false);
                    holder.friday.setChecked(false);
                    holder.saturday.setChecked(false);
                    holder.sunday.setChecked(false);

                    alarmSettingsLoader.setMonday(false);
                    alarmSettingsLoader.setTuesday(false);
                    alarmSettingsLoader.setWednesday(false);
                    alarmSettingsLoader.setThursday(false);
                    alarmSettingsLoader.setFriday(false);
                    alarmSettingsLoader.setSaturday(false);
                    alarmSettingsLoader.setSunday(false);

                    final String[] daysOfPeriod;
                    daysOfPeriod = context.getResources().getStringArray(R.array.daysOfPeriod);
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                            .setTitle("Pick the first day of period")
                            .setSingleChoiceItems(daysOfPeriod, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ListView listView = ((AlertDialog) dialogInterface).getListView();
                                    int item = listView.getCheckedItemPosition();
                                    ArrayList<Boolean> dayList = new ArrayList<>();
                                    PeriodSetter periodSetter;
                                    String buffer = daysOfPeriod[item];
                                    switch (buffer) {
                                        case "MONDAY":
                                            holder.monday.setChecked(true);
                                            alarmSettingsLoader.setMonday(true);
                                            dayList.add(alarmSettingsLoader.isMonday());
                                            dayList.add(alarmSettingsLoader.isTuesday());
                                            dayList.add(alarmSettingsLoader.isWednesday());
                                            dayList.add(alarmSettingsLoader.isThursday());
                                            dayList.add(alarmSettingsLoader.isFriday());
                                            dayList.add(alarmSettingsLoader.isSaturday());
                                            dayList.add(alarmSettingsLoader.isSunday());
                                            periodSetter = new PeriodSetter(dayList, holder.setPeriodView.getText().toString(), context);
                                            dayList = periodSetter.managePeriod();
                                            holder.monday.setChecked(dayList.get(0));
                                            holder.tuesday.setChecked(dayList.get(1));
                                            holder.wednesday.setChecked(dayList.get(2));
                                            holder.thursday.setChecked(dayList.get(3));
                                            holder.friday.setChecked(dayList.get(4));
                                            holder.saturday.setChecked(dayList.get(5));
                                            holder.sunday.setChecked(dayList.get(6));

                                            service.submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                                    Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                                    String value;
                                                    for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                                        value = entry.getValue().toString();
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(value);
                                                            JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jObj = jsonArray.getJSONObject(i);
                                                                alarmId = jObj.getString("id");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (alarmId.equals(sharedPrefsAlarmId)) {

                                                            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                                    .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                                    .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                                    .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                                    .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                                    .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                                            json = jsonSettingsStringBuilder.toString();

                                                            break;
                                                        }
                                                    }
                                                    alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                                }
                                            });
                                            break;
                                        case "TUESDAY":
                                            holder.tuesday.setChecked(true);
                                            alarmSettingsLoader.setTuesday(true);
                                            dayList.add(alarmSettingsLoader.isMonday());
                                            dayList.add(alarmSettingsLoader.isTuesday());
                                            dayList.add(alarmSettingsLoader.isWednesday());
                                            dayList.add(alarmSettingsLoader.isThursday());
                                            dayList.add(alarmSettingsLoader.isFriday());
                                            dayList.add(alarmSettingsLoader.isSaturday());
                                            dayList.add(alarmSettingsLoader.isSunday());
                                            periodSetter = new PeriodSetter(dayList, holder.setPeriodView.getText().toString(), context);
                                            dayList = periodSetter.managePeriod();
                                            holder.monday.setChecked(dayList.get(0));
                                            holder.tuesday.setChecked(dayList.get(1));
                                            holder.wednesday.setChecked(dayList.get(2));
                                            holder.thursday.setChecked(dayList.get(3));
                                            holder.friday.setChecked(dayList.get(4));
                                            holder.saturday.setChecked(dayList.get(5));
                                            holder.sunday.setChecked(dayList.get(6));

                                            service.submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                                    Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                                    String value;
                                                    for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                                        value = entry.getValue().toString();
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(value);
                                                            JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jObj = jsonArray.getJSONObject(i);
                                                                alarmId = jObj.getString("id");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (alarmId.equals(sharedPrefsAlarmId)) {

                                                            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                                    .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                                    .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                                    .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                                    .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                                    .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                                            json = jsonSettingsStringBuilder.toString();

                                                            break;
                                                        }
                                                    }
                                                    alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                                }
                                            });
                                            break;
                                        case "WEDNESDAY":
                                            holder.wednesday.setChecked(true);
                                            alarmSettingsLoader.setWednesday(true);
                                            dayList.add(alarmSettingsLoader.isMonday());
                                            dayList.add(alarmSettingsLoader.isTuesday());
                                            dayList.add(alarmSettingsLoader.isWednesday());
                                            dayList.add(alarmSettingsLoader.isThursday());
                                            dayList.add(alarmSettingsLoader.isFriday());
                                            dayList.add(alarmSettingsLoader.isSaturday());
                                            dayList.add(alarmSettingsLoader.isSunday());
                                            periodSetter = new PeriodSetter(dayList, holder.setPeriodView.getText().toString(), context);
                                            dayList = periodSetter.managePeriod();
                                            holder.monday.setChecked(dayList.get(0));
                                            holder.tuesday.setChecked(dayList.get(1));
                                            holder.wednesday.setChecked(dayList.get(2));
                                            holder.thursday.setChecked(dayList.get(3));
                                            holder.friday.setChecked(dayList.get(4));
                                            holder.saturday.setChecked(dayList.get(5));
                                            holder.sunday.setChecked(dayList.get(6));

                                            service.submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                                    Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                                    String value;
                                                    for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                                        value = entry.getValue().toString();
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(value);
                                                            JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jObj = jsonArray.getJSONObject(i);
                                                                alarmId = jObj.getString("id");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (alarmId.equals(sharedPrefsAlarmId)) {

                                                            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                                    .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                                    .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                                    .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                                    .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                                    .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                                            json = jsonSettingsStringBuilder.toString();

                                                            break;
                                                        }
                                                    }
                                                    alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                                }
                                            });
                                            break;
                                        case "THURSDAY":
                                            holder.thursday.setChecked(true);
                                            alarmSettingsLoader.setThursday(true);
                                            dayList.add(alarmSettingsLoader.isMonday());
                                            dayList.add(alarmSettingsLoader.isTuesday());
                                            dayList.add(alarmSettingsLoader.isWednesday());
                                            dayList.add(alarmSettingsLoader.isThursday());
                                            dayList.add(alarmSettingsLoader.isFriday());
                                            dayList.add(alarmSettingsLoader.isSaturday());
                                            dayList.add(alarmSettingsLoader.isSunday());
                                            periodSetter = new PeriodSetter(dayList, holder.setPeriodView.getText().toString(), context);
                                            dayList = periodSetter.managePeriod();
                                            holder.monday.setChecked(dayList.get(0));
                                            holder.tuesday.setChecked(dayList.get(1));
                                            holder.wednesday.setChecked(dayList.get(2));
                                            holder.thursday.setChecked(dayList.get(3));
                                            holder.friday.setChecked(dayList.get(4));
                                            holder.saturday.setChecked(dayList.get(5));
                                            holder.sunday.setChecked(dayList.get(6));

                                            service.submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                                    Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                                    String value;
                                                    for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                                        value = entry.getValue().toString();
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(value);
                                                            JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jObj = jsonArray.getJSONObject(i);
                                                                alarmId = jObj.getString("id");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (alarmId.equals(sharedPrefsAlarmId)) {

                                                            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                                    .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                                    .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                                    .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                                    .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                                    .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                                            json = jsonSettingsStringBuilder.toString();

                                                            break;
                                                        }
                                                    }
                                                    alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                                }
                                            });
                                            break;
                                        case "FRIDAY":
                                            holder.friday.setChecked(true);
                                            alarmSettingsLoader.setFriday(true);
                                            dayList.add(alarmSettingsLoader.isMonday());
                                            dayList.add(alarmSettingsLoader.isTuesday());
                                            dayList.add(alarmSettingsLoader.isWednesday());
                                            dayList.add(alarmSettingsLoader.isThursday());
                                            dayList.add(alarmSettingsLoader.isFriday());
                                            dayList.add(alarmSettingsLoader.isSaturday());
                                            dayList.add(alarmSettingsLoader.isSunday());
                                            periodSetter = new PeriodSetter(dayList, holder.setPeriodView.getText().toString(), context);
                                            dayList = periodSetter.managePeriod();
                                            holder.monday.setChecked(dayList.get(0));
                                            holder.tuesday.setChecked(dayList.get(1));
                                            holder.wednesday.setChecked(dayList.get(2));
                                            holder.thursday.setChecked(dayList.get(3));
                                            holder.friday.setChecked(dayList.get(4));
                                            holder.saturday.setChecked(dayList.get(5));
                                            holder.sunday.setChecked(dayList.get(6));

                                            service.submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                                    Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                                    String value;
                                                    for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                                        value = entry.getValue().toString();
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(value);
                                                            JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jObj = jsonArray.getJSONObject(i);
                                                                alarmId = jObj.getString("id");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (alarmId.equals(sharedPrefsAlarmId)) {

                                                            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                                    .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                                    .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                                    .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                                    .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                                    .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                                            json = jsonSettingsStringBuilder.toString();

                                                            break;
                                                        }
                                                    }
                                                    alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                                }
                                            });
                                            break;
                                        case "SATURDAY":
                                            holder.saturday.setChecked(true);
                                            alarmSettingsLoader.setSaturday(true);
                                            dayList.add(alarmSettingsLoader.isMonday());
                                            dayList.add(alarmSettingsLoader.isTuesday());
                                            dayList.add(alarmSettingsLoader.isWednesday());
                                            dayList.add(alarmSettingsLoader.isThursday());
                                            dayList.add(alarmSettingsLoader.isFriday());
                                            dayList.add(alarmSettingsLoader.isSaturday());
                                            dayList.add(alarmSettingsLoader.isSunday());
                                            periodSetter = new PeriodSetter(dayList, holder.setPeriodView.getText().toString(), context);
                                            dayList = periodSetter.managePeriod();
                                            holder.monday.setChecked(dayList.get(0));
                                            holder.tuesday.setChecked(dayList.get(1));
                                            holder.wednesday.setChecked(dayList.get(2));
                                            holder.thursday.setChecked(dayList.get(3));
                                            holder.friday.setChecked(dayList.get(4));
                                            holder.saturday.setChecked(dayList.get(5));
                                            holder.sunday.setChecked(dayList.get(6));

                                            service.submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                                    Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                                    String value;
                                                    for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                                        value = entry.getValue().toString();
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(value);
                                                            JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jObj = jsonArray.getJSONObject(i);
                                                                alarmId = jObj.getString("id");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (alarmId.equals(sharedPrefsAlarmId)) {

                                                            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                                    .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                                    .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                                    .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                                    .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                                    .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                                            json = jsonSettingsStringBuilder.toString();

                                                            break;
                                                        }
                                                    }
                                                    alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                                }
                                            });
                                            break;
                                        case "SUNDAY":
                                            holder.sunday.setChecked(true);
                                            alarmSettingsLoader.setSunday(true);
                                            dayList.add(alarmSettingsLoader.isMonday());
                                            dayList.add(alarmSettingsLoader.isTuesday());
                                            dayList.add(alarmSettingsLoader.isWednesday());
                                            dayList.add(alarmSettingsLoader.isThursday());
                                            dayList.add(alarmSettingsLoader.isFriday());
                                            dayList.add(alarmSettingsLoader.isSaturday());
                                            dayList.add(alarmSettingsLoader.isSunday());
                                            periodSetter = new PeriodSetter(dayList, holder.setPeriodView.getText().toString(), context);
                                            dayList = periodSetter.managePeriod();
                                            holder.monday.setChecked(dayList.get(0));
                                            holder.tuesday.setChecked(dayList.get(1));
                                            holder.wednesday.setChecked(dayList.get(2));
                                            holder.thursday.setChecked(dayList.get(3));
                                            holder.friday.setChecked(dayList.get(4));
                                            holder.saturday.setChecked(dayList.get(5));
                                            holder.sunday.setChecked(dayList.get(6));

                                            service.submit(new Runnable() {
                                                @Override
                                                public void run() {
                                                    String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                                    Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                                    String value;
                                                    for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                                        value = entry.getValue().toString();
                                                        try {
                                                            JSONObject jsonObject = new JSONObject(value);
                                                            JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                                JSONObject jObj = jsonArray.getJSONObject(i);
                                                                alarmId = jObj.getString("id");
                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                        if (alarmId.equals(sharedPrefsAlarmId)) {

                                                            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                                    .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                                    .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                                    .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                                    .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                                    .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                                            json = jsonSettingsStringBuilder.toString();

                                                            break;
                                                        }
                                                    }
                                                    alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                                }
                                            });
                                            break;
                                    }
                                }
                            });
                    alertDialog.create().show();

                } else {
                    holder.setPeriodView.setTextColor(context.getResources().getColor(R.color.color_not_active_text));
                    holder.settingsButton.setImageDrawable(context.getDrawable(R.drawable.settings_not_active));

                    holder.monday.setChecked(false);
                    holder.tuesday.setChecked(false);
                    holder.wednesday.setChecked(false);
                    holder.thursday.setChecked(false);
                    holder.friday.setChecked(false);
                    holder.saturday.setChecked(false);
                    holder.sunday.setChecked(false);

                    alarmSettingsLoader.setMonday(false);
                    alarmSettingsLoader.setTuesday(false);
                    alarmSettingsLoader.setWednesday(false);
                    alarmSettingsLoader.setThursday(false);
                    alarmSettingsLoader.setFriday(false);
                    alarmSettingsLoader.setSaturday(false);
                    alarmSettingsLoader.setSunday(false);

                    switch (witchDay()) {
                        case "Mon":
                            holder.monday.setChecked(true);
                            alarmSettingsLoader.setMonday(true);
                            break;
                        case "Tue":
                            holder.tuesday.setChecked(true);
                            alarmSettingsLoader.setTuesday(true);
                            break;
                        case "Wed":
                            holder.wednesday.setChecked(true);
                            alarmSettingsLoader.setWednesday(true);
                            break;
                        case "Thu":
                            holder.thursday.setChecked(true);
                            alarmSettingsLoader.setThursday(true);
                            break;
                        case "Fri":
                            holder.friday.setChecked(true);
                            alarmSettingsLoader.setFriday(true);
                            break;
                        case "Sat":
                            holder.saturday.setChecked(true);
                            alarmSettingsLoader.setSaturday(true);
                            break;
                        case "Sun":
                            holder.sunday.setChecked(true);
                            alarmSettingsLoader.setSunday(true);
                            break;
                    }

                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                            Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                            String value;
                            for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                value = entry.getValue().toString();
                                try {
                                    JSONObject jsonObject = new JSONObject(value);
                                    JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jObj = jsonArray.getJSONObject(i);
                                        alarmId = jObj.getString("id");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (alarmId.equals(sharedPrefsAlarmId)) {

                                    JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                            .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                            .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                            .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                            .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                            .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                    json = jsonSettingsStringBuilder.toString();

                                    break;
                                }
                            }
                            alarmSettingsMap.put(sharedPrefsAlarmId, json);
                        }
                    });

                }
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                секция полностью рабочая!!!
                String alarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                String id = "";
                SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                String key;
                String value;
                for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                    key = entry.getKey();
                    value = entry.getValue().toString();
                    try {
                        JSONObject jsonObject = new JSONObject(value);
                        JSONArray jsonArray = jsonObject.getJSONArray("settings");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jObj = jsonArray.getJSONObject(i);
                            id = jObj.getString("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (alarmId.equals(id)) {
                        editor.remove(key);
                        editor.apply();
                    }
                }
                data.remove(holder.getAdapterPosition());
                alarmSettingsMap.remove(alarmId);
                notifyDataSetChanged();
                Snackbar.make(view, "Alarm has been deleted!", Snackbar.LENGTH_SHORT).show();
            }
        });

        holder.timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                callbackDialog = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        mHour = hourOfDay;
                        mMinute = minute;

                        if (mMinute < 10) {
                            holder.timeTextView.setText(mHour + ":0" + mMinute);
                            alarmSettingsLoader.setTimeTextView(mHour + ":0" + mMinute);
                            Snackbar.make(v, "Alarm set for " + "..." + " from now", Snackbar.LENGTH_SHORT).show();
                            service.submit(new Runnable() {
                                @Override
                                public void run() {
                                    String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                    Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                    String value;
                                    for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                        value = entry.getValue().toString();
                                        try {
                                            JSONObject jsonObject = new JSONObject(value);
                                            JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jObj = jsonArray.getJSONObject(i);
                                                alarmId = jObj.getString("id");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (alarmId.equals(sharedPrefsAlarmId)) {

                                            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                    .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                    .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                    .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                    .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                    .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                            json = jsonSettingsStringBuilder.toString();

                                            break;
                                        }
                                    }
                                    alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                }
                            });
                        } else {
                            holder.timeTextView.setText(mHour + ":" + mMinute);
                            alarmSettingsLoader.setTimeTextView(mHour + ":" + mMinute);
                            Snackbar.make(view, "Alarm set for " + "..." + " from now", Snackbar.LENGTH_SHORT).show();

                            service.submit(new Runnable() {
                                @Override
                                public void run() {
                                    String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                    Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                    String value;
                                    for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                        value = entry.getValue().toString();
                                        try {
                                            JSONObject jsonObject = new JSONObject(value);
                                            JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                JSONObject jObj = jsonArray.getJSONObject(i);
                                                alarmId = jObj.getString("id");
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        if (alarmId.equals(sharedPrefsAlarmId)) {

                                            JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                    .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                    .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                    .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                    .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                    .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                            json = jsonSettingsStringBuilder.toString();

                                            break;
                                        }
                                    }
                                    alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                }
                            });
                        }

                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(context, callbackDialog, mHour, mMinute, true);
                timePickerDialog.show();
            }

        });

        holder.setPeriodView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!(holder.checkPeriod.isChecked())) return;

                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                view = ((Activity) context).getLayoutInflater().inflate(R.layout.period_picker_dialog, null);
                final View finalView = view;
                builder.setTitle("Set period between alarms")
                        .setView(view)
                        .setCancelable(true)
                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NumberPicker numberPicker1 = (NumberPicker) finalView.findViewById(R.id.numberPicker);
                                int picker1 = numberPicker1.getValue();
                                NumberPicker numberPicker2 = (NumberPicker) finalView.findViewById(R.id.numberPicker2);
                                int picker2 = numberPicker2.getValue();
                                holder.setPeriodView.setText(picker1 + "/" + picker2);
                                alarmSettingsLoader.setSetPeriodView(picker1 + "/" + picker2);

                                service.submit(new Runnable() {
                                    @Override
                                    public void run() {
                                        ArrayList<Boolean> dayList = new ArrayList<>();
                                        dayList.add(alarmSettingsLoader.isMonday());
                                        dayList.add(alarmSettingsLoader.isTuesday());
                                        dayList.add(alarmSettingsLoader.isWednesday());
                                        dayList.add(alarmSettingsLoader.isThursday());
                                        dayList.add(alarmSettingsLoader.isFriday());
                                        dayList.add(alarmSettingsLoader.isSaturday());
                                        dayList.add(alarmSettingsLoader.isSunday());

                                        PeriodSetter periodSetter = new PeriodSetter(dayList, holder.setPeriodView.getText().toString(), context); //вроде все хоккей :)
                                        dayList = periodSetter.managePeriod();
                                        holder.monday.setChecked(dayList.get(0));
                                        holder.tuesday.setChecked(dayList.get(1));
                                        holder.wednesday.setChecked(dayList.get(2));
                                        holder.thursday.setChecked(dayList.get(3));
                                        holder.friday.setChecked(dayList.get(4));
                                        holder.saturday.setChecked(dayList.get(5));
                                        holder.sunday.setChecked(dayList.get(6));

                                        String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                        Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                        String value;
                                        for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                            value = entry.getValue().toString();
                                            try {
                                                JSONObject jsonObject = new JSONObject(value);
                                                JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    JSONObject jObj = jsonArray.getJSONObject(i);
                                                    alarmId = jObj.getString("id");
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                            if (alarmId.equals(sharedPrefsAlarmId)) {

                                                JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                        .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                        .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                        .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                        .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                        .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                                json = jsonSettingsStringBuilder.toString();
                                                break;
                                            }
                                        }
                                        alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                    }
                                });

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();

                NumberPicker numberPicker1 = (NumberPicker) finalView.findViewById(R.id.numberPicker);
                numberPicker1.setMaxValue(7);
                numberPicker1.setMinValue(1);
                numberPicker1.setWrapSelectorWheel(true);

                NumberPicker numberPicker2 = (NumberPicker) finalView.findViewById(R.id.numberPicker2);
                numberPicker2.setMaxValue(7);
                numberPicker2.setMinValue(1);
                numberPicker2.setWrapSelectorWheel(true);

                builder.show();

            }
        });

        holder.setRingtoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(context, "Storage permission isn't granted", Toast.LENGTH_SHORT).show();
                }
                SoundPickerDialog fileDialog = new SoundPickerDialog(context)
                        .setFilter(".*\\.mp3")
                        .setOpenDialogListener(new SoundPickerDialog.OpenDialogListener() {
                            @Override
                            public void OnSelectedFile(String fileName) {
                                try {
                                    AudioFile file = AudioFileIO.read(new File(fileName));
                                    Tag tag = file.getTag();
                                    String t = tag.getFirst(FieldKey.ARTIST);
                                    t = t + " - " + tag.getFirst(FieldKey.TITLE);
                                    holder.setRingtoneView.setText(t);
                                    alarmSettingsLoader.setSetRingtoneView(t);
                                    alarmSettingsLoader.setSoundPath(fileName);

                                    service.submit(new Runnable() {
                                        @Override
                                        public void run() {
                                            String sharedPrefsAlarmId = data.get(holder.getAdapterPosition()).getAlarmId();
                                            Map<String, ?> stringMap = JsonParser.Companion.getSharedPrefs(context);
                                            String value;
                                            for (Map.Entry<String, ?> entry : stringMap.entrySet()) {
                                                value = entry.getValue().toString();
                                                try {
                                                    JSONObject jsonObject = new JSONObject(value);
                                                    JSONArray jsonArray = jsonObject.getJSONArray("settings");
                                                    for (int i = 0; i < jsonArray.length(); i++) {
                                                        JSONObject jObj = jsonArray.getJSONObject(i);
                                                        alarmId = jObj.getString("id");
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                if (alarmId.equals(sharedPrefsAlarmId)) {

                                                    JsonSettingsStringBuilder jsonSettingsStringBuilder = new JsonSettingsStringBuilder.Builder().setTime(holder.timeTextView.getText())
                                                            .setPeriod(holder.setPeriodView.getText()).setRingtone(holder.setRingtoneView.getText()).setOnOfSwitch(holder.onOfSwitch.isChecked())
                                                            .setMonday(holder.monday.isChecked()).setTuesday(holder.tuesday.isChecked()).setWednesday(holder.wednesday.isChecked())
                                                            .setThursday(holder.thursday.isChecked()).setFriday(holder.friday.isChecked()).setSaturday(holder.saturday.isChecked())
                                                            .setSunday(holder.sunday.isChecked()).setCheckPeriod(holder.checkPeriod.isChecked()).setAlarmId(alarmId)
                                                            .setSoundPath(data.get(holder.getAdapterPosition()).getSoundPath()).build();
                                                    json = jsonSettingsStringBuilder.toString();

                                                    break;
                                                }
                                            }
                                            alarmSettingsMap.put(sharedPrefsAlarmId, json);
                                        }
                                    });

                                } catch (CannotReadException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (TagException e) {
                                    e.printStackTrace();
                                } catch (ReadOnlyFileException e) {
                                    e.printStackTrace();
                                } catch (InvalidAudioFrameException e) {
                                    e.printStackTrace();
                                }
                                Snackbar.make(view, fileName, Snackbar.LENGTH_SHORT).show();
                            }
                        });


                fileDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private String witchDay() {
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE", Locale.getDefault());
        return sdf.format(date);
    }

}
