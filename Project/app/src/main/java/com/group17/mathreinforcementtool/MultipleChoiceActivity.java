package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class MultipleChoiceActivity extends AppCompatActivity {
    int numQuestions = 0;
    int difficulty = 0;
    int type = 0;
    int mode = 0;
    float number1 = 0f;
    float number2 = 0f;
    float numAnswer = 0f;
    int max = 9;
    int min = 1;
    TextView questionTextView = null;
    TextView questionsCorrectTextView = null;
    TextView streakTextView = null;
    TextView timerTextView = null;
    ProgressBar statusProgressBar = null;
    RadioButton answerRadioButton1 = null;
    RadioButton answerRadioButton2 = null;
    RadioButton answerRadioButton3 = null;
    RadioButton answerRadioButton4 = null;
    int correctCount = 0;
    int smallSize = 15;
    int medSize = 25;
    int largeSize = 35;
    int questionNum = 0;
    int correctAnswerStreak = 0;
    int highestCorrectAnswerStreak = 0;
    boolean timerEnabled = true;
    long totalTimeSeconds = 0;
    boolean areSavedQuestions = false;

    //statistics
    long timeStart = Calendar.getInstance().getTimeInMillis();
    int incorrectAnswerCount = 0;

    //number generator
    Random rand = new Random();

    //Arrays/Lists
    List<RadioButton> radioButtonList = new ArrayList<RadioButton>();
    List<TextView> textViewList = new ArrayList<TextView>();
    ArrayList<String> generatedQuestions = new ArrayList<>();
    ArrayList<String> stuckQuestions = new ArrayList<>();
    ArrayList<Float> generatedQuestionsNumber = new ArrayList<>();
    ArrayList<Float> stuckQuestionsAnswer = new ArrayList<>();
    ArrayList<Integer> stuckQuestionsTracker = new ArrayList<>();

    //Dark mode and font preferences
    SharedPreferences darkPreference;
    SharedPreferences fontPreference;

    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        //get difficult, type of questions (operation) and mode
        difficulty = getIntent().getIntExtra("Difficulty", 0);
        numQuestions = numberOfQuestionsBasedOnDifficulty(difficulty);
        type = getIntent().getIntExtra("Type", 0);
        mode = getIntent().getIntExtra("Mode", 0);

        //find UI elements
        questionTextView = findViewById(R.id.questionTextView);
        questionsCorrectTextView = findViewById(R.id.questionsCorectTextView);
        streakTextView = findViewById(R.id.streakTextView);
        timerTextView = findViewById(R.id.timerTextView);
        statusProgressBar = findViewById(R.id.statusProgressBar);
        answerRadioButton1 = findViewById(R.id.answerRadioButton1);
        answerRadioButton2 = findViewById(R.id.answerRadioButton2);
        answerRadioButton3 = findViewById(R.id.answerRadioButton3);
        answerRadioButton4 = findViewById(R.id.answerRadioButton4);
        radioButtonList.addAll((Collection<? extends RadioButton>) Arrays.asList(answerRadioButton1,answerRadioButton2,answerRadioButton3,answerRadioButton4));
        textViewList.addAll((Collection<? extends TextView>) Arrays.asList(questionTextView,timerTextView,questionsCorrectTextView,questionTextView, streakTextView));
        layout = findViewById(R.id.multiChoiceaActivity);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        //standard mode- set streak and timer to invisible and check for stuck questions
        //combo mode- questions are from all 4 operators
        //if there are no previous questions then generate a new starting question while new ones are generated in background
        if(areSavedQuestions == false) {
            //generate a starting question (so not calling to update text multiple times)
            generateQuestions(1);
        }
        //updateText's
        updateTexts();

        //standard mode-set streaka nd timer to invisible
        if(mode == 0){
            streakTextView.setVisibility(View.INVISIBLE);
            timerTextView.setVisibility(View.INVISIBLE);

            //check for any questions previously stuck on and add to questions array before generating more questions for the array
            areSavedQuestions = checkForSavedQuestions();
        }
        //streak- set timer to invisible
        else if (mode == 1){
            timerTextView.setVisibility(View.INVISIBLE);
        }
        //timer- set streak to invisible
        else if (mode == 2){
            streakTextView.setVisibility(View.INVISIBLE);

            //to show timer initially
            updateTimer();

            //create thread for updating timer
            Thread t = new Thread() {
                @Override
                public void run() {
                    try {
                        while (!isInterrupted()) {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //update timerTextView
                                    updateTimer();
                                    //increment timer
                                    totalTimeSeconds++;
                                }
                            });
                        }
                    } catch (InterruptedException e) {
                    }
                }
            };
            //start thread
            t.start();
        }
        //set progress bar
        statusProgressBar.setProgress(0);
        statusProgressBar.setMax(100);

        //start question generation in background
        new backgroundQuestionGeneration().execute();

        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);

            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
            for(RadioButton r: radioButtonList){
                r.setTextColor(Color.WHITE);
                r.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            }
        } else {
            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
            for(RadioButton r: radioButtonList){
                r.setTextColor(Color.BLACK);
            }
        }

        if(fontPreference.getInt("Size", medSize) == 15){
            for(TextView t: textViewList){
                t.setTextSize(smallSize);
            }
            for(RadioButton r: radioButtonList){
                r.setTextSize(smallSize);
            }
        } else if (fontPreference.getInt("Size", medSize) == 20){
            for(TextView t: textViewList){
                t.setTextSize(medSize);
            }
            for(RadioButton r: radioButtonList){
                r.setTextSize(medSize);
            }

        } else {
            for(TextView t: textViewList){
                t.setTextSize(largeSize);
            }
            for(RadioButton r: radioButtonList){
                r.setTextSize(largeSize);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //only store leftover and stuck questions if in standard mode or combo mode (mode == 1/3)
        if (mode == 0 || mode == 3) {
            try {
                //store leftover and stuck questions + answers in SharedPreferences
                SharedPreferences prefs = getSharedPreferences( "MC" + difficultyToString(difficulty) + typeToString(type) + modeToString(mode), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                //store any leftover questions
                if (generatedQuestions.size() > 0) {
                    editor.putInt("Number of leftover Questions", generatedQuestions.size());
                    for (int i = 0; i < generatedQuestions.size(); i++) {
                        editor.putString("Leftover Question" + i, generatedQuestions.get(i));
                        editor.putFloat("Leftover Answer" + i, generatedQuestionsNumber.get(i));
                    }
                }
                //store any stuck questions
                if (stuckQuestions.size() > 0) {
                    editor.putInt("Number of stuck Questions", stuckQuestions.size());
                    for (int i = 0; i < stuckQuestions.size(); i++) {
                        editor.putString("Stuck Question" + i, stuckQuestions.get(i));
                        editor.putFloat("Stuck Answer" + i, stuckQuestionsAnswer.get(i));
                    }
                }
                //commit the changes
                editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    protected void onResume(){
        super.onResume();
        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
            for(RadioButton r: radioButtonList){
                r.setTextColor(Color.WHITE);
                r.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            }
        } else {
            for(TextView t: textViewList){
                t.setTextColor(Color.BLACK);
            }
            for(RadioButton r: radioButtonList){
                r.setTextColor(Color.BLACK);
                r.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

        if(fontPreference.getInt("Size", medSize) == 15){
            for(TextView t: textViewList){
                t.setTextSize(smallSize);
            }
            for(RadioButton r: radioButtonList){
                r.setTextSize(smallSize);
            }
        } else if (fontPreference.getInt("Size", medSize) == 20){
            for(TextView t: textViewList){
                t.setTextSize(medSize);
            }
            for(RadioButton r: radioButtonList){
                r.setTextSize(medSize);
            }

        } else {
            for(TextView t: textViewList){
                t.setTextSize(largeSize );
            }
            for(RadioButton r: radioButtonList){
                r.setTextSize(largeSize);
            }
        }
    }
    //    This is here to swap to onResume on back so that flicking the "DarkSwitch" actually works without having to close down the app lmao
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
    }

    public void onClick(View view){
        //answer picked (pull from button)
        double answer = 0;

        //answer1
        if(answerRadioButton1.isChecked()){
            answer = Double.valueOf((String) answerRadioButton1.getText());
            answerRadioButton1.setChecked(false);
        }
        //answer2
        else if(answerRadioButton2.isChecked()){
            answer = Double.valueOf((String) answerRadioButton2.getText());
            answerRadioButton2.setChecked(false);
        }
        //answer2
        else if(answerRadioButton3.isChecked()){
            answer = Double.valueOf((String) answerRadioButton3.getText());
            answerRadioButton3.setChecked(false);
        }
        //answer2
        else if(answerRadioButton4.isChecked()){
            answer = Double.valueOf((String) answerRadioButton4.getText());
            answerRadioButton4.setChecked(false);
        }
        //check answer
        boolean isAnswerCorrect = correctAnswer(answer);

        //only increase index if not at the end of ArrayList
        if(questionNum < generatedQuestions.size()){
            //only increment if question wasn't true (prevent jumping index if question is right)
            if(isAnswerCorrect == false) {
                questionNum += 1;
            }
        }
        //push back index if index is out of array
        else{
            questionNum = 0;
        }
        //prevent updating text if activity finished
        if (correctCount < numQuestions) {
            //next question
            updateTexts();
        }
        return;
    }
    //checks if the answer picked matched the actual answer
    public boolean correctAnswer(double answer){
        boolean answerIsCorrect = false;
        CharSequence text;
        int duration= Toast.LENGTH_SHORT;

        //if answer is correct increment counter, check if user was stuck on question and print toast
        if((float)answer == generatedQuestionsNumber.get(questionNum)){
            answerIsCorrect = true;
            correctCount++;
            correctAnswerStreak++;

            //possibly store question
            storeStuckQuestion(generatedQuestions.remove(questionNum), answer);
            generatedQuestionsNumber.remove(questionNum);

            //update highest streak if in streak mode (mode 1)
            if (mode == 1) {
                //update highest streak if current streak is higher
                if (correctAnswerStreak > highestCorrectAnswerStreak){
                    highestCorrectAnswerStreak = correctAnswerStreak;
                }
            }
            //text for toast message
            text = "Correct!";
        }
        //answer is wrong so add to incorrect counter
        else{
            text = "Incorrect";
            Log.i("MC", "correctAnswer:" + answer + "!=" + generatedQuestionsNumber.get(questionNum));

            //only track stuck questions if in standard mode (mode 0)
            if (mode == 0) {
                trackStuckQuestions(questionNum);
            }
            //increment number of incorrect answers
            incorrectAnswerCount++;

            //reset streak if in streak mode (mode 1)
            if (mode == 1) {
                correctAnswerStreak = 0;
            }
        }
        //make and show toast message
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

        //disable timer if it was enabled then proceed to close activity
        if (correctCount >= numQuestions){
            if(mode == 2) {
                timerEnabled = false;
            }
            exitActivity();
        }
        return answerIsCorrect;
    }
    //update UI elements
    public void updateTexts(){
        //reset text in all button's
        questionTextView.setText("");
        answerRadioButton1.setText("");
        answerRadioButton2.setText("");
        answerRadioButton3.setText("");
        answerRadioButton4.setText("");

        //update correct questions answered and progress bar
        questionsCorrectTextView.setText(Integer.toString(correctCount) + "/" + Integer.toString(numQuestions));
        statusProgressBar.setProgress(0);
        statusProgressBar.setProgress(correctCount * 100 / numQuestions);

        //update streak text if in streak mode (mode 1)
        if (mode == 1) {
            streakTextView.setText("Streak: " + Integer.toString(correctAnswerStreak));
        }
        try {
            //set question text
            questionTextView.setText(generatedQuestions.get(questionNum));

            //print answer to question for testing
            Log.i("MC", "Answer=" + generatedQuestionsNumber.get(questionNum));
            int answerSpot;

            //if the number is whole then print number as integer (no decimal)
            if (generatedQuestionsNumber.get(questionNum) % 1 == 0) {
                answerSpot = pickAnswerSpot(String.format("%d", Math.round(generatedQuestionsNumber.get(questionNum))));
            }
            //number is a decimal so print as decimal number (up to 3 decimal points)
            else {
                answerSpot = pickAnswerSpot(String.format("%.3f", round(generatedQuestionsNumber.get(questionNum), 3)));
            }
            ArrayList<Double> generatedAnswers = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                //if spot isn't the spot the answer is in
                if (i != answerSpot) {
                    String answerText;

                    //generate random answer for spot
                    double answer = (generateRandomAnswer(generatedAnswers, generatedQuestionsNumber.get(questionNum)));

                    //add to array to prevent answer duplication
                    generatedAnswers.add(answer);

                    //if the number is whole then print number as integer (no decimal)
                    if (answer % 1 == 0) {
                        answerText = String.format("%d", Math.round(answer));
                    }
                    //number is a decimal so print as decimal number (up to 3 decimal points)
                    else {
                        answerText = String.format("%.3f", answer);
                    }
                    //set text for button
                    if (i == 0) {
                        answerRadioButton1.setText(answerText);
                    } else if (i == 1) {
                        answerRadioButton2.setText(answerText);
                    } else if (i == 2) {
                        answerRadioButton3.setText(answerText);
                    } else {
                        answerRadioButton4.setText(answerText);
                    }
                }
            }
        }
        //reset index if for some reason current index is out of range
        catch (Exception e) {
            questionNum = 0;
            updateTexts();
        }
    }
    //set the max and min values for generating questions and answers
    public void setMaxMinValues(){
        //easy difficulty
        if(difficulty == 0){
            //addition or subtraction
            if(type < 2 ) {
                max = 9;
            }
            //multiplication or division
            else{
                max = 4;
            }
            min = 1;
        }
        //medium difficulty
        if(difficulty == 1){
            //addition or subtraction
            if(type < 2){
                max = 99;
                min = 10;
            }
            //multiplication or division
            else{
                max = 6;
                min = 1;
            }
        }
        //hard difficulty
        if(difficulty == 2){
            //addition or subtraction
            if(type < 2){
                max = 999;
                min = 100;
            }
            //multiplication or division
            else{
                max = 8;
                min = 1;
            }
        }
        return;
    }
    //randomly pick a spot for the correct answer
    public int pickAnswerSpot(String answer){
        //pick random spot for the answer
        int answerSpot = rand.nextInt((3 - 0) + 0);
        switch (answerSpot) {
            case 0:
                answerRadioButton1.setText(answer);
                break;
            case 1:
                answerRadioButton2.setText(answer);
                break;
            case 2:
                answerRadioButton3.setText(answer);
                break;
            case 3:
                answerRadioButton4.setText(answer);
                break;
        }
        return answerSpot;
    }
    //generate questions based on operator that are not infinite, NaN, or already generated
    public void generateQuestions(int numberOfQuestionsToGenerate) {
        //set values
        setMaxMinValues();

        //loop until the number of questions required is met
        while (numberOfQuestionsToGenerate > 0 && generatedQuestions.size() < numQuestions) {
            //prevent answer from being repeated, 0, infinite or NaN
            while (Float.isInfinite(numAnswer) == true || Float.isNaN(numAnswer) == true || numAnswer == 0 || generatedQuestionsNumber.contains((float)(round(numAnswer, 3))) == true) {
                //pick 2 random numbers from the range
                number1 = (float) rand.nextInt((max - min) + min);
                number2 = (float) rand.nextInt((max - min) + min);

                //addition
                if(type == 0){
                    numAnswer = number1 + number2;
                }
                //subtraction
                else if(type == 1){
                    numAnswer = number1 - number2;
                }
                //multiplication
                else if(type == 2){
                    numAnswer = number1 * number2;
                }
                //division
                else{
                    numAnswer = number1 / number2;
                }
            }
            generatedQuestionsNumber.add((float) round(numAnswer, 3));
            //addition question
            if (type == 0) {
                //set text for question
                generatedQuestions.add((getString(R.string.questionStart) + String.format(" %d + %d", Math.round(number1), Math.round(number2)) + getString(R.string.questionEnd)));
            }
            //subtraction question
            else if (type == 1) {
                generatedQuestions.add((getString(R.string.questionStart) + String.format(" %d - %d", Math.round(number1), Math.round(number2)) + getString(R.string.questionEnd)));
            }
            //multiplication question
            else if (type == 2) {
                generatedQuestions.add((getString(R.string.questionStart) + String.format(" %d * %d", Math.round(number1), Math.round(number2)) + getString(R.string.questionEnd)));
            }
            //division question
            else if (type == 3) {
                generatedQuestions.add((getString(R.string.questionStart) + String.format(" %d / %d", Math.round(number1), Math.round(number2)) + getString(R.string.questionEnd)));
            }
            //decrease count
            numberOfQuestionsToGenerate--;
        }
        return;
    }
    //generate random answer for slot
    public double generateRandomAnswer(ArrayList<Double> generatedAnswers, double answer) {
        double randomAnswer;
        boolean shouldBeNegative = false;
        boolean shouldBePositive = false;
        //ensures answer is negative
        if(answer < 0){
            shouldBeNegative = true;
        }
        //ensures answer is positive
        else if (answer >= 0){
            shouldBePositive = true;
        }
        //addition
        if (type == 0) {
            randomAnswer = (((double) rand.nextInt((max - min) + min)) + ((double) rand.nextInt((max - min) + min)));
        }
        //subtraction
        else if (type == 1) {
            randomAnswer = (((double) rand.nextInt((max - min) + min)) - ((double) rand.nextInt((max - min) + min)));
        }
        //multiplication
        else if (type == 2) {
            randomAnswer = (((double) rand.nextInt((max - min) + min)) * ((double) rand.nextInt((max - min) + min)));
        }
        //division
        else {
            randomAnswer = (((double) rand.nextInt((max - min) + min)) / ((double) rand.nextInt((max - min) + min)));
        }
        //keep randomizing answer until it doesn't match the answer, isn't an already generated answer, isn't infinite or NaN. Also loop until randomAnswer is negative/positive so that it matches the answer
        while (Double.isInfinite(randomAnswer) == true || Double.isNaN(randomAnswer) == true|| round(randomAnswer, 3) == answer ||generatedAnswers.contains((float)(round(randomAnswer, 3))) == true || (randomAnswer > 0 && shouldBeNegative) || (randomAnswer < 0 && shouldBePositive)){
            //addition
            if (type == 0) {
                randomAnswer = (((double) rand.nextInt((max - min) + min)) + ((double) rand.nextInt((max - min) + min)));
            }
            //subtraction
            else if (type == 1) {
                randomAnswer = (((double) rand.nextInt((max - min) + min)) - ((double) rand.nextInt((max - min) + min)));
            }
            //multiplication
            else if (type == 2) {
                randomAnswer = (((double) rand.nextInt((max - min) + min)) * ((double) rand.nextInt((max - min) + min)));
            }
            //division
            else {
                randomAnswer = (((double) rand.nextInt((max - min) + min)) / ((double) rand.nextInt((max - min) + min)));
            }
        }
        return round(randomAnswer, 3);
    }

    //used to round answers
    private static double round(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    //save any data then close activity
    private void exitActivity(){
        //only get final time if not in timer mode
        if(mode != 2) {
            //get total time in activity
            long timeEnd = Calendar.getInstance().getTimeInMillis();
            totalTimeSeconds = (timeEnd - timeStart) / 1000;
        }
        Log.i("MC", "exitActivity called");
        SharedPreferences prefs = getSharedPreferences("MCStats" + difficultyToString(difficulty) + typeToString(type) + modeToString(mode), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //add operation and difficulty to intent
        editor.putString("Operation", typeToString(type));
        editor.putString("Difficulty", difficultyToString(difficulty));

        //add time elapsed since starting the activity (in seconds) to intent
        editor.putString("TotalTimeSeconds", Long.toString(totalTimeSeconds));

        //add number of times the user answered with a wrong answer
        editor.putString("CorrectAnswerCount", Integer.toString(correctCount));

        //add number of times the user answered with a wrong answer
        editor.putString("IncorrectAnswerCount", Integer.toString(incorrectAnswerCount));

        //highest streak of correct answers the user had
        editor.putString("HighestStreak", Integer.toString(highestCorrectAnswerStreak));

        editor.commit();

        //set result and finish activity
        setResult(RESULT_OK);
        finish();
    }
    //used to generate remaining questions
    private class backgroundQuestionGeneration extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            checkForSavedQuestions();
            //generate questions normally if not in combo mode
            if(mode != 3) {
                //generate remaining questions
                generateQuestions(numQuestions);
            }
            //combo mode question generation
            else{
                //determine min number of questions needed for each operation (ensure at least x amount of each operator)
                int questionPerOperation = numQuestions / 4;
                //store value of type before swapping it (needed for saving questions)
                int typeBeforeSwapping = type;

                //until array has number of questions req
                while (generatedQuestions.size() < numQuestions) {
                    //generate questions
                    generateQuestions(questionPerOperation);

                    //only increment the operator up to division
                    if(type + 1 < 4) {
                        type++;
                    }
                    //reset type to addition if on division
                    else{
                        type = 0;
                    }
                }
                //set type back to original value
                type = typeBeforeSwapping;
            }
            //close thread
            this.cancel(true);
            return null;
        }
    }
    //checks for stuck questions in shared preferences add adds them to the questions array before generating new ones and adding those
    public boolean checkForSavedQuestions(){
        boolean areSavedQuestions = false;
        //get sharedPreferences file
        SharedPreferences prefs = getSharedPreferences("MC" + difficultyToString(difficulty) + typeToString(type) + modeToString(mode), Context.MODE_PRIVATE);
        try
        {
            //get number of leftover questions to pull (how many where stored)
            int numQuestionsToLoad = prefs.getInt("Number of leftover Questions", 0);

            //pull the leftover questions and their answers and place into the questions and answers arrays
            for(int i=0;i<numQuestionsToLoad;i++) {
                String question = prefs.getString("Leftover Question" + i, "");
                if(question.compareTo("") != 0) {
                    if(areSavedQuestions == false){
                        areSavedQuestions =  true;
                    }
                    generatedQuestions.add(question);
                    generatedQuestionsNumber.add(prefs.getFloat("Leftover Answer" + i, 0));
                }
            }
            //get number of stuck questions to pull (how many where stored)
            numQuestionsToLoad = prefs.getInt("Number of stuck Questions", 0);

            //pull the questions and their answers and place into the questions and answers arrays
            for(int i=0;i<numQuestionsToLoad;i++) {
                String question = prefs.getString("Leftover Question" + i, "");
                if(question.compareTo("") != 0) {
                    if(areSavedQuestions == false){
                        areSavedQuestions =  true;
                    }
                    generatedQuestions.add(prefs.getString("Stuck Question" + i, ""));
                    generatedQuestionsNumber.add(prefs.getFloat("Stuck Answer" + i, 0));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        //clear data and commit the changes
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
        return areSavedQuestions;
    }
    //puts value in ArrayList to indicate how many times they got the answer wrong (3 or more the question will be saved)
    private void trackStuckQuestions(int questionIndex){
        //add integer if not in array
        if(stuckQuestionsTracker.size() - 1< questionIndex){
            stuckQuestionsTracker.add(1);
        }
        //update integer in array if already exists
        else {
            int num = stuckQuestionsTracker.get(questionIndex);
            stuckQuestionsTracker.set(questionIndex, num + 1);
        }
    }
    //store stuck questions in ArrayList's to be stored later (called when question is removed from array)
    private void storeStuckQuestion(String question, double answer){
        //if the question was being tracked and it has had 3 or more incorrect answers
        if(stuckQuestionsTracker.size() > questionNum && stuckQuestionsTracker.get(questionNum) >=3){
            stuckQuestions.add(question);
            stuckQuestionsAnswer.add((float)answer);
        }
    }
    //convert type to string for saving + stats
    private String typeToString(int type){
        String convertStr = "";
        //addition
        if(type == 0){
            convertStr = "Addition";
        }
        //subtraction
        else if(type == 1){
            convertStr = "Subtraction";
        }
        //multiplication
        else if(type == 2){
            convertStr = "Multiplication";
        }
        //division
        else if(type == 3){
            convertStr = "Division";
        }
        return convertStr;
    }
    //convert mode to string for saving
    private String modeToString(int mode){
        String convertStr = "";
        //standard
        if(mode == 0){
            convertStr = "Standard";
        }
        //Streak
        else if(mode == 1){
            convertStr = "Streak";
        }
        //timer
        else if(mode == 2){
            convertStr = "Timer";
        }
        return convertStr;
    }
    //convert difficulty to string for saving + stats
    private String difficultyToString(int difficulty){
        String convertStr = "";
        //easy
        if(difficulty == 0){
            convertStr = "Easy";
        }
        //medium
        else if(mode == 1){
            convertStr = "Medium";
        }
        //hard
        else if(mode == 2){
            convertStr = "hard";
        }
        return convertStr;
    }
    private int numberOfQuestionsBasedOnDifficulty(int difficulty){
        int tempNum = 0;

        //easy
        if (difficulty == 0){
            tempNum = 10;
        }
        //medium
        else if (difficulty == 1){
            tempNum = 15;
        }
        //hard
        else if (difficulty == 2){
            tempNum = 20;
        }
        return tempNum;
    }
    private void updateTimer(){
        //get total time in activity

        //update timer's text
        timerTextView.setText("Time: " + totalTimeSeconds + "s");
    }
}