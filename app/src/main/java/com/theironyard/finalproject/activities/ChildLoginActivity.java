package com.theironyard.finalproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.theironyard.finalproject.R;

import butterknife.Bind;

public class ChildLoginActivity extends AppCompatActivity {

    public static final String TAG = ChildLoginActivity.class.getSimpleName();

    @Bind(R.id.cUsernameText)
    EditText mChildUsernameText;

    @Bind(R.id.cPasswordText)
    EditText mPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_login);
    }
}
