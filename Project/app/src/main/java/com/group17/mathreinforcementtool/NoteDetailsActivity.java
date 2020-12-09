package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class NoteDetailsActivity extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "NoteDetailsActivity";
    // noteType - 100 = oldNote, 101 = newNote
    private int noteType;
    private String newTitle = null;
    private String newBody = null;
    private long noteID = -1;
    private String noteTitle;
    private String noteBody;
    EditText etNoteTitle;
    EditText etNoteBody;
    FloatingActionButton fabSave;
    KeyListener mKeyListenerTitle;
    KeyListener mKeyListenerBody;

    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;
    int sizeStatus;
    boolean darkStatus = false;
    ConstraintLayout layout;
    SharedPreferences darkPreference;
    SharedPreferences fontPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        etNoteTitle = (EditText) findViewById(R.id.etNoteHeading);
        etNoteBody = (EditText) findViewById(R.id.etNoteBody);
        mKeyListenerTitle = etNoteTitle.getKeyListener();
        mKeyListenerBody = etNoteBody.getKeyListener();
        //Toolbar toolbar = findViewById(R.id.toolbarNote);
        fabSave = findViewById(R.id.fabSave);
        fabSave.setVisibility(View.INVISIBLE);


        noteID = getIntent().getLongExtra("ID", -1);
        Log.i(ACTIVITY_NAME, Long.toString(noteID));
        if (noteID != -1){
            noteType = 100;
            noteTitle = getIntent().getStringExtra("Title");
            noteBody = getIntent().getStringExtra("Body");
            etNoteTitle.setKeyListener(null);
            etNoteBody.setKeyListener(null);
            etNoteTitle.setText(noteTitle);
            etNoteBody.setText(noteBody);
        } else {
            noteType = 101;
            etNoteTitle.setKeyListener(mKeyListenerTitle);
            etNoteBody.setKeyListener(mKeyListenerBody);
            etNoteTitle.setText("");
            etNoteBody.setText("");
            fabSave.setVisibility(View.VISIBLE);
        }
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etNoteTitle.getText().toString().matches("")) {
                    Toast toast = Toast.makeText(getBaseContext(), R.string.toastNoteIncorrect, Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    fabSave.setVisibility(View.INVISIBLE);
                    etNoteTitle.setKeyListener(null);
                    etNoteBody.setKeyListener(null);
                    newTitle = etNoteTitle.getText().toString();
                    newBody = etNoteBody.getText().toString();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("ID", noteID);
                    resultIntent.putExtra("Title", newTitle);
                    resultIntent.putExtra("Body", newBody);
                    setResult(noteType, resultIntent);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(fabSave.getWindowToken(), 0);
                }
            }
        });

        layout = findViewById(R.id.noteDetailsActivity);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            etNoteBody.setTextColor(Color.WHITE);
            etNoteTitle.setTextColor(Color.WHITE);
            etNoteBody.setHintTextColor(Color.WHITE);
            etNoteTitle.setHintTextColor(Color.WHITE);
            etNoteTitle.setHighlightColor(Color.WHITE);
        }
        else {
            layout.setBackgroundColor(Color.WHITE);
            etNoteBody.setTextColor(Color.BLACK);
            etNoteTitle.setTextColor(Color.BLACK);
            etNoteBody.setHintTextColor(Color.BLACK);
            etNoteTitle.setHintTextColor(Color.BLACK);
        }
        if(fontPreference.getInt("Size", medSize) == smallSize){
            etNoteTitle.setTextSize(smallSize);
            etNoteBody.setTextSize(smallSize);
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            etNoteTitle.setTextSize(medSize);
            etNoteBody.setTextSize(medSize);
        } else{
            etNoteTitle.setTextSize(largeSize);
            etNoteBody.setTextSize(largeSize);
        }
    }

    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu_note, m);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi){
        switch(mi.getItemId()){
            case R.id.action_one:
                etNoteTitle.setKeyListener(mKeyListenerTitle);
                etNoteBody.setKeyListener(mKeyListenerBody);
                fabSave.setVisibility(View.VISIBLE);
                Toast toast = Toast.makeText(getBaseContext(), R.string.toastEditable, Toast.LENGTH_LONG);
                toast.show();
                break;
            case R.id.action_two:
                AlertDialog.Builder confirm_delete = new AlertDialog.Builder(NoteDetailsActivity.this);
                confirm_delete.setTitle(R.string.dialogDeleteTitle);
                // Add the buttons
                confirm_delete.setPositiveButton(R.string.dialogYes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i(ACTIVITY_NAME, "Delete Note");
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("ID", noteID);
                        setResult(99, resultIntent);
                        finish();
                    }
                });
                confirm_delete.setNegativeButton(R.string.dialogCancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = confirm_delete.create();
                dialog.show();
                break;
            default:
                break;
        }
        return true;
    }
}