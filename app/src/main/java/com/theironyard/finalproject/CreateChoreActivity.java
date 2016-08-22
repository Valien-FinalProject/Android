package com.theironyard.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class CreateChoreActivity extends AppCompatActivity {

    EditText mDescription;

    EditText mEndDate;

    EditText mName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chore);
    }
}
