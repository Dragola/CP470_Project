package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LevelTypeSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_type_select);
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

    // Loads the Combined fragment when the button is clicked
    public void onClickCombined(View view){
        QuestionTypeCombined combinedFragment = new QuestionTypeCombined();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flLevelType, combinedFragment, "Combined_Fragment");
        ft.commit();
    }
}