package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    /************************************
     * Navigation
     ************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.parent_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();

        if (id == R.id.parent_home){
            startParentProfileActivity();
            return true;
        }
        else if(id == R.id.nav_view_wishlists){
            startParentViewWishlistActivity();
            return true;
        }
        else if(id == R.id.nav_create_child){
            startCreateChildActivity();
            return true;
        }
        else if(id == R.id.nav_create_reward){
            startCreateRewardActivity();
            return true;
        }
        else if(id == R.id.nav_create_chore){
            startCreateChoresActivity();
            return true;
        }
        else if(id == R.id.nav_update){
            startUpdateProfileActivity();
            return true;
        }
        else if(id == R.id.nav_logout){
            startParentLogoutActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startParentProfileActivity() {
        Intent intent = new Intent(this, ParentProfileActivity.class);
        startActivity(intent);
    }
    private void startParentViewWishlistActivity() {
        Intent intent = new Intent(this, ParentViewWishlistActivity.class);
        startActivity(intent);
    }
    private void startCreateChildActivity() {
        Intent intent = new Intent(this, CreateChildActivity.class);
        startActivity(intent);
    }
    private void startCreateRewardActivity() {
        Intent intent = new Intent(this, CreateRewardActivity.class);
        startActivity(intent);
    }
    private void startCreateChoresActivity() {
        Intent intent = new Intent(this, CreateChoreActivity.class);
        startActivity(intent);
    }
    private void startUpdateProfileActivity() {
        Intent intent = new Intent(this, UpdateProfileActivity.class);
        startActivity(intent);
    }
    private void startParentLogoutActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
