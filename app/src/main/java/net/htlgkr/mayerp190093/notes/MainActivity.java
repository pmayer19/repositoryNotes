package net.htlgkr.mayerp190093.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Application;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mListView;

    private List<Note> notes = new ArrayList<>();
    NoteAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.listView);
        mAdapter = new NoteAdapter(getApplicationContext(),R.layout.noteadapter,notes);
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);
        readStorage();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        int viewId = v.getId();
        if(viewId == R.id.listView)
        {
            getMenuInflater().inflate(R.menu.menu ,menu);
        }

        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbarconfig,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.addGame:
                setUpAddButton();
                break;
            case R.id.save:
                writeStorage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuBearbeiten)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            alert.setTitle("Notiz umändern");
            String[] arr = notes.get(info.position).toString().split("");
            String datum = "";
            for (int i = 0; i < 17; i++) {
                datum += arr[i];
            }
            String inputNote = "";
            for (int i = 17; i < arr.length; i++) {
                inputNote += arr[i];
            }

            EditText mTextViewDatum = new EditText(this);
            mTextViewDatum.setText(datum);
            layout.addView(mTextViewDatum);
            EditText mTextViewNotiz = new EditText(this);
            mTextViewNotiz.setText(inputNote);
            layout.addView(mTextViewNotiz);
            alert.setView(layout);
            String finalDatum = datum;
            alert.setPositiveButton("add", new DialogInterface.OnClickListener() {
                @SuppressLint("SimpleDateFormat")
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    editNote(dialogInterface, mTextViewDatum, mTextViewNotiz,info.position, finalDatum);
                }
            });
            alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            alert.show();
        }
        if(item.getItemId() == R.id.menuDetails)
        {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            String name = "";
            if(info != null){
                long id = info.id;
                int pos = info.position;
                name = info != null ?
                        mListView.getAdapter().getItem(pos).toString():
                        "";
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage(notes.get(info.position).toString());

            alert.show();
            Toast.makeText(this,"Showing Details !" + name,Toast.LENGTH_SHORT).show();
            return true;
        }

        if(item.getItemId() == R.id.menuLöschen){
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            int pos = info.position;;
            notes.remove(pos);
            mAdapter.notifyDataSetChanged();
            Toast.makeText(this,"Deleting item",Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void setUpAddButton()
    {
                final int[] datum = {0};
                LinearLayout layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.VERTICAL);
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Notiz hinzufügen");
                EditText mTextViewDatum = new EditText(this);
                layout.addView(mTextViewDatum);
                EditText mTextViewNotiz = new EditText(this);
                mTextViewNotiz.setHint("Notiz eingeben");
                layout.addView(mTextViewNotiz);
                alert.setView(layout);
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                alert.setNeutralButton("set Time", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DialogFragment newFragment = new TimePickerFragment();
                        newFragment.show(getSupportFragmentManager(),"timePicker");
                        
                    }
                });
                alert.setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @SuppressLint("SimpleDateFormat")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        addNote(dialogInterface, mTextViewDatum, mTextViewNotiz);
                    }
                });
                alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                alert.show();

            }




    private void addNote(DialogInterface dialogInterface, EditText mTextViewDatum, EditText mTextViewNotiz) {
        String inputDate = mTextViewDatum.getText().toString();
        String inputNote = mTextViewNotiz.getText().toString();
        Date noteDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

        try {
            noteDate = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        notes.add(new Note(noteDate,inputNote));
        mAdapter.notifyDataSetChanged();
        dialogInterface.cancel();
    }

    private void editNote(DialogInterface dialogInterface, EditText mTextViewDatum, EditText mTextViewNotiz,int pos,String datum) {
        String inputDate = mTextViewDatum.getText().toString();
        String inputNote = mTextViewNotiz.getText().toString();
        Date noteDate = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        try {
            noteDate = simpleDateFormat.parse(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        notes.remove(pos);
        notes.add(pos,new Note(noteDate, inputNote));
        mAdapter.notifyDataSetChanged();
        dialogInterface.cancel();
    }

    private void writeStorage()
    {
        String fileName = "notes.csv";
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(fileName,MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
        for (int i = 0; i < notes.size(); i++) {
            String input = notes.get(i).toString();
            out.println(input+",");
            out.flush();
        }
        out.close();
    }

    private void readStorage()
    {
        try {
            FileInputStream fis = openFileInput("notes.csv");
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            String line;
            StringBuffer buffer = new StringBuffer();
            while((line = in.readLine()) != null){
                buffer.append(line);
            }
            String datum = "";
            String inputNote = "";

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");


            String[] storage = buffer.toString().split(",");
            for (int i = 0; i < storage.length; i++) {
                String[] arr = storage[i].split(";");
                datum = "";
                inputNote = "";

                for (int j = 0; j < arr.length; j = j+2) {
                    datum = arr[j];
                }
                for (int j = 1; j < arr.length; j = j+2) {
                    inputNote = arr[j];
                }

                notes.add(new Note(simpleDateFormat.parse(datum),inputNote));
                mAdapter.notifyDataSetChanged();
            }

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}