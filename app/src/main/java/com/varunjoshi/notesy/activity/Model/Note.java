package com.varunjoshi.notesy.activity.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Notesy
 * Created by Varun Joshi on Thu, {1/2/18}.
 */
@Entity(tableName = "notes")
public class Note implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int uid;
    @ColumnInfo(name = "note_title")
    private String note_title;
    @ColumnInfo(name = "note_description")
    private String note_description;
    @ColumnInfo(name = "hasReminder")
    private boolean hasReminder;
    @ColumnInfo(name = "hasImage")
    private boolean hasImage;
    @ColumnInfo(name = "isDone")
    private int isDone;
    @ColumnInfo(name = "reminder")
    private long timestamp;
    @ColumnInfo(name = "image_path")
    private String image_path;
    @ColumnInfo(name = "colorInfo")
    private String color;
    @ColumnInfo(name = "alarmId")
    private int alarmId;

    protected Note(Parcel in) {
        uid = in.readInt();
        note_title = in.readString();
        note_description = in.readString();
        hasReminder = in.readByte() != 0;
        hasImage = in.readByte() != 0;
        isDone = in.readInt();
        timestamp = in.readLong();
        image_path = in.readString();
        color = in.readString();
        alarmId = in.readInt();
    }

    public Note(){

    }
    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }
    // 1 Done
    // 0 Not Done

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getNote_title() {
        return note_title;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public String getNote_description() {
        return note_description;
    }

    public void setNote_description(String note_description) {
        this.note_description = note_description;
    }

    public boolean isHasReminder() {
        return hasReminder;
    }

    public void setHasReminder(boolean hasReminder) {
        this.hasReminder = hasReminder;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }


    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(uid);
        dest.writeString(note_title);
        dest.writeString(note_description);
        dest.writeByte((byte) (hasReminder ? 1 : 0));
        dest.writeByte((byte) (hasImage ? 1 : 0));
        dest.writeInt(isDone);
        dest.writeLong(timestamp);
        dest.writeString(image_path);
        dest.writeString(color);
        dest.writeInt(alarmId);
    }
}
