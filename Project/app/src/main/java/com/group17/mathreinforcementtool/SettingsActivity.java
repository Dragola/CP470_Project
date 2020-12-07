package com.group17.mathreinforcementtool;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SettingsActivity extends AppCompatActivity {
    Switch darkSwitch;
//Test
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

    Snackbar temp;


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
        textViewList.addAll((Collection<? extends TextView>) Arrays.asList((TextView) findViewById(R.id.Title), (TextView) findViewById(R.id.FontLabel)));

        layout = (RelativeLayout) findViewById(R.id.Settings);

        if(darkPreference.getBoolean("DarkStatus", true) == true){
            layout.setBackgroundColor(Color.BLACK);
            darkSwitch.setChecked(true);
            darkSwitch.setTextColor(Color.WHITE);
            for (RadioButton r: radioButtonList){
                r.setTextColor(Color.WHITE);
                r.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
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
        }
    }
    public boolean onCreateOptionsMenu (Menu m){
        getMenuInflater().inflate(R.menu.toolbar_menu, m );
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mi) {
        switch (mi.getItemId()) {
            case R.id.AgreementButton: Log.d("Toolbar", "You pressed the Agreement Button, this will be implemented soon");
                AlertDialog.Builder builderPol = new AlertDialog.Builder(this);
                builderPol.setTitle(R.string.agreementButton);
                builderPol.setMessage(R.string.userAgreement);

                builderPol.setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        temp = Snackbar.make(layout, R.string.userAgreed, Snackbar.LENGTH_LONG);
                        temp.show();
                    }
                });
                builderPol.setNegativeButton(R.string.btnBack, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });

                AlertDialog dialogPol = builderPol.create();
                dialogPol.show();
                break;
            case R.id.PolicyButton: Log.d("Toolbar", "You pressed the Policy Button, this will be implemented soon");
                AlertDialog.Builder builderAgree = new AlertDialog.Builder(this);
                builderAgree.setTitle(R.string.privacyPolicyButton);
                builderAgree.setMessage(R.string.privacyPolicy);

                builderAgree.setPositiveButton(R.string.btnOK, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        temp = Snackbar.make(layout, R.string.userAgreed, Snackbar.LENGTH_LONG);
                        temp.show();
                    }
                });
                builderAgree.setNegativeButton(R.string.btnBack, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.exit(0);
                    }
                });

                AlertDialog dialogAgree = builderAgree.create();
                dialogAgree.show();
                break;

            case R.id.BugButton: Log.d("Toolbar", "You pressed the Bug Button");
                Intent intent = new Intent(this, EmailBug.class);
                startActivity(intent);

                break;
            case R.id.about: Toast toast = Toast.makeText(this, "This is the app called Simply Calculated.  Created by: Amandeep Toora, Bryan Gadd, Colin Sweitzer, Giuseppe Lombardo, and Riley Voigt", Toast.LENGTH_LONG);
                toast.show();
                break;
        }
        return true;
    }

    public void onDarkClick(View v){
        if (darkSwitch.isChecked()){
            darkEditor.putBoolean("DarkStatus", true);
            layout.setBackgroundColor(Color.BLACK);
            darkSwitch.setTextColor(Color.WHITE);
            for (RadioButton r: radioButtonList){
                r.setTextColor(Color.WHITE);
                r.setButtonTintList(ColorStateList.valueOf(Color.WHITE));
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
                r.setButtonTintList(ColorStateList.valueOf(Color.BLACK));
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
        fontEditor.putInt("Size", largeSize);
        fontEditor.commit();
    }
}