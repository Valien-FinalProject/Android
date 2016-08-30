package com.theironyard.finalproject.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import com.theironyard.finalproject.services.ParentChoreService;
import com.theironyard.finalproject.R;
import com.theironyard.finalproject.representations.Child;
import com.theironyard.finalproject.representations.Chore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentProfileActivity extends AppCompatActivity

        implements View.OnClickListener {

    Spinner topSpinner;
    ListView pendingList;
    Button mApprovePending;
    Button mDenyPending;
    SimpleAdapter pendingAdapter;


    ArrayList<Chore> pendingChores = new ArrayList<>();
    Map<String, Chore> pendingChoreMap = new HashMap<>();
    final Map<String, Integer> childMap = new HashMap<>();
    int childId;
    final ParentChoreService parentChoreService = new ParentChoreService();
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);
        mApprovePending = (Button)findViewById(R.id.pProfileApproveButton);
        mDenyPending = (Button)findViewById(R.id.pProfileDenyButton);
        ButterKnife.bind(this);
        token = "token " + parentChoreService.getCurrentToken();
        pendingList = (ListView) findViewById(R.id.pProfileChoresPendingListView);
        mApprovePending.setOnClickListener(this);
        mDenyPending.setOnClickListener(this);


        /************************************
         * Spinner
         ************************************/
        topSpinner = (Spinner) findViewById(R.id.pProfileChildSpinner);
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
                            new ArrayAdapter<>(ParentProfileActivity.this,
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
        childId = childMap.get(topSpinner.getSelectedItem().toString());
        /*******************************************
         * Today's Chores ListView and ArrayAdapter
         *******************************************/

        try {
            Call<ArrayList<Chore>> callCurrentChores = parentChoreService.getParentApi().getCurrentChores(token, childId);
            callCurrentChores.enqueue(new Callback<ArrayList<Chore>>() {
                @Override
                public void onResponse(Call<ArrayList<Chore>> callCurrentChores, Response<ArrayList<Chore>> response) {
                    ArrayList<Chore> chores = response.body();
//                     ArrayList<String> choreNames = new ArrayList<>();
                    List<Map<String, String>> data = new ArrayList<>();

                    for (Chore chore : chores){
                        Map<String, String> datum = new HashMap<>(2);

                        datum.put("name", chore.getName());
                        datum.put("points", String.valueOf(chore.getValue()) + " Points");

                        if (!data.contains(datum)){
                            data.add(datum);
                        }

//                        choreNames.add(chore.getName());
                    }
                    SimpleAdapter adapter = new SimpleAdapter(ParentProfileActivity.this, data,
                            android.R.layout.simple_list_item_2,
                            new String[] {"name", "points"},
                            new int[] {android.R.id.text1, android.R.id.text2});
                    ListView myList=(ListView) findViewById(R.id.pProfileChoresTodayListView);
                    myList.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Chore>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*******************************************
         * Pending Chores ListView and ArrayAdapter
         *******************************************/

        try {
            Call<ArrayList<Chore>> callPendingChores = parentChoreService.getParentApi().getPendingChores(token, childId);
            callPendingChores.enqueue(new Callback<ArrayList<Chore>>() {
                @Override
                public void onResponse(Call<ArrayList<Chore>> callCurrentChores, Response<ArrayList<Chore>> response) {
                    pendingChores = response.body();
                    List<Map<String, String>> data = new ArrayList<>();

                    for (Chore chore : pendingChores){
                        Map<String, String> datum = new HashMap<>(2);

                        pendingChoreMap.put(chore.getName(), chore);

                        datum.put("name", chore.getName());
                        datum.put("points", String.valueOf(chore.getValue()) + " Points");

                        if (!data.contains(datum)){
                            data.add(datum);
                        }
                    }
                    pendingAdapter = new SimpleAdapter(ParentProfileActivity.this, data,
                            android.R.layout.simple_list_item_2,
                            new String[] {"name", "points"},
                            new int[] {android.R.id.text1, android.R.id.text2});
                    ListView myList=(ListView)
                            findViewById(R.id.pProfileChoresPendingListView);
                    myList.setAdapter(pendingAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Chore>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        /****************************************************
         * Today's Completed Chores ListView and ArrayAdapter
         ****************************************************/

        try {
            Call<ArrayList<Chore>> callCompletedChores = parentChoreService.getParentApi().getCompletedChores(token, childId);
            callCompletedChores.enqueue(new Callback<ArrayList<Chore>>() {
                @Override
                public void onResponse(Call<ArrayList<Chore>> callCurrentChores, Response<ArrayList<Chore>> response) {
                    ArrayList<Chore> completeChores = response.body();

                    List<Map<String, String>> data = new ArrayList<>();

                    for (Chore chore: completeChores){
                        Map<String, String> datum = new HashMap<>(2);

                        datum.put("name", chore.getName());
                        datum.put("points", String.valueOf(chore.getValue()) + " Points");

                        if (!data.contains(datum)){
                            data.add(datum);
                        }
                    }
                    SimpleAdapter adapter = new SimpleAdapter(ParentProfileActivity.this, data,
                            android.R.layout.simple_list_item_2,
                            new String[] {"name", "points"},
                            new int[] {android.R.id.text1, android.R.id.text2});
                    ListView myList=(ListView)findViewById(R.id.pProfileChoresCompletedListView);
                    myList.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Chore>> call, Throwable t) {

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

    @Override
    public void onClick(final View view) {

        String wishName= (String) pendingList.getAdapter().getItem((int)pendingList.getSelectedItemId());
        Chore pendingChore = pendingChoreMap.get(wishName);
        int pendingChoreId = pendingChore.getId();

        switch (view.getId()){
            case (R.id.pProfileApproveButton):


                try {
                    ParentChoreService.getParentApi().approveChore(childId, pendingChoreId, token)
                            .enqueue(new Callback<Chore>() {
                                @Override
                                public void onResponse(Call<Chore> call, Response<Chore> response) {
                                    if (response.code() == 200){
                                        Snackbar.make(view, "Chore is now complete!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                                pendingAdapter.notifyDataSetChanged();
                                    }

                                }

                                @Override
                                public void onFailure(Call<Chore> call, Throwable t) {
                                    Snackbar.make(view, "There was an issue with the API!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case (R.id.pProfileDenyButton):
                try {
                    Call<Chore> denyChore = ParentChoreService.getParentApi().denyChore(pendingChoreId, token);
                            denyChore.enqueue(new Callback<Chore>() {
                                @Override
                                public void onResponse(Call<Chore> call, Response<Chore> response) {
                                    if (response.code() == 200) {
                                        Snackbar.make(view, "Chore has been denied and returned to the child's list!", Snackbar.LENGTH_LONG)
                                                .setAction("Action", null).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Chore> call, Throwable t) {
                                    Snackbar.make(view, "There was an issue with the API!", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }

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