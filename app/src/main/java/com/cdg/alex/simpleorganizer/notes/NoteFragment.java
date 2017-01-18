package com.cdg.alex.simpleorganizer.notes;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cdg.alex.simpleorganizer.R;

/**
 * Created by alex on 26/10/16.
 */

public class NoteFragment extends Fragment {

    public static NoteFragment newInstance(int num) {
        NoteFragment noteFragment = new NoteFragment();
        Bundle args = new Bundle();
        noteFragment.setArguments(args);
        return noteFragment;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_note, container, false);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fabAddNotes);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Not realised function yet :)", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // Inflate the layout for this fragment
        return v;
    }
}
