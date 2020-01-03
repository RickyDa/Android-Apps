package com.example.myapplication;
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
import java.util.Collections;
import java.util.Objects;

public class ScoreboardActivity extends MyAppCompatActivity {

    private TextView[] scoresView;
    private final String scoreFile = "Scores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scoresView = new TextView[]{findViewById(R.id.first_score), findViewById(R.id.second_score), findViewById(R.id.third_score)};

        ArrayList<Integer> scoresFromFile = readScores();

        Bundle extra = getIntent().getExtras();
        if(extra != null){
            int score = Integer.parseInt(Objects.requireNonNull(extra.getString(EXT_SCORE)));
            scoresFromFile.add(score);

            reorderScores(scoresFromFile);
        }

        showScores(scoresFromFile);
        saveScores(scoresFromFile);

        findViewById(R.id.menuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void reorderScores(ArrayList<Integer> scoresFromFile) {

        Collections.sort(scoresFromFile,Collections.<Integer>reverseOrder());
        scoresFromFile.remove(scoresFromFile.size()-1); // the lowest number will be removed

    }


    private void showScores(ArrayList<Integer> scoresFromFile) {

        for (int i = 0; i < scoresFromFile.size(); i++)
            scoresView[i].setText(String.valueOf(scoresFromFile.get(i)));

    }

    private ArrayList<Integer> readScores() {
        int scoreSize = 4;
        FileInputStream fis;
        ObjectInputStream is;
        ArrayList<Integer> scores = new ArrayList<>(scoreSize);

        try {
            fis = getApplicationContext().openFileInput(scoreFile);
        } catch (FileNotFoundException e) {
            // in case the file does not exist, generate minimal scores.
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
            fos = getApplicationContext().openFileOutput(scoreFile, MODE_PRIVATE);
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

