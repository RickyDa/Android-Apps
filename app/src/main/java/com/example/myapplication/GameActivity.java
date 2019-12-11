package com.example.myapplication;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
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

    private ViewGroup gameLayout;
    private ViewGroup blockLayout;

    private ImageView player;
    private ImageView[] hearts;

    private TextView score;

    private int screenHeight;
    private int screenWidth;

    private final int SIZE_BLOCK = 150;
    private int livesLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        this.screenHeight = metrics.heightPixels;
        this.screenWidth = metrics.widthPixels;
        this.livesLeft = 2;
        this.gameLayout = findViewById(R.id.gameLayout);
        this.player = findViewById(R.id.player);
        this.score = findViewById(R.id.pointsTextView);
        this.blockLayout = findViewById(R.id.blockLayout);
        this.hearts = new ImageView[]{findViewById(R.id.heart1), findViewById(R.id.heart2), findViewById(R.id.heart3)};
        this.gameLayout.setOnTouchListener(this);
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

    public void drop() {

        final ImageView block = createBlock();
        blockLayout.addView(block);
        block.animate()
                .translationY(screenHeight)
                .setDuration(3000)
                .setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {

                        if ((player.getX() <= block.getX() + SIZE_BLOCK && block.getX() <= player.getX() + player.getWidth())
                                && (player.getY() <= block.getY() + SIZE_BLOCK && block.getY() <= player.getY() + player.getHeight())) {

                            if (livesLeft >= 0) {
                                hearts[livesLeft--].setVisibility(View.INVISIBLE);

                                if (livesLeft < 0) {
                                    Intent startGame = new Intent(getApplicationContext(), GameOverActivity.class);
                                    startGame.putExtra("score", score.getText().toString());
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

    public final ImageView createBlock() {

        ImageView newBlock = new ImageView(this);

        newBlock.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newBlock.setImageResource(R.drawable.block);
        newBlock.getLayoutParams().height = SIZE_BLOCK;
        newBlock.getLayoutParams().width = SIZE_BLOCK;
        newBlock.setX(new Random().nextInt(screenWidth - SIZE_BLOCK));
        newBlock.setBackgroundColor(Color.rgb(100, 100, 50));
        return newBlock;
    }

    private void startScoreAnimation() {
        final int scoreDuration = 1000000;
        ValueAnimator scoreAnimation = ValueAnimator.ofInt(0, scoreDuration / 10);// /10 for slower animation
        scoreAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();
                score.setText(String.valueOf(animatedValue));
                score.requestLayout();
            }
        });
        scoreAnimation.setInterpolator(new LinearInterpolator());
        scoreAnimation.setDuration(scoreDuration).start();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX();

        if (event.getAction() == MotionEvent.ACTION_MOVE)
            player.setX(x - (player.getWidth() / 2f));
        return true;
    }
}
