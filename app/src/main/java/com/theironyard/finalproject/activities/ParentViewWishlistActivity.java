package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.command.RewardCommand;
import com.theironyard.finalproject.representations.Child;
import com.theironyard.finalproject.representations.Chore;
import com.theironyard.finalproject.representations.Reward;
import com.theironyard.finalproject.services.ParentChoreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentViewWishlistActivity extends AppCompatActivity{

//    ArrayAdapter<Reward> wishes;
//    ListView wishList;
//
//    @Bind(R.id.pViewWishlistPointValueText)
//    EditText mPoints;
//
//    @Bind(R.id.pViewWishlistApproveButton)
//    Button mApproveButton;

//    @Bind(R.id.pViewWishlistDenyButton)
//    Button mDenyButton;

    Spinner topSpinner;
    final Map<String, Integer> childMap = new HashMap<>();
    final ParentChoreService parentChoreService = new ParentChoreService();
    String token = "";
//    private int rewardId;
//    private int childId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_view_wishlist);
        ButterKnife.bind(this);
        token = "token " + parentChoreService.getCurrentToken();

//        mApproveButton.setOnClickListener(this);
//        mDenyButton.setOnClickListener(this);

        /************************************
         * Spinner
         ************************************/
        topSpinner = (Spinner) findViewById(R.id.parentViewWishlistSpinner);
        try {
            Call<ArrayList<Child>> call = parentChoreService.getParentApi().getChildren(token);
            call.enqueue(new Callback<ArrayList<Child>>() {
                @Override
                public void onResponse(Call<ArrayList<Child>> call, Response<ArrayList<Child>> response) {
                    ArrayList<Child> children = response.body();
                    ArrayList<String> childrenNames = new ArrayList<String>();
                    for (Child child : children) {
                        childrenNames.add(child.getName());
                        childMap.put(child.getName(), child.getId());
                        //childId = child.getId();
                    }
                    ArrayAdapter<String> stringArrayAdapter =
                            new ArrayAdapter<>(ParentViewWishlistActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item,
                                    childrenNames);
                    topSpinner.setAdapter(stringArrayAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Child>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        topSpinner.setOnItemSelectedListener(onSpinner);
    }

    AdapterView.OnItemSelectedListener onSpinner =  new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            int childId = childMap.get(topSpinner.getSelectedItem().toString());
        }
            /*******************************************
             * Wishlist By Child
             *******************************************/

//            try {
//                Call<ArrayList<Reward>> callCurrentRewards = parentChoreService.getParentApi().getChildWishlist(token, childId);
//                callCurrentRewards.enqueue(new Callback<ArrayList<Reward>>() {
//                    @Override
//                    public void onResponse(Call<ArrayList<Reward>> callCurrentRewards, Response<ArrayList<Reward>> response) {
//                        ArrayList<Reward> rewards = response.body();
//                        ArrayList<String> rewardNames = new ArrayList<>();
//                        for (Reward reward : rewards) {
//                            rewardNames.add(reward.getName());
//                        }
//                        ArrayAdapter<String> wishes =
//                                new ArrayAdapter<>(ParentViewWishlistActivity.this,
//                                        android.R.layout.simple_list_item_1,
//                                        rewardNames);
//                        wishList = (ListView) findViewById(R.id.pViewWishlistListView);
//                        wishList.setAdapter(wishes);
//                    }
//
//                    @Override
//                    public void onFailure(Call<ArrayList<Reward>> call, Throwable t) {
//
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            wishList.setOnItemSelectedListener(selectWish);
//        }

        @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

//    AdapterView.OnItemSelectedListener selectWish = new AdapterView.OnItemSelectedListener() {
//        @Override
//        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//            rewardId = (int) wishes.getItemId(position);
//        }
//
//        @Override
//        public void onNothingSelected(AdapterView<?> adapterView) {
//
//        }
//    };
    /*******************************************
     * Approve/Deny Wishlist Item
     *******************************************/
//    public void onClick(View view) {
//
//        ParentChoreService parentChoreService = new ParentChoreService();
//
//        switch (view.getId()) {
//            case (R.id.pViewWishlistApproveButton):
//
//                int points = Integer.parseInt(mPoints.getText().toString());
//                RewardCommand rewardCommand = new RewardCommand(points);
//
//                try {
//                    parentChoreService.getParentApi().updateRewardInfo(rewardCommand, childId, rewardId)
//                            .enqueue(new Callback<RewardCommand>() {
//                                @Override
//                                public void onResponse(Call<RewardCommand> call, Response<RewardCommand> response) {
//                                    if(response.code() == 200){
//                                        RewardCommand rewardCommand = response.body();
//                                        ParentChoreService.saveReward(rewardCommand);
//
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<RewardCommand> call, Throwable t) {
//
//                                }
//                            });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            case (R.id.pViewWishlistDenyButton):
//                try {
//                    parentChoreService.getParentApi().deleteReward(token, rewardId)
//                            .enqueue(new Callback<ArrayList<Reward>>() {
//                                @Override
//                                public void onResponse(Call<ArrayList<Reward>> call, Response<ArrayList<Reward>> response) {
//
//                                }
//
//                                @Override
//                                public void onFailure(Call<ArrayList<Reward>> call, Throwable t) {
//
//                                }
//                            });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//        }
//    }
}
