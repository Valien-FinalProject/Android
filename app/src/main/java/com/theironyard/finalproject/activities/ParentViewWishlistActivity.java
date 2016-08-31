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
import android.widget.Spinner;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.command.RewardCommand;
import com.theironyard.finalproject.representations.Child;
import com.theironyard.finalproject.representations.Reward;
import com.theironyard.finalproject.services.ParentChoreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentViewWishlistActivity extends AppCompatActivity implements View.OnClickListener{

    ArrayAdapter<String> wishAdapter;
    ArrayList<Reward> rewards;
    ListView wishList;
    Map<String, Integer> rewardIndexMap = new HashMap<>();
    Map<String, Reward> rewardMap = new HashMap<>();
    ArrayList<String> wishNamesList;

    @Bind(R.id.pViewWishlistPointValueText)
    EditText mPoints;

    @Bind(R.id.pViewWishlistApproveButton)
    Button mApproveButton;

    @Bind(R.id.pViewWishlistDenyButton)
    Button mDenyButton;

    Spinner topSpinner;
    int childId;
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
            childId = childMap.get(topSpinner.getSelectedItem().toString());

            try {
                Call<ArrayList<Reward>> callCurrentRewards = parentChoreService.getParentApi().getChildWishlist(token, childId);
                    callCurrentRewards.enqueue(new Callback<ArrayList<Reward>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Reward>> callCurrentRewards, Response<ArrayList<Reward>> response) {
                            rewards = response.body();

                            wishNamesList = new ArrayList<>();
                            int index = -1;
                            for (Reward reward : rewards){
                                index ++;
                                rewardMap.put(reward.getName(), reward);
                                rewardIndexMap.put(reward.getName(), index);
                                wishNamesList.add(reward.getName());
                            }
                            wishAdapter = new ArrayAdapter<>(ParentViewWishlistActivity.this, android.R.layout.simple_list_item_1, wishNamesList);
                            wishList = (ListView) findViewById(R.id.pViewWishlistListView);
                            wishList.setAdapter(wishAdapter);
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
                rewardId = (int) wishAdapter.getItemId(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        };

        /*******************************************
         * Approve/Deny Wishlist Item
         *******************************************/
        public void onClick(final View view) {

        childId = childMap.get(topSpinner.getSelectedItem().toString());
            switch (view.getId()) {
                case (R.id.pViewWishlistApproveButton):

                    int points = 0;
                    if (!mPoints.getText().toString().equals("")) {
                        points = Integer.parseInt(mPoints.getText().toString());
                    }
                    final String wishName= (String) wishList.getAdapter().getItem(wishList.getCheckedItemPosition());
                    Reward reward = rewardMap.get(wishName);
                    RewardCommand rewardCommand = new RewardCommand(reward.getName(), reward.getDescription(),points);

                    try {
                        ParentChoreService.getParentApi().updateRewardInfo(rewardCommand, childId, reward.getId())
                                .enqueue(new Callback<RewardCommand>() {
                                    @Override
                                    public void onResponse(Call<RewardCommand> call, Response<RewardCommand> response) {
                                        if (response.code() == 200) {
                                            RewardCommand rewardCommand = response.body();
                                            ParentChoreService.saveReward(rewardCommand);
                                            Snackbar.make(view, "The wish has been approved is now a reward!", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            setDefaultValues();
                                            /* Get this to work later */
//                                            int index = rewardIndexMap.get(wishName);
//                                            wishNamesList.remove(index);

                                            wishList.refreshDrawableState();
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
                    final String deleteWishName= (String) wishList.getAdapter().getItem((int)wishList.getSelectedItemId());
                    Reward deleteReward = rewardMap.get(deleteWishName);
                    int deleteRewardId = deleteReward.getId();

                    try {
                        ParentChoreService.getParentApi().killWish(childId, deleteRewardId, token)
                                .enqueue(new Callback<Child>() {
                                    @Override
                                    public void onResponse(Call<Child> call, Response<Child> response) {
                                        if (response.code() == 200){
                                            Snackbar.make(view, "The wish has been deleted!", Snackbar.LENGTH_LONG)
                                                    .setAction("Action", null).show();
                                            wishNamesList.remove(rewardIndexMap.get(deleteWishName));
                                            wishAdapter.notifyDataSetChanged();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Child> call, Throwable t) {
                                        Snackbar.make(view, "API call failed.", Snackbar.LENGTH_LONG)
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

