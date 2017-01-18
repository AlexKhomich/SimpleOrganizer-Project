package com.cdg.alex.simpleorganizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.cdg.alex.simpleorganizer.alarm_list_view.AlarmsAdapter;
import com.cdg.alex.simpleorganizer.notes.NoteFragment;

import java.util.Map;

public class OrganizerActivity extends AppCompatActivity {


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_organizer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
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
        super.onStop();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment myFragment = null;

            if (position == 0) {
                myFragment = DateAndTimeFragment.newInstance(position);

            } else if (position == 1) {
                myFragment = AlarmFragment.newInstance(position);

            } else if (position == 2) {
                myFragment = NoteFragment.newInstance(position);

            }
            return myFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.dateAndTimeTab);
                case 1:
                    return getString(R.string.alarmsTab);
                case 2:
                    return getString(R.string.notesTab);
            }
            return null;
        }
    }
}
