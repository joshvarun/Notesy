package com.varunjoshi.notesy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.Util.FontFamily;

import java.text.SimpleDateFormat;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewNoteActivity extends AppCompatActivity {

    @BindView(R.id.ic_Back)
    ImageView mIcBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.text_note_title)
    TextView mTextNoteTitle;
    @BindView(R.id.note_title)
    TextView mNoteTitle;
    @BindView(R.id.textNoteDescription)
    TextView mTextNoteDescription;
    @BindView(R.id.note_descripiton)
    TextView mNoteDescription;
    Note mNote;
    Intent mIntent;
    @BindView(R.id.textTime)
    TextView mTextTime;
    @BindView(R.id.time)
    TextView mTime;
    @BindView(R.id.reminderImg)
    ImageView mReminderImg;

    FontFamily mFontFamily;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.btnEdtNote)
    FloatingActionButton mBtnEdtNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);
        ButterKnife.bind(this);

        mIcBack.setVisibility(View.VISIBLE);
        mToolbarTitle.setText("View Note");
        mIntent = getIntent();
        mNote = mIntent.getParcelableExtra("object");

        mFontFamily = new FontFamily(this);

        mFontFamily.setMediumFont(mToolbarTitle);
        mFontFamily.setMediumItalicFont(mTextNoteTitle);
        mFontFamily.setRegularFont(mNoteTitle);
        mFontFamily.setRegularFont(mNoteDescription);
        mFontFamily.setRegularFont(mTime);
        mFontFamily.setMediumItalicFont(mTextNoteDescription);
        mFontFamily.setMediumItalicFont(mTextTime);

        setData();
    }

    private void setData() {
        if (!Objects.equals(mNote.getNote_title(), ""))
            mNoteTitle.setText(mNote.getNote_title());
        else {
            mNoteTitle.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
            mTextNoteTitle.setVisibility(View.GONE);
        }
        mNoteDescription.setText(mNote.getNote_description());
        //   mTextTime.setPaintFlags(mTextTime.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        if (mNote.isHasReminder()) {
            mTextTime.setText(getResources().getString(R.string.reminder_text));
            mReminderImg.setVisibility(View.VISIBLE);
        } else {
            mTextTime.setText(getResources().getString(R.string.created_text));
            mReminderImg.setVisibility(View.GONE);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        mTime.setText(dateFormat.format(mNote.getTimestamp()));
    }

    @OnClick(R.id.ic_Back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.btnEdtNote)
    public void onFabClicked() {
        startActivity(new Intent(this, NewNoteActivity.class)
                .putExtra("object", mNote)
                .putExtra("isEdit", true));
    }
}
