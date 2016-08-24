package com.theironyard.finalproject.activities;

import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.theironyard.finalproject.R;
import com.theironyard.finalproject.representations.Child;
import com.theironyard.finalproject.representations.Chore;
import com.theironyard.finalproject.representations.Reward;
import com.theironyard.finalproject.services.ParentChoreService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParentViewWishlistActivity extends AppCompatActivity{

//    Spinner topSpinner;
//    final Map<String, Integer> childMap = new HashMap<>();
//    final ParentChoreService parentChoreService = new ParentChoreService();
//    String token = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_parent_view_wishlist);
//        ButterKnife.bind(this);
//        token = "token " + parentChoreService.getCurrentToken();
//
//    }
}