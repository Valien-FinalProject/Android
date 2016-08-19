package com.theironyard.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListView;

public class CalendarActivity extends AppCompatActivity implements
        View.OnClickListener, CalendarView.OnDateChangeListener{

    private ArrayAdapter<String> chores;
    private ListView mAllChoresListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mAllChoresListView = (ListView)findViewById(R.id.allChoresByDayListView);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onSelectedDayChange(CalendarView calendarView, int i, int i1, int i2) {

    }
}
