package com.cdg.alex.simpleorganizer.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.cdg.alex.simpleorganizer.fragments.AlarmFragment;
import com.cdg.alex.simpleorganizer.service.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AlarmParser extends JsonParser {
    private String result, time, ringtone, period, id, soundPath;
    private boolean switchState, isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSunday, checkPeriod;


    @Override
    public String getTime (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                time = jObj.getString("timeTextView");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return time;
    }

    @Override
    public String getRingtone (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                ringtone = jObj.getString("setRingtoneView");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ringtone;
    }

    @Override
    public String getPeriod (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                period = jObj.getString("setPeriodView");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return period;
    }

    @Override
    public boolean getSwitchState (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                switchState = jObj.getBoolean("onOfSwitch");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return switchState;
    }

    @Override
    public boolean mondayState (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                isMonday = jObj.getBoolean("monday");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isMonday;
    }

    @Override
    public boolean tuesdayState (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                isTuesday = jObj.getBoolean("tuesday");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isTuesday;
    }

    @Override
    public boolean wednesdayState (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                isWednesday = jObj.getBoolean("wednesday");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isWednesday;
    }

    @Override
    public boolean thursdayState (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                isThursday = jObj.getBoolean("thursday");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isThursday;
    }

    @Override
    public boolean fridayState (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                isFriday = jObj.getBoolean("friday");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isFriday;
    }

    @Override
    public boolean saturdayState (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                isSaturday = jObj.getBoolean("saturday");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isSaturday;
    }

    @Override
    public boolean sundayState (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                isSunday = jObj.getBoolean("sunday");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return isSunday;
    }

    @Override
    public String getAlarmId (@NonNull Context context) {

        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                id = jObj.getString("id");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public String getSoundPath (@NonNull Context context) {
        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                soundPath = jObj.getString("soundPath");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return soundPath;
    }

    @Override
    public boolean checkPeriod(@NonNull Context context) {
        result = JsonParser.Companion.getAllAlarms(context).get(AlarmFragment.getElementOfAlarm());

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONArray jsonArray = jsonObject.getJSONArray("settings");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObj = jsonArray.getJSONObject(i);
                checkPeriod = jObj.getBoolean("checkPeriod");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return checkPeriod;
    }
}
