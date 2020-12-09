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
    double number1 = 0f;
    double number2 = 0f;
    double numAnswer = 0f;
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

    //statistics
    long timeStart = Calendar.getInstance().getTimeInMillis();
    String operationString = "";
    String difficultyString = "";
    int incorrectAnswerCount = 0;

    //number generator
    Random rand = new Random();

    //Arrays/Lists
    List<RadioButton> radioButtonList = new ArrayList<RadioButton>();
    List<TextView> textViewList = new ArrayList<TextView>();
    ArrayList<String> generatedQuestions = new ArrayList<>();
    ArrayList<String> stuckQuestions = new ArrayList<>();
    ArrayList<Float> stuckQuestionsAnswer = new ArrayList<>();
    ArrayList<Integer> stuckQuestionsTracker = new ArrayList<>();
    ArrayList<Double> generatedQuestionsNumber = new ArrayList<>();

    //Dark mode and font preferences
    SharedPreferences darkPreference;
    SharedPreferences fontPreference;

    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        //get difficult, type of questions (operation) and mode

        numQuestions = getIntent().getIntExtra("numQuestions", 10);
        difficulty = getIntent().getIntExtra("Difficulty", 0);
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

        //standard mode- set streak and timer to invisible
        if(mode == 0){
            streakTextView.setVisibility(View.INVISIBLE);
            timerTextView.setVisibility(View.INVISIBLE);
            //check for any questions previously stuck on and add before generating more questions
            checkForStuckQuestions();
        }
        //streak- set timer to invisible
        else if (mode == 1){
            timerTextView.setVisibility(View.INVISIBLE);
        }
        //timer- set streak to invisible
        else{
            streakTextView.setVisibility(View.INVISIBLE);
            new timerTextUpdate().execute();
        }
        //set progress bar
        statusProgressBar.setProgress(0);
        statusProgressBar.setMax(100);

        //generate 5 more questions then update text (create other questions in background)
        new backgroundQuestionGeneration().execute();
        //generateQuestions(5);
        updateTexts();

        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);

//            questionTextView.setTextColor(Color.WHITE);
//            timerTextView.setTextColor(Color.WHITE);
//            questionsCorrectTextView.setTextColor(Color.WHITE);
//            streakTextView.setTextColor(Color.WHITE);

            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
            for(RadioButton r: radioButtonList){
                r.setTextColor(Color.WHITE);
                r.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            }
        } else {
//            layout.setBackgroundColor(Color.WHITE);
//            questionTextView.setTextColor(Color.BLACK);
//            timerTextView.setTextColor(Color.BLACK);
//            questionsCorrectTextView.setTextColor(Color.BLACK);
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
        double answer = 0f;

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
        correctAnswer(answer);

        //only increase index if not at the end of ArrayList
        if(questionNum < generatedQuestions.size()){
            //only increment if question wasn't true (prevent jumping index)
            questionNum += 1;

        }
        //push back index
        else{
            questionNum = 0;
        }
        //prevent updating text is activity is finished
        if (correctCount < numQuestions) {
            //next question
            updateTexts();
        }
        return;
    }
    public void correctAnswer(double answer){
        CharSequence text;
        int duration= Toast.LENGTH_SHORT;

        //if answer is correct increment counter and print toast
        if(answer == generatedQuestionsNumber.get(questionNum)){
            //possibly store question is
            storeStuckQuestion(generatedQuestions.remove(questionNum), answer);
            generatedQuestionsNumber.remove(questionNum);
            correctCount++;
            correctAnswerStreak++;

            //update highest streak if current streak is higher
            if (correctAnswerStreak > highestCorrectAnswerStreak){
                highestCorrectAnswerStreak = correctAnswerStreak;
            }
            text = "Correct!";
        }
        //answer is wrong so add to incorrect counter
        else{
            text = "Incorrect";

            //only track stuck questions if in standard mode
            if (mode == 0) {
                trackStuckQuestions(questionNum);
            }

            Log.i("MC", "Answer= " + answer + " != " + generatedQuestionsNumber.get(questionNum));
            incorrectAnswerCount++;
            correctAnswerStreak = 0;
        }
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

        //disable timer if enabled then close activity
        if (correctCount >= numQuestions){
            if(mode == 2) {
                timerEnabled = false;
            }
            exitActivity();
        }
        return;
    }

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

        //streak mode- update text
        if(mode == 1){
            streakTextView.setText("Streak: " + Integer.toString(correctAnswerStreak));
        }
        try {
            Log.i("MC", "Attempting to set questionTextView, questionNum= " + questionNum + ", the generatedQuestions.size()= " + generatedQuestions.size());
            questionTextView.setText(generatedQuestions.get(questionNum));
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
                if (i != answerSpot) {
                    String answerText;

                    double answer = (generateRandomAnswer(generatedAnswers, generatedQuestionsNumber.get(questionNum)));
                    generatedAnswers.add(answer);

                    //if the number is whole then print number as integer (no decimal)
                    if (answer % 1 == 0) {
                        answerText = String.format("%d", Math.round(answer));
                    }
                    //number is a decimal so print as decimal number (up to 3 decimal points)
                    else {
                        answerText = String.format("%.3f", answer);
                    }
                    //set buttons text
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
        }catch (Exception e){
            questionNum = 0;
            updateTexts();
        }
    }
    public void setMaxMinValues(){
        //easy difficulty
        if(difficulty == 0){
            //set difficulty string
            difficultyString = "Easy";

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
            //set difficulty string
            difficultyString = "Medium";

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
            //set difficulty string
            difficultyString = "Hard";
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
    public int pickAnswerSpot(String answer){
        //set the answer to one of the radio buttons
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
            while (Double.isInfinite(numAnswer) == true || Double.isNaN(numAnswer) == true || numAnswer == 0 || generatedQuestionsNumber.contains(round(numAnswer, 3)) == true) {
                //pick 2 random numbers from the range
                number1 = (float) rand.nextInt((max - min) + min);
                number2 = (float) rand.nextInt((max - min) + min);

                //addition
                if(type == 0){
                    numAnswer = number1 + number2;
                    if(operationString.compareTo("") == 0){
                        operationString = "Addition";
                    }
                }
                //subtraction
                else if(type == 1){
                    numAnswer = number1 - number2;
                    if(operationString.compareTo("") == 0){
                        operationString = "Division";
                    }
                }
                //multiplication
                else if(type == 2){
                    numAnswer = number1 * number2;
                    if(operationString.compareTo("") == 0){
                        operationString = "Multiplication";
                    }
                }
                //division
                else{
                    numAnswer = number1 / number2;
                    if(operationString.compareTo("") == 0){
                        operationString = "/";
                    }
                }
                //decrease count
                numberOfQuestionsToGenerate--;
            }
            generatedQuestionsNumber.add((round(numAnswer, 3)));
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
            numQuestions -= 1;
        }
        return;
    }
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
        //keep randomizing answer until it doesn't match the answer, isn't an already generated answer, isn't infinite or NaN. Also loop until randomAnswer is negative so that it matches the answer
        while (Double.isInfinite(randomAnswer) == true || Double.isNaN(randomAnswer) == true|| round(randomAnswer, 3) == answer ||generatedAnswers.contains(round(randomAnswer, 3)) == true || (randomAnswer > 0 && shouldBeNegative) || (randomAnswer < 0 && shouldBePositive)){
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


    private static double round(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    private void exitActivity(){
        //get total time in activity
        long timeEnd = Calendar.getInstance().getTimeInMillis();
        totalTimeSeconds = (timeEnd - timeStart) / 1000;

        //only store stuck questions if in standard mode
        if (mode == 0) {
            //if there are questions the user was stuck on
            if(stuckQuestions.size() > 0) {
                //store questions and answers in SharedPreferences
                SharedPreferences prefs = getSharedPreferences("MultipleChoiceStuckQuestions", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                try {
                    editor.putInt("Number of Questions", stuckQuestions.size());
                    for (int i = 0; i < stuckQuestions.size(); i++) {
                        editor.putString("Question" + i, stuckQuestions.get(i));
                        editor.putFloat("Answer" + i, stuckQuestionsAnswer.get(i));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //commit the changes
                editor.commit();
            }
        }

        Intent stats = new Intent();
        //The operation and difficulty
        stats.putExtra("Operation", operationString);
        stats.putExtra("Difficulty", difficultyString);

        //The time elapsed since starting the activity, in seconds
        stats.putExtra("TotalTimeSeconds", Long.toString(totalTimeSeconds));

        //Number of times the user answered with a wrong answer
        stats.putExtra("IncorrectAnswerCount", Integer.toString(incorrectAnswerCount));

        //streak
        if(mode == 1){
            //highest streak of correct answers the user had
            stats.putExtra("HighestStreak", Integer.toString(highestCorrectAnswerStreak));
        }
        setResult(RESULT_OK, stats);
        finish();
    }
    //used to update timerTextView
    private class timerTextUpdate extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            //only run timer is enabled
            while (timerEnabled) {
                long timeEnd = Calendar.getInstance().getTimeInMillis();
                totalTimeSeconds = (timeEnd - timeStart) / 1000;
                timerTextView.setText("Time: " + Long.toString(totalTimeSeconds) + "s");
            }
            //close
            this.cancel(true);
            return null;
        }
    }
    //used to generate remaining questions
    private class backgroundQuestionGeneration extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            //generate remaining questions
            generateQuestions(numQuestions);
            //close
            this.cancel(true);
            return null;
        }
    }
    //runs before generated questions so questions are added
    public void checkForStuckQuestions(){
        //get sharedPreferences file
        SharedPreferences prefs = getSharedPreferences("MultipleChoiceStuckQuestions", Context.MODE_PRIVATE);
        try
        {
            //get number of questions to pull (how many where stored)
            int numStuckQuestions = prefs.getInt("Number of Questions", 0);

            //pull the questions and their answers and place into the questions and answers arrays
            for(int i=0;i<numStuckQuestions;i++) {
                generatedQuestions.add(prefs.getString("Question" + i, ""));
                generatedQuestionsNumber.add((double) prefs.getFloat("Answer" + i, 0));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        //clear data and commit the changes
        SharedPreferences.Editor editor =prefs.edit();
        editor.clear();
        editor.commit();
    }
    //puts value in ArrayList to indicate how many times they got the answer wrong (3 or more the question will be saved)
    public void trackStuckQuestions(int questionIndex){
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
    //store stuck questions in ArrayList's to be stored later
    public void storeStuckQuestion(String question, double answer){
        //if the question was being tracked and it has had 3 or more incorrect answers
        if(stuckQuestionsTracker.size() > questionNum && stuckQuestionsTracker.get(questionNum) >=3){
            stuckQuestions.add(question);
            stuckQuestionsAnswer.add((float)answer);
        }
    }
}