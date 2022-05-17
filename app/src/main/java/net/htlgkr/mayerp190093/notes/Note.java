package net.htlgkr.mayerp190093.notes;

import androidx.annotation.NonNull;

import java.util.Date;

public class Note {


    Date noteDate;
    String noteMessage;

    public Note(Date noteDate, String noteMessage) {
        this.noteDate = noteDate;
        this.noteMessage = noteMessage;
    }

    public Date getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(Date noteDate) {
        this.noteDate = noteDate;
    }

    public String getNoteMessage() {
        return noteMessage;
    }

    public void setNoteMessage(String noteMessage) {
        this.noteMessage = noteMessage;
    }

    @Override
    public String toString() {
        return noteDate + " " + noteMessage;
    }

}
