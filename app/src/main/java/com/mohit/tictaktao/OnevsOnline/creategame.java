package com.mohit.tictaktao.OnevsOnline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohit.tictaktao.MainActivity;
import com.mohit.tictaktao.R;

import java.util.ArrayList;
import java.util.Collections;

public class creategame extends AppCompatActivity {
    private EditText editTextUserName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String selectedOption = "X"; // Default selection
    private LinearLayout favX, favO;
    private RadioGroup radioGroupMode;
    private long total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategame);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        initUI();
//        signInAnonymously();
    }

//    private void signInAnonymously() {
//        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(this, task -> {
//            if (task.isSuccessful()) {
//                // Optional: Update UI or notify user of successful sign in if needed
//            } else {
//                Toast.makeText(creategame.this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    private void initUI() {
        Button startGameButton = findViewById(R.id.buttonStartNewGame);
        favX = findViewById(R.id.fav_x);
        favO = findViewById(R.id.fav_o);
        editTextUserName = findViewById(R.id.editTextGameName);

        // Click listener for fav_x
        favX.setOnClickListener(v -> {
            selectedOption = "X";
            selectOption(favX, favO);
        });

        // Click listener for fav_o
        favO.setOnClickListener(v -> {
            selectedOption = "O";
            selectOption(favO, favX);
        });
        radioGroupMode = findViewById(R.id.radioGroupMode);
        radioGroupMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Use if-else statements to check the selected RadioButton
                if (checkedId == R.id.radioButton5) {
                    // The '5' mode is selected
                   total=5;
                } else if (checkedId == R.id.radioButton3) {
                    // The '3' mode is selected
                    total=3;
                } else if (checkedId == R.id.radioButton10) {
                    // The '10' mode is selected
                   total=10;
                } else {
                    // Handle default case or error
                }
            }
        });

        startGameButton.setOnClickListener(v -> createGame());
    }

    private void selectOption(LinearLayout selected, LinearLayout other) {
        // Here, add logic to change the appearance based on selection
        // For simplicity, let's assume it changes the background
        selected.setBackgroundResource(R.drawable.bg_slt); // Make sure this drawable exists
        other.setBackgroundResource(R.drawable.bg_edittext); // Default background
    }

    public void createGame() {
        String name = editTextUserName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = db.collection("users").document(userId);
            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Long coins = documentSnapshot.getLong("coins");
                    if (coins != null && coins >= 150) {
                        // Show confirmation dialog
                        new AlertDialog.Builder(this)
                                .setTitle("Confirm Game Creation")
                                .setMessage("You have " + coins + " coins. Creating a game room will deduct 150 coins. Continue?")
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    // Deduct coins and create the game
                                    deductCoinsAndCreateGame(userId, name, coins - 150);
                                })
                                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                                .show();
                    } else {
                        Toast.makeText(this, "Not enough coins to create a game. You need at least 150 coins.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e -> Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show());
        } else {
            // Handle the case where the user is not signed in
            Toast.makeText(this, "You need to be signed in to create a game.", Toast.LENGTH_SHORT).show();
        }
    }

    private void deductCoinsAndCreateGame(String userId, String name, long newCoinValue) {
        DocumentReference userRef = FirebaseFirestore.getInstance().collection("users").document(userId);
        userRef.update("coins", newCoinValue)
                .addOnSuccessListener(aVoid -> {
                    // Proceed with game creation
                    GameSession newGame = new GameSession();
                    newGame.playerOneId = userId;
                    newGame.gameBoard = new ArrayList<>(Collections.nCopies(9, ""));
                    newGame.status = "waiting";
                    newGame.currentPlayerId = userId;
                    newGame.playerOneName = name;
                    newGame.playerTwoName = "waiting...";
                    newGame.playerOneIcon = selectedOption;
                    newGame.gamesize = total;

                    FirebaseFirestore.getInstance().collection("gameSessions")
                            .add(newGame)
                            .addOnSuccessListener(documentReference -> {
                                String sessionId = documentReference.getId();
                                Intent intent = new Intent(creategame.this, OnevsOnline.class);
                                intent.putExtra("SESSION_ID", sessionId);
                                intent.putExtra("action", "create");
                                intent.putExtra("icon", selectedOption);
                                startActivity(intent);
                            })
                            .addOnFailureListener(e -> Toast.makeText(creategame.this, "Error starting new game", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(creategame.this, "Failed to deduct coins", Toast.LENGTH_SHORT).show());
    }


}