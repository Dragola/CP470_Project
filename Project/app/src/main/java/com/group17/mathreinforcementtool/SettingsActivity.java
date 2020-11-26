package com.group17.mathreinforcementtool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SettingsActivity extends AppCompatActivity {
    Switch darkSwitch;

    List<RadioButton> radioButtonList = new ArrayList<RadioButton>();
    List<TextView> textViewList = new ArrayList<TextView>();
    List<Button> buttonList = new ArrayList<Button>();

    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;

    SharedPreferences darkPreference;
    SharedPreferences fontPreference;
    SharedPreferences.Editor darkEditor;
    SharedPreferences.Editor fontEditor;

    RelativeLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);
        darkEditor = darkPreference.edit();
        fontEditor = fontPreference.edit();

        darkSwitch = findViewById(R.id.DarkSwitch);

        radioButtonList.addAll((Collection<? extends RadioButton>) Arrays.asList((RadioButton) findViewById(R.id.SmallButton), (RadioButton) findViewById(R.id.MedButton), (RadioButton) findViewById(R.id.LargeButton)));
        textViewList.addAll((Collection<? extends TextView>) Arrays.asList((TextView) findViewById(R.id.Title), (TextView) findViewById(R.id.FontLabel), (TextView) findViewById(R.id.VolumeLabel)));
        buttonList.addAll((Collection<? extends Button>) Arrays.asList((Button) findViewById(R.id.AgreementButton), (Button) findViewById(R.id.PolicyButton), (Button) findViewById(R.id.RestartButton),(Button) findViewById(R.id.BugButton)));

        layout = (RelativeLayout) findViewById(R.id.Settings);

        if(darkPreference.getBoolean("DarkStatus", true) == true){
            layout.setBackgroundColor(Color.BLACK);
            darkSwitch.setChecked(true);
            darkSwitch.setTextColor(Color.WHITE);
            for (RadioButton r: radioButtonList){
                r.setTextColor(Color.WHITE);
            }
            for (TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }

        }
        if(fontPreference.getInt("Size", medSize) == smallSize){
            for (RadioButton r: radioButtonList){
                r.setTextSize(smallSize);
                r.setChecked(false);
            }
            radioButtonList.get(0).setChecked(true);

            for (TextView t: textViewList){
                t.setTextSize(smallSize);
            }
            textViewList.get(0).setTextSize(30);
            textViewList.get(1).setTextSize(20);
            ;
            for (Button b: buttonList){
                b.setTextSize(smallSize);
            }
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            for (RadioButton r: radioButtonList){
                r.setTextSize(medSize);
                r.setChecked(false);
            }
            radioButtonList.get(1).setChecked(true);

            for (TextView t: textViewList){
                t.setTextSize(medSize);
            }
            textViewList.get(0).setTextSize(45);
            textViewList.get(1).setTextSize(25);
            for (Button b: buttonList){
                b.setTextSize(medSize);
            }
        }
        else{
            for (RadioButton r: radioButtonList){
                r.setTextSize(largeSize);
                r.setChecked(false);
            }
            radioButtonList.get(2).setChecked(true);

            for (TextView t: textViewList){
                t.setTextSize(largeSize);
            }
            textViewList.get(0).setTextSize(60);
            textViewList.get(1).setTextSize(30);

            for (Button b: buttonList){
                b.setTextSize(largeSize);
            }
        }
    }
    public void onDarkClick(View v){
        if (darkSwitch.isChecked()){
            darkEditor.putBoolean("DarkStatus", true);
            layout.setBackgroundColor(Color.BLACK);
            darkSwitch.setTextColor(Color.WHITE);
            for (RadioButton r: radioButtonList){
                r.setTextColor(Color.WHITE);
            }
            for (TextView t: textViewList){
                t.setTextColor(Color.WHITE);
            }
        }
        else{
            darkEditor.putBoolean("DarkStatus", false);
            layout.setBackgroundColor(Color.WHITE);
            darkSwitch.setTextColor(Color.BLACK);
            for (RadioButton r: radioButtonList){
                r.setTextColor(Color.BLACK);
            }
            for (TextView t: textViewList){
                t.setTextColor(Color.BLACK);
            }
        }
        darkEditor.commit();
    }
    public void onSmallClick(View v){
        for (RadioButton r: radioButtonList){
            r.setTextSize(smallSize);
            r.setChecked(false);
        }
        radioButtonList.get(0).setChecked(true);

        for (TextView t: textViewList){
            t.setTextSize(smallSize);
        }
        textViewList.get(0).setTextSize(30);
        textViewList.get(1).setTextSize(20);
        for (Button b: buttonList){
            b.setTextSize(smallSize);
        }
        fontEditor.putInt("Size", smallSize);
        fontEditor.commit();
    }
    public void onMedClick(View v){
        for (RadioButton r: radioButtonList){
            r.setTextSize(medSize);
            r.setChecked(false);
        }
        radioButtonList.get(1).setChecked(true);

        for (TextView t: textViewList){
            t.setTextSize(medSize);
        }
        textViewList.get(0).setTextSize(45);
        textViewList.get(1).setTextSize(25);

        for (Button b: buttonList){
            b.setTextSize(medSize);
        }
        fontEditor.putInt("Size", medSize);
        fontEditor.commit();
    }
    public void onLargeClick(View v){
        for (RadioButton r: radioButtonList){
            r.setTextSize(largeSize);
            r.setChecked(false);
        }
        radioButtonList.get(2).setChecked(true);

        for (TextView t: textViewList){
            t.setTextSize(largeSize);
        }
        textViewList.get(0).setTextSize(60);
        textViewList.get(1).setTextSize(30);
        for (Button b: buttonList){
            b.setTextSize(largeSize);
        }
        fontEditor.putInt("Size", largeSize);
        fontEditor.commit();
    }
    public void onRestartClick(View v){
//This might not even be in settings lmaooooooooooooo
    }
    public void onBugClick(View v){
//Figure out how to send an email
    }
    public void onAgreeClick(View v){

    }
    public void onPolicyClick(View v){

    }
}