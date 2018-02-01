package com.varunjoshi.notesy.activity.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Notesy
 * Created by Varun Joshi on Thu, {1/2/18}.
 */
@Entity(tableName = "notes")
public class Note {
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
    @ColumnInfo(name = "reminder")
    private long timestamp;
    @ColumnInfo(name = "image_path")
    private String image_path;

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
}
