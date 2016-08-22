package com.theironyard.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;
import android.widget.Spinner;

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
        implements NavigationView.OnNavigationItemSelectedListener {

    Spinner topSpinner;
    final Map<String, Integer> childMap = new HashMap<>();
    final ParentChoreService parentChoreService = new ParentChoreService();
    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_profile);
        ButterKnife.bind(this);
        token = "token " + parentChoreService.getCurrentToken();

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

        /************************************
         * Navigation
         ************************************/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        topSpinner.setOnItemSelectedListener(onSpinner);
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

        if (id == R.id.nav_settings) {
            // Handle the camera action
        } else if (id == R.id.nav_view_chores) {
            startPViewChores();
        } else if (id == R.id.nav_view_wishlists) {
            startPViewWishlist();
        } else if (id == R.id.nav_view_calendar) {
            startViewCalendar();
        } else if (id == R.id.nav_create_child) {
            startCreateChild();
        } else if (id == R.id.nav_create_chore) {
            startCreateChores();
        }else if (id == R.id.nav_update) {
            startUpdateProfile();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void startPViewChores() {
        Intent intent = new Intent(this, ParentViewChoresActivity.class);
        startActivity(intent);
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

    private void startUpdateProfile() {
        Intent intent = new Intent(this, UpdateProfileActivity.class);
        startActivity(intent);
    }

    AdapterView.OnItemSelectedListener onSpinner =  new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int childId = childMap.get(topSpinner.getSelectedItem().toString());
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

//        String[]  myStringArray={"Chore1","Chore2","Chore3"};
//        ArrayAdapter<String> myAdapter=new
//                ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                myStringArray);
//        ListView myList=(ListView)
//                findViewById(R.id.pProfileChoresTodayListView);
//        myList.setAdapter(myAdapter);

        /*******************************************
         * Pending Chores ListView and ArrayAdapter
         *******************************************/

        try {
            Call<ArrayList<Chore>> callPendingChores = parentChoreService.getParentApi().getPendingChores(token, childId);
            callPendingChores.enqueue(new Callback<ArrayList<Chore>>() {
                @Override
                public void onResponse(Call<ArrayList<Chore>> callCurrentChores, Response<ArrayList<Chore>> response) {
                    ArrayList<Chore> chores = response.body();
                    ArrayList<String> choreNames = new ArrayList<>();
                    Iterator<Chore> choreNamesIterator = chores.iterator();
                    while(choreNamesIterator.hasNext()){
                        choreNames.add(choreNamesIterator.next().getName());
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
//        String[]  myStringArray2={"Chore1","Chore2","Chore3"};
//        ArrayAdapter<String> myAdapter2=new
//                ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                myStringArray);
//        ListView myList2=(ListView)
//                findViewById(R.id.pProfileChoresPendingListView);
//        myList2.setAdapter(myAdapter2);

        /****************************************************
         * Today's Completed Chores ListView and ArrayAdapter
         ****************************************************/

        try {
            Call<ArrayList<Chore>> callCompletedChores = parentChoreService.getParentApi().getCompletedChores(token, childId);
            callCompletedChores.enqueue(new Callback<ArrayList<Chore>>() {
                @Override
                public void onResponse(Call<ArrayList<Chore>> callCurrentChores, Response<ArrayList<Chore>> response) {
                    ArrayList<Chore> chores = response.body();
                    ArrayList<String> choreNames = new ArrayList<>();
                    Iterator<Chore> choreNamesIterator = chores.iterator();
                    while(choreNamesIterator.hasNext()){
                        choreNames.add(choreNamesIterator.next().getName());
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


//        String[]  myStringArray3={"Chore1","Chore2","Chore3"};
//        ArrayAdapter<String> myAdapter3=new
//                ArrayAdapter<String>(
//                this,
//                android.R.layout.simple_list_item_1,
//                myStringArray);
//        ListView myList3=(ListView)
//                findViewById(R.id.pProfileChoresCompletedListView);
//        myList3.setAdapter(myAdapter3);


}
