package com.theironyard.finalproject.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.theironyard.finalproject.R;

import butterknife.ButterKnife;

public class CreateRewardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reward);
        ButterKnife.bind(this);

        /*******************************************
         * All Rewards ListView and ArrayAdapter
         *******************************************/

        String[]  myStringArray={"Reward1","Reward2","Reward3"};
        ArrayAdapter<String> myAdapter=new
                ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                myStringArray);
        ListView myList=(ListView)
                findViewById(R.id.allRewardsListView);
        myList.setAdapter(myAdapter);
    }
}
