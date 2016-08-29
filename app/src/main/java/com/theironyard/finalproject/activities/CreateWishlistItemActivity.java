package com.theironyard.finalproject.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.representations.Reward;
import com.theironyard.finalproject.services.ChildChoreService;

import java.util.ArrayList;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateWishlistItemActivity extends AppCompatActivity {



    final ChildChoreService childChoreService = new ChildChoreService();
    String token = "token " + childChoreService.getCurrentToken();

    android.widget.SearchView searchView;
    ListView SearchListView;
    Button searchButton;
    Button addtoWishListButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wishlist_item);
        setTitle("Wish List");

        searchView = (android.widget.SearchView) findViewById(R.id.searchWalmartView);
        SearchListView = (ListView)findViewById(R.id.searchWalmartListView);
        searchButton = (Button)findViewById(R.id.searchWalmartButton);
        addtoWishListButton = (Button)findViewById(R.id.addItemToWishlistButton);
        searchButton.setOnClickListener(addToWishlist);


        Call<ArrayList<Reward>> getWishList = null;
        try {
            getWishList = childChoreService.getChildApi().getWishlist(token);
            getWishList.enqueue(new Callback<ArrayList<Reward>>() {
                @Override
                public void onResponse(Call<ArrayList<Reward>> call, Response<ArrayList<Reward>> response) {

                    ArrayList<Reward> childWishList = response.body();
                    ArrayList<String> childWishListNames = new ArrayList<>();

                    for (Reward reward : childWishList){
                        childWishListNames.add(reward.getName());
                    }

                    ArrayAdapter<String> stringArrayAdapter =
                            new ArrayAdapter<>(CreateWishlistItemActivity.this,
                                    android.R.layout.simple_list_item_1,
                                    childWishListNames);
                    ListView myList = (ListView) findViewById(R.id.searchWalmartListView);
                    myList.setAdapter(stringArrayAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Reward>> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        ButterKnife.bind(this);
    }

    View.OnClickListener addToWishlist = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String wishListItemName = searchView.getQuery().toString();

            Call<Reward> callAddToWishList = null;
            try {
                callAddToWishList = childChoreService.getChildApi().addToWishList(token, wishListItemName);
                callAddToWishList.enqueue(new Callback<Reward>() {
                    @Override
                    public void onResponse(Call<Reward> call, Response<Reward> response) {

                    }

                    @Override
                    public void onFailure(Call<Reward> call, Throwable t) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

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
    private void startCreateWishlistItemActivity() {
        Intent intent = new Intent(this, CreateWishlistItemActivity.class);
        startActivity(intent);
    }
    private void startChildLogoutActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
