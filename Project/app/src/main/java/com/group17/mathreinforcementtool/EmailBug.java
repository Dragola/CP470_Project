package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class EmailBug extends AppCompatActivity {
    int smallSize = 15;
    int medSize = 20;
    int largeSize = 25;


    SharedPreferences darkPreference;
    SharedPreferences fontPreference;
    SharedPreferences bugPreference;
    SharedPreferences.Editor bugEditor;
    ConstraintLayout layout;

    TextView greeting;
    Button sendButton;
    EditText bugText;

//    List<String> bugList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_bug);

        greeting = findViewById(R.id.greetingsText);
        sendButton = findViewById(R.id.buttonSend);
        bugText = findViewById(R.id.bugText);

        darkPreference = getSharedPreferences("DarkStatus", Context.MODE_PRIVATE);
        fontPreference = getSharedPreferences("FontSize", Context.MODE_PRIVATE);
        bugPreference = getSharedPreferences("BugList", Context.MODE_PRIVATE);
        bugEditor = bugPreference.edit();


        layout = (ConstraintLayout) findViewById(R.id.Email);

        if(darkPreference.getBoolean("DarkStatus", true) == true){
            layout.setBackgroundColor(Color.BLACK);
            greeting.setTextColor(Color.WHITE);
            bugText.setTextColor(Color.WHITE);
        }
        else{
            layout.setBackgroundColor(Color.WHITE);
            greeting.setTextColor(Color.BLACK);
            bugText.setTextColor(Color.BLACK);
        }
        if(fontPreference.getInt("Size", medSize) == smallSize){
            greeting.setTextSize(smallSize);
            sendButton.setTextSize(smallSize);
            bugText.setTextSize(smallSize);
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            greeting.setTextSize(medSize);
            sendButton.setTextSize(medSize);
            bugText.setTextSize(medSize);
        }
        else{
            greeting.setTextSize(largeSize);
            sendButton.setTextSize(largeSize);
            bugText.setTextSize(largeSize);
        }
    }
    protected void onResume(){
        super.onResume();
        Log.i("OnResume", "In On Resume");
        if (darkPreference.getBoolean("DarkStatus", true) == true) {
            layout.setBackgroundColor(Color.BLACK);
            greeting.setTextColor(Color.WHITE);
            bugText.setTextColor(Color.WHITE);
        } else {
            layout.setBackgroundColor(Color.WHITE);
            greeting.setTextColor(Color.BLACK);
            bugText.setTextColor(Color.BLACK);
        }

        if(fontPreference.getInt("Size", medSize) == smallSize){
            greeting.setTextSize(smallSize);
            sendButton.setTextSize(smallSize);
            bugText.setTextSize(smallSize);
        }
        else if(fontPreference.getInt("Size", medSize) == medSize){
            greeting.setTextSize(medSize);
            sendButton.setTextSize(medSize);
            bugText.setTextSize(medSize);
        }
        else{
            greeting.setTextSize(largeSize);
            sendButton.setTextSize(largeSize);
            bugText.setTextSize(largeSize);
        }
    }

    //    This is here to swap to onResume on back so that flicking the "DarkSwitch" actually works without having to close down the app lmao
    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
    }
    public void onBugButtonClick(View view) {
        bugText = findViewById(R.id.bugText);
        String[] TO = {"mecrocon@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Bug Problems");
        emailIntent.putExtra(Intent.EXTRA_TEXT, bugText.getText().toString());
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        bugText.setText("");
    }
}
