package com.group17.mathreinforcementtool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class QuestionTypeAlgebra extends Fragment {
    protected static final String ACTIVITY_NAME = "AlgebraFragment";

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
    }
}