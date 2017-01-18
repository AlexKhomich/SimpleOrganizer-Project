package com.cdg.alex.simpleorganizer.settings_list_view;

/**
 * Created by alex on 01/11/16.
 */

public class SettingsLoader {

    private String timeOfSilenceAfter;
    private String timeOfSnooze;
    private boolean vibrateWithSound;
    private int volumeValue;

//    default constructor
    public SettingsLoader() {

    }

    public SettingsLoader(String timeOfSilenceAfter, String timeOfSnooze, boolean vibrateWithSound, int volumeValue) {
        this.timeOfSilenceAfter = timeOfSilenceAfter;
        this.timeOfSnooze = timeOfSnooze;
        this.vibrateWithSound = vibrateWithSound;
        this.volumeValue = volumeValue;
    }



    public String getTimeOfSilenceAfter() {
        return timeOfSilenceAfter;
    }

    public void setTimeOfSilenceAfter(String timeOfSilenceAfter) {
        this.timeOfSilenceAfter = timeOfSilenceAfter;
    }

    public String getTimeOfSnooze() {
        return timeOfSnooze;
    }

    public void setTimeOfSnooze(String timeOfSnooze) {
        this.timeOfSnooze = timeOfSnooze;
    }

    public boolean isVibrateWithSound() {
        return vibrateWithSound;
    }

    public void setVibrateWithSound(boolean vibrateWithSound) {
        this.vibrateWithSound = vibrateWithSound;
    }

    public int getVolumeValue() {
        return volumeValue;
    }

    public void setVolumeValue(int volumeValue) {
        this.volumeValue = volumeValue;
    }

    public String toStringVibrate() {
        return "" + vibrateWithSound;

    }

    public String toStringVolume() {
        return "" + volumeValue;
    }
}
