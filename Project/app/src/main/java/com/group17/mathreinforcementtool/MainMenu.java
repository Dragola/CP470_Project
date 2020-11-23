package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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

        textViewList.addAll((Collection<? extends TextView>) Arrays.asList((TextView) findViewById(R.id.textMenuMultiple), (TextView) findViewById(R.id.textMenuAddition), (TextView) findViewById(R.id.textMenuSubtraction), (TextView) findViewById(R.id.textMenuMultiple), (TextView) findViewById(R.id.textMenuDivision)));
        littleButtonList.addAll((Collection<? extends Button>) Arrays.asList((Button) findViewById(R.id.easyAdd), (Button) findViewById(R.id.medAdd), (Button) findViewById(R.id.hardAdd), (Button) findViewById(R.id.easyMult), (Button) findViewById(R.id.medMult), (Button) findViewById(R.id.hardMult), (Button) findViewById(R.id.easySub), (Button) findViewById(R.id.medSub), (Button) findViewById(R.id.hardSub), (Button) findViewById(R.id.easyDiv), (Button) findViewById(R.id.medDiv), (Button) findViewById(R.id.hardDiv)));
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
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
    }

    //swap to settings activity
    public void onClickSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    //easy multiple choice
    public void onClickEasyMultipleChoice(View view){
        Intent intent = new Intent(this, multipleChoice.class);
        intent.putExtra("difficulty", 0);
        intent.putExtra("type", 2);
        startActivity(intent);
    }
    //medium multiple choice
    public void onClickMediumMultipleChoice(View view){
        Intent intent = new Intent(this, multipleChoice.class);
        intent.putExtra("difficulty", 1);
        intent.putExtra("type", 0);
        startActivity(intent);
    }
    //medium multiple choice
    public void onClickHardMultipleChoice(View view){
        Intent intent = new Intent(this, multipleChoice.class);
        intent.putExtra("difficulty", 2);
        intent.putExtra("type", 0);
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
}