package com.theironyard.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private Button mRegisterButton;
    private Button mPLoginButton;
    private Button mCLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mRegisterButton = (Button)findViewById(R.id.registerButton);
        mPLoginButton = (Button)findViewById(R.id.pLoginButton);
        mCLoginButton = (Button)findViewById(R.id.cLoginButton);

        mRegisterButton.setOnClickListener(this);
        mPLoginButton.setOnClickListener(this);
        mCLoginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        if(view == mRegisterButton){
            startRegistrationActivity();
        }
        else if(view == mPLoginButton){
            startPLoginActivity();
        }
        else if(view == mCLoginButton){
            startCLoginActivity();
        }
    }
    private void startRegistrationActivity() {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }
    private void startPLoginActivity() {
        Intent intent = new Intent(this, ParentLoginActivity.class);
        startActivity(intent);
    }
    private void startCLoginActivity() {
        Intent intent = new Intent(this, ChildLoginActivity.class);
        startActivity(intent);
    }
}
