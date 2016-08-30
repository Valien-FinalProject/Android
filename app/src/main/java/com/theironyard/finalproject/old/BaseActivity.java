//package com.theironyard.finalproject.old;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//
//import com.theironyard.finalproject.R;
//import com.theironyard.finalproject.activities.CalendarActivity;
//import com.theironyard.finalproject.activities.CreateChildActivity;
//import com.theironyard.finalproject.activities.CreateChoreActivity;
//import com.theironyard.finalproject.activities.CreateRewardActivity;
//import com.theironyard.finalproject.activities.ParentViewWishlistActivity;
//import com.theironyard.finalproject.activities.UpdateProfileActivity;
//
//public class BaseActivity extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener{
//
//
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
//
//    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//    setSupportActionBar(toolbar);
//
//    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//    drawer.setDrawerListener(toggle);
//    toggle.syncState();
//
//    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//    navigationView.setNavigationItemSelectedListener(this);
//}
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
//        getMenuInflater().inflate(R.menu.parent_profile, menu);
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
//        if (id == R.id.nav_update) {
//            startUpdateProfile();
//        } else if (id == R.id.nav_view_wishlists) {
//            startPViewWishlist();
//        } else if (id == R.id.nav_view_calendar) {
//            startViewCalendar();
//        } else if (id == R.id.nav_create_child) {
//            startCreateChild();
//        } else if (id == R.id.nav_create_chore) {
//            startCreateChores();
//        }else if (id == R.id.nav_create_reward){
//            startCreateRewards();
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    private void startPViewWishlist() {
//        Intent intent = new Intent(this, ParentViewWishlistActivity.class);
//        startActivity(intent);
//    }
//
//    private void startViewCalendar() {
//        Intent intent = new Intent(this, CalendarActivity.class);
//        startActivity(intent);
//    }
//
//    private void startCreateChild() {
//        Intent intent = new Intent(this, CreateChildActivity.class);
//        startActivity(intent);
//    }
//
//    private void startCreateChores() {
//        Intent intent = new Intent(this, CreateChoreActivity.class);
//        startActivity(intent);
//    }
//
//    private void startCreateRewards(){
//        Intent intent = new Intent(this, CreateRewardActivity.class);
//        startActivity(intent);
//    }
//
//    private void startUpdateProfile() {
//        Intent intent = new Intent(this, UpdateProfileActivity.class);
//        startActivity(intent);
//    }
//}
