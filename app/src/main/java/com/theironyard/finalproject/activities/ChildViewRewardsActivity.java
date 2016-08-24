package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.theironyard.finalproject.R;

import butterknife.ButterKnife;

public class ChildViewRewardsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view_rewards);
        ButterKnife.bind(this);
    }

    /************************************
     * Navigation
     ************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.child_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();

        if (id == R.id.childHome){
            startChildProfileActivity();
            return true;
        }
        else if(id == R.id.rewards){
            startChildViewRewardsActivity();
            return true;
        }
        else if(id == R.id.wishlist){
            startCreateWishlistItemActivity();
            return true;
        }
        else if(id == R.id.chores){
            startChildViewChoresActivity();
            return true;
        }
        else if(id == R.id.childLogout){
            startChildLogoutActivity();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startChildProfileActivity() {
        Intent intent = new Intent(this, ChildProfileActivity.class);
        startActivity(intent);
    }
    private void startChildViewRewardsActivity() {
        Intent intent = new Intent(this, ChildViewRewardsActivity.class);
        startActivity(intent);
    }
    private void startCreateWishlistItemActivity() {
        Intent intent = new Intent(this, CreateWishlistItemActivity.class);
        startActivity(intent);
    }
    private void startChildViewChoresActivity() {
        Intent intent = new Intent(this, ChildViewChoresActivity.class);
        startActivity(intent);
    }
    private void startChildLogoutActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
