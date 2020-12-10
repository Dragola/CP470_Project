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

public class MathAlgebraActivity extends AppCompatActivity implements View.OnTouchListener
{
    String ACTIVITY_NAME = "MathAlgebraActivity";

    private class Tuple
    {
        int num;
        String nextOperator;

        Tuple(int num, String nextOperator)
        {
            this.num = num;
            this.nextOperator = nextOperator;
        }
    }

    TextView textEquation;
    TextView textUserInput;
    TextView textCorrectAnswerCount;
    TextView answerString;
    ProgressBar pbCorrectAnswerCount;
    ImageView imgDisplayResult;
    long timeStart;
    int min;
    int max;
    int numTotal;
    int correctAnswerCount = 0;
    int incorrectAnswerCount = 0;
    int buttonPressCount = 0;
    String currentOperation;
    String currentDifficulty;
    String currentEquation = "";

    SharedPreferences fontPreference;
    SharedPreferences darkPreference;
    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;
    List<Button> btnList = new ArrayList<Button>();
    List<TextView> textViewList = new ArrayList<TextView>();
    ConstraintLayout layout;
    TextView titleString;


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
                    ButtonOKClick(v);
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
        setContentView(R.layout.activity_math_algebra);

        textEquation = findViewById(R.id.textEquation);
        textUserInput = findViewById(R.id.textUserInput);
        imgDisplayResult = findViewById(R.id.imgDisplayResult);
        textCorrectAnswerCount = findViewById(R.id.textCorrectAnswerCount);
        pbCorrectAnswerCount = findViewById(R.id.pbCorrectAnswerCount);
        currentOperation = getIntent().getStringExtra("Type");
        currentDifficulty = getIntent().getStringExtra("Difficulty");

        answerString = findViewById(R.id.textAnswerDisplay);

        textViewList.addAll((Collection<? extends  TextView>) Arrays.asList(textEquation, textUserInput));
        btnList.addAll((Collection<? extends Button>) Arrays.asList((Button) findViewById(R.id.button0), (Button) findViewById(R.id.button1), (Button) findViewById(R.id.button2), (Button) findViewById(R.id.button3), (Button) findViewById(R.id.button4), (Button) findViewById(R.id.button5), (Button) findViewById(R.id.button6), (Button) findViewById(R.id.button7), (Button) findViewById(R.id.button8), (Button) findViewById(R.id.button9), (Button) findViewById(R.id.buttonOK), (Button) findViewById(R.id.buttonBack)));

        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        layout = findViewById(R.id.textDisplayLastResult);

        if(darkPreference.getBoolean("DarkStatus", true) == true){
            layout.setBackgroundColor(Color.BLACK);
            textCorrectAnswerCount.setTextColor(Color.WHITE);
            answerString.setTextColor(Color.WHITE);
            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
            for(Button b: btnList){
                b.setTextColor(Color.BLACK);
            }
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
            textCorrectAnswerCount.setTextColor(Color.BLACK);
            answerString.setTextColor(Color.BLACK);
            for(TextView t: textViewList){
                t.setTextColor(Color.BLACK);
            }
            for(Button b: btnList){
                b.setTextColor(Color.BLACK);
            }
        }
        if (fontPreference.getInt("Size", medSize) == 15) {
            for (Button b : btnList) {
                b.setTextSize(15);
            }
            for(TextView t: textViewList){
                t.setTextSize(smallSize);
            }
            answerString.setTextSize(smallSize);
        } else if (fontPreference.getInt("Size", medSize) == 20) {
            for (Button b : btnList) {
                b.setTextSize(20);
            }
            for(TextView t: textViewList){
                t.setTextSize(medSize);
            }
            answerString.setTextSize(medSize);
        } else {
            for (Button b : btnList) {
                b.setTextSize(25);
            }
            for(TextView t: textViewList){
                t.setTextSize(largeSize);
            }
            answerString.setTextSize(largeSize);
        }
        if(currentOperation.equals("Algebra"))
        {
            switch(currentDifficulty)
            {
                case "Easy":
                    min = 2; max = 15;
                    break;
                case "Medium":
                    min = 3; max = 30;
                    break;
                case "Hard":
                    min = 4; max = 40;
                    break;
                default:
                    Log.e(ACTIVITY_NAME, "Invalid Difficulty");
                    throw new IllegalStateException();
            }

            _generateNewEquation();
            textEquation.setText(currentEquation);
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
    public void ButtonOKClick(View v)
    {
        String userInputText = textUserInput.getText().toString();
        if(userInputText.equals(""))
        {
            return;
        }

        int userInputNum = Integer.parseInt(userInputText);

        if(userInputNum == numTotal)
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
            Log.i(ACTIVITY_NAME, "Equation: " + currentEquation + ", Answer: " + numTotal);
        }

        _generateNewEquation();

        textUserInput.setText("");

    }

    public void AllQuestionsFinished()
    {
        long timeEnd = Calendar.getInstance().getTimeInMillis();
        long totalTimeSeconds = (timeEnd - timeStart) / 1000;

        Intent stats = new Intent();
        stats.putExtra("Operation", currentOperation);
        stats.putExtra("Difficulty", currentDifficulty);

        // The time elapsed since starting the activity, in seconds
        stats.putExtra("TotalTimeSeconds", Long.toString(totalTimeSeconds));

        // How many times the user inputted an incorrect answer
        stats.putExtra("IncorrectAnswerCount", Integer.toString(incorrectAnswerCount));

        // Percentage of correct answers
        stats.putExtra("PercentCorrect", Double.toString(correctAnswerCount/20.0));

        // The amount of times the user pressed a button (fun stat)
        stats.putExtra("ButtonPressCount", Integer.toString(buttonPressCount));

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

    public String _getRandomOperator()
    {
        int decider = _getRandomNumber(0, 3);

        if(decider == 0) // Addition
        {
            return "+";
        }
        else if (decider == 1) // Subtraction
        {
            return "-";
        }
        else if(decider == 2) // Multiplication
        {
            return "*";
        }
        else  // decider == 3,  Division
        {
            return "/";
        }
    }

    public void _generateNewEquation()
    {
        ArrayList<Tuple> theArray = new ArrayList<Tuple>(); // Create an ArrayList object
        currentEquation = "";

        Tuple lastElement = new Tuple(_getRandomNumber(min, max), "");
        currentEquation = currentEquation + " " + lastElement.num;
        theArray.add(0, lastElement);
        int currentNum;
        String lastOperator = "";
        String currentOperator = _getRandomOperator();

        for(int i = 0; i < min; i++)
        {
            while(currentOperator == lastOperator)
            {
                currentOperator = _getRandomOperator();
            }

            if(currentOperator == "+")
            {
                currentNum = _getRandomNumber(min, max);
            }
            else if(currentOperator == "-")
            {
                if(theArray.get(0).nextOperator == "*")
                {
                    currentOperator = "+";
                }

                if(theArray.get(0).num <= max)
                {
                    currentNum = _getRandomNumber(theArray.get(0).num, max);
                }
                else
                {
                    currentNum = _getRandomNumber(theArray.get(0).num, theArray.get(0).num + 10);
                }
            }
            else if(currentOperator == "*")
            {
                currentNum = _getRandomNumber(2, min);
            }
            else // currentOperator == "/"
            {

                if(theArray.get(0).nextOperator == "-")
                {
                    currentOperator = "*";
                    currentNum = _getRandomNumber(2, min);
                }
                else
                {
                    currentNum = theArray.get(0).num * _getRandomNumber(2, min);
                }
            }

            currentEquation = currentNum + " " + currentOperator + " " + currentEquation;
            Tuple currentTuple = new Tuple(currentNum, currentOperator);
            theArray.add(0, currentTuple);

            lastOperator = currentOperator;
        }

        // Solve it
        Tuple current;

        //BE>DM<AS
        for(int i = 0; i < theArray.size(); i++)
        {
            if(theArray.get(i).nextOperator == "*")
            {
                current = new Tuple(theArray.get(i).num * theArray.get(i + 1).num,
                        theArray.get(i + 1).nextOperator);

                theArray.set(i + 1, current);
                theArray.remove(i);

                i--;
            }
            else if(theArray.get(i).nextOperator == "/")
            {
                current = new Tuple(theArray.get(i).num / theArray.get(i + 1).num,
                        theArray.get(i + 1).nextOperator);

                theArray.set(i + 1, current);
                theArray.remove(i);

                i--;
            }
        }

        //BEDM>AS<
        for(int i = 0; i < theArray.size(); i++)
        {
            if(theArray.get(i).nextOperator == "+")
            {
                current = new Tuple(theArray.get(i).num + theArray.get(i + 1).num,
                        theArray.get(i + 1).nextOperator);

                theArray.set(i + 1, current);
                theArray.remove(i);

                i--;
            }
            else if(theArray.get(i).nextOperator == "-")
            {
                current = new Tuple(theArray.get(i).num - theArray.get(i + 1).num,
                        theArray.get(i + 1).nextOperator);

                theArray.set(i + 1, current);
                theArray.remove(i);

                i--;
            }
        }

        current = theArray.get(0);
        numTotal = current.num;
        textEquation.setText(currentEquation);
    }
}