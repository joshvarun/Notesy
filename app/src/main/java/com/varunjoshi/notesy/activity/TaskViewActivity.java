package com.varunjoshi.notesy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Adapters.NotesAdapter;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.dao.NoteDao;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskViewActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.floatingActionButton)
    FloatingActionButton btn_add_new;
    @BindView(R.id.no_notes_img)
    ImageView img_no_notes;
    @BindView(R.id.no_notes_text)
    TextView txt_no_notes;
    AppDatabase mAppDatabase;
    NotesAdapter mAdapter;
    NoteDao mNoteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        ButterKnife.bind(this);
        mAppDatabase = AppDatabase.getAppDatabase(this);
        mAdapter = new NotesAdapter(new ArrayList<Note>(), this);

        getAllData();
    }

    @OnClick(R.id.floatingActionButton)
    public void goToAddNewNote() {
        startActivity(new Intent(TaskViewActivity.this, NewNoteActivity.class));
    }

    public void getAllData() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mNoteDao = mAppDatabase.mNoteDao();
        mNoteDao.getAll().observe(this, notes -> {
            if (notes != null && notes.size() > 0) {
                img_no_notes.setVisibility(View.GONE);
                txt_no_notes.setVisibility(View.GONE);
                Collections.reverse(notes);
                mAdapter.setItems(notes);
            } else {
                img_no_notes.setVisibility(View.VISIBLE);
                txt_no_notes.setVisibility(View.VISIBLE);
            }
        });
    }
}
