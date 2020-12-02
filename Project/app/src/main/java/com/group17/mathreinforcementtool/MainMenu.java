package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainMenu extends AppCompatActivity {

    List<Button> buttonList = new ArrayList<Button>();
    List<Button> littleButtonList = new ArrayList<Button>();
    List<TextView> textViewList = new ArrayList<TextView>();

    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;
    int littleButtSmallSize = 6;
    int littleButtMedSize = 12;
    int littleButtLargeSize = 14;
    ConstraintLayout layout;
    SharedPreferences darkPreference;
    SharedPreferences fontPreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        textViewList.addAll((Collection<? extends TextView>) Arrays.asList((TextView) findViewById(R.id.textMenuMultiple), (TextView) findViewById(R.id.textMenuAddition), (TextView) findViewById(R.id.textMenuSubtraction), (TextView) findViewById(R.id.textMenuMultiplication), (TextView) findViewById(R.id.textMenuDivision), (TextView) findViewById(R.id.textMenuPerimeter), (TextView) findViewById(R.id.textMenuAlgebra), (TextView) findViewById(R.id.textMenuArea)));
        littleButtonList.addAll((Collection<? extends Button>) Arrays.asList((Button) findViewById(R.id.easyAdd), (Button) findViewById(R.id.medAdd), (Button) findViewById(R.id.hardAdd), (Button) findViewById(R.id.easyMult), (Button) findViewById(R.id.medMult), (Button) findViewById(R.id.hardMult), (Button) findViewById(R.id.easySub), (Button) findViewById(R.id.medSub), (Button) findViewById(R.id.hardSub), (Button) findViewById(R.id.easyDiv), (Button) findViewById(R.id.medDiv), (Button) findViewById(R.id.hardDiv), (Button) findViewById(R.id.easyPerim), (Button) findViewById(R.id.medPerim), (Button) findViewById(R.id.hardPerim), (Button) findViewById(R.id.easyArea), (Button) findViewById(R.id.medArea), (Button) findViewById(R.id.hardArea), (Button) findViewById(R.id.easyAdd2), (Button) findViewById(R.id.medPerim2), (Button) findViewById(R.id.hardPerim2)));
        buttonList.addAll((Collection<? extends  Button>) Arrays.asList((Button) findViewById(R.id.easyMultipleChoice), (Button) findViewById(R.id.mediumMultipleChoice), (Button) findViewById(R.id.hardMultipleChoice), (Button) findViewById(R.id.settingsButton)));


        layout = findViewById(R.id.MainMenu);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        Log.i("OnCreate", "We're in OnCreate");

        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
        }
        else {
            layout.setBackgroundColor(Color.WHITE);
            for(TextView t: textViewList){
                t.setTextColor(Color.BLACK);
            }
        }


        if(fontPreference.getInt("Size", medSize) == smallSize){

            for(TextView t: textViewList){
                t.setTextSize(smallSize);
            }
            for(Button b: littleButtonList){
                b.setTextSize(littleButtSmallSize);
            }
            for(Button b: buttonList){
                b.setTextSize(smallSize);
            }
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            for(TextView t: textViewList){
                t.setTextSize(medSize);
            }
            for(Button b: littleButtonList){
                b.setTextSize(littleButtMedSize);
            }
            for(Button b: buttonList){
                b.setTextSize(medSize);
            }

        } else{
            for(TextView t: textViewList){
                t.setTextSize(largeSize);
            }
            for(Button b: littleButtonList){
                b.setTextSize(littleButtLargeSize);
            }
            for(Button b: buttonList){
                b.setTextSize(largeSize);
            }
        }
    }

    protected void onResume(){
        super.onResume();
        Log.i("OnResume", "In On Resume");
        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            for(TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
        }
        else {
            layout.setBackgroundColor(Color.WHITE);
            for(TextView t: textViewList){
                t.setTextColor(Color.BLACK);
            }
        }
        if(fontPreference.getInt("Size", medSize) == smallSize){
            for(TextView t: textViewList){
                t.setTextSize(smallSize);
            }
            for(Button b: littleButtonList){
                b.setTextSize(littleButtSmallSize);
            }
            for(Button b: buttonList){
                b.setTextSize(smallSize);
            }
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            for(TextView t: textViewList){
                t.setTextSize(medSize);
            }
            for(Button b: littleButtonList){
                b.setTextSize(littleButtMedSize);
            }
            for(Button b: buttonList){
                b.setTextSize(medSize);
            }

        } else{
            for(TextView t: textViewList){
                t.setTextSize(largeSize);
            }
            for(Button b: littleButtonList){
                b.setTextSize(littleButtLargeSize);
            }
            for(Button b: buttonList){
                b.setTextSize(largeSize);
            }

        }
    }

//    This is here to swap to onResume on back so that flicking the "DarkSwitch" actually works without having to close down the app lmao
    protected void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);

        // Coming from MathTemplateActivity (maybe more if same stats are tracked)
        if(requestCode == 10)
        {
            if(responseCode == RESULT_OK)
            {
                /*
                 * This is all the data returned from MathTemplateActivity
                 *
                 * Operation            - Currently can be "Addition", "Subtraction", "Multiplication",
                 *                        "Division", "Perimeter", "Area", "Algebra"
                 * Difficulty           - "Easy", "Medium", or "Hard"
                 * totalTimeSeconds     - The time elapsed since starting the activity, in seconds
                 * incorrectAnswerCount - How many times the user inputted an incorrect answer
                 * buttonPressCount     - The amount of times the user pressed a button (fun stat)
                 *
                 * Some stats are implied as well. For example,
                 * Total Question Count - 20 + IncorrectAnswerCount (CorrectAnswer = 20 always)
                 * Attempted Addition   - 1 (If currentOperation == "Addition")
                 */

                String currentOperation = data.getStringExtra("Operation");
                String currentDifficulty = data.getStringExtra("Difficulty");
                long totalTimeSeconds = Integer.parseInt(data.getStringExtra("TotalTimeSeconds"));
                int incorrectAnswerCount = Integer.parseInt(data.getStringExtra("IncorrectAnswerCount"));
                int buttonPressCount = Integer.parseInt(data.getStringExtra("ButtonPressCount"));

                // Stat use example (Display Time)
                long totalTimeMinutes = TimeUnit.SECONDS.toMinutes(totalTimeSeconds);
                String displayTime = getString(R.string.Show_MTA_Time, totalTimeMinutes,
                        totalTimeSeconds - TimeUnit.MINUTES.toSeconds(totalTimeMinutes));

                Toast t = Toast.makeText(this, displayTime, Toast.LENGTH_LONG);
                t.show();
            }
        }
    }

    //swap to settings activity
    public void onClickSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    //easy multiple choice
    public void onClickEasyMultipleChoice(View view){
        Intent intent = new Intent(this, MultipleChoiceActivity.class);
        intent.putExtra("Difficulty", 0);
        intent.putExtra("Type", 1);
        startActivity(intent);
    }
    //medium multiple choice
    public void onClickMediumMultipleChoice(View view){
        Intent intent = new Intent(this, MultipleChoiceActivity.class);
        intent.putExtra("Difficulty", 1);
        intent.putExtra("Type", 3);
        startActivity(intent);
    }
    //medium multiple choice
    public void onClickHardMultipleChoice(View view){
        Intent intent = new Intent(this, MultipleChoiceActivity.class);
        intent.putExtra("Difficulty", 2);
        intent.putExtra("Type", 1);
        startActivity(intent);
    }

    /*
    ----------------------------------------------------
    Parameters:   v (View)
    Return:       None
    Description:  -Called when the buttons related to non-MC Addition are clicked
                  -Records difficulty ('Easy', 'Medium', 'Hard') the user chose and
                  switches to MathTemplateActivity.
                  -!! Although startActivityForResult() is called,
                  onActivityResult() is not yet implemented.

                  -All of these could be one function in the future, however I likely
                  would need a different representation than just buttons.
    ----------------------------------------------------
    */
    public void onAdditionClick(View v)
    {
        Button currentButton = findViewById(v.getId());
        String buttonText = currentButton.getText().toString();
        Intent i = new Intent(this, MathTemplateActivity.class);
        i.putExtra("Type", "Addition");
        i.putExtra("Difficulty", buttonText);
        startActivityForResult(i, 10);
    }

    public void onSubtractionClick(View v)
    {
        Button currentButton = findViewById(v.getId());
        String buttonText = currentButton.getText().toString();
        Intent i = new Intent(this, MathTemplateActivity.class);
        i.putExtra("Type", "Subtraction");
        i.putExtra("Difficulty", buttonText);
        startActivityForResult(i, 10);
    }

    public void onMultiplicationClick(View v)
    {
        Button currentButton = findViewById(v.getId());
        String buttonText = currentButton.getText().toString();
        Intent i = new Intent(this, MathTemplateActivity.class);
        i.putExtra("Type", "Multiplication");
        i.putExtra("Difficulty", buttonText);
        startActivityForResult(i, 10);
    }

    public void onDivisionClick(View v)
    {
        Button currentButton = findViewById(v.getId());
        String buttonText = currentButton.getText().toString();
        Intent i = new Intent(this, MathTemplateActivity.class);
        i.putExtra("Type", "Division");
        i.putExtra("Difficulty", buttonText);
        startActivityForResult(i, 10);
    }

    public void onPerimeterClick(View v)
    {
        Button currentButton = findViewById(v.getId());
        String buttonText = currentButton.getText().toString();
        Intent i = new Intent(this, MathShapesActivity.class);
        i.putExtra("Type", "Perimeter");
        i.putExtra("Difficulty", buttonText);
        startActivityForResult(i, 10);
    }

    public void onAreaClick(View v)
    {
        Button currentButton = findViewById(v.getId());
        String buttonText = currentButton.getText().toString();
        Intent i = new Intent(this, MathShapesActivity.class);
        i.putExtra("Type", "Area");
        i.putExtra("Difficulty", buttonText);
        startActivityForResult(i, 10);
    }

    public void onAlgebraClick(View v)
    {
        Button currentButton = findViewById(v.getId());
        String buttonText = currentButton.getText().toString();
        Intent i = new Intent(this, MathAlgebraActivity.class);
        i.putExtra("Type", "Algebra");
        i.putExtra("Difficulty", buttonText);
        startActivityForResult(i, 10);
    }

    public void onTestLevelSelect(View v)
    {
        Intent intent = new Intent(this, LevelTypeSelect.class);
        startActivity(intent);
    }
}