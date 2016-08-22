package com.theironyard.finalproject;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.theironyard.finalproject.command.TokenCommand;
import com.theironyard.finalproject.command.UserCommand;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateChildActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = CreateChildActivity.class.getSimpleName();

    @Bind(R.id.createChildNameText)
    EditText mName;

    @Bind(R.id.createChildUsernameText)
    EditText mUsername;

    @Bind(R.id.createChildPasswordText)
    EditText mPassword;

    @Bind(R.id.createChildConfirmPasswordText)
    EditText mConfirmPassword;

    @Bind(R.id.createChildEmailText)
    EditText mEmail;

    @Bind(R.id.createChildPhoneText)
    EditText mPhone;

    @Bind(R.id.createChildButton)
    Button mCreateChildButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_child);
        ButterKnife.bind(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @OnClick(R.id.createChildButton)
    public void onClick(View view) {

        ParentChoreService parentChoreService = new ParentChoreService();
        String name = mName.getText().toString();
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();
        String phone = mPhone.getText().toString();
        final UserCommand userCommand = new UserCommand(username, password, name, email, phone);

        try {
            parentChoreService.getParentApi().getChildInfo(userCommand)
                    .enqueue(new Callback<UserCommand>() {
                        @Override
                        public void onResponse(Call<UserCommand> call, Response<UserCommand> response) {
                            if (response.code() == 200) {
                                UserCommand userCommand = response.body();
                                ParentChoreService.saveUser(userCommand);
                                Snackbar.make(mCreateChildButton,"You have created a new child!", Snackbar.LENGTH_LONG);
                                setDefaultValues();
                                startActivity(new Intent(CreateChildActivity.this, ParentProfileActivity.class));
                            } else {
                                Snackbar.make(mCreateChildButton, "Unable to Create New Child.Try again", Snackbar.LENGTH_LONG);
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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateChild Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.theironyard.finalproject/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "CreateChild Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.theironyard.finalproject/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }
}
