package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private Button locateFileButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        //locate UI
        activitySpinner = findViewById(R.id.activitySpinner);
        modeSpinner = findViewById(R.id.modeSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        difficultySpinner = findViewById(R.id.difficultySpinner);
        locateFileButton = findViewById(R.id.locateFileButton);
        locateFileButton.setEnabled(false);

        //set UI to invisible
        modeSpinner.setVisibility(View.INVISIBLE);
        typeSpinner.setVisibility(View.INVISIBLE);
        difficultySpinner.setVisibility(View.INVISIBLE);

        activitiesSpinner();
    }

    public void activitiesSpinner() {
        activitiesList = Arrays.asList(getResources().getStringArray(R.array.Activities));

        //add cities to dropdown menu for spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Activities, android.R.layout.simple_spinner_dropdown_item);

        //set adapter and add listener
        activitySpinner.setAdapter(adapter);
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //item selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity = activitiesList.get(i);
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

            //add MCModes to dropdown menu for spinner
            adapter = ArrayAdapter.createFromResource(this, R.array.MCModes, android.R.layout.simple_spinner_dropdown_item);
        }
        else if (activity.compareTo("Algebra Input") == 0) {
            modesList = Arrays.asList(getResources().getStringArray(R.array.AlgebraModes));

            //add AlgebraModes to dropdown menu for spinner
            adapter = ArrayAdapter.createFromResource(this, R.array.AlgebraModes, android.R.layout.simple_spinner_dropdown_item);
        }
        else if (activity.compareTo("Geometry Input") == 0) {
            modesList = Arrays.asList(getResources().getStringArray(R.array.GeometryModes));

            //add GeometryModes to dropdown menu for spinner
            adapter = ArrayAdapter.createFromResource(this, R.array.GeometryModes, android.R.layout.simple_spinner_dropdown_item);
        }

        //set adapter and add listener
        modeSpinner.setAdapter(adapter);
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //once city is selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get item
                mode = modesList.get(i);

                //multiple choice
                if(activity.compareTo("Multiple Choice") == 0){
                    //if not combination then use type
                    if(mode.compareTo("Combination") != 0){
                        typeSpinner.setVisibility(View.VISIBLE);
                        typesSpinner();
                    }
                    else{
                        typeSpinner.setVisibility(View.INVISIBLE);
                        type = "Addition";
                    }
                }
                //algebra or geometry
                else{
                    if(mode.compareTo("Combination") != 0){
                        typeSpinner.setVisibility(View.VISIBLE);
                        typesSpinner();
                    }
                    else{
                        typeSpinner.setVisibility(View.INVISIBLE);
                        type = "Addition";
                    }
                }
            }
            //if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void typesSpinner() {
        ArrayAdapter<CharSequence> adapter = null;

        //multiple choice
        if(activity.compareTo("Multiple Choice") == 0) {
            typesList = Arrays.asList(getResources().getStringArray(R.array.MCTypes));
            //add MCTypes to dropdown menu for spinner
            adapter = ArrayAdapter.createFromResource(this, R.array.MCTypes, android.R.layout.simple_spinner_dropdown_item);
        }
        else{
            typesList = Arrays.asList(getResources().getStringArray(R.array.AGTypes));
            //add AGTypes to dropdown menu for spinner
            adapter = ArrayAdapter.createFromResource(this, R.array.AGTypes, android.R.layout.simple_spinner_dropdown_item);
        }
        //set adapter and add listener
        typeSpinner.setAdapter(adapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //once city is selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get item
                type = typesList.get(i);
                difficultySpinner.setVisibility(View.VISIBLE);
                difficultiesSpinner();
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Difficulty, android.R.layout.simple_spinner_dropdown_item);

        //set adapter and add listener
        difficultySpinner.setAdapter(adapter);
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            //once city is selected
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //get data for city
                difficulty = modesList.get(i);
                locateFileButton.setEnabled(true);
            }

            //if nothing is selected
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    public void onClickFileLocate(View v){
        new backgroundQuestionGeneration().execute();
    }
    //used to generate remaining questions
    private class backgroundQuestionGeneration extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String correctCount = "";
            String temp = "";

            try {
                Log.i("Stats", "backgroundQuestionGeneration created");
                if (activity.compareTo("Multiple Choice") == 0) {
                    Log.i("Stats", "backgroundQuestionGeneration: Activity = Multiple Choice. Attempting to get file: " + "MCStats" + difficulty + type + mode);

                    SharedPreferences prefs = getSharedPreferences("MCStats" + difficulty + type + mode, Context.MODE_PRIVATE);

                    correctCount = prefs.getString("CorrectAnswerCount", "");

                    Log.i("Stats", "Correct answers=" + correctCount);
                    Log.i("Stats", "Incorrect answers=" + prefs.getString("IncorrectAnswerCount", ""));
                    Log.i("Stats", "TotalTimeSeconds =" + prefs.getString("TotalTimeSeconds", ""));
                    Log.i("Stats", "Difficulty =" + prefs.getString("Difficulty", ""));
                    Log.i("Stats", "Operation =" + prefs.getString("Operation", ""));
                    Log.i("Stats", "Highest Streak =" + prefs.getString("HighestStreak", ""));
                } else if (activity.compareTo("Algebra Input") == 0) {
                    SharedPreferences prefs = getSharedPreferences("AIStats" + difficulty + type + mode, Context.MODE_PRIVATE);
                } else if (activity.compareTo("Geometry Input") == 0) {
                    SharedPreferences prefs = getSharedPreferences("GIStats" + difficulty + type + mode, Context.MODE_PRIVATE);
                }
                //pull general stuff

            } catch (Exception e){
                e.printStackTrace();
            }
            this.cancel(true);
            return null;
        }
    }
}