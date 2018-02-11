package com.varunjoshi.notesy.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.Util.Util;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * The configuration screen for the {@link NotesyAppWidget NotesyAppWidget} AppWidget.
 */
public class NotesyAppWidgetConfigureActivity extends Activity implements TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {
    private static final String TAG = "NotesyAppWidgetConfigur";
    private static final String PREFS_NAME = "com.varunjoshi.notesy.activity.NotesyAppWidget";
    private static final String PREF_PREFIX_TITLE_KEY = "appwidget_title_";
    private static final String PREF_PREFIX_DESC_KEY = "appwidget_desc_";
    private static final String PREF_PREFIX_COLOR_KEY = "appwidget_color_";
    private final static AtomicInteger ALARM_ID = new AtomicInteger(0);
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = NotesyAppWidgetConfigureActivity.this;

            // When the button is clicked, store the string locally
            String widgetText = mAppWidgetText.getText().toString();
            //  saveTitlePref(context, mAppWidgetId, widgetText);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            NotesyAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };
    @BindView(R.id.edt_noteTitle)
    EditText mEdtNoteTitle;
    @BindView(R.id.edt_noteDescription)
    EditText mEdtNoteDescription;
    @BindView(R.id.switch_reminder)
    Switch mSwitchReminder;
    @BindView(R.id.fab_saveNote)
    FloatingActionButton mFabSaveNote;
    @BindView(R.id.fab_deleteNote)
    FloatingActionButton mFabDeleteNote;

    boolean isEdit, isAlarmSet;
    int dayOfMonth, year, monthOfYear;
    Note mNote;
    CompoundButton.OnCheckedChangeListener listener;
    Intent mIntent;
    AppDatabase mAppDatabase;
    @BindView(R.id.text_reminder)
    TextView mTextReminder;
    private int alarmId;
    private Date mUserReminderDate;
    private long reminderTimestamp;
    private boolean hasReminder;
    private String defaultColor = "#1488CC";

    public NotesyAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String title, String description) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_TITLE_KEY + appWidgetId, title);
        prefs.putString(PREF_PREFIX_DESC_KEY + appWidgetId, description);
        prefs.apply();
    }

    static void saveColorPref(Context context, int appWidgetId, String color) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_COLOR_KEY + appWidgetId, color);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_TITLE_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static String loadDescPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String descValue = prefs.getString(PREF_PREFIX_DESC_KEY + appWidgetId, null);
        if (descValue != null) {
            return descValue;
        } else {
            return "";
        }
    }

    static String loadColorPref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String colorValue = prefs.getString(PREF_PREFIX_COLOR_KEY + appWidgetId, null);
        if (colorValue != null) {
            return colorValue;
        } else {
            return "#1488CC";
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_TITLE_KEY + appWidgetId);
        prefs.remove(PREF_PREFIX_DESC_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.notesy_app_widget_configure);
        ButterKnife.bind(this);
        mAppDatabase = AppDatabase.getAppDatabase(this);
        mNote = new Note();
//        mAppWidgetText = (EditText) findViewById(R.id.appwidget_text);
//        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        listener = (buttonView, isChecked) -> {
            if (isChecked) {
                hasReminder = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), TAG);
            } else {
                hasReminder = false;
                mTextReminder.setText(getResources().getString(R.string.note_reminder_hint));
            }
        };

        mSwitchReminder.setOnCheckedChangeListener(listener);

//        mAppWidgetText.setText(loadTitlePref(NotesyAppWidgetConfigureActivity.this, mAppWidgetId));
    }

    @OnClick({R.id.fab_saveNote, R.id.fab_deleteNote})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_saveNote:
                Calendar c = Calendar.getInstance();
                // Note note = new Note();
                if (mEdtNoteTitle.getText().toString().trim().length() == 0) {
                    mNote.setNote_title("");
                } else {
                    mNote.setNote_title(mEdtNoteTitle.getText().toString().trim());
                }
                if (mEdtNoteDescription.getText().toString().trim().length() == 0) {
                    mNote.setNote_description("");
                } else {
                    mNote.setNote_description(mEdtNoteDescription.getText().toString().trim());
                }
                if (mEdtNoteTitle.getText().toString().length() == 0 &&
                        mEdtNoteDescription.getText().toString().length() == 0) {
                    finish();
                } else {
                    mNote.setColor(defaultColor);

                    if (hasReminder) {
                        mNote.setHasReminder(true);
                        mNote.setTimestamp(reminderTimestamp);

                        alarmId = Util.getAlarmId();
                        mNote.setAlarmId(alarmId);
                        isAlarmSet = Util.setAlarm(this, reminderTimestamp, alarmId,
                                mEdtNoteTitle.getText().toString().trim(),
                                mEdtNoteDescription.getText().toString().trim());
                    } else {
                        mNote.setHasReminder(false);
                        mNote.setTimestamp(0);
                    }
                    // 1 Done
                    // 0 Not Done
                    mNote.setIsDone(0);
                    mNote.setCreatedDate(c.getTimeInMillis());
                    if (isEdit) {
                        mAppDatabase.mNoteDao().update(mNote);
                        startActivity(new Intent(this, TaskViewActivity.class));
                        finish();
                    } else {
                        mAppDatabase.mNoteDao().insert(mNote);
                        // When the button is clicked, store the string locally
                        final Context context = NotesyAppWidgetConfigureActivity.this;
                        String title = mEdtNoteTitle.getText().toString();
                        String desc = mEdtNoteDescription.getText().toString();
                        saveTitlePref(context, mAppWidgetId, title, desc);

                        // It is the responsibility of the configuration activity to update the app widget
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                        NotesyAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

                        // Make sure we pass back the original appWidgetId
                        Intent resultValue = new Intent();
                        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                        setResult(RESULT_OK, resultValue);
                        finish();
                    }
                }
                break;
            case R.id.fab_deleteNote:
                finish();
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        // mTextReminder.setText(dayOfMonth + (monthOfYear + 1) + year + "");
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear + 1;
        this.year = year;
        setDate(year, monthOfYear, dayOfMonth);
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.show(getFragmentManager(), "Datepickerdialog");

        tpd.setOnCancelListener(dialogInterface -> {
            hasReminder = false;
            Log.d(TAG, "Dialog was cancelled");
            Toast.makeText(this, "Please select time to set a reminder.",
                    Toast.LENGTH_SHORT).show();
            mSwitchReminder.setChecked(false);
            mTextReminder.setText(getResources().getString(R.string.note_reminder_hint));
        });
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String s = mTextReminder.getText().toString();
        mTextReminder.setText(MessageFormat.format("{0}/{1}/{2} {3}:{4}",
                dayOfMonth, monthOfYear, year, hourOfDay, minute));
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

    }

}

