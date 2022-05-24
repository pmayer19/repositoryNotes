package net.htlgkr.mayerp190093.notes;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoteAdapter extends BaseAdapter {

    private List<Note> notes = new ArrayList<>();
    private int layoutId;
    private LayoutInflater inflater;

    public NoteAdapter(Context ctx, int layoutId, List<Note> notes)
    {
        this.notes = notes;
        this.layoutId = layoutId;
        this.inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Note note = notes.get(i);
        View listItem = (view == null) ? inflater.inflate(this.layoutId,null) : view;
        ((TextView) listItem.findViewById(R.id.textView)).setText(note.toString());

        if(note.getNoteDate().after(new Date()))
        {
            ((TextView) listItem.findViewById(R.id.textView)).setBackgroundColor(Color.GREEN);

        }

        return listItem;
    }
}
