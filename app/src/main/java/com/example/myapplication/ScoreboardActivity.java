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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ScoreboardActivity extends AppCompatActivity {

    private TextView[] names;
    private TextView[] scores;

    /**
     * In the file score we saved a Hashmap of the scores.
     * we saved Keys as number, but on later use we can conver it to names.
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        scores = new TextView[]{findViewById(R.id.first_score),findViewById(R.id.second_score),findViewById(R.id.third_score)};

        names = new TextView[]{findViewById(R.id.first_name),findViewById(R.id.second_name),findViewById(R.id.third_name)};

        HashMap<String,Integer> scoresFromFile = readScores();

        showScores(scoresFromFile);

        saveScores(scoresFromFile);

        findViewById(R.id.menuBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(menu);
            }
        });
    }

    private void showScores(HashMap<String, Integer> scoresFromFile) {
        // In the futrue we can use names instead of numbers in the scoreboard
        Iterator<Map.Entry<String, Integer>> itr = scoresFromFile.entrySet().iterator();
        for(int i = 0;i<scoresFromFile.size();i++) {
            Map.Entry<String, Integer> entry = itr.next();
            String key = entry.getKey();
            int value = entry.getValue();
            names[i].setText((i+1) + "");
            scores[i].setText(value+"");
        }
    }

    private HashMap<String, Integer> readScores() {

        FileInputStream fis = null;
        HashMap<String,Integer> lscores = new HashMap<>();
        try {
            fis = getApplicationContext().openFileInput("Scores");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectInputStream is = null;

        try {
            is = new ObjectInputStream(fis);
            lscores = ( HashMap<String,Integer>) is.readObject();
            is.close();
            if(fis != null)
                fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return sortByValue(lscores);
    }

    private void saveScores(HashMap<String, Integer> scores) {

        FileOutputStream fos = null;
        try {
            fos = getApplicationContext().openFileOutput("Scores", MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
            os.writeObject(scores);
            os.close();
            if(fos != null)
                fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (-(o1.getValue()).compareTo(o2.getValue()));
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
