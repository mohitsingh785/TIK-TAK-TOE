package com.mohit.tictaktao.OnevsOnline;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mohit.tictaktao.R;

import java.util.ArrayList;
import java.util.Collections;

public class LobbyActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText editTextGameSessionId;
    private  EditText editTextUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        // Initialize UI components and anonymous sign-in
        initUI();
//        signInAnonymously();
    }

    private void initUI() {
        Button startGameButton = findViewById(R.id.buttonStartNewGame);
        Button joinGameButton = findViewById(R.id.buttonJoinGame);
        editTextGameSessionId = findViewById(R.id.editTextGameSessionId);
        editTextUserName = findViewById(R.id.editTextGameName);

        startGameButton.setOnClickListener(v -> createGame());
        joinGameButton.setOnClickListener(v -> joinGame());
    }

    private void signInAnonymously() {
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                // Optional: Update UI or notify user of successful sign in if needed
            } else {
                Toast.makeText(LobbyActivity.this, "Authentication failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void createGame() {

           startActivity(new Intent(LobbyActivity.this, creategame.class));
    }


    private void joinGame() {
        String sessionId = editTextGameSessionId.getText().toString().trim();
        if (sessionId.isEmpty()) {
            Toast.makeText(this, "Please enter a session ID", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = editTextUserName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            db.collection("gameSessions").document(sessionId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        GameSession gameSession = documentSnapshot.toObject(GameSession.class);
                        if (gameSession != null && "waiting".equals(gameSession.status)) {
                            db.collection("gameSessions").document(sessionId)
                                    .update("playerTwoId", userId, "status", "active","sessionId",sessionId,"playerTwoName",editTextUserName.getText().toString())
                                    .addOnSuccessListener(aVoid -> {
                                        Intent intent = new Intent(LobbyActivity.this, OnevsOnline.class);
                                        intent.putExtra("SESSION_ID", sessionId);
                                        intent.putExtra("action","join");
                                        startActivity(intent);
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(LobbyActivity.this, "Failed to join the game", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(LobbyActivity.this, "Invalid session ID or game already in progress", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(LobbyActivity.this, "Failed to find the game", Toast.LENGTH_SHORT).show());
        } else {
//            signInAnonymously(); // Attempt to sign in anonymously if not already signed in
        }
    }
}
