package com.example.marco.hw2;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongListFragment extends ListFragment {


    public SongListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<Songs>(getActivity(),android.R.layout.simple_list_item_1,android.R.id.text1,MainActivity.myTasks));
    }



}
