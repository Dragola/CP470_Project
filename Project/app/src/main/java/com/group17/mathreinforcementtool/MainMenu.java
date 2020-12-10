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

    TextView title;
    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;
    ConstraintLayout layout;
    SharedPreferences darkPreference;
    SharedPreferences fontPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        title = findViewById(R.id.titleText);
        buttonList.addAll((Collection<? extends  Button>) Arrays.asList((Button) findViewById(R.id.settingsButton), (Button) findViewById(R.id.notesButton), (Button) findViewById(R.id.levelSelectButton), (Button) findViewById(R.id.statsButton)));


        layout = findViewById(R.id.MainMenu);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        Log.i("OnCreate", "We're in OnCreate");

        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            title.setTextColor(Color.WHITE);
        }
        else {
            layout.setBackgroundColor(Color.WHITE);
            title.setTextColor(Color.BLACK);
        }


        if(fontPreference.getInt("Size", medSize) == smallSize){
            title.setTextSize(30);
            for(Button b: buttonList){
                b.setTextSize(smallSize);
            }
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            title.setTextSize(40);
            for(Button b: buttonList){
                b.setTextSize(medSize);
            }
        } else{
            title.setTextSize(50);
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
            title.setTextColor(Color.WHITE);
        }
        else {
            layout.setBackgroundColor(Color.WHITE);
            title.setTextColor(Color.BLACK);

        }
        if(fontPreference.getInt("Size", medSize) == smallSize){
            title.setTextSize(30);

            for(Button b: buttonList){
                b.setTextSize(smallSize);
            }
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            title.setTextSize(40);
            for(Button b: buttonList){
                b.setTextSize(medSize);
            }

        } else{
            title.setTextSize(50);
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

    public void onLevelSelectClick(View v)
    {
        Intent intent = new Intent(this, LevelTypeSelect.class);
        startActivity(intent);
    }

    public void onNotesClick(View v)
    {
        Intent intent = new Intent(this, NoteTakingActivity.class);
        startActivity(intent);
    }
    public void onStatsClick (View v){
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }
}