package com.varunjoshi.notesy.activity.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.varunjoshi.notesy.activity.Model.Note;

import java.util.List;

/**
 * Notesy
 * Created by Varun Joshi on Fri, {2/2/18}.
 */
@Dao
public interface NoteDao {
    @Query("SELECT * FROM notes WHERE isDone=0")
    LiveData<List<Note>> getAll();

    @Query("SELECT * FROM notes WHERE isDone=1")
    LiveData<List<Note>> getCompletedNotes();

    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);

}
