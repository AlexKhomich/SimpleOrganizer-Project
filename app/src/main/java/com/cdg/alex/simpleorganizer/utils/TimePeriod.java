package com.cdg.alex.simpleorganizer.utils;

import com.cdg.alex.simpleorganizer.alarm_list_view.AlarmSettingsLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

/**
 * Created by alex on 26/10/16.
 */

public class TimePeriod {
    private static TimePeriod ourInstance = new TimePeriod();

    public static TimePeriod getInstance() {
        return ourInstance;
    }

    private AlarmSettingsLoader alarmSettingsLoader = new AlarmSettingsLoader();
    private static final int MON = 0;
    private static final int TUE = 1;
    private static final int WED = 2;
    private static final int THU = 3;
    private static final int FRI = 4;
    private static final int SAT = 5;
    private static final int SUN = 6;
    private static int currentDay;

    private long time = System.currentTimeMillis();
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:dd:MMM:yyyy", Locale.getDefault());
    private String currTime = simpleDateFormat.format(time);
    private String[] fullTimeArray = currTime.split(":");
    private int hour = Integer.parseInt(fullTimeArray[0]);
    private int minute = Integer.parseInt(fullTimeArray[1]);
    private String date = fullTimeArray[2];

    private TimePeriod() {
    }

    public String getPeriodTime() {
        nextDate(getNextDay());
        return ":)";
    }

    private int nextDate(int nextDay) {
        Calendar calendar = new GregorianCalendar(Locale.getDefault());
        Date trialDate = new Date();
        calendar.setTime(trialDate);
        calendar.add(Calendar.DAY_OF_MONTH, nextDay);

        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    private int getNextDay() {

        Boolean[] days = {alarmSettingsLoader.isMonday(),
                alarmSettingsLoader.isTuesday(),
                alarmSettingsLoader.isWednesday(),
                alarmSettingsLoader.isThursday(),
                alarmSettingsLoader.isFriday(),
                alarmSettingsLoader.isSaturday(),
                alarmSettingsLoader.isSunday()};

        List<Boolean> nextDay = new ArrayList<>();
        Collections.addAll(nextDay, days);
        ListIterator<Boolean> listIterator = nextDay.listIterator();
        long time = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:EEE", Locale.getDefault());
        String cTime = simpleDateFormat.format(time);
        String[] fullTimeArray = cTime.split(":");
        String date = fullTimeArray[2];

        switch (date) {
            case "Mon":
                currentDay = MON;
                break;
            case "Tue":
                currentDay = TUE;
                break;
            case "Wed":
                currentDay = WED;
                break;
            case "Thu":
                currentDay = THU;
                break;
            case "Fri":
                currentDay = FRI;
                break;
            case "Sat":
                currentDay = SAT;
                break;
            case "Sun":
                currentDay = SUN;
        }

        int i;
        boolean temp = false;
        if (currentDay == SUN) {
            for (i = 0; listIterator.hasNext(); i++) {
                if (nextDay.get(i)) {
                    break;
                } else listIterator.next();
            }
        } else {
            for (i = currentDay + 1; listIterator.hasNext(); i++) {
                if (nextDay.get(i)) {
                    temp = true;
                    break;
                } else listIterator.next();
            }

            if (!temp) {
                for (i = 0; listIterator.hasNext(); i++) {
                    if (nextDay.get(i)) {
                        break;
                    } else listIterator.next();
                }
            }
        }
        return i;
    }
}
