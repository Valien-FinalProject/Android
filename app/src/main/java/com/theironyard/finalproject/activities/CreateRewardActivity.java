package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.theironyard.finalproject.command.RewardCommand;
import com.theironyard.finalproject.representations.Reward;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import com.theironyard.finalproject.R;
import com.theironyard.finalproject.services.ParentChoreService;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateRewardActivity extends AppCompatActivity implements AdapterView.OnClickListener {

    @Bind(R.id.createRewardNameText)
    EditText mName;

    @Bind(R.id.createRewardDescText)
    EditText mDescription;

    @Bind(R.id.createRewardPointValueText)
    EditText mPoints;

    @Bind(R.id.createRewardButton)
    Button mButton;

    SimpleAdapter rewardAdapter;
    List<Map<String, String>> rewardData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reward);
        ButterKnife.bind(this);

        ParentChoreService parentChoreService = new ParentChoreService();
        final String token = "token " + parentChoreService.getCurrentToken();
        try {
            Call<ArrayList<Reward>> callAllRewards = parentChoreService.getParentApi().getParentRewards(token);
            callAllRewards.enqueue(new Callback<ArrayList<Reward>>() {
                @Override
                public void onResponse(Call<ArrayList<Reward>> callCurrentChores, Response<ArrayList<Reward>> response) {
                    ArrayList<Reward> rewards = response.body();

                    rewardData = new ArrayList<>();

                    for (Reward reward : rewards){
                        Map<String, String> datum = new HashMap<>(2);

                        datum.put("name", reward.getName());
                        datum.put("points", String.valueOf(reward.getPoints()) + " Points");

                        rewardData.add(datum);

                    }
                    rewardAdapter = new SimpleAdapter(CreateRewardActivity.this, rewardData,
                            android.R.layout.simple_list_item_2,
                            new String[] {"name", "points"},
                            new int[] {android.R.id.text1, android.R.id.text2});
                    ListView myList = (ListView)
                            findViewById(R.id.allRewardsListView);
                    myList.setAdapter(rewardAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Reward>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.createRewardButton)
    public void onClick(View view) {

        ParentChoreService parentChoreService = new ParentChoreService();
        final String token = "token " + parentChoreService.getCurrentToken();

        String name = mName.getText().toString();
        String description = mDescription.getText().toString();
        int points = Integer.parseInt(mPoints.getText().toString());
        RewardCommand rewardCommand = new RewardCommand(name, description, points);

        try {
            parentChoreService.getParentApi().getRewardInfo(rewardCommand)
                    .enqueue(new Callback<RewardCommand>() {
                        @Override
                        public void onResponse(Call<RewardCommand> call, Response<RewardCommand> response) {

                            if (response.code() == 200) {
                                RewardCommand rewardCommand = response.body();
                                ParentChoreService.saveReward(rewardCommand);
                                Snackbar.make(mButton, "You have created a new reward!", Snackbar.LENGTH_LONG).show();
                                Map<String, String> newDatum = new HashMap<>(2);

                                newDatum.put("name", rewardCommand.getName());
                                newDatum.put("points", String.valueOf(rewardCommand.getPoints()) + " Points");
                                rewardData.add(newDatum);
                                rewardAdapter.notifyDataSetChanged();
                                setDefaultValues();

                            } else {
                                Snackbar.make(mButton, "Unable to Create New Reward.Try again", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RewardCommand> call, Throwable t) {

                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDefaultValues() {
        mName.setText("");
        mDescription.setText("");
        mPoints.setText("");
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