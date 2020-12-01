package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;

public class multipleChoice extends AppCompatActivity {
    int difficulty = 0;
    int type = 0;
    float number1 = 0f;
    float number2 = 0f;
    float numAnswer = 0f;
    int max = 9;
    int min = 1;
    TextView questionTextView = null;
    TextView questionsCorrectTextView = null;
    ProgressBar statusProgressBar = null;
    RadioButton answerRadioButton1 = null;
    RadioButton answerRadioButton2 = null;
    RadioButton answerRadioButton3 = null;
    RadioButton answerRadioButton4 = null;
    int correctCount = 0;
    int smallSize = 15;
    int medSize = 25;
    int largeSize = 35;

    //number generator
    Random rand = new Random();

    List<RadioButton> radioButtonList = new ArrayList<RadioButton>();
    ArrayList<String> generatedQuestions = new ArrayList<>();
    ArrayList<Float> generatedQuestionsNumber = new ArrayList<>();
    int questionNum = 0;

    SharedPreferences darkPreference;
    SharedPreferences fontPreference;

    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_choice);

        //get difficult and type of questions to generate
        difficulty = getIntent().getIntExtra("Difficulty", 0);
        type = getIntent().getIntExtra("Type", 0);

        //find UI elements
        questionTextView = findViewById(R.id.questionTextView);
        questionsCorrectTextView = findViewById(R.id.questionsCorectTextView);
        statusProgressBar = findViewById(R.id.statusProgressBar);
        answerRadioButton1 = findViewById(R.id.answerRadioButton1);
        answerRadioButton2 = findViewById(R.id.answerRadioButton2);
        answerRadioButton3 = findViewById(R.id.answerRadioButton3);
        answerRadioButton4 = findViewById(R.id.answerRadioButton4);
        radioButtonList.addAll((Collection<? extends RadioButton>) Arrays.asList(answerRadioButton1,answerRadioButton2,answerRadioButton3,answerRadioButton4));
        layout = findViewById(R.id.multiChoiceaActivity);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        //generate x number of questions
        generateQuestions(10);

        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            questionTextView.setTextColor(Color.WHITE);
            for(RadioButton r: radioButtonList){
                r.setTextColor(Color.WHITE);
                r.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            }
        } else {
            layout.setBackgroundColor(Color.WHITE);
            questionTextView.setTextColor(Color.BLACK);
            for(RadioButton r: radioButtonList){
                r.setTextColor(Color.BLACK);
            }
        }

        if(fontPreference.getInt("Size", medSize) == 15){
            questionTextView.setTextSize(smallSize);
            for(RadioButton r: radioButtonList){
                r.setTextSize(smallSize);
            }
        } else if (fontPreference.getInt("Size", medSize) == 20){
            questionTextView.setTextSize(medSize);
            for(RadioButton r: radioButtonList){
                r.setTextSize(medSize);
            }

        } else {
            questionTextView.setTextSize(largeSize);
            for(RadioButton r: radioButtonList){
                r.setTextSize(largeSize);
            }
        }
    }
    protected void onResume(){
        super.onResume();
        Log.i("OnResume", "In On Resume");
        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            questionTextView.setTextColor(Color.WHITE);
            for(RadioButton r: radioButtonList){
                r.setTextColor(Color.WHITE);
                r.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
            }
        } else {
            layout.setBackgroundColor(Color.WHITE);
            questionTextView.setTextColor(Color.BLACK);
            for(RadioButton r: radioButtonList){
                r.setTextColor(Color.BLACK);
                r.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
            }
        }

        if(fontPreference.getInt("Size", medSize) == 15){
            questionTextView.setTextSize(smallSize);
            for(RadioButton r: radioButtonList){
                r.setTextSize(smallSize);
            }
        } else if (fontPreference.getInt("Size", medSize) == 20){
            questionTextView.setTextSize(medSize);
            for(RadioButton r: radioButtonList){
                r.setTextSize(medSize);
            }

        } else {
            questionTextView.setTextSize(largeSize);
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
        float answer = 0f;

        //answer1
        if(answerRadioButton1.isChecked()){
            answer = Float.valueOf((String) answerRadioButton1.getText());
            answerRadioButton1.setChecked(false);
        }
        //answer2
        else if(answerRadioButton2.isChecked()){
            answer = Float.valueOf((String) answerRadioButton2.getText());
            answerRadioButton2.setChecked(false);
        }
        //answer2
        else if(answerRadioButton3.isChecked()){
            answer = Float.valueOf((String) answerRadioButton3.getText());
            answerRadioButton3.setChecked(false);
        }
        //answer2
        else if(answerRadioButton4.isChecked()){
            answer = Float.valueOf((String) answerRadioButton4.getText());
            answerRadioButton4.setChecked(false);
        }
        //check answer
        boolean isCorrect = correctAnswer(answer);

        //only increase index if not at the end of ArrayList
        if(questionNum < generatedQuestions.size()){
            //only increment if question wasn't true (prevent jumping index)
            if(isCorrect == false) {
                questionNum += 1;
            }
        }
        //push back index
        else{
            questionNum = 0;
        }
        //next question
        updateTexts();
        return;
    }
    public boolean correctAnswer(float answer){
        boolean isCorrect = false;
        CharSequence text;
        int duration= Toast.LENGTH_SHORT;

        //if answer is correct increment counter and print toast
        if(answer == generatedQuestionsNumber.get(questionNum)){
            isCorrect = true;
            generatedQuestions.remove(questionNum);
            generatedQuestionsNumber.remove(questionNum);
            correctCount++;
            text = "Correct!";
        }
        //answer is wrong just print toast message
        else{
            text = "Incorrect";
        }
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();

        //WIP- close activity (save any stats or return anything before finishing, and indicate to user that they have completed the 10 questions)
        if (correctCount >= 10){
            finish();
        }
        return isCorrect;
    }

    public void updateTexts(){
        //reset text in all button's
        questionTextView.setText("");
        answerRadioButton1.setText("");
        answerRadioButton2.setText("");
        answerRadioButton3.setText("");
        answerRadioButton4.setText("");

        try {
            questionTextView.setText(generatedQuestions.get(questionNum));

            int answerSpot;

            //if the number is whole then print number as integer (no decimal)
            if (generatedQuestionsNumber.get(questionNum) % 1 == 0) {
                answerSpot = pickAnswerSpot(String.format("%d", Math.round(generatedQuestionsNumber.get(questionNum))));
            }
            //number is a decimal so print as decimal number (up to 3 decimal points)
            else {
                answerSpot = pickAnswerSpot(String.format("%.3f", round(generatedQuestionsNumber.get(questionNum), 3)));
            }
            ArrayList<Float> generatedAnswers = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (i != answerSpot) {
                    String answerText;

                    float answer = (generateRandomAnswer(generatedAnswers));
                    generatedAnswers.add(answer);

                    //if the number is whole then print number as integer (no decimal)
                    if (answer % 1 == 0) {
                        answerText = String.format("%d", Math.round(answer));
                    }
                    //number is a decimal so print as decimal number (up to 3 decimal points)
                    else {
                        answerText = String.format("%.3f", round(answer, 3));
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
    //generate 2 numbers based on the difficulty and put into activity
    public void generateQuestions(int numQuestions) {
        //set values
        setMaxMinValues();

        //loop until the number of questions required is met
        while (numQuestions > 0) {
            //prevent answer from being repeated, 0, infinite or NaN
            while (generatedQuestionsNumber.contains(numAnswer) == true || numAnswer == 0 || Float.isInfinite(numAnswer) == true || Float.isNaN(numAnswer) == true) {
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
            //debug
            generatedQuestionsNumber.add(numAnswer);
            //addition question
            if (type == 0) {
                //set text for question
                generatedQuestions.add((getString(R.string.questionText) + String.format(" %d + %d", Math.round(number1), Math.round(number2)) + " equals?"));
            }
            //subtraction question
            else if (type == 1) {
                generatedQuestions.add((getString(R.string.questionText) + String.format(" %d - %d", Math.round(number1), Math.round(number2))+ " equals?"));
            }
            //multiplication question
            else if (type == 2) {
                generatedQuestions.add((getString(R.string.questionText) + String.format(" %d * %d", Math.round(number1), Math.round(number2))+ " equals?"));
            }
            //division question
            else if (type == 3) {
                generatedQuestions.add((getString(R.string.questionText) + String.format(" %d / %d", Math.round(number1), Math.round(number2))+ " equals?"));
            }
            numQuestions -= 1;
        }
        //initial text set
        updateTexts();
        return;
    }
    public float generateRandomAnswer(ArrayList<Float> generatedAnswers) {
        float randomAnswer;
        int i = 0;
        //addition
        if (type == 0) {
            randomAnswer = (((float) rand.nextInt((max - min) + min)) + ((float) rand.nextInt((max - min) + min)));
        }
        //subtraction
        else if (type == 1) {
            randomAnswer = (((float) rand.nextInt((max - min) + min)) - ((float) rand.nextInt((max - min) + min)));
        }
        //multiplication
        else if (type == 2) {
            randomAnswer = (((float) rand.nextInt((max - min) + min)) * ((float) rand.nextInt((max - min) + min)));
        }
        //division
        else {
            randomAnswer = (((float) rand.nextInt((max - min) + min)) / ((float) rand.nextInt((max - min) + min)));
        }
        //keep randomizing answer until it doesn't match the answer, isn't an already generated answer, isn't infinite or NaN
        while (generatedAnswers.contains(randomAnswer) == true || Float.isInfinite(randomAnswer) == true || Float.isNaN(randomAnswer) == true) {
            //addition
            if (type == 0) {
                randomAnswer = (((float) rand.nextInt((max - min) + min)) + ((float) rand.nextInt((max - min) + min)));
            }
            //subtraction
            else if (type == 1) {
                randomAnswer = (((float) rand.nextInt((max - min) + min)) - ((float) rand.nextInt((max - min) + min)));
            }
            //multiplication
            else if (type == 2) {
                randomAnswer = (((float) rand.nextInt((max - min) + min)) * ((float) rand.nextInt((max - min) + min)));
            }
            //division
            else {
                randomAnswer = (((float) rand.nextInt((max - min) + min)) / ((float) rand.nextInt((max - min) + min)));
            }
        }
        return randomAnswer;
    }


    private static double round(double value, int decimalPlaces) {
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}