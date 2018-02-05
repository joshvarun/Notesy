package com.varunjoshi.notesy.activity.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.varunjoshi.notesy.R;

import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowPendingNotes extends Fragment {


    public ShowPendingNotes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_pending_notes, container, false);
    }

    public void getAllData() {
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(mAdapter);
//        mNoteDao = mAppDatabase.mNoteDao();
//        mNoteDao.getAll().observe(this, notes -> {
//            if (notes != null && notes.size() > 0) {
//                img_no_notes.setVisibility(View.GONE);
//                txt_no_notes.setVisibility(View.GONE);
//                Collections.reverse(notes);
//                mAdapter.setItems(notes);
//            } else {
//                img_no_notes.setVisibility(View.VISIBLE);
//                txt_no_notes.setVisibility(View.VISIBLE);
//            }
//        });
    }
}
