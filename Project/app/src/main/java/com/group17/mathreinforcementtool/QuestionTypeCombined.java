package com.group17.mathreinforcementtool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class QuestionTypeCombined extends Fragment {
    protected static final String ACTIVITY_NAME = "AlgebraFragment";

    public QuestionTypeCombined() {
    }

    SharedPreferences fontPreference;
    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;
    List<Button> btnList = new ArrayList<Button>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_type_combined, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set the button's onClickListeners with the correct Intents
        Button btnMCQ = view.findViewById(R.id.btnMCQ);
        Button btnCalculate = view.findViewById(R.id.btnCalculate);
        Button btnTestLvl = view.findViewById(R.id.btnTestLevelSelect);
        btnList.addAll((Collection<? extends Button>) Arrays.asList(btnMCQ, btnCalculate));
        btnMCQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCQ");
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
        btnTestLvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "TestLvl");
                startActivity(intent);
            }
        });

        fontPreference = this.getActivity().getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        if (fontPreference.getInt("Size", medSize) == 15) {
            for (Button b : btnList) {
                b.setTextSize(10);
            }
        } else if (fontPreference.getInt("Size", medSize) == 20) {
            for (Button b : btnList) {
                b.setTextSize(15);
            }
        } else {
            for (Button b : btnList) {
                b.setTextSize(20);
            }

        }
    }
}