package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class NoteTakingActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "NoteTakingActivity";
    ArrayList<String> list_notes;
    ListView listViewNotes;
    FloatingActionButton fabNew;
    NoteAdapter noteAdapter;
    NotesDatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private Cursor cursor;

    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;
    int sizeStatus;
    boolean darkStatus = false;
    RelativeLayout layout;
    SharedPreferences darkPreference;
    SharedPreferences fontPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taking);

        list_notes = new ArrayList<String>();
        listViewNotes = (ListView) findViewById(R.id.listViewNotes);

        noteAdapter =new NoteAdapter(this);
        listViewNotes.setAdapter(noteAdapter);
        dbHelper = new NotesDatabaseHelper(this);
        database = dbHelper.getWritableDatabase();
        cursor = database.query(NotesDatabaseHelper.TABLE_NAME, new String[] {"*"}, null, null, null,null,null);
        cursor.moveToFirst();
        Log.i(ACTIVITY_NAME, "Cursor’s  column count =" + cursor.getColumnCount());
        for(int i = 0; i < cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME, "Column[" + i + "] = " + cursor.getColumnName(i));
        }
        while(!cursor.isAfterLast()){
            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.KEY_NOTE_TITLE)));
            list_notes.add(cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.KEY_NOTE_TITLE)));
            cursor.moveToNext();
        }

        fabNew = findViewById(R.id.fabNotes);
        fabNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "Creating a new Note");
                Intent intent = new Intent(getBaseContext(), NoteDetailsActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        listViewNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(ACTIVITY_NAME, "Opened a Note");
                Intent intent = new Intent(getBaseContext(), NoteDetailsActivity.class);
                intent.putExtra("ID", noteAdapter.getItemId(position));
                intent.putExtra("Title", noteAdapter.getNoteTitle(position));
                intent.putExtra("Body", noteAdapter.getNoteBody(position));
                startActivityForResult(intent, 1);
            }
        });
        layout = findViewById(R.id.noteTakingActivity);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            darkStatus = true;
        }
        else {
            layout.setBackgroundColor(Color.WHITE);
            darkStatus = false;
        }
        if(fontPreference.getInt("Size", medSize) == smallSize){
            sizeStatus = smallSize;
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            sizeStatus = medSize;
        } else{
            sizeStatus = largeSize  ;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(ACTIVITY_NAME, "On result " + Integer.toString(resultCode));
        if (resultCode == 99) {
            long idPassed = data.getLongExtra("ID", -1);
            if (idPassed != -1){
                String query = "SELECT * FROM " + NotesDatabaseHelper.TABLE_NAME +
                        " WHERE " + NotesDatabaseHelper.KEY_ID + " = ?";
                Cursor cursorNew = database.rawQuery(query, new String[] {Long.toString(idPassed)});
                cursorNew.moveToFirst();
                String removeStr = cursorNew.getString(cursorNew.getColumnIndex(NotesDatabaseHelper.KEY_NOTE_TITLE));
                int position = list_notes.indexOf(removeStr);
                list_notes.remove(position);
                database.delete(NotesDatabaseHelper.TABLE_NAME, NotesDatabaseHelper.KEY_ID + " = ?", new String[] {Long.toString(idPassed)});
                noteAdapter.notifyDataSetChanged();
                cursor = database.query(NotesDatabaseHelper.TABLE_NAME, new String[] {"*"}, null, null, null,null,null);
            }
        } else if (resultCode == 100){
            long idPassed = data.getLongExtra("ID", -1);
            if (idPassed != -1) {
                String noteTitle = data.getStringExtra("Title");
                String noteBody = data.getStringExtra("Body");
                String query = "SELECT * FROM " + NotesDatabaseHelper.TABLE_NAME +
                        " WHERE " + NotesDatabaseHelper.KEY_ID + " = ?";
                Cursor cursorNew = database.rawQuery(query, new String[]{Long.toString(idPassed)});
                cursorNew.moveToFirst();
                String editStr = cursorNew.getString(cursorNew.getColumnIndex(NotesDatabaseHelper.KEY_NOTE_TITLE));
                int position = list_notes.indexOf(editStr);
                list_notes.remove(position);
                list_notes.add(position, noteTitle);
                ContentValues cv = new ContentValues();
                cv.put(NotesDatabaseHelper.KEY_NOTE_TITLE, noteTitle);
                cv.put(NotesDatabaseHelper.KEY_NOTE_BODY, noteBody);
                database.update(NotesDatabaseHelper.TABLE_NAME, cv, NotesDatabaseHelper.KEY_ID + " = ?", new String[]{Long.toString(idPassed)});
                noteAdapter.notifyDataSetChanged();
                cursor = database.query(NotesDatabaseHelper.TABLE_NAME, new String[] {"*"}, null, null, null,null,null);
            }
        } else if (resultCode == 101){
            long idPassed = data.getLongExtra("ID", -1);
            String noteTitle = data.getStringExtra("Title");
            String noteBody = data.getStringExtra("Body");
            list_notes.add(noteTitle);
            ContentValues cv = new ContentValues();
            cv.put(NotesDatabaseHelper.KEY_NOTE_TITLE, noteTitle);
            cv.put(NotesDatabaseHelper.KEY_NOTE_BODY, noteBody);
            database.insert(NotesDatabaseHelper.TABLE_NAME, null, cv);
            noteAdapter.notifyDataSetChanged();
            cursor = database.query(NotesDatabaseHelper.TABLE_NAME, new String[] {"*"}, null, null, null,null,null);
        }
    }

    private class NoteAdapter extends ArrayAdapter<String> {
        public NoteAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount(){
            return list_notes.size();
        }

        public long getItemId(int position){
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(NotesDatabaseHelper.KEY_ID));
        }

        public String getNoteTitle(int position){
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.KEY_NOTE_TITLE));
        }

        public String getNoteBody(int position){
            cursor.moveToPosition(position);
            return cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.KEY_NOTE_BODY));
        }

        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = NoteTakingActivity.this.getLayoutInflater();
            View result = inflater.inflate(R.layout.notes_row, null);
            TextView note = (TextView) result.findViewById(R.id.tvNoteTitle);
            if (darkStatus == true){
                note.setTextColor(Color.WHITE);
            }
            else {
                note.setTextColor(Color.BLACK);
            }
            if(sizeStatus == smallSize){
                note.setTextSize(smallSize);
            }
            else if(sizeStatus == medSize){
                note.setTextSize(medSize);
            }
            else{
                note.setTextSize(largeSize);
            }
            note.setText(getNoteTitle(position)); // get the string at position
            return result;
        }
    }
}