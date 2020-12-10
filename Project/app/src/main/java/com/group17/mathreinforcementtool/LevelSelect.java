package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    protected static final int ACTIVITY_MCQ = 0;
    protected static final int ACTIVITY_USER_INPUT = 1;

    ScrollView layout;
    SharedPreferences darkPreference;
    SharedPreferences fontPreference;
    int size;
    int completed;
    String operator;
    String file;
    private ArrayList<String> levelsData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        // Get the request code (the math activity to select levels for)
        Intent intent = getIntent();
        String requestCode = intent.getStringExtra("requestCode");

        layout = findViewById(R.id.lvlSelect);
        // The current color scheme to use
        colorLocked = ResourcesCompat.getColor(getResources(), R.color.colorLightLocked, null);
        colorUnattempted = ResourcesCompat.getColor(getResources(), R.color.colorLightUnattempted, null);
        colorCompleted = ResourcesCompat.getColor(getResources(), R.color.colorLightCompleted, null);
        colorInprogress = ResourcesCompat.getColor(getResources(), R.color.colorLightInprogress, null);


        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            // The dark color scheme to use
            colorLocked = ResourcesCompat.getColor(getResources(), R.color.colorDarkLocked, null);
            colorUnattempted = ResourcesCompat.getColor(getResources(), R.color.colorDarkUnattempted, null);
            colorCompleted = ResourcesCompat.getColor(getResources(), R.color.colorDarkCompleted, null);
            colorInprogress = ResourcesCompat.getColor(getResources(), R.color.colorDarkInprogress, null);
        }

        // Clears Saved preferences for testing purposes
        // getSharedPreferences(requestCode, Context.MODE_PRIVATE).edit().clear().apply();

        // Load list of levels for the math activity from shared preferences (completed, uncompleted, unattempted, unavailable)
        levelsData = loadLevelsFromPrefrences(requestCode);
        file = requestCode;
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

        if(fontPreference.getInt("Size", 15) == 15){
            size = 10;
        } else if (fontPreference.getInt("Size", 15) == 20){
            size = 15;
        } else {
            size = 20;
        }

        // Populate layout with n buttons and set their colour, other values and onclick functions
        populateLayout(levelsData);

        // Save level progression

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
        int buttonWidth = (deviceWidth-7*paddingValue)/columns;

        // Makes the grid layout
        GridLayout grid = (GridLayout) findViewById(R.id.gridLayoutLevels);
        grid.removeAllViews();
        grid.setColumnCount(columns);
        grid.setRowCount(rows);
        grid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);

        // Adds padding to each button
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(buttonWidth, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = paddingValue;
        layoutParams.rightMargin = paddingValue;
        layoutParams.topMargin = paddingValue;
        layoutParams.bottomMargin = paddingValue;
        // Creates the button and sets its values for every level data in the array
        for (String data : levelData){
            // Splits the string with /
            String dataArray[] = data.split("/");
            //"{Level Type}/{Level Difficulty}/{Level Completions}/{Button Color}/{Level Description Tag}/{Progress(Times completed out of 10)}/{Locked (0 or 1)}/{Intent Type}/{Intent Difficulty}/{Intent Mode}"
            // Extracts the values
            final String levelType = dataArray[0];
            final String difficulty = dataArray[1];
            final int levelCompletion = Integer.parseInt(dataArray[2]);
            int colorCode = Integer.parseInt(dataArray[3]);
            final String description = dataArray[4];
            final int progress = Integer.parseInt(dataArray[5]);
            int isLocked = Integer.parseInt(dataArray[6]);
            final String IntentTypeTag = dataArray[7];
            final String IntentDifficultyTag = dataArray[8];
            final String IntentModeTag = dataArray[9];
            // Makes the button and sets its values
            Button buttonLevel = new Button(this);
            buttonLevel.setTag(difficulty);
            Log.i(ACTIVITY_NAME, difficulty);
            String str1 = "com.group17.mathreinforcementtool:string/";
            buttonLevel.setText(getResources().getString(getResources().getIdentifier(str1 + difficulty, null, null)));
            buttonLevel.setLayoutParams(layoutParams);
            buttonLevel.setTextSize(size);
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
                        //TextView tvLevelDescription = view.findViewById(R.id.textLevelDescription);
                        Log.i(ACTIVITY_NAME, "Level type selected: " + levelType);
                        Log.i(ACTIVITY_NAME, "Level difficulty selected: " + difficulty);
                        Log.i(ACTIVITY_NAME, "Level Description: " + description);
                        // Part of the string to get resource with, has to be done this way since Im creating the buttons dynamically
                        String str = "com.group17.mathreinforcementtool:string/";
                        // Gets the strings to be displayed in the dialog box
                        String levelTypeOut = getResources().getString(R.string.textActivityType) + " " + getResources().getString(getResources().getIdentifier(str + levelType, null, null));
                        String levelDiffOut = getResources().getString(R.string.textLevelDifficulty) + " " + getResources().getString(getResources().getIdentifier(str + difficulty, null, null));
                        //String levelDescOut = getResources().getString(R.string.textLevelDesc) + " " + getResources().getString(getResources().getIdentifier(str + description, null, null));
                        // Sets the strings values
                        tvLevelType.setText(levelTypeOut);
                        tvLevelDifficulty.setText(levelDiffOut);
                        //tvLevelDescription.setText(levelDescOut);
                        // Creates the dialog box
                        dialogBox.setView(view)
                                .setPositiveButton(R.string.startLevel, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // OnClick Start
                                        Log.i(ACTIVITY_NAME, "Pressed Start");
                                        Intent intent;
                                        // If the level type is MCQ (the intent inputs are different between this and the other activities)
                                        if (levelType.indexOf("MC") != -1){
                                            intent = new Intent(getBaseContext(), MultipleChoiceActivity.class);
                                            intent.putExtra("Difficulty", Integer.parseInt(IntentDifficultyTag));
                                            intent.putExtra("Type", Integer.parseInt(IntentTypeTag));
                                            intent.putExtra("Mode", Integer.parseInt(IntentModeTag));
                                            startActivityForResult(intent, ACTIVITY_MCQ);
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
                                            startActivityForResult(intent, ACTIVITY_USER_INPUT);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Result from MCQ Activity
            if (requestCode == ACTIVITY_MCQ) {

            }
            // Result from User Input Activity
            else if (requestCode == ACTIVITY_USER_INPUT) {
                double percentCompleted = data.getDoubleExtra("PercentCorrect", 0);
                //saveLevelsToPrefrences(percentCompleted);
                //saveLevelsToPrefrences(1);
            }
        }
    }

    // To be implemented later, saves user's level progression
    private void saveLevelsToPrefrences(double completed){
        Log.i(ACTIVITY_NAME, "Saving levels");
        String data = "";
        if (levelsData.size() > 0){
            //"{Level Type}/{Level Difficulty}/{Level Completions}/{Button Color}/{Level Description Tag}/{Progress(Times completed out of 10)}/{Locked (0 or 1)}/{Intent Type}/{Intent Difficulty}/{Intent Mode}"
            if (completed > 0) {
                for (String data2 : levelsData) {
                    Log.i(ACTIVITY_NAME, data2);
                    String dataArray[] = data2.split("/");
                    dataArray[2] = Integer.toString(Integer.parseInt(dataArray[2]) + 1);
                    dataArray[5] = Integer.toString(Integer.parseInt(dataArray[5]) + 1);
                    dataArray[3] = Integer.toString(COLOR_INPROGRESS);
                    if (Integer.parseInt(dataArray[5]) > 9){
                        dataArray[3] = Integer.toString(COLOR_COMPLETED);
                    }
                    Log.i(ACTIVITY_NAME, data2);
                }
            }
            data = levelsData.get(0);
        }
        for (int i = 1; i < levelsData.size(); i++){
            data += "@" + levelsData.get(i);
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

    // Creates default data array for each level
    // Currently has random numbers and colours for testing purposes
    private ArrayList<String> generateSharedPrefs(String requestCode){
        Log.i(ACTIVITY_NAME, "Generating Default Levels");
        ArrayList<String> levelData = new ArrayList<String>();
        //"{Level Type}/{Level Difficulty}/{Level Completions}/{Button Color}/{Level Description Tag}/{Progress(Times completed out of 10)}/{Locked (0 or 1)}/{Intent Type}/{Intent Difficulty}/{Intent Mode}"
        if (requestCode.indexOf("MC") == -1){
            // If were making a file for Addition
            if (requestCode.equals("Addition")){
                levelData.add("Addition" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AdditionEasyDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Addition" + "/" + "Easy" + "/" + "0");
                levelData.add("Addition" + "/" + "Medium" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AdditionMediumDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Addition" + "/" + "Medium" + "/" + "0");
                levelData.add("Addition" + "/" + "Hard" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AdditionHardDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Addition" + "/" + "Hard" + "/" + "0");
                // If were making a file for Subtraction
            } else if (requestCode.equals("Subtraction")) {
                levelData.add("Subtraction" + "/" + "Easy" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "SubtractionEasyDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Subtraction" + "/" + "Easy" + "/" + "0");
                levelData.add("Subtraction" + "/" + "Medium" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "SubtractionMediumDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Subtraction" + "/" + "Medium" + "/" + "0");
                levelData.add("Subtraction" + "/" + "Hard" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "SubtractionHardDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Subtraction" + "/" + "Hard" + "/" + "0");
                // If were making a file for Multiplication
            } else if (requestCode.equals("Multiplication")) {
                levelData.add("Multiplication" + "/" + "Easy" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MultiplicationEasyDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Multiplication" + "/" + "Easy" + "/" + "0");
                levelData.add("Multiplication" + "/" + "Medium" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MultiplicationMediumDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Multiplication" + "/" + "Medium" + "/" + "0");
                levelData.add("Multiplication" + "/" + "Hard" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MultiplicationHardDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "Multiplication" + "/" + "Hard" + "/" + "0");
                // If were making a file for Division
            } else if (requestCode.equals("Division")) {
                levelData.add("Division" + "/" + "Easy" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "DivisionEasyDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Division" + "/" + "Easy" + "/" + "0");
                levelData.add("Division" + "/" + "Medium" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "DivisionMediumDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Division" + "/" + "Medium" + "/" + "0");
                levelData.add("Division" + "/" + "Hard" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "DivisionHardDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "Division" + "/" + "Hard" + "/" + "0");
                // If were making a file for Perimeter
            } else if (requestCode.equals("Perimeter")) {
                levelData.add("Perimeter" + "/" + "Easy" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "PerimeterEasyDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Perimeter" + "/" + "Easy" + "/" + "0");
                levelData.add("Perimeter" + "/" + "Medium" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "PerimeterMediumDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Perimeter" + "/" + "Medium" + "/" + "0");
                levelData.add("Perimeter" + "/" + "Hard" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "PerimeterHardDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "Perimeter" + "/" + "Hard" + "/" + "0");
                // If were making a file for Area
            } else if (requestCode.equals("Area")) {
                levelData.add("Area" + "/" + "Easy" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AreaEasyDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Area" + "/" + "Easy" + "/" + "0");
                levelData.add("Area" + "/" + "Medium" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AreaMediumDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Area" + "/" + "Medium" + "/" + "0");
                levelData.add("Area" + "/" + "Hard" + "/" + Integer.toString(0)  + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AreaHardDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "Area" + "/" + "Hard" + "/" + "0");
                // If were making a file for Algebra
            } else if (requestCode.equals("Algebra")) {
                levelData.add("Algebra" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AlgebraEasyDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Algebra" + "/" + "Easy" + "/" + "0");
                levelData.add("Algebra" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AlgebraMediumDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "Algebra" + "/" + "Medium" + "/" + "0");
                levelData.add("Algebra" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "AlgebraHardDescription" + "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "Algebra" + "/" + "Hard" + "/" + "0");
            }
        }
        //"{Level Type}/{Level Difficulty}/{Level Completions}/{Button Color}/{Level Description Tag}/{Progress(Times completed out of 10)}/{Locked (0 or 1)}/{Intent Type}/{Intent Difficulty}/{Intent Mode}"
        else {
            // If were making a file for MCQ
            if (requestCode.equals("MCStandardAddition")) {
                levelData.add("MCStandardAddition" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardAdditionEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "0" + "/" + "0" + "/" + "0");
                levelData.add("MCStandardAddition" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardAdditionMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "0" + "/" + "1" + "/" + "0");
                levelData.add("MCStandardAddition" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardAdditionHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "0" + "/" + "2" + "/" + "0");
            } else if (requestCode.equals("MCTimerAddition")) {
                levelData.add("MCTimerAddition" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerAdditionEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "0" + "/" + "0" + "/" + "2");
                levelData.add("MCTimerAddition" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerAdditionMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "0" + "/" + "1" + "/" + "2");
                levelData.add("MCTimerAddition" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerAdditionHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "0" + "/" + "2" + "/" + "2");
            } else if (requestCode.equals("MCStreakAddition")) {
                levelData.add("MCStreakAddition" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakAdditionEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "0" + "/" + "0" + "/" + "1");
                levelData.add("MCStreakAddition" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakAdditionMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "0" + "/" + "1" + "/" + "1");
                levelData.add("MCStreakAddition" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakAdditionHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "0" + "/" + "2" + "/" + "1");
            } else if (requestCode.equals("MCStandardSubtraction")) {
                levelData.add("MCStandardSubtraction" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardSubtractionEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "1" + "/" + "0" + "/" + "0");
                levelData.add("MCStandardSubtraction" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardSubtractionMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "1" + "/" + "1" + "/" + "0");
                levelData.add("MCStandardSubtraction" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardSubtractionHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "1" + "/" + "2" + "/" + "0");
            } else if (requestCode.equals("MCTimerSubtraction")) {
                levelData.add("MCTimerSubtraction" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerSubtractionEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "1" + "/" + "0" + "/" + "2");
                levelData.add("MCTimerSubtraction" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerSubtractionMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "1" + "/" + "1" + "/" + "2");
                levelData.add("MCTimerSubtraction" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerSubtractionHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "1" + "/" + "2" + "/" + "2");
            } else if (requestCode.equals("MCStreakSubtraction")) {
                levelData.add("MCStreakSubtraction" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakSubtractionEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "1" + "/" + "0" + "/" + "1");
                levelData.add("MCStreakSubtraction" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakSubtractionMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "1" + "/" + "1" + "/" + "1");
                levelData.add("MCStreakSubtraction" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakSubtractionHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "1" + "/" + "2" + "/" + "1");
            } else if (requestCode.equals("MCStandardMultiplication")) {
                levelData.add("MCStandardMultiplication" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardMultiplicationEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "2" + "/" + "0" + "/" + "0");
                levelData.add("MCStandardMultiplication" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardMultiplicationMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "2" + "/" + "1" + "/" + "0");
                levelData.add("MCStandardMultiplication" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardMultiplicationHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "2" + "/" + "2" + "/" + "0");
            } else if (requestCode.equals("MCTimerMultiplication")) {
                levelData.add("MCTimerMultiplication" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerMultiplicationEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "2" + "/" + "0" + "/" + "2");
                levelData.add("MCTimerMultiplication" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerMultiplicationMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "2" + "/" + "1" + "/" + "2");
                levelData.add("MCTimerMultiplication" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerMultiplicationHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "2" + "/" + "2" + "/" + "2");
            } else if (requestCode.equals("MCStreakMultiplication")) {
                levelData.add("MCStreakMultiplication" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakMultiplicationEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "2" + "/" + "0" + "/" + "1");
                levelData.add("MCStreakMultiplication" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakMultiplicationMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "2" + "/" + "1" + "/" + "1");
                levelData.add("MCStreakMultiplication" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakMultiplicationHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "2" + "/" + "2" + "/" + "1");
            } else if (requestCode.equals("MCStandardDivision")) {
                levelData.add("MCStandardDivision" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardDivisionEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "3" + "/" + "0" + "/" + "0");
                levelData.add("MCStandardDivision" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardDivisionMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "3" + "/" + "1" + "/" + "0");
                levelData.add("MCStandardDivision" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStandardDivisionHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "3" + "/" + "2" + "/" + "0");
            } else if (requestCode.equals("MCTimerDivision")) {
                levelData.add("MCTimerDivision" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerDivisionEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "3" + "/" + "0" + "/" + "2");
                levelData.add("MCTimerDivision" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerDivisionMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "3" + "/" + "1" + "/" + "2");
                levelData.add("MCTimerDivision" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCTimerDivisionHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "3" + "/" + "2" + "/" + "2");
            } else if (requestCode.equals("MCStreakDivision")) {
                levelData.add("MCStreakDivision" + "/" + "Easy" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakDivisionEasyDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "3" + "/" + "0" + "/" + "1");
                levelData.add("MCStreakDivision" + "/" + "Medium" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakDivisionMediumDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(0) + "/" + "3" + "/" + "1" + "/" + "1");
                levelData.add("MCStreakDivision" + "/" + "Hard" + "/" + Integer.toString(0) + "/" + Integer.toString(COLOR_UNATTEMPTED) + "/" + "MCStreakDivisionHardDescription" +  "/" + Integer.toString(0) + "/" + Integer.toString(1) + "/" + "3" + "/" + "2" + "/" + "1");
            }
        }
        // saves the list to a SharedPrefrence file
        saveLevelsToPrefrences(0);
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