package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
    private TextView correctAnswerCountTextView = null;
    private TextView incorrectAnswerCountTextView = null;
    private TextView totalTimeTextView = null;
    private TextView highestStreakTextView = null;

    private String correctAnswerCount = "";
    private String incorrectAnswerCount = "";
    private String totalTime = "";
    private String highestStreak = "";

    boolean gotAnswers = false;

    List<TextView> textViewList = new ArrayList<TextView>();
    List<Button> buttonList = new ArrayList<Button>();
    List<Spinner> spinnerList = new ArrayList<Spinner>();

    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;


    SharedPreferences darkPreference;
    SharedPreferences fontPreference;

    ConstraintLayout layout;

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
        correctAnswerCountTextView = findViewById(R.id.correctAnswerCountTextView);
        incorrectAnswerCountTextView = findViewById(R.id.incorrectAnswerCounterTextView);
        totalTimeTextView = findViewById(R.id.totalTimeTextView);
        highestStreakTextView = findViewById(R.id.highestStreakTextView);

        //set texts to invisible
        correctAnswerCountTextView.setVisibility(View.INVISIBLE);
        incorrectAnswerCountTextView.setVisibility(View.INVISIBLE);
        totalTimeTextView.setVisibility(View.INVISIBLE);
        highestStreakTextView.setVisibility(View.INVISIBLE);

        //set UI to invisible
        modeSpinner.setVisibility(View.INVISIBLE);
        typeSpinner.setVisibility(View.INVISIBLE);
        difficultySpinner.setVisibility(View.INVISIBLE);

        activitiesSpinner();

        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        layout =  findViewById(R.id.statsConstraintLayout);

        spinnerList.addAll((Collection<? extends Spinner>) Arrays.asList(activitySpinner, modeSpinner, typeSpinner, difficultySpinner));
        textViewList.addAll((Collection<? extends TextView>) Arrays.asList(correctAnswerCountTextView,incorrectAnswerCountTextView,totalTimeTextView,highestStreakTextView, (TextView) findViewById(R.id.activityTextView), (TextView) findViewById(R.id.difficultyTextView), (TextView) findViewById(R.id.modeTextView), (TextView) findViewById(R.id.typeTextView)));



        if(darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            for (TextView t : textViewList) {
                t.setTextColor(Color.WHITE);
            }
            for (Spinner s : spinnerList) {
                s.setBackgroundColor(Color.GRAY);
            }
            locateFileButton.setBackgroundColor(Color.GRAY);
        }
            else{
                layout.setBackgroundColor(Color.WHITE);
                for (TextView t: textViewList){
                    t.setTextColor(Color.BLACK);
                }
                for(Spinner s: spinnerList){
                    s.setBackgroundColor(Color.WHITE);
                }
            }
        if(fontPreference.getInt("Size", medSize) == smallSize){
            for (TextView t: textViewList){
                t.setTextSize(smallSize);
            }
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){

            for (TextView t: textViewList){
                t.setTextSize(medSize);
            }
        }
        else{
            for (TextView t: textViewList){
                t.setTextSize(largeSize);
            }

        }
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
                //set texts to invisible
                correctAnswerCountTextView.setVisibility(View.INVISIBLE);
                incorrectAnswerCountTextView.setVisibility(View.INVISIBLE);
                totalTimeTextView.setVisibility(View.INVISIBLE);
                highestStreakTextView.setVisibility(View.INVISIBLE);

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
        else if (activity.compareTo("Math Template") == 0) {
            modesList = Arrays.asList(getResources().getStringArray(R.array.GeometryModes));

            //add AlgebraModes to dropdown menu for spinner
            adapter = ArrayAdapter.createFromResource(this, R.array.GeometryModes, android.R.layout.simple_spinner_dropdown_item);
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
                if(activity.compareTo("Multiple Choice") == 0 || activity.compareTo("Math Template") == 0){
                    typeSpinner.setVisibility(View.VISIBLE);
                    typesSpinner();
                }
                //algebra, geometry or
                else{
                    //if mode isn't combination
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
                difficulty = difficultiesList.get(i);
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
    private class backgroundQuestionGeneration extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            SharedPreferences prefs = null;
            try {
                Log.i("Stats", "backgroundQuestionGeneration created");
                //multiple choice
                if (activity.compareTo("Multiple Choice") == 0) {
                    prefs = getSharedPreferences("MCStats" + difficulty + type + mode, Context.MODE_PRIVATE);
                    //get extra variable if in streak mode
                    if(mode.compareTo("Streak") == 0) {
                        highestStreak = prefs.getString("HighestStreak", "");
                    }
                }
                else if(activity.compareTo("Math Template") == 0){
                    prefs = getSharedPreferences("MTStats" + difficulty + type + 0, Context.MODE_PRIVATE);
                }
                else if (activity.compareTo("Algebra Input") == 0) {
                    prefs = getSharedPreferences("AIStats" + difficulty + type + mode, Context.MODE_PRIVATE);
                } else if (activity.compareTo("Geometry Input") == 0) {
                    prefs = getSharedPreferences("GIStats" + difficulty + type + 0, Context.MODE_PRIVATE);
                }
                //get general stats
                correctAnswerCount = prefs.getString("CorrectAnswerCount", "");
                incorrectAnswerCount = prefs.getString("IncorrectAnswerCount", "");
                totalTime = prefs.getString("TotalTimeSeconds", "");
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String parm) {
            updateTexts();
        }
    }
    private void updateTexts(){
        if(highestStreak.compareTo("") != 0) {
            //set text and show
            highestStreakTextView.setText(getString(R.string.highestStreak) + highestStreak);
            highestStreakTextView.setVisibility(View.VISIBLE);
        }

        //set texts
        correctAnswerCountTextView.setText(getString(R.string.correctAnswers) + correctAnswerCount);
        incorrectAnswerCountTextView.setText(getString(R.string.incorrectAnswers) + incorrectAnswerCount);
        totalTimeTextView.setText(getString(R.string.totalTime) + totalTime + getString(R.string.second));

        //show texts
        correctAnswerCountTextView.setVisibility(View.VISIBLE);
        incorrectAnswerCountTextView.setVisibility(View.VISIBLE);
        totalTimeTextView.setVisibility(View.VISIBLE);
    }
}