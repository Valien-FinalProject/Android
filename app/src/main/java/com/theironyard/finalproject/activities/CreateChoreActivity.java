package com.theironyard.finalproject.activities;

import android.support.v7.app.AppCompatActivity;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.theironyard.finalproject.command.ChoreCommand;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.services.ParentChoreService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateChoreActivity extends AppCompatActivity{

    @Bind(R.id.createChoreNameText)
    EditText mName;

    @Bind(R.id.createChoreDescText)
    EditText mDescription;

    @Bind(R.id.createChoreValueText)
    EditText mValue;

    @Bind(R.id.createChoreButton)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chore);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.createChoreButton)
    public void onClick(View view){

        ParentChoreService parentChoreService = new ParentChoreService();
        String name = mName.getText().toString();
        String description = mDescription.getText().toString();
        int value = Integer.parseInt(mValue.getText().toString());
        final ChoreCommand choreCommand = new ChoreCommand(name, description, value);

        try{
            parentChoreService.getParentApi().getChoreInfo(choreCommand)
                    .enqueue(new Callback<ChoreCommand>() {
                        @Override
                        public void onResponse(Call<ChoreCommand> call, Response<ChoreCommand> response) {
                            if (response.code() == 200) {
                                ChoreCommand choreCommand = response.body();
                                ParentChoreService.saveChore(choreCommand);
                                Snackbar.make(mButton, "You have created a new chore!", Snackbar.LENGTH_LONG).show();
                                setDefaultValues();
                                startActivity(new Intent(CreateChoreActivity.this, ParentProfileActivity.class));
                            } else {
                                Snackbar.make(mButton, "Unable to Create New Chore.Try again", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChoreCommand> call, Throwable t) {

                        }
                    });

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setDefaultValues() {
        mName.setText("");
        mDescription.setText("");
        mValue.setText("");
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
