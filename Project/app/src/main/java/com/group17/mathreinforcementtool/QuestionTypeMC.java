package com.group17.mathreinforcementtool;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


public class    QuestionTypeMC extends Fragment {
    protected static final String ACTIVITY_NAME = "MCFragment";

    SharedPreferences fontPreference;
    SharedPreferences darkPreference;
    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;
    List<Button> btnList = new ArrayList<Button>();
    List<TextView> textViewList = new ArrayList<TextView>();
    public QuestionTypeMC() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_question_type_mc, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Set the button's onClickListeners with the correct Intents
        Button btnMCStanAdd = view.findViewById(R.id.btnMCStanAdd);
        Button btnMCTimerAdd = view.findViewById(R.id.btnMCTimeAdd);
        Button btnMCStreakAdd = view.findViewById(R.id.btnMCStreakAdd);
        Button btnMCStanSub = view.findViewById(R.id.btnMCStanSub);
        Button btnMCTimerSub = view.findViewById(R.id.btnMCTimeSub);
        Button btnMCStreakSub = view.findViewById(R.id.btnMCStreakSub);
        Button btnMCStanMulti = view.findViewById(R.id.btnMCStanMulti);
        Button btnMCTimerMulti = view.findViewById(R.id.btnMCTimeMulti);
        Button btnMCStreakMulti = view.findViewById(R.id.btnMCStreakMulti);
        Button btnMCStanDiv = view.findViewById(R.id.btnMCStanDiv);
        Button btnMCTimerDiv = view.findViewById(R.id.btnMCTimeDiv);
        Button btnMCStreakDiv = view.findViewById(R.id.btnMCStreakDiv);
        btnList.addAll((Collection<? extends Button>) Arrays.asList(btnMCStanAdd, btnMCTimerAdd, btnMCStreakAdd, btnMCStanSub, btnMCTimerSub, btnMCStreakSub, btnMCStanMulti, btnMCTimerMulti, btnMCStreakMulti, btnMCStanDiv, btnMCTimerDiv, btnMCStreakDiv));
        btnMCStanAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCStandardAddition");
                startActivity(intent);
            }
        });
        btnMCTimerAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCTimerAddition");
                startActivity(intent);
            }
        });
        btnMCStreakAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCStreakAddition");
                startActivity(intent);
            }
        });
        btnMCStanSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCStandardSubtraction");
                startActivity(intent);
            }
        });
        btnMCTimerSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCTimerSubtraction");
                startActivity(intent);
            }
        });
        btnMCStreakSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCStreakSubtraction");
                startActivity(intent);
            }
        });
        btnMCStanMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCStandardMultiplication");
                startActivity(intent);
            }
        });
        btnMCTimerMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCTimerMultiplication");
                startActivity(intent);
            }
        });
        btnMCStreakMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCStreakMultiplication");
                startActivity(intent);
            }
        });
        btnMCStanDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCStandardDivision");
                startActivity(intent);
            }
        });
        btnMCTimerDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCTimerDivision");
                startActivity(intent);
            }
        });
        btnMCStreakDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LevelSelect.class);
                intent.putExtra("requestCode", "MCStreakDivision");
                startActivity(intent);
            }
        });

        fontPreference = this.getActivity().getSharedPreferences("FontSize", Context.MODE_PRIVATE);
        darkPreference = this.getActivity().getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        textViewList.addAll((Collection<? extends TextView>) Arrays.asList((TextView) view.findViewById(R.id.txtAddition), (TextView) view.findViewById(R.id.txtSubtraction), (TextView) view.findViewById(R.id.txtMultiplication), (TextView) view.findViewById(R.id.txtDivision)
        ));


        if(darkPreference.getBoolean("DarkStatus", true) == true){
            for (TextView t : textViewList) {
                t.setTextColor(Color.WHITE);
            }
        }
        else{
            for (TextView t : textViewList) {
                t.setTextColor(Color.BLACK);
            }
        }
        if (fontPreference.getInt("Size", medSize) == 15) {
            for (Button b : btnList) {
                b.setTextSize(10);
            }
            for (TextView t : textViewList) {
                t.setTextSize(15);
            }
        } else if (fontPreference.getInt("Size", medSize) == 20) {
            for (Button b : btnList) {
                b.setTextSize(15);
            }
            for (TextView t : textViewList) {
                t.setTextSize(20);
            }
        } else {
            for (Button b : btnList) {
                b.setTextSize(20);
            }
            for (TextView t : textViewList) {
                t.setTextSize(25);
            }
        }
    }
}