package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.command.UserCommand;
import com.theironyard.finalproject.services.ParentChoreService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    @Bind(R.id.updateProfileNameText)
    EditText mName;

    @Bind(R.id.updateProfileUsernameText)
    EditText mUsername;

    @Bind(R.id.updateProfilePasswordText)
    EditText mPassword;

    @Bind(R.id.updateProfileConfirmPasswordText)
    EditText mConfirmPassword;

    @Bind(R.id.updateProfileEmailText)
    EditText mEmail;

    @Bind(R.id.updateProfilePhoneText)
    EditText mPhone;

    @Bind(R.id.updateProfileEmailOptInCheckBox)
    CheckBox mEmailOptIn;

    @Bind(R.id.updateProfilePhoneOptInCheckBox)
    CheckBox mPhoneOptIn;

    @Bind(R.id.updateProfileButton)
    Button mUpdateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.updateProfileButton)
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
            parentChoreService.getParentApi().updateParentInfo(userCommand)
                    .enqueue(new Callback<UserCommand>() {
                        @Override
                        public void onResponse(Call<UserCommand> call, Response<UserCommand> response) {
                            if (response.code() == 200) {
                                UserCommand userCommand = response.body();
                                ParentChoreService.saveUser(userCommand);
                                Snackbar.make(mUpdateButton,"You have updated your profile!", Snackbar.LENGTH_LONG).show();
                                setDefaultValues();
                                startActivity(new Intent(UpdateProfileActivity.this, ParentProfileActivity.class));
                            } else {
                                Snackbar.make(mUpdateButton, "Unable to update your profile. Please try again", Snackbar.LENGTH_LONG).show();
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
