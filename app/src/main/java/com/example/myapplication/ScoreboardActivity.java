package com.example.myapplication;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardActivity extends MyAppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private Query mGetReference = mDatabase.getReference().child("Score").orderByChild("score");
    private String userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        final List<Score> list = new ArrayList<>();
        Bundle extra = getIntent().getExtras();
        if(extra != null){
            userName = extra.getString(EXT_SCORE);
        }

        mGetReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children){
                        list.add(child.getValue(Score.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        Log.d("Itay",list.size()+"");
    }
}


