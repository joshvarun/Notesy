package com.varunjoshi.notesy.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Util.FontFamily;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewNoteActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private static final String TAG = "NewNoteActivity";
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

    FontFamily mFontFamily;
    @BindView(R.id.add_note_title)
    TextView mAddNoteTitle;

    private Date mUserReminderDate;
    private long reminderTimestamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ButterKnife.bind(this);
        mFontFamily = new FontFamily(this);

        mFontFamily.setMediumFont(mAddNoteTitle);
        mFontFamily.setMediumFont(mEdtNoteTitle);
        mFontFamily.setRegularFont(mEdtNoteDescription);
        mFontFamily.setLightFont(mTextReminder);
        mFontFamily.setLightFont(mTextAddImage);

        mSwitchReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Calendar now = Calendar.getInstance();
                    DatePickerDialog dpd = DatePickerDialog.newInstance(
                            NewNoteActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                    dpd.show(getFragmentManager(), "Datepickerdialog");
                }
            }
        });
    }


    @OnClick(R.id.select_image)
    public void onMSelectImageClicked() {

    }

    @OnClick(R.id.fab_saveNote)
    public void onMFabSaveNoteClicked() {
    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mTextReminder.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        setDate(year, monthOfYear, dayOfMonth);
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                NewNoteActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.show(getFragmentManager(), "Datepickerdialog");
        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Log.d(TAG, "Dialog was cancelled");
                Toast.makeText(NewNoteActivity.this, "Please select time to set a reminder.",
                        Toast.LENGTH_SHORT).show();
                mSwitchReminder.setChecked(false);
                mTextReminder.setText(getResources().getString(R.string.note_reminder_hint));
            }
        });
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String s = mTextReminder.getText().toString();
        mTextReminder.setText(s + " " + hourOfDay + ":" + minute);
        setTime(hourOfDay, minute);
    }

    public void setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        int hour, minute;
        Calendar reminderCalendar = Calendar.getInstance();
        reminderCalendar.set(year, month, day);

        if (reminderCalendar.before(calendar)) {
            Toast.makeText(this, "My Time Machine doesn't work yet!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }

        if (DateFormat.is24HourFormat(this)) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
        } else {
            hour = calendar.get(Calendar.HOUR);
        }
        minute = calendar.get(Calendar.MINUTE);

        calendar.set(year, month, day, hour, minute);
        mUserReminderDate = calendar.getTime();

    }

    public void setTime(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        if (mUserReminderDate != null) {
            calendar.setTime(mUserReminderDate);
        }

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(year, month, day, hour, minute, 0);
        mUserReminderDate = calendar.getTime();
        reminderTimestamp = calendar.getTimeInMillis();
        Log.d(TAG, "setTime: " + reminderTimestamp);
        //  mTextReminder.setText(DateFormat.getTimeFormat(this).format(calendar));
        setAlarm(reminderTimestamp);
    }

    private void setAlarm(long reminderTimestamp) {
        String note_title = mEdtNoteTitle.getText().toString();
        String note_description = mEdtNoteDescription.getText().toString();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(NewNoteActivity.this, AlarmReceiver.class);
        if (note_title.length() == 0) {
            note_title = "";
        }
        if (note_description.length() == 0){
            Toast.makeText(this, "Please write a note!", Toast.LENGTH_SHORT).show();
            return;
        }
        intent.putExtra("note_title", note_title);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(NewNoteActivity.this,
                1, intent, 0);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, reminderTimestamp, pendingIntent);
        }

    }
}
