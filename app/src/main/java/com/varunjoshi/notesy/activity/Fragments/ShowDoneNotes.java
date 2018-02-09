package com.varunjoshi.notesy.activity.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Adapters.NotesAdapter;
import com.varunjoshi.notesy.activity.AppDatabase;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.dao.NoteDao;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowDoneNotes extends Fragment {


    @BindView(R.id.no_notes_img)
    ImageView mNoNotesImg;
    @BindView(R.id.no_notes_text)
    TextView mNoNotesText;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    AppDatabase mAppDatabase;
    NoteDao mNoteDao;
    NotesAdapter mAdapter;

    @BindView(R.id.adView)
    AdView mAdView;

    public ShowDoneNotes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_done_notes, container, false);
        unbinder = ButterKnife.bind(this, view);
        getDoneNotes();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }

    public void getDoneNotes() {
        mAdapter = new NotesAdapter(new ArrayList<>(), true, mRecyclerView, getActivity());
        mAppDatabase = AppDatabase.getAppDatabase(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mNoteDao = mAppDatabase.mNoteDao();
        mNoteDao.getCompletedNotes().observe(this, notes -> {
            if (notes != null && notes.size() > 0) {
                mNoNotesImg.setVisibility(View.GONE);
                mNoNotesText.setVisibility(View.GONE);
                Collections.reverse(notes);
                mAdapter.setItems(notes);
            } else {
                mNoNotesImg.setVisibility(View.VISIBLE);
                mNoNotesText.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
