package com.varunjoshi.notesy.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.varunjoshi.notesy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewNoteActivity extends AppCompatActivity {

    @BindView(R.id.note_bg)
    ImageView mNoteBg;
    @BindView(R.id.edt_noteTitle)
    EditText mEdtNoteTitle;
    @BindView(R.id.edt_noteDescription)
    EditText mEdtNoteDescription;
    @BindView(R.id.text_reminder)
    TextView mTextReminder;
    @BindView(R.id.switch_reminder)
    Switch mSwitchReminder;
    @BindView(R.id.text_addImage)
    TextView mTextAddImage;
    @BindView(R.id.select_image)
    ImageView mSelectImage;
    @BindView(R.id.fab_saveNote)
    FloatingActionButton mFabSaveNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ButterKnife.bind(this);
    }


    @OnClick(R.id.select_image)
    public void onMSelectImageClicked() {
    }

    @OnClick(R.id.fab_saveNote)
    public void onMFabSaveNoteClicked() {
    }
}
