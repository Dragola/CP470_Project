package com.group17.mathreinforcementtool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class QuestionTypeAlgebra extends Fragment {
    protected static final String ACTIVITY_NAME = "AlgebraFragment";

    SharedPreferences fontPreference;
    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;
    List<Button> btnList = new ArrayList<Button>();
    public QuestionTypeAlgebra() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_type_algebra, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set the button's onClickListeners with the correct Intents
        Button btnAddition = view.findViewById(R.id.btnAddition);
        Button btnSubtraction = view.findViewById(R.id.btnSubtraction);
        Button btnMultiplication = view.findViewById(R.id.btnMultiplication);
        Button btnDivision = view.findViewById(R.id.btnDivision);
        Button btnCalculate = view.findViewById(R.id.btnCalculate);
        btnList.addAll((Collection<? extends Button>) Arrays.asList(btnAddition,btnDivision,btnMultiplication,btnSubtraction,btnCalculate));
        btnAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "Addition");
                startActivity(intent);
            }
        });
        btnSubtraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "Subtraction");
                startActivity(intent);
            }
        });
        btnMultiplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "Multiplication");
                startActivity(intent);
            }
        });
        btnDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "Division");
                startActivity(intent);
            }
        });
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "Algebra");
                startActivity(intent);
            }
        });

        fontPreference = this.getActivity().getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        if(fontPreference.getInt("Size", medSize) == 15){
            for(Button b: btnList){
                b.setTextSize(10);
            }
        }
        else if(fontPreference.getInt("Size", medSize) == 20){
            for(Button b: btnList){
                b.setTextSize(15);
            }
        } else{
            for(Button b: btnList){
                b.setTextSize(20);
            }
        }
    }
}