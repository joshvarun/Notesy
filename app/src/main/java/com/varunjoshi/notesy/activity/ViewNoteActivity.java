package com.varunjoshi.notesy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.Util.FontFamily;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewNoteActivity extends AppCompatActivity {

    private static final String TAG = "ViewNoteActivity";
    Note mNote;
    Intent mIntent;
    FontFamily mFontFamily;
    @BindView(R.id.ic_Back)
    ImageView mIcBack;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;
    @BindView(R.id.txt_note_title)
    TextView mTxtNoteTitle;
    @BindView(R.id.title_note)
    TextView mTitleNote;
    @BindView(R.id.view)
    View mView;
    @BindView(R.id.view3)
    View mView3;
    @BindView(R.id.view2)
    View mView2;
    @BindView(R.id.txtNoteDescription)
    TextView mTxtNoteDescription;
    @BindView(R.id.description_note)
    TextView mDescriptionNote;
    @BindView(R.id.txtReminder)
    TextView mTxtReminder;
    @BindView(R.id.txtCreatedOn)
    TextView mTxtCreatedOn;
    @BindView(R.id.reminder)
    TextView mReminder;
    @BindView(R.id.createdOn)
    TextView mCreatedOn;
    @BindView(R.id.scrollView2)
    ScrollView mScrollView2;
    @BindView(R.id.btnEdtNote)
    FloatingActionButton mBtnEdtNote;
    @BindView(R.id.adView)
    AdView mAdView;

//    @BindView(R.id.view)
//    View mView;
//    @BindView(R.id.btnEdtNote)
//    FloatingActionButton mBtnEdtNote;
//    @BindView(R.id.adView)
//    AdView mAdView;
//    @BindView(R.id.view3)
//    View mView3;
//    @BindView(R.id.textReminderTime)
//    TextView mTextReminderTime;
//    @BindView(R.id.reminderTime)
//    TextView mReminderTime;

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
        mFontFamily.setMediumItalicFont(mTxtNoteTitle);
        mFontFamily.setRegularFont(mTitleNote);
        mFontFamily.setRegularFont(mDescriptionNote);
        mFontFamily.setRegularFont(mCreatedOn);
        mFontFamily.setRegularFont(mReminder);
        mFontFamily.setMediumItalicFont(mTxtNoteDescription);
        mFontFamily.setMediumItalicFont(mTxtReminder);
        mFontFamily.setMediumItalicFont(mTxtCreatedOn);
       // mFontFamily.setMediumItalicFont(mTextReminderTime);

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        setData();
    }

    private void setData() {
        if (!Objects.equals(mNote.getNote_title(), ""))
            mTitleNote.setText(mNote.getNote_title());
        else {
            mTitleNote.setVisibility(View.GONE);
            mView.setVisibility(View.GONE);
            mTxtNoteTitle.setVisibility(View.GONE);
        }
        mDescriptionNote.setText(mNote.getNote_description());
        //   mTextTime.setPaintFlags(mTextTime.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        if (mNote.isHasReminder()) {
            mTxtReminder.setVisibility(View.VISIBLE);
            mReminder.setVisibility(View.VISIBLE);
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd MMM yyyy, HH:mm");
            mReminder.setText(dateFormat1.format(mNote.getTimestamp()));
        } else {
            mTxtReminder.setVisibility(View.GONE);
            mReminder.setVisibility(View.GONE);
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        //Timestamp ts = new Timestamp(mNote.getCreatedDate());

        Log.d(TAG, "setData: "+mNote.getCreatedDate());
        mCreatedOn.setText(dateFormat.format(mNote.getCreatedDate()));
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
