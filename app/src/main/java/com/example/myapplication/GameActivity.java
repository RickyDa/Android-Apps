package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {

    private LinearLayout gameLayout;

    private View[] blocks;
    private ImageView player;
    private ImageView[] lives;
    private TextView score;
    private GestureDetectorCompat mDetector;
    private int livesLeft;
    private static final String DEBUG_TAG = "tag";

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameLayout = findViewById(R.id.game_layout);
        blocks = new View[]{findViewById(R.id.v_middle),findViewById(R.id.v_right),findViewById(R.id.v_left)};
        player = findViewById(R.id.player);
        mDetector = new GestureDetectorCompat(this, this);
        score = findViewById(R.id.pointsTextView);
        livesLeft = 2;
        lives = new ImageView[]{findViewById(R.id.heart1), findViewById(R.id.heart2), findViewById(R.id.heart3)};
        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blocks[0].setVisibility(View.INVISIBLE);
                blocks[1].setVisibility(View.INVISIBLE);
                blocks[2].setVisibility(View.INVISIBLE);
                findViewById(R.id.startBtn).setVisibility(View.INVISIBLE);
                dropEndlessly();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                int points = 0;
                while (true) {
                    try {
                        Thread.sleep(100);
                        score.setText((points++) + "");
                    } catch (InterruptedException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }).start();


    }

    private void dropEndlessly() {
        int blockId = new Random().nextInt(3);
        blocks[blockId].setVisibility(View.VISIBLE);
        drop(blocks[blockId]);
        findViewById(R.id.game_layout).postDelayed(new Runnable() {
            @Override
            public void run() {
                dropEndlessly();
            }
        }, 1500);

    }

    private void drop(final View view) {
        view.animate().translationY(findViewById(R.id.game_layout)
                .getHeight()).setDuration(1000).withEndAction(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.INVISIBLE);
                view.animate().translationY(0).setDuration(0).start();
            }
        }).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if ((player.getX() >= view.getX() && player.getX() <= view.getX() + view.getWidth())
                        &&
                        player.getY() >= view.getY() && player.getY() <= view.getY() + view.getHeight()) {
                    Log.d(DEBUG_TAG, "**HIT**");
                    if (livesLeft >= 0) {
                        lives[livesLeft--].setVisibility(View.INVISIBLE);
                        view.setVisibility(View.INVISIBLE);
                        view.animate().translationY(0).setDuration(0).start();

                        if (livesLeft < 0) {
                            Intent startGame = new Intent(getApplicationContext(), GameOverActivity.class);
                            startGame.putExtra("score", score.getText().toString());
                            startActivity(startGame);
                        }

                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX,float velocityY) {
        boolean result = false;
        try {
            float step = blocks[0].getWidth();
            float diffY = event2.getY() - event1.getY();
            float diffX = event2.getX() - event1.getX();
            float currentLocation = player.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        if (currentLocation + step < gameLayout.getWidth()) {
                            Log.d(DEBUG_TAG, "Right");
                            player.setX(player.getX() + step);
                        }
                    } else {
                        if (currentLocation - step > 0) {
                            Log.d(DEBUG_TAG, "Left");
                            player.setX(player.getX() - step);
                        }
                    }
                    result = true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return result;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX,
                            float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

}
