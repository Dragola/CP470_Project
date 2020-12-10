package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MathTemplateActivity extends AppCompatActivity implements View.OnTouchListener
{
    String ACTIVITY_NAME = "MathTemplateActivity";

    TextView textNum1;
    TextView textNum2;
    TextView textUserInput;
    TextView textOperator;
    TextView textCorrectAnswerCount;
    TextView answerString;
    ProgressBar pbCorrectAnswerCount;
    ImageView imgDisplayResult;
    long timeStart;
    int min;
    int max;
    int num1;
    int num2;
    int correctAnswerCount = 0;
    int incorrectAnswerCount = 0;
    int buttonPressCount = 0;
    int smallSize = 35;
    int medSize = 45;
    int largeSize = 55;
    String currentOperation;
    String currentDifficulty;

    List<TextView> textViewList = new ArrayList<TextView>();
    List<Button> buttonList = new ArrayList<Button>();
    SharedPreferences darkPreference;
    SharedPreferences fontPreference;

    ConstraintLayout layout;

    /*
    ----------------------------------------------------
    Parameters:   v (View), event (MotionEvent)
    Return:       boolean
    Description:  -OnTouchListener for MathTemplateActivity
                  -This listener performs operations when a
                  listed button is pressed down
                  -This is important because it is quicker and
                  more reliable than onClick() which requires all
                  three motion operations (down, hold, & release),
                  making it more suitable to accurately assess time
    ----------------------------------------------------
    */
    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            buttonPressCount++;

            switch (v.getId())
            {
                case R.id.button1: case R.id.button2: case R.id.button3:
                case R.id.button4: case R.id.button5: case R.id.button6:
                case R.id.button7: case R.id.button8: case R.id.button9:
                case R.id.button0:
                    ButtonNumberClick(v);
                    break;
                case R.id.buttonBack:
                    ButtonBackClick(v);
                    break;
                case R.id.buttonOK:

                    switch(currentOperation)
                    {
                        case "Addition":
                            ButtonOKAdditionClick(v);
                            break;
                        case "Subtraction":
                            ButtonOKSubtractionClick(v);
                            break;
                        case "Multiplication":
                            ButtonOKMultiplicationClick(v);
                            break;
                        case "Division":
                            ButtonOKDivisionClick(v);
                            break;
                    }
                    break;
            }
        }
        return true;
    }

    /*
    ----------------------------------------------------
    Parameters:   savedInstanceState (bundle)
    Return:       None
    Description:  -onCreate() function for MathTemplateActivity
                  -Gets Operation Type & Difficulty from relevant views, and
                  sets up the activity according to those options
                  -Calculates and displays first question
    ----------------------------------------------------
    */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_template);

        textNum1 = findViewById(R.id.textNum1);
        textNum2 = findViewById(R.id.textNum2);
        textUserInput = findViewById(R.id.textUserInput);
        imgDisplayResult = findViewById(R.id.imgDisplayResult);
        textOperator = findViewById(R.id.textOperator);
        textCorrectAnswerCount = findViewById(R.id.textCorrectAnswerCount);
        pbCorrectAnswerCount = findViewById(R.id.pbCorrectAnswerCount);
        currentOperation = getIntent().getStringExtra("Type");
        currentDifficulty = getIntent().getStringExtra("Difficulty");
        answerString = findViewById(R.id.textAnswerDisplay);

        layout = findViewById(R.id.textDisplayLastResult);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        textViewList.addAll((Collection<? extends TextView>) Arrays.asList(textNum1, textNum2, textUserInput, textOperator));
        buttonList.addAll((Collection<?extends Button>) Arrays.asList((Button) findViewById(R.id.button0), (Button) findViewById(R.id.button1), (Button) findViewById(R.id.button2), (Button) findViewById(R.id.button3), (Button) findViewById(R.id.button4), (Button) findViewById(R.id.button5), (Button) findViewById(R.id.button6), (Button) findViewById(R.id.button7), (Button) findViewById(R.id.button8), (Button) findViewById(R.id.button9), (Button) findViewById(R.id.buttonOK), (Button) findViewById(R.id.buttonBack))   );

        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            textCorrectAnswerCount.setTextColor(Color.WHITE);
            answerString.setTextColor(Color.WHITE);
            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }

        } else {
            layout.setBackgroundColor(Color.WHITE);
            textCorrectAnswerCount.setTextColor(Color.BLACK);
            answerString.setTextColor(Color.BLACK);
            for(TextView t: textViewList){
                t.setTextColor(Color.BLACK);
            }
        }

        if(fontPreference.getInt("Size", medSize) == 15){
            for(TextView t: textViewList){
                t.setTextSize(smallSize);
            }
            for(Button b: buttonList){
                b.setTextSize(15);
            }
        } else if (fontPreference.getInt("Size", medSize) == 20){
            for(TextView t: textViewList){
                t.setTextSize(medSize);
            }
            for(Button b: buttonList){
                b.setTextSize(20);
            }

        } else {
            for(TextView t: textViewList){
                t.setTextSize(largeSize);
            }
            for(Button b: buttonList){
                b.setTextSize(25);
            }
        }
        if(currentOperation.equals("Addition"))
        {
            textOperator.setText("+");
            switch(currentDifficulty)
            {
                case "Easy":
                    min = 1; max = 49;
                    break;
                case "Medium":
                    min = 10; max = 499;
                    break;
                case "Hard":
                    min = 100; max = 4999;
                    break;
                default:
                    Log.e(ACTIVITY_NAME, "Invalid Difficulty");
                    throw new IllegalStateException();
            }

            num1 = _getRandomNumber(min, max);
            num2 = _getRandomNumber(min, max);
            textNum1.setText(String.valueOf(num1));
            textNum2.setText(String.valueOf(num2));
            textUserInput.setText("");
        }

        else if(currentOperation.equals("Subtraction"))
        {
            textOperator.setText("-");
            switch(currentDifficulty)
            {
                case "Easy":
                    min = 1; max = 49;
                    break;
                case "Medium":
                    min = 10; max = 499;
                    break;
                case "Hard":
                    min = 100; max = 4999;
                    break;
                default:
                    Log.e(ACTIVITY_NAME, "Invalid Difficulty");
                    throw new IllegalStateException();
            }

            num1 = _getRandomNumber(min, max);
            num2 = _getRandomNumber(min, max);

            // Ensures no negative answer
            if(num2 > num1)
            {
                int temp = num1;
                num1 = num2;
                num2 = temp;
            }

            textNum1.setText(String.valueOf(num1));
            textNum2.setText(String.valueOf(num2));
            textUserInput.setText("");
        }

        else if(currentOperation.equals("Multiplication"))
        {
            textOperator.setText("*");
            switch(currentDifficulty)
            {
                case "Easy":
                    min = 2; max = 6;
                    break;
                case "Medium":
                    min = 2; max = 12;
                    break;
                case "Hard":
                    min = 2; max = 16;
                    break;
                default:
                    Log.e(ACTIVITY_NAME, "Invalid Difficulty");
                    throw new IllegalStateException();
            }

            num1 = _getRandomNumber(min, max);
            num2 = _getRandomNumber(min, max);
            textNum1.setText(String.valueOf(num1));
            textNum2.setText(String.valueOf(num2));
            textUserInput.setText("");
        }

        else if(currentOperation.equals("Division"))
        {
            textOperator.setText("/");
            switch(currentDifficulty)
            {
                case "Easy":
                    min = 8; max = 49;
                    break;
                case "Medium":
                    min = 15; max = 199;
                    break;
                case "Hard":
                    min = 30; max = 499;
                    break;
                default:
                    Log.e(ACTIVITY_NAME, "Invalid Difficulty");
                    throw new IllegalStateException();
            }

            num1 = _getRandomNumber(min, max);
            num2 = _getRandomNumber(2, min); // Ensures answer is always above 0
            textNum1.setText(String.valueOf(num1));
            textNum2.setText(String.valueOf(num2));
            textUserInput.setText("");
        }

        else
        {
            Log.e(ACTIVITY_NAME, "Invalid Type");
            throw new IllegalStateException();
        }

        timeStart = Calendar.getInstance().getTimeInMillis();

        //Setting listeners
        findViewById(R.id.button1).setOnTouchListener(this);
        findViewById(R.id.button2).setOnTouchListener(this);
        findViewById(R.id.button3).setOnTouchListener(this);
        findViewById(R.id.button4).setOnTouchListener(this);
        findViewById(R.id.button5).setOnTouchListener(this);
        findViewById(R.id.button6).setOnTouchListener(this);
        findViewById(R.id.button7).setOnTouchListener(this);
        findViewById(R.id.button8).setOnTouchListener(this);
        findViewById(R.id.button9).setOnTouchListener(this);
        findViewById(R.id.button0).setOnTouchListener(this);
        findViewById(R.id.buttonBack).setOnTouchListener(this);
        findViewById(R.id.buttonOK).setOnTouchListener(this);
    }

    protected void onResume(){
        super.onResume();
        Log.i("OnResume", "In On Resume");
        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            textCorrectAnswerCount.setTextColor(Color.WHITE);
            answerString.setTextColor(Color.WHITE);
            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
        } else {
            layout.setBackgroundColor(Color.WHITE);
            textCorrectAnswerCount.setTextColor(Color.BLACK);
            answerString.setTextColor(Color.BLACK);
            for(TextView t: textViewList){
                t.setTextColor(Color.BLACK);
            }
        }

        if(fontPreference.getInt("Size", medSize) == 15){
            for(TextView t: textViewList){
                t.setTextSize(smallSize);
            }
            for(Button b: buttonList){
                b.setTextSize(15);
            }
        } else if (fontPreference.getInt("Size", medSize) == 20){
            for(TextView t: textViewList){
                t.setTextSize(medSize);
            }
            for(Button b: buttonList){
                b.setTextSize(20);
            }

        } else {
            for(TextView t: textViewList){
                t.setTextSize(largeSize);
            }
            for(Button b: buttonList){
                b.setTextSize(25);
            }
        }
    }

    //    This is here to swap to onResume on back so that flicking the "DarkSwitch" actually works without having to close down the app lmao
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
    }

    /*
    ----------------------------------------------------
    Parameters:   v (View)
    Return:       None
    Description:  -Called when any numbered buttons (button0 ~ button9) are clicked
                  -Appends value of the button to textUserInput
    ----------------------------------------------------
    */
    public void ButtonNumberClick(View v)
    {
        Button currentButton = findViewById(v.getId());
        String buttonText = currentButton.getText().toString();
        String userInputText = textUserInput.getText().toString();
        textUserInput.setText(userInputText + buttonText);
    }

    /*
    ----------------------------------------------------
    Parameters:   v (View)
    Return:       None
    Description:  -Called when the back button is clicked
                  -Removes last value of textUserInput
    ----------------------------------------------------
    */
    public void ButtonBackClick(View v)
    {
        String userInputText = textUserInput.getText().toString();
        int userInputLen = userInputText.length();
        if(userInputLen > 0)
        {
            userInputText = userInputText.substring(0, userInputLen - 1);
            textUserInput.setText(userInputText);
        }
    }

    /*
    ----------------------------------------------------
    Parameters:   v (View)
    Return:       None
    Description:  -Called when the OK button is clicked
                  -The function ran depends on the operation type
                  of the activity
                  -If user input is blank, does nothing
                  -Determines if the user input was correct, and
                  updates textDisplayResult accordingly
                  -Prepares the next question
    ----------------------------------------------------
    */
    public void ButtonOKAdditionClick(View v)
    {
        String userInputText = textUserInput.getText().toString();
        if(userInputText.equals(""))
        {
            return;
        }

        int userInputNum = Integer.parseInt(userInputText);
        if(userInputNum == num1 + num2)
        {
            imgDisplayResult.setImageResource(R.drawable.img_circle_checkmark);

            correctAnswerCount++;
            textCorrectAnswerCount.setText(getString(R.string.textCorrectAnswerCount, correctAnswerCount));
            pbCorrectAnswerCount.incrementProgressBy(1);

            if(correctAnswerCount >= 20)
            {
                AllQuestionsFinished();
            }
        }
        else
        {
            imgDisplayResult.setImageResource(R.drawable.img_circle_x);
            incorrectAnswerCount++;
        }

        num1 = _getRandomNumber(min, max);
        num2 = _getRandomNumber(min, max);
        textNum1.setText(String.valueOf(num1));
        textNum2.setText(String.valueOf(num2));
        textUserInput.setText("");

        // if questioncount == 25 finish somehow.
    }

    public void ButtonOKSubtractionClick(View v)
    {
        String userInputText = textUserInput.getText().toString();
        if(userInputText.equals(""))
        {
            return;
        }

        int userInputNum = Integer.parseInt(userInputText);
        if(userInputNum == num1 - num2)
        {
            imgDisplayResult.setImageResource(R.drawable.img_circle_checkmark);

            correctAnswerCount++;
            textCorrectAnswerCount.setText(getString(R.string.textCorrectAnswerCount, correctAnswerCount));
            pbCorrectAnswerCount.incrementProgressBy(1);

            if(correctAnswerCount >= 20)
            {
                AllQuestionsFinished();
            }
        }
        else
        {
            imgDisplayResult.setImageResource(R.drawable.img_circle_x);
            incorrectAnswerCount++;
        }

        num1 = _getRandomNumber(min, max);
        num2 = _getRandomNumber(min, max);

        if(num2 > num1)
        {
            int temp = num1;
            num1 = num2;
            num2 = temp;
        }

        textNum1.setText(String.valueOf(num1));
        textNum2.setText(String.valueOf(num2));
        textUserInput.setText("");

    }

    public void ButtonOKMultiplicationClick(View v)
    {
        String userInputText = textUserInput.getText().toString();
        if(userInputText.equals(""))
        {
            return;
        }

        int userInputNum = Integer.parseInt(userInputText);
        if(userInputNum == num1 * num2)
        {
            imgDisplayResult.setImageResource(R.drawable.img_circle_checkmark);

            correctAnswerCount++;
            textCorrectAnswerCount.setText(getString(R.string.textCorrectAnswerCount, correctAnswerCount));
            pbCorrectAnswerCount.incrementProgressBy(1);

            if(correctAnswerCount >= 20)
            {
                AllQuestionsFinished();
            }
        }
        else
        {
            imgDisplayResult.setImageResource(R.drawable.img_circle_x);
            incorrectAnswerCount++;
        }

        num1 = _getRandomNumber(min, max);
        num2 = _getRandomNumber(min, max);
        textNum1.setText(String.valueOf(num1));
        textNum2.setText(String.valueOf(num2));
        textUserInput.setText("");

    }

    public void ButtonOKDivisionClick(View v)
    {
        String userInputText = textUserInput.getText().toString();
        if(userInputText.equals(""))
        {
            return;
        }

        int userInputNum = Integer.parseInt(userInputText);
        if(userInputNum == num1 / num2)
        {
            imgDisplayResult.setImageResource(R.drawable.img_circle_checkmark);

            correctAnswerCount++;
            textCorrectAnswerCount.setText(getString(R.string.textCorrectAnswerCount, correctAnswerCount));
            pbCorrectAnswerCount.incrementProgressBy(1);

            if(correctAnswerCount >= 20)
            {
                AllQuestionsFinished();
            }
        }
        else
        {
            imgDisplayResult.setImageResource(R.drawable.img_circle_x);
            incorrectAnswerCount++;
        }

        num1 = _getRandomNumber(min, max);
        num2 = _getRandomNumber(2, min);
        textNum1.setText(String.valueOf(num1));
        textNum2.setText(String.valueOf(num2));
        textUserInput.setText("");

    }

    public void AllQuestionsFinished()
    {
        long timeEnd = Calendar.getInstance().getTimeInMillis();
        long totalTimeSeconds = (timeEnd - timeStart) / 1000;

        Intent stats = new Intent();
        // Percentage of correct answers
        stats.putExtra("PercentCorrect", Double.toString(correctAnswerCount/20.0));

        SharedPreferences prefs = getSharedPreferences("MTStats" + currentDifficulty + currentOperation + 0, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        //add operation and difficulty to intent
        editor.putString("Operation", currentOperation);
        editor.putString("Difficulty", currentDifficulty);

        //add time elapsed since starting the activity (in seconds) to intent
        editor.putString("TotalTimeSeconds", Long.toString(totalTimeSeconds));

        //add number of times the user answered with a wrong answer
        editor.putString("CorrectAnswerCount", Integer.toString(correctAnswerCount));

        //add number of times the user answered with a wrong answer
        editor.putString("IncorrectAnswerCount", Integer.toString(incorrectAnswerCount));

        //commit changes
        editor.commit();

        setResult(RESULT_OK, stats);
        finish();
    }
    /*
    ----------------------------------------------------
    Parameters:   min (int), max (int)
    Return:       num (int)
    Description:  -Helper function that generates a random number
                  between min and max
    ----------------------------------------------------
    */
    public int _getRandomNumber(int min, int max)
    {
        Random numGenerator = new Random();
        return numGenerator.nextInt((max - min) + 1) + min;
    }
}