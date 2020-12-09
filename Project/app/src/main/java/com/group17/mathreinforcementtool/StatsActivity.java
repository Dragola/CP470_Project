package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class StatsActivity extends AppCompatActivity {
    //Handler for the spinner
    private Spinner activitySpinner = null;
    private Spinner modeSpinner = findViewById(R.id.modeSpinner);
    private Spinner typeSpinner = findViewById(R.id.typeSpinner);
    private Spinner difficultySpinner = findViewById(R.id.difficultySpinner);
    private List<String> activitiesList;
    private List<String> modesList;
    private List<String> typesList;
    private List<String> difficultiesList;


    //TextView's
    TextView activityTextView = null;
    TextView modeTextView = null;
    TextView typeTextView = null;
    TextView operationTextView = null;
    TextView difficultyTextView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        activitiesSpinner();
        modesSpinner();
        typesSpinner();
        difficultiesSpinner();
    }

    public void activitiesSpinner() {
        activitySpinner = findViewById(R.id.activitySpinner);

        activitiesList = Arrays.asList(getResources().getStringArray(R.array.Activities));

        //add cities to dropdown menu for spinner
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.Activities, android.R.layout.simple_spinner_dropdown_item);

        //set adapter and add listener
        activitySpinner.setAdapter(adapter);
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //once city is selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get data for city
                //activitiesList.get(i);
            }

            //if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void modesSpinner() {
        modesList = Arrays.asList(getResources().getStringArray(R.array.Modes));

        //add cities to dropdown menu for spinner
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.Modes, android.R.layout.simple_spinner_dropdown_item);

        //set adapter and add listener
        activitySpinner.setAdapter(adapter);
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //once city is selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get data for city
                //modesList.get(i);
            }

            //if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void typesSpinner() {
        typesList = Arrays.asList(getResources().getStringArray(R.array.Types));

        //add cities to dropdown menu for spinner
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.Types, android.R.layout.simple_spinner_dropdown_item);

        //set adapter and add listener
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //once city is selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get data for city
                //modesList.get(i);
            }

            //if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void difficultiesSpinner() {
        difficultiesList = Arrays.asList(getResources().getStringArray(R.array.Difficulty));

        //add cities to dropdown menu for spinner
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.Difficulty, android.R.layout.simple_spinner_dropdown_item);

        //set adapter and add listener
        difficultySpinner.setAdapter(adapter);
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //once city is selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get data for city
                //modesList.get(i);
            }

            //if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
}