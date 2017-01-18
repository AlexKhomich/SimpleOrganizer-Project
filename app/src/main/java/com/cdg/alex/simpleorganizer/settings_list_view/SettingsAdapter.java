package com.cdg.alex.simpleorganizer.settings_list_view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.cdg.alex.simpleorganizer.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by alex on 01/11/16.
 */

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsHolder> {

    private final int ITEM_SILENCE_AFTER = 0;
    private final int ITEM_SNOOZE_TIME = 1;
    private final int ITEM_VIBRATE_WITH_SOUND = 2;
    private final int ITEM_VOLUME_CONTROL = 3;
    private SettingsLoader loader;
    private Context context;
    private static Map<String, String> cacheSettings;

    public static Map<String, String> getCacheSettings() {
        return cacheSettings;
    }

    public static class SettingsHolder extends RecyclerView.ViewHolder {

        TextView silenceAfter;
        TextView timeOfSilenceAfter;
        TextView snoozeTime;
        TextView timeOfSnooze;
        TextView vibrateWithSound;
        Switch vibrateSwitch;
        ImageView volumeImage;
        SeekBar volumeBar;

        public SettingsHolder(View itemView) {
            super(itemView);
            silenceAfter = (TextView) itemView.findViewById(R.id.silenceAfter);
            timeOfSilenceAfter = (TextView) itemView.findViewById(R.id.timeOfSilenceAfter);
            snoozeTime = (TextView) itemView.findViewById(R.id.snoozeTime);
            timeOfSnooze = (TextView) itemView.findViewById(R.id.timeOfSnooze);
            vibrateWithSound = (TextView) itemView.findViewById(R.id.vibrateWithSound);
            vibrateSwitch = (Switch) itemView.findViewById(R.id.vibrateSwitch);
            volumeImage = (ImageView) itemView.findViewById(R.id.volumeImage);
            volumeBar = (SeekBar) itemView.findViewById(R.id.volumeBar);

        }
    }

    public SettingsAdapter(Context context, SettingsLoader loader) {
        this.loader = loader;
        this.context = context;
    }

    @Override
    public SettingsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = null;

        switch (viewType) {
            case ITEM_SILENCE_AFTER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.silence_after_layout, parent, false);
                break;
            case ITEM_SNOOZE_TIME:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.snooze_time_layout, parent, false);
                break;
            case ITEM_VIBRATE_WITH_SOUND:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vibrate_with_sound_layout, parent, false);
                break;
            case ITEM_VOLUME_CONTROL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.volume_control_layout, parent, false);

        }
        return new SettingsHolder(view);
    }

    @Override
    public void onBindViewHolder(final SettingsHolder holder, int position) {

        final int itemType = getItemViewType(position);
        cacheSettings = new HashMap<>();

        switch (itemType) {
            case ITEM_SILENCE_AFTER:
                holder.timeOfSilenceAfter.setText(loader.getTimeOfSilenceAfter());
                holder.timeOfSilenceAfter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String [] silenceAfterArr;
                        silenceAfterArr = context.getResources().getStringArray(R.array.silenceAfterItems);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                                .setTitle("Silence After")
                                .setSingleChoiceItems(silenceAfterArr, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ListView listView = ((AlertDialog) dialogInterface).getListView();
                                        int item = listView.getCheckedItemPosition();
                                        String buffer = silenceAfterArr[item];
                                        loader.setTimeOfSnooze(buffer);
                                        holder.timeOfSilenceAfter.setText(buffer);
                                        cacheSettings.put("silence_after",buffer);
                                    }
                                });
                        alertDialog.create().show();

                    }
                });
                break;

            case ITEM_SNOOZE_TIME:
                holder.timeOfSnooze.setText(loader.getTimeOfSnooze());
                holder.timeOfSnooze.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final String [] snoozeTimeArr;
                        snoozeTimeArr = context.getResources().getStringArray(R.array.snoozeTimeItems);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                                .setTitle("Snooze Time")
                                .setSingleChoiceItems(snoozeTimeArr, 0, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                })
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ListView listView = ((AlertDialog) dialogInterface).getListView();
                                        int item = listView.getCheckedItemPosition();
                                        String buffer = snoozeTimeArr[item];
                                        loader.setTimeOfSnooze(buffer);
                                        holder.timeOfSnooze.setText(buffer);
                                        cacheSettings.put("time_of_snooze", buffer);
                                    }
                                });
                        alertDialog.create().show();

                    }
                });

                break;

            case ITEM_VIBRATE_WITH_SOUND:
                holder.vibrateSwitch.setChecked(loader.isVibrateWithSound());
                holder.vibrateSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        loader.setVibrateWithSound(holder.vibrateSwitch.isChecked());
                        cacheSettings.put("vibrate", loader.toStringVibrate());
                    }
                });

                break;

            case ITEM_VOLUME_CONTROL:
                holder.volumeBar.setProgress(loader.getVolumeValue());
                holder.volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        loader.setVolumeValue(holder.volumeBar.getProgress());
                        cacheSettings.put("volume", loader.toStringVolume());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
        }

    }

    @Override
    public int getItemViewType(int position) {

        int itemType = 0;

        if (position == 0)
           itemType =  ITEM_SILENCE_AFTER;

         else if(position == 1)
            itemType = ITEM_SNOOZE_TIME;

         else if(position == 2)
            itemType = ITEM_VIBRATE_WITH_SOUND;

         else if(position == 3)
            itemType = ITEM_VOLUME_CONTROL;

        return itemType;
    }

    @Override
    public int getItemCount() {
        return 4;
    }



}
