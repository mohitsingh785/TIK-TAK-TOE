package com.mohit.tictaktao;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mohit.tictaktao.login.Login;

public class BottomActivity extends AppCompatActivity {
    ViewPager slider_page;
    LinearLayout progress_dot;
    Button back_btn, next_btn, skip_btn;

    TextView[] dots;
    private SharedPreferences sp;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        back_btn = findViewById(R.id.backbtn);
        next_btn = findViewById(R.id.nextbtn);
        sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        skip_btn = findViewById(R.id.skipButton);
        final ConstraintLayout loginLayout = findViewById(R.id.onboard_layout);

        // Initial colors of the gradient
        int[] colors = new int[2];
        colors[0] = getResources().getColor(R.color.gradient_start_1);
        colors[1] = getResources().getColor(R.color.gradient_end_1);

        final GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors);
        gradientDrawable.setCornerRadius(0f);

        loginLayout.setBackground(gradientDrawable);

        // Animate gradient
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(3000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimator.addUpdateListener(animation -> {
            float fraction = animation.getAnimatedFraction();
            int newStartColor = (int) new ArgbEvaluator().evaluate(fraction, getResources().getColor(R.color.gradient_start_1), getResources().getColor(R.color.gradient_start_2));
            int newEndColor = (int) new ArgbEvaluator().evaluate(fraction, getResources().getColor(R.color.gradient_end_1), getResources().getColor(R.color.gradient_end_2));
            GradientDrawable newGradient = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{newStartColor, newEndColor});
            newGradient.setCornerRadius(0f);
            loginLayout.setBackground(newGradient);
        });

        valueAnimator.start();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) > 0){

                    slider_page.setCurrentItem(getitem(-1),true);

                }

            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getitem(0) < 3)
                    slider_page.setCurrentItem(getitem(1),true);
                else {
                    sp.edit().putBoolean("onboard",true).commit();
                    Intent i = new Intent(BottomActivity.this, Login.class);
                    startActivity(i);
                    finish();

                }

            }
        });

        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sp.edit().putBoolean("onboard",true).commit();
                Intent i = new Intent(BottomActivity.this, Login.class);
                startActivity(i);
                finish();

            }
        });

        slider_page = (ViewPager) findViewById(R.id.slideViewPager);
        progress_dot = (LinearLayout) findViewById(R.id.indicator_layout);

        viewPagerAdapter = new ViewPagerAdapter(this);

        slider_page.setAdapter(viewPagerAdapter);

        setUpindicator(0);
        slider_page.addOnPageChangeListener(viewListener);
    }


    public void setUpindicator(int position){

        dots = new TextView[4];
        progress_dot.removeAllViews();

        for (int i = 0 ; i < dots.length ; i++){

            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.inactive,getApplicationContext().getTheme()));
            progress_dot.addView(dots[i]);

        }

        dots[position].setTextColor(getResources().getColor(R.color.active,getApplicationContext().getTheme()));

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            setUpindicator(position);

            if (position > 0){

                back_btn.setVisibility(View.VISIBLE);

            }else {

                back_btn.setVisibility(View.INVISIBLE);

            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private int getitem(int i){

        return slider_page.getCurrentItem() + i;
    }
}