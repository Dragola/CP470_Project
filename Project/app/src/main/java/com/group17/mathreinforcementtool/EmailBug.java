package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EmailBug extends AppCompatActivity {
    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;


    SharedPreferences darkPreference;
    SharedPreferences fontPreference;
    ConstraintLayout layout;

    TextView greeting;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_bug);

        greeting = findViewById(R.id.greetingsText);
        sendButton = findViewById(R.id.buttonSend);

        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);

        layout = (ConstraintLayout) findViewById(R.id.Email);

        if(darkPreference.getBoolean("DarkStatus", true) == true){
            layout.setBackgroundColor(Color.BLACK);
            greeting.setTextColor(Color.WHITE);
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
            greeting.setTextColor(Color.BLACK);
        }
        if(fontPreference.getInt("Size", medSize) == smallSize){
            greeting.setTextSize(smallSize);
            sendButton.setTextSize(smallSize);
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            greeting.setTextSize(medSize);
            sendButton.setTextSize(medSize);
        }
        else{
            greeting.setTextSize(largeSize);
            sendButton.setTextSize(largeSize);
        }
    }
    protected void onResume(){
        super.onResume();
        Log.i("OnResume", "In On Resume");
        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            greeting.setTextColor(Color.WHITE);
        } else {
            layout.setBackgroundColor(Color.WHITE);
            greeting.setTextColor(Color.BLACK);
        }

        if(fontPreference.getInt("Size", medSize) == smallSize){
            greeting.setTextSize(smallSize);
            sendButton.setTextSize(smallSize);
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            greeting.setTextSize(medSize);
            sendButton.setTextSize(medSize);
        }
        else{
            greeting.setTextSize(largeSize);
            sendButton.setTextSize(largeSize);
        }
    }

    //    This is here to swap to onResume on back so that flicking the "DarkSwitch" actually works without having to close down the app lmao
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
    }
}