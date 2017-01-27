package com.cdg.alex.simpleorganizer.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import com.cdg.alex.simpleorganizer.R;

import java.text.SimpleDateFormat;
import java.util.Locale;


public class DateAndTimeFragment extends Fragment {

    public static DateAndTimeFragment newInstance(int num) {
        DateAndTimeFragment dateAndTimeFragment = new DateAndTimeFragment();

            Bundle args = new Bundle();
            dateAndTimeFragment.setArguments(args);

        return dateAndTimeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.date_time_fragment, container, false);
        TextView textView = (TextView) v.findViewById(R.id.textDate);
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
        String dateString = sdf.format(date);
        textView.setText(dateString);
        CalendarView calendarView = (CalendarView) v.findViewById(R.id.calendarView);
        calendarView.getDate();

        return v;

    }

}
