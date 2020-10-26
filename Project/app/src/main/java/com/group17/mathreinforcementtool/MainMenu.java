package com.group17.mathreinforcementtool;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }
    //swap to settings activity
    public void onClickSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
    //swap to multiple choice (questions)
    public void onClickMultipleChoice(View view){
        Intent intent = new Intent(this, multipleChoice.class);
        startActivity(intent);
    }

    /*
    ----------------------------------------------------
    Parameters:   v (View)
    Return:       None
    Description:  -Called when the buttons related to non-MC Addition are clicked
                  -Records difficulty ('Easy', 'Medium', 'Hard') the user chose and
                  switches to MathTemplateActivity.
                  -!! Although startActivityForResult() is called,
                  onActivityResult() is not yet implemented.
    ----------------------------------------------------
    */
    public void onAdditionClick(View v)
    {
        Button currentButton = findViewById(v.getId());
        String buttonText = currentButton.getText().toString();
        Intent i = new Intent(this, MathTemplateActivity.class);
        i.putExtra("AdditionDifficulty", buttonText);
        startActivityForResult(i, 10);
    }
}