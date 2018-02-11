package com.varunjoshi.notesy.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.Util.FontFamily;
import com.varunjoshi.notesy.activity.Util.Util;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import petrov.kristiyan.colorpicker.ColorPicker;

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
    @BindView(R.id.fab_deleteNote)
    FloatingActionButton mFabDeleteNote;
    @BindView(R.id.img_back)
    ImageView mImgBack;
    AppDatabase mAppDatabase;
    @BindView(R.id.note_color_select)
    CircleImageView mNoteColorSelect;
    @BindView(R.id.text_noteColor)
    TextView mTextNoteColor;
    @BindView(R.id.adView)
    AdView mAdView;
    boolean isEdit, isAlarmSet;
    int dayOfMonth, year, monthOfYear;
    Note mNote;
    CompoundButton.OnCheckedChangeListener listener;
    Intent mIntent;
    private int alarmId;
    private Date mUserReminderDate;
    private long reminderTimestamp;
    private boolean hasReminder;
    private String defaultColor = "#1488CC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        ButterKnife.bind(this);
        mFontFamily = new FontFamily(this);

        mFontFamily.setMediumFont(mAddNoteTitle);
        mFontFamily.setRegularFont(mEdtNoteTitle);
        mFontFamily.setRegularFont(mEdtNoteDescription);
        mFontFamily.setLightFont(mTextNoteColor);
        mFontFamily.setLightFont(mTextReminder);
        mFontFamily.setLightFont(mTextAddImage);
        mNote = new Note();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        listener = (buttonView, isChecked) -> {
            if (isChecked) {
                hasReminder = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        NewNoteActivity.this,
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

        mAppDatabase = AppDatabase.getAppDatabase(this);

        mIntent = getIntent();
        isEdit = mIntent.getBooleanExtra("isEdit", false);

        if (isEdit) {
            mNote = mIntent.getParcelableExtra("object");
            setData();
        }
    }

    private void setData() {
        mAddNoteTitle.setText("Edit Note");
        mEdtNoteTitle.setText(mNote.getNote_title());
        mEdtNoteDescription.setText(mNote.getNote_description());
        if (mNote.isHasReminder()) {
            mSwitchReminder.setOnCheckedChangeListener(null);
            mSwitchReminder.setChecked(true);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
            mTextReminder.setText(dateFormat.format(mNote.getTimestamp()));
            mSwitchReminder.setOnCheckedChangeListener(listener);
        }
        mNoteColorSelect.setColorFilter(Color.parseColor(mNote.getColor()));
    }


    @OnClick(R.id.fab_saveNote)
    public void onMFabSaveNoteClicked() {
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
                mEdtNoteDescription.getText().toString().length() == 0){
            finish();
        }else {
            mNote.setColor(defaultColor);
//        if (hasImage) {
//            note.setHasImage(true);
//            note.setImage_path(mMediaUri.getPath());
//        } else
//            note.setHasImage(false);
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
            Log.d(TAG, "onMFabSaveNoteClicked: created" + c.getTimeInMillis());
            if (isEdit) {
                mAppDatabase.mNoteDao().update(mNote);
                startActivity(new Intent(this, TaskViewActivity.class));
                finish();
            } else {
                mAppDatabase.mNoteDao().insert(mNote);
                finish();
            }
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
                NewNoteActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.show(getFragmentManager(), "Datepickerdialog");

        tpd.setOnCancelListener(dialogInterface -> {
            hasReminder = false;
            Log.d(TAG, "Dialog was cancelled");
            Toast.makeText(NewNoteActivity.this, "Please select time to set a reminder.",
                    Toast.LENGTH_SHORT).show();
            mSwitchReminder.setChecked(false);
            mTextReminder.setText(getResources().getString(R.string.note_reminder_hint));
        });
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String s = mTextReminder.getText().toString();
        mTextReminder.setText(MessageFormat.format("{0}/{1}/{2} {3}:{4}",
                dayOfMonth, monthOfYear, year + "", hourOfDay, minute));
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

    @OnClick({R.id.fab_deleteNote, R.id.img_back})
    public void onViewClicked(View view) {
        AlertDialog.Builder builder;
        switch (view.getId()) {
            case R.id.fab_deleteNote:
                if (mEdtNoteDescription.getText().length() == 0 && mEdtNoteTitle.getText().length() == 0) {
                    finish();
                } else {
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Alert")
                            .setMessage("Unsaved changes will be lost.")
                            .setPositiveButton("Continue", (dialog, which) -> finish())
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .show();
                }
                break;
            case R.id.img_back:
                if (mEdtNoteDescription.getText().length() == 0 && mEdtNoteTitle.getText().length() == 0) {
                    finish();
                } else {
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Alert")
                            .setMessage("Unsaved changes will be lost.")
                            .setPositiveButton("Continue", (dialog, which) -> finish())
                            .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                            .show();
                }
                break;
        }
    }

    @OnClick(R.id.note_color_select)
    public void onViewClicked() {
        final ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position, int color) {
                mNoteColorSelect.setColorFilter(color);
                defaultColor = String.format("#%06X", (0xFFFFFF & color));
                Log.d(TAG, "setOnFastChooseColorListener: " + defaultColor);

            }

            @Override
            public void onCancel() {
                // put code
            }
        })
                .setColors(R.array.mdcolor_400)
                .setRoundColorButton(true)
                .setColumns(5)
                .setTitle("Choose Note Color")
                .show();
    }


    //
//    void takePicture() {
//        mMediaUri = Util.getMediaOutputUri(this);
//        if (mMediaUri == null) {
//            // error
//            Toast.makeText(this, "There was a problem accessing your device's storage."
//                    , Toast.LENGTH_SHORT).show();
//        } else {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, mMediaUri);
//            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
//        }
//    }
//
//    void pickImage() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(intent, REQUEST_PICK_PHOTO);
//    }

//    @OnClick(R.id.select_image)
//    public void onMSelectImageClicked() {
//        Dexter.withActivity(this)
//                .withPermissions(
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.MANAGE_DOCUMENTS
//                ).withListener(new MultiplePermissionsListener() {
//            @Override
//            public void onPermissionsChecked(MultiplePermissionsReport report) {
//
//            }
//
//            @Override
//            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//
//            }
//        }).check();
//        final CharSequence[] options = {"Take Photo", "Choose from Gallery"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Add Image");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo")) {
//                    takePicture();
//                } else if (options[item].equals("Choose from Gallery")) {
//                    pickImage();
//                }
//            }
//        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
//    }
}
