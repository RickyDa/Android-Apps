package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.view.View;

import java.util.Random;

public class GameActivity extends AppCompatActivity {


    private View middle;
    private View right;
    private View left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        middle = findViewById(R.id.v_middle);
        right = findViewById(R.id.v_right);
        left = findViewById(R.id.v_left);

        middle.setVisibility(View.INVISIBLE);
        right.setVisibility(View.INVISIBLE);
        left.setVisibility(View.INVISIBLE);

        findViewById(R.id.game_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findViewById(R.id.tap_to_start).setVisibility(View.INVISIBLE);
                dropEndlessly();
            }
        });
    }

    private void dropEndlessly() {
        switch (new Random().nextInt(3)) {
            case 0:
                right.setVisibility(View.VISIBLE);
                drop(right);
                break;
            case 1:
                left.setVisibility(View.VISIBLE);
                drop(left);
                break;
            case 2:
                middle.setVisibility(View.VISIBLE);
                drop(middle);
                break;
        }
        findViewById(R.id.game_layout).postDelayed(new Runnable() {
            @Override
            public void run() {
                dropEndlessly();
            }
        }, 1000);
    }


    private void drop(final View view) {
        view.animate().translationY(findViewById(R.id.game_layout)
                .getHeight()).setDuration(500).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
                view.animate().translationY(0).setDuration(0).start();
            }
        }).start();
    }
}
