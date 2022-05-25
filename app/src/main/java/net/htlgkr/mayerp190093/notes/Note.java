package net.htlgkr.mayerp190093.notes;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Note {


    Date noteDate;
    String noteMessage;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public Note(Date noteDate, String noteMessage) {
        this.noteDate = noteDate;
        this.noteMessage = noteMessage;
    }

    public Date getNoteDate()
    {
        return noteDate;
    }

    @Override
    public String toString() {
        return simpleDateFormat.format(noteDate) + "; " + noteMessage;
    }

}
