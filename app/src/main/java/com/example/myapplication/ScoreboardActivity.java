package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class ScoreboardActivity extends AppCompatActivity {

    private TextView[] scoresView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoresView = new TextView[]{findViewById(R.id.first_score), findViewById(R.id.second_score), findViewById(R.id.third_score)};

        ArrayList<Integer> scoresFromFile = readScores();

        final Bundle extra = getIntent().getExtras();

        if(extra != null){
            int score = Integer.parseInt(extra.getString("score"));
            scoresFromFile.add(score);
            Collections.sort(scoresFromFile,Collections.<Integer>reverseOrder());
            scoresFromFile.remove(3);
        }

        showScores(scoresFromFile);
        saveScores(scoresFromFile);

        findViewById(R.id.menuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(menu);
            }
        });

    }


    private void showScores(ArrayList<Integer> scoresFromFile) {

        for (int i = 0; i < scoresFromFile.size(); i++)
            scoresView[i].setText(String.valueOf(scoresFromFile.get(i)));

    }

    private ArrayList<Integer> readScores() {

        FileInputStream fis;
        ObjectInputStream is;
        ArrayList<Integer> scores = new ArrayList<>(4);

        try {
            fis = getApplicationContext().openFileInput("Scores");
        } catch (FileNotFoundException e) {
            scores.add(100);
            scores.add(90);
            scores.add(80);
            return scores;
        }

        try {
            is = new ObjectInputStream(fis);
            scores = (ArrayList<Integer>) is.readObject();
            is.close();
            if (fis != null)
                fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return scores;
    }

    private void saveScores(ArrayList<Integer> scores) {

        FileOutputStream fos = null;
        ObjectOutputStream os;

        try {
            fos = getApplicationContext().openFileOutput("Scores", MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            os = new ObjectOutputStream(fos);
            os.writeObject(scores);
            os.close();
            if (fos != null)
                fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
