package com.theironyard.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateChildActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mName;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mEmail;
    private EditText mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_child);

        mName = (EditText)findViewById(R.id.childNameText);
        mPassword = (EditText)findViewById(R.id.childPasswordText);
        mConfirmPassword = (EditText)findViewById(R.id.childConfirmPasswordText);
        mEmail = (EditText)findViewById(R.id.childEmailText);
        mPhone = (EditText)findViewById(R.id.childPhoneText);
    }

    @Override
    public void onClick(View view) {

    }
}
