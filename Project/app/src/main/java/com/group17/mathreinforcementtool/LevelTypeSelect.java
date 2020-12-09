package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class LevelTypeSelect extends AppCompatActivity {
    SharedPreferences fontPreference;
    SharedPreferences darkPreference;
    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;
    List<Button> btnList = new ArrayList<Button>();
    ConstraintLayout layout;
    TextView titleString;
    FrameLayout fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_type_select);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);
        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        titleString =  findViewById(R.id.textViewLevelTypeSelect);
        fragment = findViewById(R.id.flLevelType);

        btnList.addAll((Collection<? extends Button>) Arrays.asList((Button) findViewById(R.id.buttonTypeAlgebra), (Button) findViewById(R.id.buttonTypeGeometry),(Button) findViewById(R.id.buttonTypeMC)));

        layout = findViewById(R.id.lvlSelect);

        if(darkPreference.getBoolean("DarkStatus", true) == true){
            layout.setBackgroundColor(Color.BLACK);
           titleString.setTextColor(Color.WHITE);
           fragment.setBackgroundColor(Color.parseColor("#332F2F"));
        }
        if (fontPreference.getInt("Size", medSize) == 15) {
            for (Button b : btnList) {
                b.setTextSize(10);
            }
            titleString.setTextSize(20);
        } else if (fontPreference.getInt("Size", medSize) == 20) {
            for (Button b : btnList) {
                b.setTextSize(15);
            }
            titleString.setTextSize(25);
        } else {
            for (Button b : btnList) {
                b.setTextSize(20);
            }
            titleString.setTextSize(30);
        }
    }

    // Loads the Algebra fragment when the button is clicked
    public void onClickAlgebra(View view){
        QuestionTypeAlgebra algebraFragment = new QuestionTypeAlgebra();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flLevelType, algebraFragment, "Algebra_Fragment");
        ft.commit();
    }

    // Loads the Geometry fragment when the button is clicked
    public void onClickGeometry(View view){
        QuestionTypeGeometry geometryFragment = new QuestionTypeGeometry();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flLevelType, geometryFragment, "Geometry_Fragment");
        ft.commit();
    }

    // Loads the MC fragment when the button is clicked
    public void onClickMC(View view){
        QuestionTypeMC MCFragment = new QuestionTypeMC();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flLevelType, MCFragment, "MC_Fragment");
        ft.commit();
    }
}