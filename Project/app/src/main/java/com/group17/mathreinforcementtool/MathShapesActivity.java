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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class MathShapesActivity extends AppCompatActivity implements View.OnTouchListener
{
    String ACTIVITY_NAME = "MathShapesActivity";

    TextView textNum1;
    TextView textNum2;
    TextView textNum3;
    TextView textUserInput;
    TextView textNumTotal;
    TextView textCorrectAnswerCount;
    TextView answerString;
    ProgressBar pbCorrectAnswerCount;
    ImageView imgDisplayResult;
    ImageView imgShapes;
    long timeStart;
    int min;
    int max;
    int num1;
    int num2;
    int num3;
    int numTotal;
    int correctAnswerCount = 18;
    int incorrectAnswerCount = 0;
    int buttonPressCount = 0;
    String currentOperation;
    String currentOperationInitial;
    String currentState;
    String currentDifficulty;

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

                    switch(currentOperation)
                    {
                        case "Perimeter":
                            ButtonOKPerimeterClick(v);
                            break;
                        case "Area":
                            ButtonOKAreaClick(v);
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
        setContentView(R.layout.activity_math_shapes);

        textNum1 = findViewById(R.id.textNum1);
        textNum2 = findViewById(R.id.textNum2);
        textNum3 = findViewById(R.id.textNum3);
        textNumTotal = findViewById(R.id.textNumTotal);
        textUserInput = findViewById(R.id.textUserInput);
        imgDisplayResult = findViewById(R.id.imgDisplayResult);
        imgShapes = findViewById(R.id.imgShapes);
        textCorrectAnswerCount = findViewById(R.id.textCorrectAnswerCount);
        pbCorrectAnswerCount = findViewById(R.id.pbCorrectAnswerCount);
        currentOperation = getIntent().getStringExtra("Type");
        currentDifficulty = getIntent().getStringExtra("Difficulty");
        boolean coin = _coinFlip();
        answerString = findViewById(R.id.textAnswerDisplay);

        textViewList.addAll((Collection<? extends  TextView>) Arrays.asList(textNum1,textNum2,textNum3,textNumTotal, textUserInput));
        btnList.addAll((Collection<? extends Button>) Arrays.asList((Button) findViewById(R.id.button0), (Button) findViewById(R.id.button1), (Button) findViewById(R.id.button2), (Button) findViewById(R.id.button3), (Button) findViewById(R.id.button4), (Button) findViewById(R.id.button5), (Button) findViewById(R.id.button6), (Button) findViewById(R.id.button7), (Button) findViewById(R.id.button8), (Button) findViewById(R.id.button9), (Button) findViewById(R.id.buttonOK), (Button) findViewById(R.id.buttonBack)));

        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        layout = findViewById(R.id.textDisplayLastResult);

        if(darkPreference.getBoolean("DarkStatus", true) == true){
            layout.setBackgroundColor(Color.BLACK);
            textCorrectAnswerCount.setTextColor(Color.WHITE);
            imgShapes.setColorFilter(Color.WHITE);
            answerString.setTextColor(Color.WHITE);
            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
            textCorrectAnswerCount.setTextColor(Color.BLACK);
            imgShapes.setColorFilter(Color.BLACK);
            answerString.setTextColor(Color.BLACK);
            for(TextView t: textViewList){
                t.setTextColor(Color.BLACK);
            }
        }
        if (fontPreference.getInt("Size", medSize) == 15) {
            for (Button b : btnList) {
                b.setTextSize(10);
            }
            for(TextView t: textViewList){
                t.setTextSize(smallSize);
            }
            answerString.setTextSize(smallSize);
        } else if (fontPreference.getInt("Size", medSize) == 20) {
            for (Button b : btnList) {
                b.setTextSize(15);
            }
            for(TextView t: textViewList){
                t.setTextSize(medSize);
            }
            answerString.setTextSize(medSize);
        } else {
            for (Button b : btnList) {
                b.setTextSize(20);
            }
            for(TextView t: textViewList){
                t.setTextSize(largeSize);
            }
            answerString.setTextSize(largeSize);
        }

        if(currentOperation.equals("Perimeter"))
        {
            currentOperationInitial = "P";
            imgShapes.setImageResource(R.drawable.triangle);

            switch (currentDifficulty)
            {
                case "Easy":
                    min = 1;
                    max = 9;
                    break;
                case "Medium":
                    min = 1;
                    max = 50;
                    break;
                case "Hard":
                    min = 10;
                    max = 150;
                    break;
                default:
                    Log.e(ACTIVITY_NAME, "Invalid Difficulty");
                    throw new IllegalStateException();
            }

            num1 = _getRandomNumber(min, max);
            num2 = _getRandomNumber(min, max);
            num3 = _getRandomNumber(min, max);
            numTotal = num1 + num2 + num3;

            if(coin == true)
            {
                currentState = "Addition";
            }
            else
            {
                currentState = "Subtraction";
            }

        }


        else if(currentOperation.equals("Area"))
        {
            currentOperationInitial = "A";

            switch (currentDifficulty)
            {
                case "Easy":
                    min = 2;
                    max = 4;
                    break;
                case "Medium":
                    min = 2;
                    max = 6;
                    break;
                case "Hard":
                    min = 2;
                    max = 8;
                    break;
                default:
                    Log.e(ACTIVITY_NAME, "Invalid Difficulty");
                    throw new IllegalStateException();
            }

            num1 = _getRandomNumber(min, max);
            num2 = _getRandomNumber(min, max);
            num3 = _getRandomNumber(min, max);
            numTotal = num1 * num2 * num3;

            if(coin == true)
            {
                currentState = "Multiplication";
            }
            else
            {
                currentState = "Division";
            }
        }

        else
        {
            Log.e(ACTIVITY_NAME, "Invalid Type");
            throw new IllegalStateException();
        }

        if(coin == true) // Adding or Multiplying
        {
            textNum1.setText(getString(R.string.textUnitMeters, num1));
            textNum2.setText(getString(R.string.textUnitMeters, num2));
            textNum3.setText(getString(R.string.textUnitMeters, num3));
            textNumTotal.setText(getString(R.string.textNumPerimTotalStr, currentOperationInitial, "?"));

        }
        else if(coin == false) // Subtracting or Dividing
        {
            int choosePositions = _getRandomNumber(0, 2);

            if(choosePositions == 0)
            {
                textNum1.setText(getString(R.string.textUnitMetersStr, "?"));
                textNum2.setText(getString(R.string.textUnitMeters, num1));
                textNum3.setText(getString(R.string.textUnitMeters, num2));
            }

            else if(choosePositions == 1)
            {
                textNum1.setText(getString(R.string.textUnitMeters, num1));
                textNum2.setText(getString(R.string.textUnitMetersStr, "?"));
                textNum3.setText(getString(R.string.textUnitMeters, num2));
            }

            else if(choosePositions == 2)
            {
                textNum1.setText(getString(R.string.textUnitMeters, num1));
                textNum2.setText(getString(R.string.textUnitMeters, num2));
                textNum3.setText(getString(R.string.textUnitMetersStr, "?"));
            }

            textNumTotal.setText(getString(R.string.textNumPerimTotal, currentOperationInitial, numTotal));
        }
        textUserInput.setText("");

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
    public void ButtonOKPerimeterClick(View v)
    {
        String userInputText = textUserInput.getText().toString();
        if(userInputText.equals(""))
        {
            return;
        }

        int userInputNum = Integer.parseInt(userInputText);

        if(currentState == "Addition")
        {
            if (userInputNum == numTotal)
            {
                imgDisplayResult.setImageResource(R.drawable.img_circle_checkmark);

                correctAnswerCount++;
                textCorrectAnswerCount.setText(getString(R.string.textCorrectAnswerCount, correctAnswerCount));
                pbCorrectAnswerCount.incrementProgressBy(1);
                if (correctAnswerCount >= 20)
                {
                    AllQuestionsFinished();
                }
            }
            else
            {
                imgDisplayResult.setImageResource(R.drawable.img_circle_x);
                incorrectAnswerCount++;
            }
        }

        if(currentState == "Subtraction")
        {
            if (userInputNum == num3)
            {
                imgDisplayResult.setImageResource(R.drawable.img_circle_checkmark);

                correctAnswerCount++;
                textCorrectAnswerCount.setText(getString(R.string.textCorrectAnswerCount, correctAnswerCount));
                pbCorrectAnswerCount.incrementProgressBy(1);
                if (correctAnswerCount >= 20)
                {
                    AllQuestionsFinished();
                }
            }
            else
            {
                imgDisplayResult.setImageResource(R.drawable.img_circle_x);
                incorrectAnswerCount++;
            }
        }


        num1 = _getRandomNumber(min, max);
        num2 = _getRandomNumber(min, max);
        num3 = _getRandomNumber(min, max);
        numTotal = num1 + num2 + num3;
        boolean coin = _coinFlip();

        if(coin == true)
        {
            currentState = "Addition";

            textNum1.setText(getString(R.string.textUnitMeters, num1));
            textNum2.setText(getString(R.string.textUnitMeters, num2));
            textNum3.setText(getString(R.string.textUnitMeters, num3));
            textNumTotal.setText(getString(R.string.textNumPerimTotalStr, currentOperationInitial, "?"));
        }
        else
        {
            currentState = "Subtraction";

            int choosePositions = _getRandomNumber(0, 2);

            if(choosePositions == 0)
            {
                textNum1.setText(getString(R.string.textUnitMetersStr, "?"));
                textNum2.setText(getString(R.string.textUnitMeters, num1));
                textNum3.setText(getString(R.string.textUnitMeters, num2));
            }

            else if(choosePositions == 1)
            {
                textNum1.setText(getString(R.string.textUnitMeters, num1));
                textNum2.setText(getString(R.string.textUnitMetersStr, "?"));
                textNum3.setText(getString(R.string.textUnitMeters, num2));
            }

            else if(choosePositions == 2)
            {
                textNum1.setText(getString(R.string.textUnitMeters, num1));
                textNum2.setText(getString(R.string.textUnitMeters, num2));
                textNum3.setText(getString(R.string.textUnitMetersStr, "?"));
            }

            textNumTotal.setText(getString(R.string.textNumPerimTotal, currentOperationInitial, numTotal));
        }
        textUserInput.setText("");

    }

    public void ButtonOKAreaClick(View v)
    {
        String userInputText = textUserInput.getText().toString();
        if(userInputText.equals(""))
        {
            return;
        }

        int userInputNum = Integer.parseInt(userInputText);

        if(currentState == "Multiplication")
        {
            if (userInputNum == numTotal)
            {
                imgDisplayResult.setImageResource(R.drawable.img_circle_checkmark);

                correctAnswerCount++;
                textCorrectAnswerCount.setText(getString(R.string.textCorrectAnswerCount, correctAnswerCount));
                pbCorrectAnswerCount.incrementProgressBy(1);
                if (correctAnswerCount >= 20)
                {
                    AllQuestionsFinished();
                }
            }
            else
            {
                imgDisplayResult.setImageResource(R.drawable.img_circle_x);
                incorrectAnswerCount++;
            }
        }

        if(currentState == "Division")
        {
            if (userInputNum == num3)
            {
                imgDisplayResult.setImageResource(R.drawable.img_circle_checkmark);

                correctAnswerCount++;
                textCorrectAnswerCount.setText(getString(R.string.textCorrectAnswerCount, correctAnswerCount));
                pbCorrectAnswerCount.incrementProgressBy(1);

                if (correctAnswerCount >= 20)
                {
                    AllQuestionsFinished();
                }
            }
            else
            {
                imgDisplayResult.setImageResource(R.drawable.img_circle_x);
                incorrectAnswerCount++;
            }
        }

        num1 = _getRandomNumber(min, max);
        num2 = _getRandomNumber(min, max);
        num3 = _getRandomNumber(min, max);
        numTotal = num1 * num2 * num3;
        boolean coin = _coinFlip();

        if(coin == true)
        {
            currentState = "Multiplication";

            textNum1.setText(getString(R.string.textUnitMeters, num1));
            textNum2.setText(getString(R.string.textUnitMeters, num2));
            textNum3.setText(getString(R.string.textUnitMeters, num3));
            textNumTotal.setText(getString(R.string.textNumPerimTotalStr, currentOperationInitial, "?"));
        }
        else
        {
            currentState = "Division";

            int choosePositions = _getRandomNumber(0, 2);

            if(choosePositions == 0)
            {
                textNum1.setText(getString(R.string.textUnitMetersStr, "?"));
                textNum2.setText(getString(R.string.textUnitMeters, num1));
                textNum3.setText(getString(R.string.textUnitMeters, num2));
            }

            else if(choosePositions == 1)
            {
                textNum1.setText(getString(R.string.textUnitMeters, num1));
                textNum2.setText(getString(R.string.textUnitMetersStr, "?"));
                textNum3.setText(getString(R.string.textUnitMeters, num2));
            }

            else if(choosePositions == 2)
            {
                textNum1.setText(getString(R.string.textUnitMeters, num1));
                textNum2.setText(getString(R.string.textUnitMeters, num2));
                textNum3.setText(getString(R.string.textUnitMetersStr, "?"));
            }

            textNumTotal.setText(getString(R.string.textNumPerimTotal, currentOperationInitial, numTotal));
        }
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

    /*
    ----------------------------------------------------
    Parameters:   min (int), max (int)
    Return:       num (int)
    Description:  -Helper function that generates a random number
                  between min and max
    ----------------------------------------------------
    */
    public boolean _coinFlip()
    {
        Random numGenerator = new Random();
        int num = numGenerator.nextInt((1 - 0) + 1) + 0;

        if(num == 1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}