package com.varunjoshi.notesy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.varunjoshi.notesy.R;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_view);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.floatingActionButton)
    public void goToAddNewNote(){
        startActivity(new Intent(TaskViewActivity.this, NewNoteActivity.class));
    }
}
