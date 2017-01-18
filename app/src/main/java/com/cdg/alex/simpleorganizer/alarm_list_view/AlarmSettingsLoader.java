package com.cdg.alex.simpleorganizer.alarm_list_view;

/**
 * Created by alex on 26/10/16.
 */

public class AlarmSettingsLoader {

    private String timeTextView;
    private String setRingtoneView;
    private String setPeriodView;
    private String id;
    private String soundPath;
    private boolean onOfSwitch, monday, tuesday, wednesday, thursday, friday, saturday, sunday, checkPeriod;

    public AlarmSettingsLoader() {
    }



    public AlarmSettingsLoader(String timeTextView, boolean onOfSwitch, boolean monday, boolean tuesday, boolean wednesday,
                               boolean thursday, boolean friday, boolean saturday, boolean sunday, boolean checkPeriod,
                               String setRingtoneView, String setPeriodView, String id, String soundPath) {

        this.timeTextView = timeTextView;
        this.onOfSwitch = onOfSwitch;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.checkPeriod = checkPeriod;
        this.setRingtoneView = setRingtoneView;
        this.setPeriodView = setPeriodView;
        this.id = id;
        this.soundPath = soundPath;


    }

    public void setSetRingtoneView(String setRingtoneView) {
        this.setRingtoneView = setRingtoneView;
    }

    public void setSetPeriodView(String setPeriodView) {
        this.setPeriodView = setPeriodView;
    }

    public void setOnOfSwitch(boolean onOfSwitch) {
        this.onOfSwitch = onOfSwitch;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public void setTimeTextView(String timeTextView) {
        this.timeTextView = timeTextView;
    }

    public void setAlarmId(String id) {
        this.id = id;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    public void setCheckPeriod(boolean checkPeriod) {
        this.checkPeriod = checkPeriod;
    }

    public boolean isCheckPeriod() {
        return checkPeriod;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public String getAlarmId() {
        return id;
    }

    public String getTimeTextView() {
        return timeTextView;
    }

    public String getSetRingtoneView() {
        return setRingtoneView;
    }

    public String getSetPeriodView() {
        return setPeriodView;
    }

    public boolean isOnOfSwitch() {
        return onOfSwitch;
    }

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

}
