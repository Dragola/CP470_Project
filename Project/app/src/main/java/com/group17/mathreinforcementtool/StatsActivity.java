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
    private Spinner modeSpinner = null;
    private Spinner typeSpinner = null;
    private Spinner difficultySpinner = null;
    private List<String> activitiesList;
    private List<String> modesList;
    private List<String> typesList;
    private List<String> difficultiesList;

    private String activity = "";
    private String mode = "";
    private String type = "";
    private String operation = "";
    private String difficulty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //locate UI
        activitySpinner = findViewById(R.id.activitySpinner);
        modeSpinner = findViewById(R.id.modeSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        difficultySpinner = findViewById(R.id.difficultySpinner);

        //set UI to invisible
        modeSpinner.setVisibility(View.INVISIBLE);
        typeSpinner.setVisibility(View.INVISIBLE);
        difficultySpinner.setVisibility(View.INVISIBLE);

        activitiesSpinner();
    }

    public void activitiesSpinner() {
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
                String tempStr = activitiesList.get(i);

                //Multiple choice
                if(tempStr.compareTo("Multiple Choice") == 0){
                    activity = tempStr;
                }
                //User Input
                else if (tempStr.compareTo("User Input") == 0){
                    activity = tempStr;
                }
                modesSpinner();
                modeSpinner.setVisibility(View.VISIBLE);
            }

            //if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void modesSpinner() {
        ArrayAdapter<CharSequence> adapter = null;

        if(activity.compareTo("Multiple Choice") == 0) {
            modesList = Arrays.asList(getResources().getStringArray(R.array.MCModes));

            //add cities to dropdown menu for spinner
            adapter = ArrayAdapter.createFromResource(this, R.array.MCModes, android.R.layout.simple_spinner_dropdown_item);
        }
        else if (activity.compareTo("User Input") == 0) {
            modesList = Arrays.asList(getResources().getStringArray(R.array.UIModes));

            //add cities to dropdown menu for spinner
            adapter = ArrayAdapter.createFromResource(this, R.array.UIModes, android.R.layout.simple_spinner_dropdown_item);
        }

        //set adapter and add listener
        modeSpinner.setAdapter(adapter);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //once city is selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get data for city
                String tempStr = modesList.get(i);

                //
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