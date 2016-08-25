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
import com.theironyard.finalproject.services.ChildChoreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildProfileActivity extends AppCompatActivity {

//public class ChildProfileActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener {
    final ChildChoreService childChoreService = new ChildChoreService();
    final Map<String, Integer> childMap = new HashMap<>();

    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_profile);
        ButterKnife.bind(this);
        token = "token" + childChoreService.getCurrentToken();

        /*******************************************
         * Child's Total Points
         *******************************************/

        Call<Integer> callChildPoints = null;
        try {
            callChildPoints = childChoreService.getChildApi().getPoints();
        } catch (Exception e) {
            e.printStackTrace();
        }

        callChildPoints.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                int childPoints = response.body();

                TextView pointText = (TextView)findViewById(R.id.childProfilePointTotalText);
                pointText.setText(Integer.toString(childPoints));
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

        /*******************************************
         * Today's Chores ListView and ArrayAdapter
         *******************************************/

        Call<ArrayList<Chore>> callCurrentChores = null;
        try {
            callCurrentChores = childChoreService.getChildApi().getChores();
        } catch (Exception e) {
            e.printStackTrace();
        }

        callCurrentChores.enqueue(new Callback<ArrayList<Chore>>() {
            @Override
            public void onResponse(Call<ArrayList<Chore>> call, Response<ArrayList<Chore>> response) {

                ArrayList<Chore> chores = response.body();
                ArrayList<String> choreNames = new ArrayList<>();

                for (Chore chore : chores){
                    choreNames.add(chore.getName());
                    childMap.put(chore.getName(), chore.getId());
                }

                ArrayAdapter<String> stringArrayAdapter =
                        new ArrayAdapter<String>(ChildProfileActivity.this,
                                android.R.layout.simple_list_item_1,
                                choreNames);
                ListView myList = (ListView) findViewById(R.id.childProfileChoresListView);
                myList.setAdapter(stringArrayAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Chore>> call, Throwable t) {
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
            startCreateWishListItemActivity();
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
    private void startCreateWishListItemActivity() {
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
        /************************************
         * OLD Navigation
         ************************************/

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.child_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_settings) {
//            // Handle the camera action
//        } else if (id == R.id.nav_view_chores) {
//            startCViewChores();
//        } else if (id == R.id.nav_view_wishlists) {
//            startCViewWishlist();
//        } else if (id == R.id.nav_view_calendar) {
//            startViewCalendar();
//        } else if (id == R.id.nav_create_wishlist_item) {
//            startCreateWishlistItem();
//        } else if (id == R.id.nav_update) {
//            startUpdateProfile();
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    private void startCViewChores() {
//        Intent intent = new Intent(this, ChildViewChoresActivity.class);
//        startActivity(intent);
//    }
//
//    private void startCViewWishlist() {
//        Intent intent = new Intent(this, ChildViewWishlistActivity.class);
//        startActivity(intent);
//    }
//
//    private void startViewCalendar() {
//        Intent intent = new Intent(this, CalendarActivity.class);
//        startActivity(intent);
//    }
//
//    private void startCreateWishlistItem() {
//        Intent intent = new Intent(this, CreateWishlistItemActivity.class);
//        startActivity(intent);
//    }
//
//    private void startUpdateProfile() {
//        Intent intent = new Intent(this, UpdateProfileActivity.class);
//        startActivity(intent);
//    }


