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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.theironyard.finalproject.R;
import com.theironyard.finalproject.command.RewardCommand;
import com.theironyard.finalproject.representations.Child;
import com.theironyard.finalproject.representations.Chore;
import com.theironyard.finalproject.representations.Reward;
import com.theironyard.finalproject.services.ParentChoreService;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentViewWishlistActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayAdapter<Reward> wishes;
    ListView wishList;
    Map<String, Reward> rewardMap = new HashMap<>();

    @Bind(R.id.pViewWishlistPointValueText)
    EditText mPoints;

    @Bind(R.id.pViewWishlistApproveButton)
    Button mApproveButton;

    @Bind(R.id.pViewWishlistDenyButton)
    Button mDenyButton;

    Spinner topSpinner;
    final Map<String, Integer> childMap = new HashMap<>();
    final ParentChoreService parentChoreService = new ParentChoreService();
    private List<Reward> childWishes = new ArrayList<>();
    String token = "";
    private int rewardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_view_wishlist);
        ButterKnife.bind(this);
        token = "token " + parentChoreService.getCurrentToken();

        mApproveButton.setOnClickListener(this);
        mDenyButton.setOnClickListener(this);

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
//        populateListView();
    }

    /*******************************************
     * Wishlist By Child
     *******************************************/

    AdapterView.OnItemSelectedListener onSpinner =  new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            int childId = childMap.get(topSpinner.getSelectedItem().toString());

            try {
                Call<ArrayList<Reward>> callCurrentRewards = parentChoreService.getParentApi().getChildWishlist(token, childId);
                    callCurrentRewards.enqueue(new Callback<ArrayList<Reward>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Reward>> callCurrentRewards, Response<ArrayList<Reward>> response) {
                            ArrayList<Reward> rewards = response.body();

                            ArrayList<String> rewardNames = new ArrayList<>();
                            for (Reward reward : rewards){
                                rewardMap.put(reward.getName(), reward);
                                rewardNames.add(reward.getName());
                            }
                            ArrayAdapter<String> wishes =
                                    new ArrayAdapter<>(ParentViewWishlistActivity.this, android.R.layout.simple_list_item_1, rewardNames);
                            wishList = (ListView) findViewById(R.id.pViewWishlistListView);
                            wishList.setAdapter(wishes);
                            wishList.setOnItemSelectedListener(selectWish);
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Reward>> call, Throwable t) {

                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

        AdapterView.OnItemSelectedListener selectWish = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                rewardId = (int) wishes.getItemId(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        /*******************************************
         * Insert image and url into ListView
         *******************************************/

//        private void populateListView() {
//            ArrayAdapter<Reward> adapter = new MyListAdapter();
//            ListView list = (ListView) findViewById(R.id.pViewWishlistListView);
//            list.setAdapter(adapter);
//        }
//
//    private class MyListAdapter extends ArrayAdapter<Reward> {
//        public MyListAdapter() {
//            super(ParentViewWishlistActivity.this, R.layout.item_view, childWishes);
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            View itemView = convertView;
//            if (itemView == null) {
//                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
//            }
//
//            // Select reward to view
//            Reward currentWish = rewards.get(position);
//
//            ImageView imageView = (ImageView)itemView.findViewById(R.id.wishlistItemImageView);
//            Picasso.with(ParentViewWishlistActivity.this).load(currentWish.getImageUrl()).into(imageView);
//
//            TextView nameText = (TextView) itemView.findViewById(R.id.viewWishlistNameText);
//            nameText.setText(currentWish.getName());
//
//            TextView urlText = (TextView) itemView.findViewById(R.id.viewWishlistUrlText);
//            urlText.setText(currentWish.getUrl());
//
//            return itemView;
//        }
//    }

        /*******************************************
         * Approve/Deny Wishlist Item
         *******************************************/
        public void onClick(final View view) {

        int childId = childMap.get(topSpinner.getSelectedItem().toString());
            switch (view.getId()) {
                case (R.id.pViewWishlistApproveButton):

                    int points = Integer.parseInt(mPoints.getText().toString());
                    String wishName= (String) wishList.getAdapter().getItem((int)wishList.getSelectedItemId());
                    Reward reward = rewardMap.get(wishName);
                    RewardCommand rewardCommand = new RewardCommand(reward.getName(), reward.getDescription(),points);

                    try {
                        parentChoreService.getParentApi().updateRewardInfo(rewardCommand, childId, reward.getId())
                                .enqueue(new Callback<RewardCommand>() {
                                    @Override
                                    public void onResponse(Call<RewardCommand> call, Response<RewardCommand> response) {
                                        if (response.code() == 200) {
                                            RewardCommand rewardCommand = response.body();
                                            ParentChoreService.saveReward(rewardCommand);
                                            Snackbar.make(view, "The wish has been approved is now a reward!", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            setDefaultValues();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<RewardCommand> call, Throwable t) {

                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                case (R.id.pViewWishlistDenyButton):
                    String deleteWishName= (String) wishList.getAdapter().getItem((int)wishList.getSelectedItemId());
                    Reward deleteReward = rewardMap.get(deleteWishName);
                    try {
                        parentChoreService.getParentApi().deleteReward(token, deleteReward.getId())
                                .enqueue(new Callback<ArrayList<Reward>>() {
                                    @Override
                                    public void onResponse(Call<ArrayList<Reward>> call, Response<ArrayList<Reward>> response) {
                                        Snackbar.make(view, "That reward has been denied!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }

                                    @Override
                                    public void onFailure(Call<ArrayList<Reward>> call, Throwable t) {
                                        Snackbar.make(view, "There was an issue with the API.", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

    public void setDefaultValues() {
        mPoints.setText("");
    }

    private void startPViewWishlist() {
        Intent intent = new Intent(this, ParentViewWishlistActivity.class);
        startActivity(intent);
    }
}

