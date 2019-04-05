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
    private ImageView welcomeView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeView = (ImageView) findViewById(R.id.logo);
        welcomeView2= (ImageView) findViewById(R.id.logo2);
        startAnimation(welcomeView2);
        startAnimation(welcomeView);
    }

    public void startAnimation(ImageView imageView) {
        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(imageView, "scaleX", 0f, 1f);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(imageView, "scaleY", 0f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(imageView, "alpha", 0f, 1f);
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
       Intent intent = new Intent(getApplicationContext(),MyActivity.class);
       startActivity(intent);
        finish();
    }
}
