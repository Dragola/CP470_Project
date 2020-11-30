package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
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
    TextView textNum2;
    TextView textUserInput;
    TextView textCorrectAnswerCount;
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

        if(currentOperation.equals("Algebra"))
        {
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
        Log.i(ACTIVITY_NAME, "Button Num pressed");
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

        for(int i = 0; i < 4; i++)
        {
            int currentNum = _getRandomNumber(min, max);

            String currentOperator = _getRandomOperator();
            currentEquation = currentEquation + " " + currentNum + " " + currentOperator;

            Tuple currentTuple = new Tuple(currentNum, currentOperator);
            theArray.add(i, currentTuple);
        }

        Tuple lastElement = new Tuple(_getRandomNumber(min, max), "");
        currentEquation = currentEquation + " " + lastElement.num;
        theArray.add(4, lastElement);

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