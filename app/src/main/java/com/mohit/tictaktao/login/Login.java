package com.mohit.tictaktao.login;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.mohit.tictaktao.MainActivity;
import com.mohit.tictaktao.R;
import com.mohit.tictaktao.singup.signup;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private SharedPreferences  sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        // Initialize UI elements
        emailEditText = findViewById(R.id.login_email);
        passwordEditText = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        signUpTextView = findViewById(R.id.textViewSignUp);
         sp=getSharedPreferences("login", Context.MODE_PRIVATE);

        // Find the ConstraintLayout
        final ConstraintLayout loginLayout = findViewById(R.id.login_layout);

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
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn(emailEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, signup.class));
            }
        });
    }


    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        // Sign in existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            sp.edit().putBoolean("success",true).commit();
                            Log.e("Loginnnnnnnnnnnnnnn", ""+sp.getBoolean("success", false));
                            updateUI(user);

                        } else {
                            // Sign in fails
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
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
            // Navigate to your main activity or update UI accordingly
            Intent intent = new Intent(Login.this, MainActivity.class); // Change MainActivity to your main activity
            startActivity(intent);
            finish(); // Finish this activity so the user can't return to it by pressing the back button
        } else {
            // Optionally, handle UI changes when sign in fails
        }
    }
}