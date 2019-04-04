package com.man.manmusic;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;


import com.man.manmusic.R;


public class WelcomeActivity extends AppCompatActivity {

    private ImageView welcomeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeView = (ImageView) findViewById(R.id.logo);
        startAnimation();
    }

    public void startAnimation() {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(welcomeView, "scaleX", 0f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(welcomeView, "scaleY", 0f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(welcomeView, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alphaAnimator).with(scaleXAnimator).with(scaleYAnimator);
        animatorSet.setDuration(1000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                hasLogin();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void hasLogin() {
       Intent intent = new Intent(getApplicationContext(),PlayActivity.class);
       startActivity(intent);
        finish();
    }
}
