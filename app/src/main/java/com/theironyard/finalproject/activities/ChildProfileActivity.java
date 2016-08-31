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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.representations.Child;
import com.theironyard.finalproject.representations.Chore;
import com.theironyard.finalproject.services.ChildChoreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChildProfileActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    ListView choreList;
    final ChildChoreService childChoreService = new ChildChoreService();
    final Map<String, Integer> childMap = new HashMap<>();
    Map<String, Chore> choreMap = new HashMap<>();
    List<Map<String, String>> choreData;
    ArrayList<Chore> chores;
    SimpleAdapter adapter;

    String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_child_profile);
        ButterKnife.bind(this);
        token = "token" + childChoreService.getCurrentToken();
        choreList = (ListView) findViewById(R.id.childProfileChoresListView);
        choreList.setOnItemLongClickListener(this);


        /*****************************************
         * Get the Child's name and change Title.
         *****************************************/

        Call<Child> callChild = null;
        try {
            callChild = childChoreService.getChildApi().getChild(token, childChoreService.getChildId());
            callChild.enqueue(new Callback<Child>() {
                @Override
                public void onResponse(Call<Child> call, Response<Child> response) {
                    Child child = response.body();
                    setTitle("Hi, " + child.getName() + "!");
                }

                @Override
                public void onFailure(Call<Child> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*******************************************
         * Child's Total Points
         *******************************************/

        Call<Integer> callChildPoints = null;
        try {
            callChildPoints = childChoreService.getChildApi().getPoints(token);
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
            callCurrentChores = childChoreService.getChildApi().getChores(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (callCurrentChores != null) {
            callCurrentChores.enqueue(new Callback<ArrayList<Chore>>() {
                @Override
                public void onResponse(Call<ArrayList<Chore>> call, Response<ArrayList<Chore>> response) {

                    chores = response.body();
                    ArrayList<String> choreNames = new ArrayList<>();
                    choreData = new ArrayList<>();
                    for (Chore chore : chores){

                        Map<String, String> datum = new HashMap<>(2);
                        datum.put("name", chore.getName());
                        datum.put("value", "Value: " + String.valueOf(chore.getValue()) + " Points");
                        choreData.add(datum);
                        choreNames.add(chore.getName());
                        choreMap.put(chore.getName(),chore);
                        childMap.put(chore.getName(), chore.getId());
                    }

                   adapter = new SimpleAdapter(ChildProfileActivity.this, choreData,
                            android.R.layout.simple_list_item_2,
                            new String[] {"name", "value"},
                            new int[] {android.R.id.text1, android.R.id.text2});
                    ListView myList = (ListView) findViewById(R.id.childProfileChoresListView);
                    myList.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Chore>> call, Throwable t) {
                }
            });
        }

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
        else if(id == R.id.childLogout){
            try {
                startChildLogoutActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    private void startChildLogoutActivity() throws Exception {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
        final Map choreName = (Map)adapter.getItem(position);
        Chore chore = choreMap.get(choreName.get("name"));

        try {
            Call<Child> callMakeChorePending = childChoreService.getChildApi().changeToPending(token, chore.getId());
            callMakeChorePending.enqueue(new Callback<Child>() {
                @Override
                public void onResponse(Call<Child> call, Response<Child> response) {
                    Snackbar.make(choreList,"Your Chore is now Pending ;)", Snackbar.LENGTH_LONG).show();
                    choreData.remove(choreName);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<Child> call, Throwable t) {
                    Snackbar.make(choreList,"Your Chore didn't make it through...booo! :'(", Snackbar.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}