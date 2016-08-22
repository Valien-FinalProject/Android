package com.theironyard.finalproject;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.theironyard.finalproject.command.RewardCommand;
import com.theironyard.finalproject.command.UserCommand;
import com.theironyard.finalproject.representations.Chore;
import com.theironyard.finalproject.representations.Reward;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reward);
        ButterKnife.bind(this);

        mButton.setOnClickListener(this);

        ParentChoreService parentChoreService = new ParentChoreService();
        final String token = "token " + parentChoreService.getCurrentToken();
        try {
            Call<ArrayList<Reward>> callAllRewards = parentChoreService.getParentApi().getParentRewards(token);
            callAllRewards.enqueue(new Callback<ArrayList<Reward>>() {
                @Override
                public void onResponse(Call<ArrayList<Reward>> callCurrentChores, Response<ArrayList<Reward>> response) {
                    ArrayList<Reward> rewards = response.body();
                    ArrayList<String> rewardNames = new ArrayList<>();
                    Iterator<Reward> rewardNamesIterator = rewards.iterator();
                    while (rewardNamesIterator.hasNext()) {
                        rewardNames.add(rewardNamesIterator.next().getName());
                    }
                    ArrayAdapter<String> stringArrayAdapter =
                            new ArrayAdapter<>(CreateRewardActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    rewardNames);
                    ListView myList = (ListView)
                            findViewById(R.id.allRewardsListView);
                    myList.setAdapter(stringArrayAdapter);
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

        /*******************************************
         * All Rewards ListView and ArrayAdapter
         *******************************************/

        try {
            Call<ArrayList<Reward>> callAllRewards = parentChoreService.getParentApi().getParentRewards(token);
            callAllRewards.enqueue(new Callback<ArrayList<Reward>>() {
                @Override
                public void onResponse(Call<ArrayList<Reward>> callCurrentChores, Response<ArrayList<Reward>> response) {
                    ArrayList<Reward> rewards = response.body();
                    ArrayList<String> rewardNames = new ArrayList<>();
                    Iterator<Reward> rewardNamesIterator = rewards.iterator();
                    while (rewardNamesIterator.hasNext()) {
                        rewardNames.add(rewardNamesIterator.next().getName());
                    }
                    ArrayAdapter<String> stringArrayAdapter =
                            new ArrayAdapter<>(CreateRewardActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    rewardNames);
                    ListView myList = (ListView)
                            findViewById(R.id.allRewardsListView);
                    myList.setAdapter(stringArrayAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Reward>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    AdapterView.OnClickListener mListener = new AdapterView.OnClickListener() {

        @Override
        public void onClick(View view) {

        }
    };

    public void setDefaultValues() {
        mName.setText("");
        mDescription.setText("");
        mPoints.setText("");
    }
}
