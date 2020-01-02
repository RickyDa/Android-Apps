package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends MyAppCompatActivity implements View.OnTouchListener {

    private ViewGroup blockLayout;

    private ImageView player;
    private ImageView[] hearts;

    private TextView scoreView;

    private int livesLeft;
    private int bonusPoints;

    private int screenHeight;
    private int screenWidth;

    private final int SIZE_BLOCK = 150;
    private final long fallingDuaration =3000;
    public static String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        this.screenHeight = metrics.heightPixels;
        this.screenWidth = metrics.widthPixels;
        this.livesLeft = 2;

        this.player = findViewById(R.id.player);
        this.scoreView = findViewById(R.id.pointsTextView);
        this.blockLayout = findViewById(R.id.blockLayout);
        this.hearts = new ImageView[]{findViewById(R.id.heart1), findViewById(R.id.heart2), findViewById(R.id.heart3)};

        this.bonusPoints = 0;
        findViewById(R.id.gameLayout).setOnTouchListener(this);
        findViewById(R.id.startBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                startScoreAnimation();
                dropEndlessly();
            }
        });

    }

    private void dropEndlessly() {
        drop();
        blockLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                dropEndlessly();
            }
        }, 1200);

    }

    private void drop() {
        int randValue = new Random().nextInt(screenWidth - SIZE_BLOCK);

        if (randValue % 5 == 0)
            createBonus(randValue);
        else
            createBlock(randValue);

    }

    private ImageView createFallingObject(int location, int id){
        final ImageView bonus = new ImageView(this);

        bonus.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bonus.setImageResource(id);
        bonus.getLayoutParams().height = SIZE_BLOCK;
        bonus.getLayoutParams().width = SIZE_BLOCK;
        bonus.setX(location);
//        bonus.setBackgroundColor(Color.rgb(100, 100, 50)); // Draw hit box

        blockLayout.addView(bonus);
        return bonus;
    }

    private void createBonus(int location) {
        final ImageView bonus = createFallingObject(location,R.drawable.shmeckels);
        bonus.animate()
                .translationY(screenHeight)
                .setDuration(fallingDuaration)
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (isCollided(player, bonus)) {
                            bonusPoints += 1000;
                            animation.end();
                        }
                    }
                })
                .setInterpolator(new LinearInterpolator()
                ).withEndAction(new Runnable() {
            @Override
            public void run() {
                blockLayout.removeView(bonus);
            }
        }).start();
    }

    private void createBlock(int location) {

        final ImageView block = createFallingObject(location,R.drawable.block);
        block.animate()
                .translationY(screenHeight)
                .setDuration(fallingDuaration)
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        if (isCollided(player, block)) {
                            if (livesLeft >= 0) {
                                hearts[livesLeft--].setVisibility(View.INVISIBLE);
                                if (livesLeft < 0) {
                                    Intent startGame = new Intent(getApplicationContext(), GameOverActivity.class);
                                    startGame.putExtra("scoreView", scoreView.getText().toString());
                                    finish();
                                    startActivity(startGame);
                                }
                                animation.end();
                            }
                        }
                    }
                })
                .setInterpolator(new LinearInterpolator()
                ).withEndAction(new Runnable() {
            @Override
            public void run() {
                blockLayout.removeView(block);
            }
        }).start();
    }

    private void startScoreAnimation() {
        final int scoreDuration = 1000000;
        ValueAnimator scoreAnimation = ValueAnimator.ofInt(0, scoreDuration / 10);// /10 for slower animation
        scoreAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedScoreValue = (int) animation.getAnimatedValue();
                scoreView.setText(String.valueOf(animatedScoreValue + bonusPoints));
                scoreView.requestLayout();

            }
        });
        scoreAnimation.setInterpolator(new LinearInterpolator());
        scoreAnimation.setDuration(scoreDuration).start();
    }

    private boolean isCollided(View v1, View v2) {
        return (v1.getX() <= v2.getX() + SIZE_BLOCK) && (v2.getX() <= v1.getX() + v1.getWidth())
                && (v1.getY() <= v2.getY() + SIZE_BLOCK) && (v2.getY() <= v1.getY() + v1.getHeight());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();

        if (event.getAction() == MotionEvent.ACTION_MOVE)
            player.setX(x - (player.getWidth() / 2f));
        return true;
    }
}
