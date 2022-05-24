package net.htlgkr.mayerp190093.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView mListView;
    Button buttonAddGame;
    Button buttonSave;
    private List<Note> notes = new ArrayList<>();
    NoteAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.listView);
        buttonAddGame = findViewById(R.id.buttonAddNote);
        buttonSave = findViewById(R.id.buttonSave);
        mAdapter = new NoteAdapter(getApplicationContext(),R.layout.noteadapter,notes);
        mListView.setAdapter(mAdapter);
        registerForContextMenu(mListView);
        readStorage();
        setUpAddButton();
        setUpSaveButton();
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
        buttonAddGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout layout = new LinearLayout(view.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setTitle("Notiz hinzufügen");
                EditText mTextViewDatum = new EditText(view.getContext());
                mTextViewDatum.setHint("Datum eingeben(dd.MM.yyyy HH:mm");
                layout.addView(mTextViewDatum);
                EditText mTextViewNotiz = new EditText(view.getContext());
                mTextViewNotiz.setHint("Notiz eingeben");
                layout.addView(mTextViewNotiz);
                alert.setView(layout);
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
        });
    }

    public void setUpSaveButton()
    {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeStorage();
            }
        });
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
        for (int i = 0; i < notes.size(); i++) {
            String input = notes.get(i).toString();
            try {
                FileOutputStream fos = openFileOutput(fileName,MODE_PRIVATE);
                PrintWriter out = new PrintWriter(new OutputStreamWriter(fos));
                out.println(input+",");
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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
                String[] arr = storage[i].split("");
                datum = "";
                inputNote = "";
                for (int j = 0; j < 17; j++) {
                    datum += arr[j];
                }
                for (int j = 17; j < arr.length; j++) {
                    inputNote += arr[j];
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