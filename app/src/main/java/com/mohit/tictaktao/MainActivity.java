package com.mohit.tictaktao;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mohit.tictaktao.OnevsOnline.LobbyActivity;
import com.mohit.tictaktao.OnevsOnline.OnevsOnline;
import com.mohit.tictaktao.databinding.ActivityMainBinding;
import com.mohit.tictaktao.onevscpu.OnevsCpu;
import com.mohit.tictaktao.onevscpu.chosecharacter;
import com.mohit.tictaktao.onevsone.OnevsOne;
import com.mohit.tictaktao.onevsone.select_page;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater()); // Initialize the binding
        View view = binding.getRoot();
        setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        final TextView textView = findViewById(R.id.home_title);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid();
        fetchUserData(userId);
//        final LinearLayout mainLayout = findViewById(R.id.main_layout);
//
//        // Initial colors of the gradient
//        int[] colors = new int[2];
//        colors[0] = getResources().getColor(R.color.gradient_start_1);
//        colors[1] = getResources().getColor(R.color.gradient_end_1);
//
//        final GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.BL_TR, colors);
//        gradientDrawable.setCornerRadius(0f);
//
//        mainLayout.setBackground(gradientDrawable);
//
//        // Animate gradient
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
//        valueAnimator.setDuration(3000);
//        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
//        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
//        valueAnimator.addUpdateListener(animation -> {
//            float fraction = animation.getAnimatedFraction();
//            int newStartColor = (int) new ArgbEvaluator().evaluate(fraction, getResources().getColor(R.color.gradient_start_1), getResources().getColor(R.color.gradient_start_2));
//            int newEndColor = (int) new ArgbEvaluator().evaluate(fraction, getResources().getColor(R.color.gradient_end_1), getResources().getColor(R.color.gradient_end_2));
//            GradientDrawable newGradient = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[]{newStartColor, newEndColor});
//            newGradient.setCornerRadius(0f);
//            mainLayout.setBackground(newGradient);
//        });
//
//        valueAnimator.start();
        Log.e("FirestoreFetch", "User ID: " + userId);
        // Assuming your TextView has been laid out to get its width and height
//        textView.post(new Runnable() {
//            @Override
//            public void run() {
//                Shader textShader = new LinearGradient(0, 0, 0, textView.getHeight(),
//                        new int[]{Color.RED, Color.WHITE},
//                        null, Shader.TileMode.CLAMP);
//                textView.getPaint().setShader(textShader);
//                textView.invalidate();
//            }
//        });

        binding.btnOnevsone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, select_page.class));
            }
        });

        binding.btnOnevscpu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, chosecharacter.class));
            }
        });
        binding.btnOnevsonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LobbyActivity.class));
            }
        });



    }
    public void fetchUserData(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Ensure FirebaseFirestore is initialized

        DocumentReference userRef = db.collection("users").document(userId);
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("FirestoreFetch", "Listen failed.", e);
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // User data exists, retrieve the data
                    String email = documentSnapshot.getString("email");
                    Long coins = documentSnapshot.getLong("coins");
                    Long level = documentSnapshot.getLong("level");
                    Long matchesWon = documentSnapshot.getLong("matches_won");
                    Long matchesLoss = documentSnapshot.getLong("matches_loss");
                    Long matchesDraw = documentSnapshot.getLong("matches_draw");
                    Long matchesPlayed = documentSnapshot.getLong("matches_played");

                    // Update the UI with the fetched data
                    binding.tvCoins.setText("Coins: " + coins);
                    binding.tvlevel.setText("Level: " + level);
                    binding.tvEmail.setText(email);

                    Log.d("FirestoreFetch", "Data updated in real-time.");
                } else {
                    Log.d("FirestoreFetch", "Current data: null");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}
