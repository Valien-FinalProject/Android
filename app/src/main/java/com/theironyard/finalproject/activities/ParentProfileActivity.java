package com.theironyard.finalproject.activities;


import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Spinner;

import com.theironyard.finalproject.services.ParentChoreService;
import com.theironyard.finalproject.R;
import com.theironyard.finalproject.representations.Child;
import com.theironyard.finalproject.representations.Chore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Spinner topSpinner;
    ListView pendingList;
    Button mApprovePending;
    Button mDenyPending;


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
        topSpinner = (Spinner)  findViewById(R.id.pProfileChildSpinner);
        try {
            Call<ArrayList<Child>> call = parentChoreService.getParentApi().getChildren(token);
            call.enqueue(new Callback<ArrayList<Child>>() {
                @Override
                public void onResponse(Call<ArrayList<Child>> call, Response<ArrayList<Child>> response) {
                    ArrayList<Child> children = response.body();
                    ArrayList<String> childrenNames = new ArrayList<String>();
                    for (Child child : children){
                        childrenNames.add(child.getName());
                        childMap.put(child.getName(), child.getId());
                    }
                    ArrayAdapter<String> stringArrayAdapter=
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

        /************************************
         * Navigation
         ************************************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.parent_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_update) {
            startUpdateProfile();
        } else if (id == R.id.nav_view_wishlists) {
            startPViewWishlist();
        } else if (id == R.id.nav_view_calendar) {
            startViewCalendar();
        } else if (id == R.id.nav_create_child) {
            startCreateChild();
        } else if (id == R.id.nav_create_chore) {
            startCreateChores();
        }else if (id == R.id.nav_create_reward){
            startCreateRewards();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startPViewWishlist() {
        Intent intent = new Intent(this, ParentViewWishlistActivity.class);
        startActivity(intent);
    }

    private void startViewCalendar() {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    private void startCreateChild() {
        Intent intent = new Intent(this, CreateChildActivity.class);
        startActivity(intent);
    }

    private void startCreateChores() {
        Intent intent = new Intent(this, CreateChoreActivity.class);
        startActivity(intent);
    }

    private void startCreateRewards(){
        Intent intent = new Intent(this, CreateRewardActivity.class);
        startActivity(intent);
    }

    private void startUpdateProfile() {
        Intent intent = new Intent(this, UpdateProfileActivity.class);
        startActivity(intent);
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
                    ArrayList<String> choreNames = new ArrayList<>();
                    for (Chore chore : chores){
                        choreNames.add(chore.getName());
                    }
                    ArrayAdapter<String> stringArrayAdapter=
                            new ArrayAdapter<String>(ParentProfileActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    choreNames);
                    ListView myList=(ListView) findViewById(R.id.pProfileChoresTodayListView);
                    myList.setAdapter(stringArrayAdapter);
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
                    ArrayList<String> choreNames = new ArrayList<>();

                    for (Chore chore : pendingChores){
                        pendingChoreMap.put(chore.getName(), chore);
                        choreNames.add(chore.getName());
                    }
                    ArrayAdapter<String> stringArrayAdapter=
                            new ArrayAdapter<>(ParentProfileActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    choreNames);
                    ListView myList=(ListView)
                            findViewById(R.id.pProfileChoresPendingListView);
                    myList.setAdapter(stringArrayAdapter);
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
                    ArrayList<String> choreNames = new ArrayList<>();
                    for (Chore chore: completeChores){
                        choreNames.add(chore.getName());
                    }
                    ArrayAdapter<String> stringArrayAdapter=
                            new ArrayAdapter<>(ParentProfileActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    choreNames);
                    ListView myList=(ListView)
                            findViewById(R.id.pProfileChoresCompletedListView);
                    myList.setAdapter(stringArrayAdapter);
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
}