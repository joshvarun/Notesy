package com.varunjoshi.notesy.activity.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.varunjoshi.notesy.R;
import com.varunjoshi.notesy.activity.AppDatabase;
import com.varunjoshi.notesy.activity.MainActivity;
import com.varunjoshi.notesy.activity.Model.Note;
import com.varunjoshi.notesy.activity.Util.FontFamily;
import com.varunjoshi.notesy.activity.dao.NoteDao;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Notesy
 * Created by Varun Joshi on Fri, {2/2/18}.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private List<Note> allNotes = new ArrayList<>();
    private Context mContext;
    private NoteDao mNoteDao;
    private boolean isCompleted;

    public NotesAdapter() {
    }

    public NotesAdapter(List<Note> allNotes, boolean isCompleted, Context context) {
        this.allNotes = allNotes;
        this.mContext = context;
        this.isCompleted = isCompleted;
        AppDatabase appDatabase = AppDatabase.getAppDatabase(mContext);
        mNoteDao = appDatabase.mNoteDao();
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
        if (note.getNote_title() == null)
            holder.note_headline.setVisibility(View.GONE);
        else
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

        if (isCompleted) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setOnClickListener(v -> {
                note.setIsDone(1);
                mNoteDao.update(note);
                allNotes.remove(position);
                notifyDataSetChanged();
            });
        }

        holder.layout.setOnLongClickListener(v -> {
            final Dialog dia = new Dialog(mContext);
            dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dia.setContentView(R.layout.alert_dialog);


            LinearLayout delete = dia.findViewById(R.id.delete_note);
            LinearLayout share = dia.findViewById(R.id.share_note);

            delete.setOnClickListener(v12 -> {
                mNoteDao.delete(note);
                dia.dismiss();
            });
            share.setOnClickListener(v1 -> {
                final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_TEXT, note.getNote_title()+": " + note.getNote_description());
                intent.setType("text/plain");
                mContext.startActivity(Intent.createChooser(intent, "Share via"));
                dia.dismiss();
            });
            dia.show();
            return false;
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
        ImageView mCheckBox;

        FontFamily mFontFamily;

        public NotesViewHolder(View view) {
            super(view);
            layout = view.findViewById(R.id.constraintLayout);
            note_headline = view.findViewById(R.id.note_headline);
            note_description = view.findViewById(R.id.note_description);
            note_timestamp = view.findViewById(R.id.note_timestamp);
       //     note_image = view.findViewById(R.id.note_image);
            timer_set = view.findViewById(R.id.img_timer_set);
            mCheckBox = view.findViewById(R.id.setNoteDone);

            mFontFamily = new FontFamily(mContext);
            mFontFamily.setRegularFont(note_headline);
            mFontFamily.setRegularFont(note_description);
            mFontFamily.setMediumItalicFont(note_timestamp);
        }
    }

}
