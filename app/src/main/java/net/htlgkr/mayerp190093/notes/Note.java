package net.htlgkr.mayerp190093.notes;

import androidx.annotation.NonNull;

import java.util.Date;

public class Note {


    String noteDate;
    String noteMessage;

    public Note(String noteDate, String noteMessage) {
        this.noteDate = noteDate;
        this.noteMessage = noteMessage;
    }

    public String getNoteDate() {
        return noteDate;
    }

    public void setNoteDate(String noteDate) {
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
