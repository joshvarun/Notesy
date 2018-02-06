package com.varunjoshi.notesy.activity.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Adapters.NotesAdapter;
import com.varunjoshi.notesy.activity.AppDatabase;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.NewNoteActivity;
import com.varunjoshi.notesy.activity.dao.NoteDao;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowPendingNotes extends Fragment {


    @BindView(R.id.no_notes_img)
    ImageView mNoNotesImg;
    @BindView(R.id.no_notes_text)
    TextView mNoNotesText;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton mFloatingActionButton;
    Unbinder unbinder;
    NotesAdapter mAdapter;
    AppDatabase mAppDatabase;
    NoteDao mNoteDao;

    public ShowPendingNotes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_pending_notes, container, false);
        unbinder = ButterKnife.bind(this, view);
        getAllData();
        return view;
    }

    public void getAllData() {
        mAdapter = new NotesAdapter(new ArrayList<>(),false ,getActivity());
        mAppDatabase = AppDatabase.getAppDatabase(getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mNoteDao = mAppDatabase.mNoteDao();
        mNoteDao.getAll().observe(this, notes -> {
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

    @OnClick(R.id.floatingActionButton)
    public void onViewClicked() {
        getActivity().startActivity(new Intent(getActivity(), NewNoteActivity.class));
    }
}
