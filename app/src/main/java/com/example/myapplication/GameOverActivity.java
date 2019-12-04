package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GameOverActivity extends MyAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);
        final Bundle extra = getIntent().getExtras();

        if(extra != null){
            String score = extra.getString("score");
            TextView result = findViewById(R.id.result);
            result.setText(score);
        }

        findViewById(R.id.resetBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                finish();
            }
        });

        findViewById(R.id.scoreboardBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showScoreboard = new Intent(getApplicationContext(), ScoreboardActivity.class);
                showScoreboard.putExtra("score",extra.getString("score"));
                finish();
                startActivity(showScoreboard);
            }
        });
    }
}
