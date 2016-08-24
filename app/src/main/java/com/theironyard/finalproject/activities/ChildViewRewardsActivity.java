package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.representations.Chore;
import com.theironyard.finalproject.representations.Reward;
import com.theironyard.finalproject.services.ChildChoreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildViewRewardsActivity extends AppCompatActivity {

    final ChildChoreService childChoreService = new ChildChoreService();
    final Map<String, Integer> childMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view_rewards);
        ButterKnife.bind(this);

        Call<Integer> callChildPoints = null;
        try {
            callChildPoints = childChoreService.getChildApi().getPoints();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*******************************************
         * Child's Total Points
         *******************************************/

        callChildPoints.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int childPoints = response.body();

                TextView pointText = (TextView)findViewById(R.id.childViewRewardsTotalPointsText);
                pointText.setText(Integer.toString(childPoints));
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        /*******************************************
         * Rewards View and ArrayAdapter
         *******************************************/

        Call<ArrayList<Reward>> callCurrentRewards = null;
        try {
            callCurrentRewards = childChoreService.getChildApi().getRewards();
        } catch (Exception e) {
            e.printStackTrace();
        }

        callCurrentRewards.enqueue(new Callback<ArrayList<Reward>>() {
            @Override
            public void onResponse(Call<ArrayList<Reward>> call, Response<ArrayList<Reward>> response) {

                ArrayList<Reward> chores = response.body();
                ArrayList<String> choreNames = new ArrayList<>();

                for (Reward reward : chores){
                    choreNames.add(reward.getName());
                    childMap.put(reward.getName(), reward.getId());
                }

                ArrayAdapter<String> stringArrayAdapter =
                        new ArrayAdapter<>(ChildViewRewardsActivity.this,
                                android.R.layout.simple_list_item_1,
                                choreNames);
                ListView myList = (ListView) findViewById(R.id.childProfileChoresListView);
                myList.setAdapter(stringArrayAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Reward>> call, Throwable t) {
            }
        });
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
