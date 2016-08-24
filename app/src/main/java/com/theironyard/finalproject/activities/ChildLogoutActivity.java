package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.theironyard.finalproject.R;

public class ChildLogoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_logout);
    }

    private void startChildLoginActivity() {
        Intent intent = new Intent(this, ChildLoginActivity.class);
        startActivity(intent);
    }
}
