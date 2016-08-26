package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.command.TokenCommand;
import com.theironyard.finalproject.command.UserCommand;
import com.theironyard.finalproject.services.ChildChoreService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildLoginActivity extends AppCompatActivity {

    public static final String TAG = ChildLoginActivity.class.getSimpleName();

    @Bind(R.id.cUsernameText)
    EditText mChildUsernameText;

    @Bind(R.id.cPasswordText)
    EditText mPasswordText;

    @Bind(R.id.cSignInButton)
    Button mChildSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.cSignInButton)
    public void onClick(View view){

        ChildChoreService choreService = new ChildChoreService();
        String username = mChildUsernameText.getText().toString();
        String password = mPasswordText.getText().toString();
        UserCommand userCommand = new UserCommand(username, password);

        choreService.getLoginApi()
                .getChildToken(userCommand)
                    .enqueue(new Callback<TokenCommand>() {
                        @Override
                        public void onResponse(retrofit2.Call<TokenCommand> call, retrofit2.Response<TokenCommand> response) {
                            if (response.code() == 200){
                                TokenCommand tokenCommand = response.body();
                                ChildChoreService.saveToken(tokenCommand.getToken());
                                ChildChoreService.setChildId(tokenCommand.getId());
                                startActivity(new Intent(ChildLoginActivity.this, ChildProfileActivity.class));
                            }else {
                                Snackbar.make(mChildSignInButton, "Unable to Login. Please try again", Snackbar.LENGTH_LONG).show();
                            }
                        }

                    @Override
                    public void onFailure(retrofit2.Call<TokenCommand> call, Throwable t) {
                        Snackbar.make(mChildSignInButton, "System Failed. Try again", Snackbar.LENGTH_LONG).show();
                        Log.e(TAG, "API is trippin'!", t);
                    }
                });
    }

}
