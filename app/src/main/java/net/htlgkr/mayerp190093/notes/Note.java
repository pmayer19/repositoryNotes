package net.htlgkr.mayerp190093.notes;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {


    Date noteDate;
    String noteMessage;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
    boolean boxChecked;

    public Note(Date noteDate, String noteMessage,boolean boxChecked) {
        this.noteDate = noteDate;
        this.noteMessage = noteMessage;
        this.boxChecked = boxChecked;
    }

    public Date getNoteDate()
    {
        return noteDate;
    }

    @Override
    public String toString() {
        return simpleDateFormat.format(noteDate) + "; " + noteMessage;
    }

    public boolean getBoxChecked() {
        return boxChecked;
    }

    public void setBoxChecked(boolean boxChecked) {
        this.boxChecked = boxChecked;
    }
}
