package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnTouchListener, GestureDetector.OnGestureListener {


    private View middle, right, left;
    private ImageView player;

    private GestureDetectorCompat mDetector;
    private static final String DEBUG_TAG = "tag";

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        middle = findViewById(R.id.v_middle);
        right = findViewById(R.id.v_right);
        left = findViewById(R.id.v_left);
        player = findViewById(R.id.player);
        mDetector = new GestureDetectorCompat(this, this);

        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                middle.setVisibility(View.INVISIBLE);
                right.setVisibility(View.INVISIBLE);
                left.setVisibility(View.INVISIBLE);
                findViewById(R.id.startBtn).setVisibility(View.INVISIBLE);
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
                           float velocityX, float velocityY) {

        boolean result = false;
        try {
            float diffY = event2.getY() - event1.getY();
            float diffX = event2.getX() - event1.getX();
            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        Log.d(DEBUG_TAG, "onFling: Right");
                        player.setX(player.getX() + player.getWidth() + 10);
                    } else {
                        Log.d(DEBUG_TAG, "onFling: Left");
                        player.setX(player.getX() - player.getWidth() + 10);
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
