package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class LevelSelect extends AppCompatActivity {
    protected static final String ACTIVITY_NAME = "LevelSelect";
    protected int COLOR_LOCKED = 0;
    protected int COLOR_UNATTEMPTED = 1;
    protected int COLOR_INPROGRESS = 2;
    protected int COLOR_COMPLETED = 3;

    private int colorLocked;
    private int colorUnattempted;
    private int colorCompleted;
    private int colorInprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);
        // Get the request code (the math activity to select levels for)
        Intent intent = getIntent();
        String requestCode = intent.getStringExtra("requestCode");

        // The current color scheme to use
        colorLocked = ResourcesCompat.getColor(getResources(), R.color.colorLightLocked, null);
        colorUnattempted = ResourcesCompat.getColor(getResources(), R.color.colorLightUnattempted, null);
        colorCompleted = ResourcesCompat.getColor(getResources(), R.color.colorLightCompleted, null);
        colorInprogress = ResourcesCompat.getColor(getResources(), R.color.colorLightInprogress, null);

        // Clears Saved preferences for testing purposes
        getSharedPreferences(requestCode, Context.MODE_PRIVATE).edit().clear().apply();

        // Load list of levels for the math activity from shared preferences (completed, uncompleted, unattempted, unavailable)
        ArrayList<String> levelsData = loadLevelsFromPrefrences(requestCode);
        // If the list doesnt exist yet then make it
        if (levelsData.isEmpty()){
            Log.i(ACTIVITY_NAME, "Generating Prefrences for " + requestCode);
            levelsData = generateSharedPrefs(requestCode);
        }
        // If the list generation failed then end the activity
        if (levelsData.isEmpty()){
            Log.i(ACTIVITY_NAME, "Could not load level data for file: " + requestCode);
            Toast toast = Toast.makeText(getBaseContext(), R.string.sharedPrefFailed, Toast.LENGTH_LONG);
            toast.show();
            finish();
        }

        // Populate layout with n buttons and set their colour, other values and onclick functions
        populateLayout(levelsData);

        // Save level progression
    }

    // Creates default data array for each level
    // Currently has random numbers and colours for testing purposes
    private ArrayList<String> generateSharedPrefs(String requestCode){
        ArrayList<String> levelData = new ArrayList<String>();
        //"{Level Name}/{Button Color}/{Level Description Tag}/{Locked (0 or 1)}/{Type}/{Difficulty}"
        // If were making a file for Addition
        if (requestCode.equals("Addition")){
            levelData.add("Addition" + "/" + "Easy" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AdditionEasyDescription" + "/" + Integer.toString(0) + "/" + "Addition" + "/" + "Easy");
            levelData.add("Addition" + "/" + "Medium" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AdditionMediumDescription" + "/" + Integer.toString(0) + "/" + "Addition" + "/" + "Medium");
            levelData.add("Addition" + "/" + "Hard" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AdditionHardDescription" + "/" + Integer.toString(0) + "/" + "Addition" + "/" + "Hard");
        // If were making a file for Subtraction
        } else if (requestCode.equals("Subtraction")) {
            levelData.add("Subtraction" + "/" + "Easy" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "SubtractionEasyDescription" + "/" + Integer.toString(0) + "/" + "Subtraction" + "/" + "Easy");
            levelData.add("Subtraction" + "/" + "Medium" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "SubtractionMediumDescription" + "/" + Integer.toString(0) + "/" + "Subtraction" + "/" + "Medium");
            levelData.add("Subtraction" + "/" + "Hard" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "SubtractionHardDescription" + "/" + Integer.toString(0) + "/" + "Subtraction" + "/" + "Hard");
        // If were making a file for Multiplication
        } else if (requestCode.equals("Multiplication")) {
            levelData.add("Multiplication" + "/" + "Easy" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MultiplicationEasyDescription" + "/" + Integer.toString(0) + "/" + "Multiplication" + "/" + "Easy");
            levelData.add("Multiplication" + "/" + "Medium" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MultiplicationMediumDescription" + "/" + Integer.toString(0) + "/" + "Multiplication" + "/" + "Medium");
            levelData.add("Multiplication" + "/" + "Hard" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MultiplicationHardDescription" + "/" + Integer.toString(1) + "/" + "Multiplication" + "/" + "Hard");
        // If were making a file for Division
        } else if (requestCode.equals("Division")) {
            levelData.add("Division" + "/" + "Easy" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "DivisionEasyDescription" + "/" + Integer.toString(0) + "/" + "Division" + "/" + "Easy");
            levelData.add("Division" + "/" + "Medium" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "DivisionMediumDescription" + "/" + Integer.toString(0) + "/" + "Division" + "/" + "Medium");
            levelData.add("Division" + "/" + "Hard" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "DivisionHardDescription" + "/" + Integer.toString(1) + "/" + "Division" + "/" + "Hard");
        // If were making a file for Perimeter
        } else if (requestCode.equals("Perimeter")) {
            levelData.add("Perimeter" + "/" + "Easy" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "PerimeterEasyDescription" + "/" + Integer.toString(0) + "/" + "Perimeter" + "/" + "Easy");
            levelData.add("Perimeter" + "/" + "Medium" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "PerimeterMediumDescription" + "/" + Integer.toString(0) + "/" + "Perimeter" + "/" + "Medium");
            levelData.add("Perimeter" + "/" + "Hard" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "PerimeterHardDescription" + "/" + Integer.toString(1) + "/" + "Perimeter" + "/" + "Hard");
        // If were making a file for Area
        } else if (requestCode.equals("Area")) {
            levelData.add("Area" + "/" + "Easy" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AreaEasyDescription" + "/" + Integer.toString(0) + "/" + "Area" + "/" + "Easy");
            levelData.add("Area" + "/" + "Medium" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AreaMediumDescription" + "/" + Integer.toString(0) + "/" + "Area" + "/" + "Medium");
            levelData.add("Area" + "/" + "Hard" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AreaHardDescription" + "/" + Integer.toString(1) + "/" + "Area" + "/" + "Hard");
        // If were making a file for Algebra
        } else if (requestCode.equals("Algebra")) {
            levelData.add("Algebra" + "/" + "Easy" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AlgebraEasyDescription" + "/" + Integer.toString(0) + "/" + "Algebra" + "/" + "Easy");
            levelData.add("Algebra" + "/" + "Medium" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AlgebraMediumDescription" + "/" + Integer.toString(0) + "/" + "Algebra" + "/" + "Medium");
            levelData.add("Algebra" + "/" + "Hard" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AlgebraHardDescription" + "/" + Integer.toString(1) + "/" + "Algebra" + "/" + "Hard");
            // If were making a file for MCQ
        } else if (requestCode.equals("MCQ")) {
            levelData.add("MCQ" + "/" + "Easy" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCQEasyDescription" + "/" + Integer.toString(0) + "/" + "2" + "/" + "0");
            levelData.add("MCQ" + "/" + "Medium" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCQMediumDescription" + "/" + Integer.toString(0) + "/" + "0" + "/" + "1");
            levelData.add("MCQ" + "/" + "Hard" + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCQHardDescription" + "/" + Integer.toString(1) + "/" + "0" + "/" + "2");
        }else if (requestCode.equals("TestLvl")) {
            String tempStr = "";
            for (int i = 1; i <= 75; i++) {
                if ((i >= 1) && (i <= 13)) {
                    tempStr = "TestLvl" + "/" + i + "/" + Integer.toString(COLOR_COMPLETED) + "/" + "TestLvlDescription" + "/" + Integer.toString(0) + "/" + "0" + "/" + "0";
                } else if ((i >= 14) && (i <= 20)) {
                    tempStr = "TestLvl" + "/" + i + "/" + Integer.toString(COLOR_INPROGRESS) + "/" + "TestLvlDescription" + "/" + Integer.toString(0) + "/" + "0" + "/" + "0";
                } else if ((i >= 21) && (i <= 40)) {
                    tempStr = "TestLvl" + "/" + i + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "TestLvlDescription" + "/" + Integer.toString(0) + "/" + "0" + "/" + "0";
                    if ((i == 22) || (i == 27) || (i == 30) || (i == 34)) {
                        tempStr = "TestLvl" + "/" + i + "/" + Integer.toString(COLOR_INPROGRESS) + "/" + "TestLvlDescription" + "/" + Integer.toString(0) + "/" + "0" + "/" + "0";
                    }
                } else if ((i >= 41) && (i <= 75)) {
                    tempStr = "TestLvl" + "/" + i + "/" + Integer.toString(COLOR_LOCKED) + "/" + "TestLvlDescription" + "/" + Integer.toString(1) + "/" + "0" + "/" + "0";
                }
                levelData.add(tempStr);
            }
        }
        // saves the list to a SharedPrefrence file
        //saveLevelsToPrefrences(levelData, requestCode);
        return levelData;
    }

    // Gets device dimensions to space out the buttons nicely
    private int getDeviceWidth(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = displayMetrics.widthPixels;
        return width;
    }

    // Adds the difficulty buttons to the activity
    private void populateLayout(ArrayList<String> levelData){
        Log.i(ACTIVITY_NAME, "In populateLayout()");
        // Variables to make the button layout look nice
        int numLevels = levelData.size();
        int columns = 4;
        int rows = (numLevels / columns) + 1;
        int deviceWidth = getDeviceWidth();
        int paddingValue = 20;
        int buttonWidth = (deviceWidth-8*paddingValue)/columns;

        // Makes the grid layout
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayoutLevels);
        grid.removeAllViews();
        grid.setColumnCount(columns);
        grid.setRowCount(rows);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        // Adds padding to each button
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = paddingValue;
        layoutParams.rightMargin = paddingValue;
        layoutParams.topMargin = paddingValue;
        layoutParams.bottomMargin = paddingValue;
        // Creates the button and sets its values for every level data in the array
        for (String data : levelData){
            // Splits the string with /
            String dataArray[] = data.split("/");
            // Extracts the values
            final String levelType = dataArray[0];
            final String difficulty = dataArray[1];
            int colorCode = Integer.parseInt(dataArray[2]);
            final String description = dataArray[3];
            int isLocked = Integer.parseInt(dataArray[4]);
            final String IntentTypeTag = dataArray[5];
            final String IntentDifficultyTag = dataArray[6];
            // Makes the button and sets its values
            Button buttonLevel = new Button(this);
            buttonLevel.setTag(difficulty);
            buttonLevel.setText(difficulty);
            buttonLevel.setWidth(buttonWidth);
            buttonLevel.setLayoutParams(layoutParams);
            // Set dynamic colour based on level completion
            if (colorCode == COLOR_COMPLETED) {
                buttonLevel.setBackgroundColor(colorCompleted);
            } else if (colorCode == COLOR_INPROGRESS) {
                buttonLevel.setBackgroundColor(colorInprogress);
            } else if (colorCode == COLOR_UNATTEMPTED) {
                buttonLevel.setBackgroundColor(colorUnattempted);
            }
            // Check if the button is locked or not
            if (isLocked == 1){
                buttonLevel.setBackgroundColor(colorLocked);
            }
            // Adds the button to the grid
            grid.addView(buttonLevel);
            // if the button is not locked
            if ((isLocked == 0) && (levelType.compareTo("TestLvl") != 0)) {
                // Sets the on click listener (Adding a if statement should be able to lock a button)
                int buttonLevelId = buttonLevel.getId();
                Button button = (Button) findViewById(buttonLevelId);
                buttonLevel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Use a diaglog builder to display the next step
                        final AlertDialog.Builder dialogBox = new AlertDialog.Builder(LevelSelect.this);
                        Context context = LevelSelect.this;
                        final LayoutInflater inflater = LayoutInflater.from(context);
                        final View view = inflater.inflate(R.layout.activity_level_select_dialogbox, null);
                        TextView tvLevelType = view.findViewById(R.id.textLevelType);
                        TextView tvLevelDifficulty = view.findViewById(R.id.textLevelDifficulty);
                        TextView tvLevelDescription = view.findViewById(R.id.textLevelDescription);
                        Log.i(ACTIVITY_NAME, "Level type selected" + levelType);
                        Log.i(ACTIVITY_NAME, "Level difficulty selected" + difficulty);
                        Log.i(ACTIVITY_NAME, "Level Description" + description);
                        // Part of the string to get resource with, has to be done this way since Im creating the buttons dynamically
                        String str = "com.group17.mathreinforcementtool:string/";
                        // Gets the strings to be displayed in the dialog box
                        String levelTypeOut = getResources().getString(R.string.textActivityType) + " " + getResources().getString(getResources().getIdentifier(str + levelType, null, null));
                        String levelDiffOut = getResources().getString(R.string.textLevelDifficulty) + " " + getResources().getString(getResources().getIdentifier(str + difficulty, null, null));
                        String levelDescOut = getResources().getString(R.string.textLevelDesc) + " " + getResources().getString(getResources().getIdentifier(str + description, null, null));
                        // Sets the strings values
                        tvLevelType.setText(levelTypeOut);
                        tvLevelDifficulty.setText(levelDiffOut);
                        tvLevelDescription.setText(levelDescOut);
                        // Creates the dialog box
                        dialogBox.setView(view)
                                .setPositiveButton(R.string.startLevel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // OnClick Start
                                        Log.i(ACTIVITY_NAME, "Pressed Start");
                                        Intent intent;
                                        // If the level type is MCQ (the intent inputs are different between this and the other activities)
                                        if (levelType.compareTo("MCQ") == 0){
                                            intent = new Intent(getBaseContext(), MultipleChoiceActivity.class);
                                            intent.putExtra("difficulty", Integer.parseInt(IntentDifficultyTag));
                                            intent.putExtra("type", Integer.parseInt(IntentTypeTag));
                                            startActivity(intent);
                                        // If the level type is different
                                        } else {
                                            // The intent inputs are the same format for these so they are in a separated if from MCQ Intent
                                            if ((levelType.compareTo("Area") == 0) || (levelType.compareTo("Perimeter") == 0)){
                                                intent = new Intent(getBaseContext(), MathShapesActivity.class);
                                            } else if (levelType.compareTo("Algebra") == 0){
                                                intent = new Intent(getBaseContext(), MathAlgebraActivity.class);
                                            } else {
                                                intent = new Intent(getBaseContext(), MathTemplateActivity.class);
                                            }
                                            // Starts the next activity
                                            intent.putExtra("Difficulty", IntentDifficultyTag);
                                            intent.putExtra("Type", IntentTypeTag);
                                            startActivity(intent);
                                        }
                                    }
                                })
                                .setNegativeButton(R.string.backLevel, new DialogInterface.OnClickListener() {
                                    // OnClick Cancel
                                     public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                                });
                        // Create the AlertDialog
                        dialogBox.create();
                        dialogBox.show();
                    }
                });
            }
        }
    }

    // To be implemented later, saves user's level progression
    private void saveLevelsToPrefrences(ArrayList<String> levelData, String file){
        String data = "";
        if (levelData.size() > 0){
            data = levelData.get(0);
        }
        for (int i = 1; i < levelData.size(); i++){
            data += "@" + levelData.get(i);
        }
        SharedPreferences sharedPref = getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("LevelData", data);
        editor.apply();
    }

    // To be implemented later, loads user's level progression when the levels were saved
    private ArrayList<String> loadLevelsFromPrefrences(String file){
        SharedPreferences sharedPref = getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String data = sharedPref.getString("LevelData", null);
        if (data == null) {
            ArrayList<String> levelData = new ArrayList<String>();
            return levelData;
        }
        String dataArray[] = data.split("@");
        ArrayList<String> levelData = new ArrayList(Arrays.asList(dataArray));
        return levelData;
    }

    @Override
    protected void onResume(){
        Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
    }

    @Override
    protected void onStart(){
        Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }

    @Override
    protected void onPause(){
        Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }

    @Override
    protected void onStop(){
        Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }
}