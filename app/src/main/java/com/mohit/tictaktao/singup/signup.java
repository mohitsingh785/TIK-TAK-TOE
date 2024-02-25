package com.mohit.tictaktao.singup;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mohit.tictaktao.MainActivity;
import com.mohit.tictaktao.R;
import com.mohit.tictaktao.splash_screen;

import java.util.HashMap;
import java.util.Map;

public class signup extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button signupButton;
    private SharedPreferences sp;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        sp=getSharedPreferences("login", Context.MODE_PRIVATE);
        // Initialize UI elements
        emailEditText = findViewById(R.id.signup_email);
        passwordEditText = findViewById(R.id.signup_password);
        signupButton = findViewById(R.id.signup_button);
        final ConstraintLayout loginLayout = findViewById(R.id.signup_layout);

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
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // Create a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success
                            Log.d("Signup", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            createUserInFirestore(user);
                        } else {
                            // Sign up fails
                            Log.w("Signup", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(signup.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void createUserInFirestore(FirebaseUser user) {

        if (user != null) {
            // Creating a new user with a HashMap
            Map<String, Object> newUser = new HashMap<>();
            newUser.put("email", user.getEmail());
            newUser.put("coins", 4000);
            newUser.put("level", 1);
            newUser.put("matches_won", 0);
            newUser.put("matches_loss", 0);
            newUser.put("matches_draw",0);
            newUser.put("matches_played",0);

            // Add a new document with a generated ID
            db.collection("users").document(user.getUid()).set(newUser)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                        updateUI(user); // Update UI after creating user document
                    })
                    .addOnFailureListener(e -> Log.w("Firestore", "Error writing document", e));
        }
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = emailEditText.getText().toString();
        if (email.isEmpty()) {
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        String password = passwordEditText.getText().toString();
        if (password.isEmpty()) {
            passwordEditText.setError("Required.");
            valid = false;
        } else {
            passwordEditText.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Navigate to your next activity or update the UI accordingly
            startActivity(new Intent(signup.this, MainActivity.class));
            sp.edit().putBoolean("success",true);
            Toast.makeText(signup.this, "Signup successful.", Toast.LENGTH_SHORT).show();
        } else {
            // Stay on the signup page or inform the user of the failed signup
        }
    }
}