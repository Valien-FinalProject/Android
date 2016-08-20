package com.theironyard.finalproject;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.theironyard.finalproject.command.TokenCommand;
import com.theironyard.finalproject.command.UserCommand;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Callback;

public class ParentLoginActivity extends AppCompatActivity {

    public static final String TAG = ParentLoginActivity.class.getSimpleName();

    @Bind(R.id.parentLoginUsernameText)
    EditText mParentLoginUsernameText;

    @Bind(R.id.parentLoginPasswordText)
    EditText mParentLoginPasswordText;

    @Bind(R.id.parentSignInButton)
    Button mParentLoginSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.parentSignInButton)
    public void onClick(View view) {
        Snackbar.make(view, "You clicked login!", Snackbar.LENGTH_LONG).show();

        ParentChoreService choreService = new ParentChoreService();
        String username = mParentLoginUsernameText.getText().toString();
        String password = mParentLoginPasswordText.getText().toString();
        UserCommand userCommand = new UserCommand(username, password);

        choreService.getLoginApi().getParentToken(userCommand)
                .enqueue(new Callback<TokenCommand>() {
                    @Override
                    public void onResponse(retrofit2.Call<TokenCommand> call, retrofit2.Response<TokenCommand> response) {
                        if(response.code() == 200){
                            TokenCommand tokenCommand = response.body();
                            ParentChoreService.saveToken(tokenCommand.getToken());
                            startActivity(new Intent(ParentLoginActivity.this, ParentProfileActivity.class));
                        }
                        else{
                            Snackbar.make(mParentLoginSignInButton, "Unable to Login.Try again", Snackbar.LENGTH_LONG);
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<TokenCommand> call, Throwable t) {
                        Snackbar.make(mParentLoginSignInButton, "System Failed.Try again", Snackbar.LENGTH_LONG);
                        Log.e(TAG, "API is throwing errors", t);
                    }
                });


    }
}

