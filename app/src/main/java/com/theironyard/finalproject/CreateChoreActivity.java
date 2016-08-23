package com.theironyard.finalproject;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.theironyard.finalproject.command.ChoreCommand;
import com.theironyard.finalproject.command.UserCommand;
import com.theironyard.finalproject.representations.Chore;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateChoreActivity extends AppCompatActivity {

    @Bind(R.id.createChoreNameText)
    EditText mName;

    @Bind(R.id.createChoreDescText)
    EditText mDescription;

    @Bind(R.id.createChoreValueText)
    EditText mValue;

    @Bind(R.id.createChoreButton)
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chore);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.createChoreButton)
    public void onClick(View view){

        ParentChoreService parentChoreService = new ParentChoreService();
        String name = mName.getText().toString();
        String description = mDescription.getText().toString();
        int value = Integer.parseInt(mValue.getText().toString());
        final ChoreCommand choreCommand = new ChoreCommand(name, description, value);

//        try {
//            ParentChoreService.getParentApi().getChoreInfo(choreCommand)
//                    .enqueue(new Callback<ChoreCommand>() {
//                        @Override
//                        public void onResponse(Call<ChoreCommand> call, Response<ChoreCommand> response) {
//                            if (response.code() == 200) {
//                                ChoreCommand choreCommand = response.body();
//                                ParentChoreService.saveChore(choreCommand);
//                                Snackbar.make(mButton, "You have created a new chore!", Snackbar.LENGTH_LONG);
//                                setDefaultValues();
//                                startActivity(new Intent(CreateChoreActivity.this, ParentProfileActivity.class));
//                            } else {
//                                Snackbar.make(mButton, "Unable to Create New Chore.Try again", Snackbar.LENGTH_LONG);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<ChoreCommand> call, Throwable t) {
//
//                        }
//                    });
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    public void setDefaultValues() {
        mName.setText("");
        mDescription.setText("");
        mValue.setText("");
    }
}
