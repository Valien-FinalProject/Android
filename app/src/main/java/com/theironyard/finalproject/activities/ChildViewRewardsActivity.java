package com.theironyard.finalproject.activities;

import android.app.LauncherActivity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.representations.Child;
import com.theironyard.finalproject.representations.Chore;
import com.theironyard.finalproject.representations.Reward;
import com.theironyard.finalproject.services.ChildChoreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildViewRewardsActivity extends AppCompatActivity {

    final ChildChoreService childChoreService = new ChildChoreService();
    final Map<String, Integer> childMap = new HashMap<>();
    final Map<String, Integer> indexMap = new HashMap<>();
    String token = "token " + childChoreService.getCurrentToken();
    ArrayList<Reward> rewards;

    Button cashInButton;
    TextView pointText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_view_rewards);
        setTitle("Rewards");
        cashInButton = (Button)findViewById(R.id.childViewRewardsCashInButton);
        cashInButton.setOnClickListener(cashIn);
        ButterKnife.bind(this);

        Call<Integer> callChildPoints = null;
        try {
            callChildPoints = childChoreService.getChildApi().getPoints(token);
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

                pointText = (TextView)findViewById(R.id.childViewRewardsTotalPointsText);
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
            callCurrentRewards = childChoreService.getChildApi().getRewards(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (callCurrentRewards != null) {
            callCurrentRewards.enqueue(new Callback<ArrayList<Reward>>() {
                @Override
                public void onResponse(Call<ArrayList<Reward>> call, Response<ArrayList<Reward>> response) {

                    rewards = response.body();
                    List<Map<String, String>> data = new ArrayList<>();

    //                ArrayList<String> rewardNames = new ArrayList<>();
    //                ArrayList<String> rewardPoints = new ArrayList<>();

                    for (Reward reward : rewards){
                        Map<String, String> datum = new HashMap<>(2);
    //                    rewardNames.add(reward.getName());
    //                    rewardPoints.add(reward.getPoints() + " Points");
                        datum.put("name", reward.getName());
                        datum.put("points", String.valueOf(reward.getPoints()) + " Points");
                        data.add(datum);
                        childMap.put(reward.getName(), reward.getId());
                        indexMap.put(reward.getName(), rewards.indexOf(reward));
                    }

                    SimpleAdapter adapter = new SimpleAdapter(ChildViewRewardsActivity.this, data,
                            android.R.layout.simple_list_item_2,
                            new String[] {"name", "points"},
                            new int[] {android.R.id.text1, android.R.id.text2});
                    ListView myList = (ListView) findViewById(R.id.childViewRewardsListView);
                    myList.setAdapter(adapter);
                }


                @Override
                public void onFailure(Call<ArrayList<Reward>> call, Throwable t) {
                }
            });
        }
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

    View.OnClickListener cashIn = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ListView rewardsList = (ListView)findViewById(R.id.childViewRewardsListView);

            String rewardName = (String)rewardsList.getAdapter().getItem((int)rewardsList.getSelectedItemId());
            int rewardIndex = indexMap.get(rewardName);
            final Reward selectedReward = rewards.get(rewardIndex);

            final int childPoints = Integer.parseInt(pointText.getText().toString());

            if (childPoints < selectedReward.getPoints()){
                Snackbar.make(view, "You do not have enough points for this Reward!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }else {
                try {
                    Call<Child> callDeductChildPoints = childChoreService.getChildApi().deductPoints(token, childMap.get(rewardName));
                    callDeductChildPoints.enqueue(new Callback<Child>() {
                        @Override
                        public void onResponse(Call<Child> call, Response<Child> response) {
                            String newPoints = Integer.toString(childPoints - selectedReward.getPoints());
                            pointText.setText(newPoints);
                        }

                        @Override
                        public void onFailure(Call<Child> call, Throwable t) {
                        }
                    });
                } catch (Exception e) {
                    Snackbar.make(view, "Uh oh, we were not able to deduct your points!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    e.printStackTrace();
                }
            }
        }
    };

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