package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.theironyard.finalproject.services.ParentChoreService;
import com.theironyard.finalproject.R;
import com.theironyard.finalproject.command.UserCommand;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    @Bind(R.id.regNameText)
    EditText mName;

    @Bind(R.id.regUsernameText)
    EditText mUsername;

    @Bind(R.id.regPasswordText)
    EditText mPassword;

    @Bind(R.id.regConfirmPasswordText)
    EditText mConfirmPassword;

    @Bind(R.id.regEmailText)
    EditText mEmail;

    @Bind(R.id.regPhoneText)
    EditText mPhone;

    @Bind(R.id.regEmailOptInCheckBox)
    CheckBox mEmailOptIn;

    @Bind(R.id.regPhoneOptInCheckBox)
    CheckBox mPhoneOptIn;

    @Bind(R.id.regButton)
    Button mRegistrationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.regButton)
    public void onClick(View view) {

        ParentChoreService parentChoreService = new ParentChoreService();
        String name = mName.getText().toString();
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();
        String phone = mPhone.getText().toString();
        boolean emailOptIn = mEmailOptIn.isChecked();
        boolean phoneOptIn = mPhoneOptIn.isChecked();

        UserCommand userCommand = new UserCommand(username, password, name, email,
                phone, emailOptIn, phoneOptIn);

        try {
        parentChoreService.getParentApi().getParentInfo(userCommand)
                .enqueue(new Callback<UserCommand>() {
                    @Override
                    public void onResponse(Call<UserCommand> call, Response<UserCommand> response) {
                        if (response.code() == 200) {
                            UserCommand userCommand = response.body();
                            ParentChoreService.saveUser(userCommand);
                            Snackbar.make(mRegistrationButton,"You have successfully registered!", Snackbar.LENGTH_LONG);
                            setDefaultValues();
                            startActivity(new Intent(RegistrationActivity.this, ParentProfileActivity.class));
                        } else {
                            Snackbar.make(mRegistrationButton, "Unable to register you. Please try again", Snackbar.LENGTH_LONG);
                        }
                    }
                    @Override
                    public void onFailure(Call<UserCommand> call, Throwable t) {

                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDefaultValues() {
        mName.setText("");
        mUsername.setText("");
        mPassword.setText("");
        mConfirmPassword.setText("");
        mEmail.setText("");
        mPhone.setText("");
    }

}
