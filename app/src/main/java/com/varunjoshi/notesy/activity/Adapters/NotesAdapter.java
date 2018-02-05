package com.varunjoshi.notesy.activity.Adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.uniquestudio.library.CircleCheckBox;
import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.AppDatabase;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.TaskViewActivity;
import com.varunjoshi.notesy.activity.dao.NoteDao;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Notesy
 * Created by Varun Joshi on Fri, {2/2/18}.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    List<Note> allNotes = new ArrayList<>();
    Context mContext;
    AppDatabase mAppDatabase;
    NoteDao mNoteDao;

    public NotesAdapter() {
    }

    public NotesAdapter(List<Note> allNotes, Context context) {
        this.allNotes = allNotes;
        this.mContext = context;
        mAppDatabase = AppDatabase.getAppDatabase(mContext);
        mNoteDao = mAppDatabase.mNoteDao();
    }

    public static String ConvertDateIntoSimpleFormat(String dateTime) {
        String result = "";

        String date = dateTime;//"2013-09-06'T'14:15:11.557'Z'";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        df = new SimpleDateFormat("dd/MM/yyyy, hh:mm aa");

        result = df.format(d);

        return result;
    }

    public void setItems(List<Note> notes) {
        this.allNotes = notes;
        notifyDataSetChanged();
    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.to_do_card, parent, false);

        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {

            Note note = allNotes.get(position);
        GradientDrawable bgShape = (GradientDrawable) holder.layout.getBackground();
        bgShape.setColor(getMatColor("500"));
        Timestamp timestamp = new Timestamp(note.getTimestamp());
        holder.note_headline.setText(note.getNote_title());
        holder.note_description.setText(note.getNote_description());
        if (note.isHasReminder())
            holder.timer_set.setVisibility(View.VISIBLE);
        else
            holder.timer_set.setVisibility(View.GONE);

        Date date = new Date(timestamp.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        holder.note_timestamp.setText(dateFormat.format(date));
        if (note.isHasImage())
            Picasso.with(mContext).load(note.getImage_path()).into(holder.note_image);

        // 1 Done
        // 0 Not Done
        holder.mCheckBox.setListener(isChecked -> {
            note.setIsDone(1);
            mNoteDao.update(note);
            allNotes.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    private int getMatColor(String typeColor) {
        int returnColor = Color.BLACK;
        int arrayId = mContext.getResources().getIdentifier("mdcolor_" + typeColor, "array", mContext.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = mContext.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        TextView note_headline, note_description, note_timestamp;
        ImageView note_image, timer_set;
        CircleCheckBox mCheckBox;

        public NotesViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.constraintLayout);
            note_headline = view.findViewById(R.id.note_headline);
            note_description = view.findViewById(R.id.note_description);
            note_timestamp = view.findViewById(R.id.note_timestamp);
            note_image = view.findViewById(R.id.note_image);
            timer_set = view.findViewById(R.id.img_timer_set);
            mCheckBox = view.findViewById(R.id.setNoteDone);
        }
    }

}
