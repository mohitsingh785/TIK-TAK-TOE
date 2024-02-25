package com.mohit.tictaktao.OnevsOnline;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.mohit.tictaktao.MainActivity;
import com.mohit.tictaktao.R;
import com.mohit.tictaktao.databinding.ActivityOnevsOneBinding;
import com.mohit.tictaktao.databinding.ActivityOnevsOnlineBinding;
import com.mohit.tictaktao.onevsone.OnevsOne;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OnevsOnline extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String currentSessionId;
    private TextView scoreHuman, scoreCpu;
    private Button[] gameButtons = new Button[9];
    String player1icon;
    String player2icon;
    private  long total;


    private ActivityOnevsOnlineBinding binding;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentSessionId", currentSessionId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentSessionId = savedInstanceState.getString("currentSessionId");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityOnevsOnlineBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        try{
            player1icon=intent.getStringExtra("icon");
            if(player1icon.equals("X")){
                player2icon="O";
            }
            else{
                player2icon="X";
            }
        }
        catch(Exception e){

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        // Ensure this layout exists.

        String action = intent.getStringExtra("SESSION_ID");
         currentSessionId=action;
        initializeUI();
        handleGameSessionIntent();


        db.collection("gameSessions").document(currentSessionId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {

                    player1icon=document.getString("playerOneIcon");
                    if(player1icon.equals("X")){
                        player2icon="O";
                    }
                    else{
                        player2icon="X";
                    }
                    total=document.getLong("gamesize");
                    // Assuming 'playerOneName' is a direct field of type String
                    String playerOneName = document.getString("playerOneName");
                    String playerTwoName = document.getString("playerTwoName");
                    if (playerOneName != null) {

                        binding.txtOne.setText(playerOneName);
                        if(player1icon.equals("X")){
                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.close);
                            binding.imgPlayer1.setImageDrawable(drawable);

                        }
                        else{

                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cross_24);
                            binding.imgPlayer1.setImageDrawable(drawable);

                        }
                    } else {
                        // Handle case where playerOneName might not be set or is null
                        Log.d("Firestore", "No playerOneName found in the document.");
                    }
                    if (playerTwoName != null) {

                        binding.txttwo.setText(playerTwoName);

                        if(player2icon.equals("X")){
                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.close);
                            binding.imgPlayer2.setImageDrawable(drawable);

                        }
                        else{

                            Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cross_24);
                            binding.imgPlayer2.setImageDrawable(drawable);

                        }

                    } else {
                        // Handle case where playerOneName might not be set or is null
                        Log.d("Firestore", "No playerOneName found in the document.");
                    }
                } else {
                    Log.d("Firestore", "No such document");
                }
            } else {
                Log.d("Firestore", "get failed with ", task.getException());
            }
        });

         binding.txtSid.setText("Session ID: " + currentSessionId);
        // Set an OnClickListener on the TextView
        binding.imgSid.setOnClickListener(view -> {
            // Get the ClipboardManager service
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // Create a ClipData with the session ID
            ClipData clip = ClipData.newPlainText("Session ID", currentSessionId);
            // Set the ClipData to the clipboard
            clipboard.setPrimaryClip(clip);
            // Show a toast message to inform the user
            binding.txtSid.setVisibility(View.GONE);
            binding.imgSid.setVisibility(View.GONE);
            Toast.makeText(this, "Session ID copied to clipboard", Toast.LENGTH_SHORT).show();
        });
        binding.sendMessageButton.setOnClickListener(v -> {
            String messageText = binding.messageInput.getText().toString();
            if (!messageText.isEmpty()) {
                sendMessage(messageText);
                binding.messageInput.setText(""); // Clear input field
            }
        });

        listenForMessages();
        updateCoinsDisplay();
        // Restore session ID if the activity is being recreated
        if (savedInstanceState != null) {
            currentSessionId = savedInstanceState.getString("currentSessionId", null);
        }
    }



    private void initializeUI() {
        scoreHuman = findViewById(R.id.score_human);
        scoreCpu = findViewById(R.id.score_cpu);


        for (int i = 0; i < gameButtons.length; i++) {
            String buttonID = "btn_" + (i / 3) + (i % 3);
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            gameButtons[i] = findViewById(resID);
            final int finalI = i; // Create a final variable for use in the lambda
            gameButtons[i].setOnClickListener(v -> makeMove(finalI));
        }


    }

    private void handleGameSessionIntent() {
        String action = getIntent().getStringExtra("action");
        System.out.println("llllllllllllllllllll "+action);
        if ("create".equals(action)) {
            Toast.makeText(OnevsOnline.this, "Game session created. Session ID: " + currentSessionId, Toast.LENGTH_LONG).show();

            startListeningToSessionUpdates();
        } else if ("join".equals(action)) {
            Toast.makeText(OnevsOnline.this, "Joined game session. Session ID: " + currentSessionId + "", Toast.LENGTH_SHORT).show();

            startListeningToSessionUpdates();
        }
    }

    private void createGameSession() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ArrayList<String> initialBoard = new ArrayList<>(Collections.nCopies(9, ""));
        GameSession newGame = new GameSession(userId, initialBoard, "waiting");

        db.collection("gameSessions")
                .add(newGame)
                .addOnSuccessListener(documentReference -> {
                    currentSessionId = documentReference.getId();
                    Toast.makeText(OnevsOnline.this, "Game session created. Session ID: " + currentSessionId, Toast.LENGTH_LONG).show();
                    startListeningToSessionUpdates();
                })
                .addOnFailureListener(e -> Toast.makeText(OnevsOnline.this, "Failed to create game session", Toast.LENGTH_SHORT).show());
    }

    private void joinGameSession(String sessionId) {
        DocumentReference sessionRef = db.collection("gameSessions").document(sessionId);
        Log.d("GameSession", "Session ID on create/join: " + currentSessionId);

        sessionRef.get().addOnSuccessListener(documentSnapshot -> {
            GameSession gameSession = documentSnapshot.toObject(GameSession.class);
            if (gameSession != null && "waiting".equals(gameSession.status)) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                sessionRef.update("playerTwoId", userId, "status", "active")
                        .addOnSuccessListener(aVoid -> {
                            currentSessionId = sessionId;
                            Toast.makeText(OnevsOnline.this, "Joined game session", Toast.LENGTH_SHORT).show();
                            startListeningToSessionUpdates();
                        });
            } else {
                Toast.makeText(OnevsOnline.this, "Session not available", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void makeMove(int position) {
        if (currentSessionId == null || currentSessionId.isEmpty()) {
            Toast.makeText(this, "Session not initialized.", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference sessionRef = db.collection("gameSessions").document(currentSessionId);
        sessionRef.get().addOnSuccessListener(documentSnapshot -> {
            GameSession gameSession = documentSnapshot.toObject(GameSession.class);
            if (gameSession == null) {
                Toast.makeText(this, "Game session error.", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            // Ensure it's the current player's turn and the position is not already taken
            if (!userId.equals(gameSession.currentPlayerId) || !gameSession.gameBoard.get(position).isEmpty()) {
                Toast.makeText(this, "Not your turn or position already taken", Toast.LENGTH_SHORT).show();
                return;
            }

            System.out.println("checkkk "+player1icon+" "+player2icon);
            // Update the game board
            String playerSymbol = userId.equals(gameSession.playerOneId) ? player1icon : player2icon;
            gameSession.gameBoard.set(position, playerSymbol);

            // Switch turns
            gameSession.currentPlayerId = userId.equals(gameSession.playerOneId) ? gameSession.playerTwoId : gameSession.playerOneId;

            // Check for win or draw before updating Firestore
            boolean win = checkWin(gameSession.gameBoard, playerSymbol);
            boolean draw = checkDraw(gameSession.gameBoard) && !win;

            if(win){
                if (userId.equals(gameSession.playerOneId)) {
                    gameSession.setScorePlayerOne(gameSession.getScorePlayerOne() + 1);
                } else {
                    gameSession.setScorePlayerTwo(gameSession.getScorePlayerTwo() + 1);
                }
            }
            if (win || draw) {
                gameSession.status = win ? "win" : "draw";
            }

            // Push updates to Firestore and reflect in UI
            updateFirestoreAndUI(sessionRef, gameSession, win, draw);
        }).addOnFailureListener(e -> Log.e("MakeMove", "Error fetching game session", e));
    }



    private void updateFirestoreAndUI(DocumentReference sessionRef, GameSession gameSession, boolean win, boolean draw) {
        String currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        sessionRef.set(gameSession).addOnSuccessListener(aVoid -> {
            updateBoardUI(gameSession.gameBoard);
            boolean isCurrentUserWinner = currentUserID.equals(gameSession.currentPlayerId); // Assuming currentPlayerId is the winner if win is true

            if (win) {
                resetBoardUI();
                updateUserStats(currentUserID, true, false, isCurrentUserWinner);
                String message = isCurrentUserWinner ? "You win!" : "You lose!";
                Toast.makeText(OnevsOnline.this, message, Toast.LENGTH_SHORT).show();
            } else if (draw) {
                resetBoardUI();
                updateUserStats(currentUserID, false, true, false);
                Toast.makeText(OnevsOnline.this, "It's a draw!", Toast.LENGTH_SHORT).show();
            } else {
                // If not win or draw, then it's a loss for the current user.
                updateUserStats(currentUserID, false, false, false);
            }
        }).addOnFailureListener(e -> Toast.makeText(OnevsOnline.this, "Failed to update game session", Toast.LENGTH_SHORT).show());
    }


    private void updateUserStats(String userId, boolean win, boolean draw, boolean isWinner) {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Extract current stats
                long coins = documentSnapshot.getLong("coins") != null ? documentSnapshot.getLong("coins") : 0;
                long matchesPlayed = documentSnapshot.getLong("matches_played") != null ? documentSnapshot.getLong("matches_played") : 0;
                long matchesWon = documentSnapshot.getLong("matches_won") != null ? documentSnapshot.getLong("matches_won") : 0;
                long matchesDraw = documentSnapshot.getLong("matches_draw") != null ? documentSnapshot.getLong("matches_draw") : 0;
                long matchesLoss = documentSnapshot.getLong("matches_loss") != null ? documentSnapshot.getLong("matches_loss") : 0;
                long level = documentSnapshot.getLong("level") != null ? documentSnapshot.getLong("level") : 1;

                // Update stats based on game outcome
                coins += isWinner ? 100 : 0;
                matchesPlayed += 1;
                if (win) {
                    matchesWon += 1;
                } else if (draw) {
                    matchesDraw += 1;
                } else {
                    matchesLoss += 1; // Increment loss if not a win or draw
                }

                // Calculate win rate
                double winRate = matchesPlayed > 0 ? (double) matchesWon / matchesPlayed : 0;

                // Adjust level based on criteria
                if (matchesPlayed >= 20 && winRate >= 0.5) level = 2;
                if (matchesPlayed >= 35 && winRate >= 0.7) level = 3;
                // Add more level conditions as necessary

                // Prepare update map
                Map<String, Object> updates = new HashMap<>();
                updates.put("coins", coins);
                updates.put("matches_played", matchesPlayed);
                updates.put("matches_won", matchesWon);
                updates.put("matches_draw", matchesDraw);
                updates.put("matches_loss", matchesLoss);
                updates.put("level", level);

                // Update Firestore
                userRef.update(updates)
                        .addOnSuccessListener(aVoid -> Log.d("UpdateUserStats", "User stats updated successfully"))
                        .addOnFailureListener(e -> Log.e("UpdateUserStats", "Error updating user stats", e));
            } else {
                Log.e("UpdateUserStats", "User document does not exist.");
            }
        }).addOnFailureListener(e -> Log.e("UpdateUserStats", "Error fetching user document", e));
    }


    private boolean checkDraw(ArrayList<String> gameBoard) {
        for (String spot : gameBoard) {
            if (spot.isEmpty()) {
                return false; // At least one position is not filled, no draw
            }
        }
        return true; // No positions left, it's a draw
    }

    private void updateBoardUI(ArrayList<String> gameBoard) {
        runOnUiThread(() -> {
            for (int i = 0; i < gameButtons.length; i++) {
                String value = gameBoard.get(i);
                gameButtons[i].setText(value);
                if(value.equals("X")){
                    gameButtons[i].setTextColor(Color.RED);
                }
                else{
                    gameButtons[i].setTextColor(Color.YELLOW);
                }
            }
        });
    }


    private void resetBoardUI() {
        DocumentReference sessionRef = db.collection("gameSessions").document(currentSessionId);
        sessionRef.update("gameBoard", new ArrayList<>(Collections.nCopies(9, "")),
                "status", "active").addOnSuccessListener(aVoid -> {
            for (Button gameButton : gameButtons) {
                gameButton.setText("");
            }
        });
    }

    private boolean checkWin(ArrayList<String> gameBoard, String playerSymbol) {
        // Horizontal, Vertical & Diagonal Win Check
        for (int i = 0; i < 3; i++) {
            if (playerSymbol.equals(gameBoard.get(i * 3)) && playerSymbol.equals(gameBoard.get(i * 3 + 1)) && playerSymbol.equals(gameBoard.get(i * 3 + 2)))
                return true; // Horizontal
            if (playerSymbol.equals(gameBoard.get(i)) && playerSymbol.equals(gameBoard.get(i + 3)) && playerSymbol.equals(gameBoard.get(i + 6)))
                return true; // Vertical
        }
        // Diagonals
        if (playerSymbol.equals(gameBoard.get(0)) && playerSymbol.equals(gameBoard.get(4)) && playerSymbol.equals(gameBoard.get(8)))
            return true;
        if (playerSymbol.equals(gameBoard.get(2)) && playerSymbol.equals(gameBoard.get(4)) && playerSymbol.equals(gameBoard.get(6)))
            return true;

        return false;
    }

    private void startListeningToSessionUpdates() {
        if (currentSessionId == null || currentSessionId.isEmpty()) {
            Log.w("GameUpdate", "No Session ID found for listening to updates.");
            return;
        }

        DocumentReference sessionRef = db.collection("gameSessions").document(currentSessionId);
        sessionRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("FirestoreListen", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    GameSession gameSession = snapshot.toObject(GameSession.class);
                    if (gameSession != null) {
                        updateUI(gameSession); // This updates the UI based on the current game state

                        // Now, also check if the second player has joined and update the TextViews accordingly
                        String playerTwoName = gameSession.getPlayerTwoName(); // Assuming you have this getter method in your GameSession model
                        if (playerTwoName != null && !playerTwoName.equals("waiting...")) {

                            binding.txttwo.setText(playerTwoName);

                        }
                    }
                } else {
                    Log.d("FirestoreListen", "Current data: null");
                }
            }
        });
    }


    private void updateUI(GameSession gameSession) {
        // Update the game board
        for (int i = 0; i < gameButtons.length; i++) {
            String cellValue = gameSession.getGameBoard().get(i);
            Button button = gameButtons[i];
            button.setText(cellValue); // Set text to "X", "O", or "" based on the cell value
            button.setEnabled(cellValue.isEmpty()); // Disable the button if the cell is not empty
        }

        // Update scores, game status, etc.
        scoreHuman.setText(String.valueOf(gameSession.getScorePlayerOne()));

        scoreCpu.setText(String.valueOf(gameSession.getScorePlayerTwo()));
        // Add more UI updates as needed
        if(gameSession.getScorePlayerOne() + gameSession.getScorePlayerTwo() == gameSession.gamesize) {
            if(gameSession.getScorePlayerOne() > gameSession.getScorePlayerTwo()) {
                showWinnerAndCleanup(gameSession.playerOneName + " Won");
            } else {
                showWinnerAndCleanup(gameSession.playerTwoName + " Won");
            }
        }

        if(player1icon.equals("X")){
            scoreHuman.setTextColor(Color.RED);
            scoreCpu.setTextColor(Color.YELLOW);

        }
        else{
            scoreCpu.setTextColor(Color.RED);

            scoreHuman.setTextColor(Color.YELLOW);

        }
    }


    private void showWinnerAndCleanup(String winnerName) {
        // Inflate the custom layout
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_game_over, null);

        // Find the TextView in the custom layout and set the winner announcement
        TextView tvWinnerAnnouncement = dialogView.findViewById(R.id.tvWinnerAnnouncement);
        tvWinnerAnnouncement.setText(winnerName + " Won");

        // Create the AlertDialog and set the custom layout
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();

        // Find the button in the custom layout and set its onClick listener
        Button btnGoToMain = dialogView.findViewById(R.id.btnGoToMain);
        btnGoToMain.setOnClickListener(v -> {
            // Intent to go back to MainActivity
            Intent intent = new Intent(OnevsOnline.this, MainActivity.class);
            startActivity(intent);
            deleteDocumentFromFirestore();

            // Delete the document from Firestore
            // Add your Firestore deletion code here

            dialog.dismiss(); // Dismiss the dialog
        });

        dialog.show();
    }

    private void sendMessage(String messageText) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Deduct 50 coins for sending a message
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                long currentCoins = documentSnapshot.getLong("coins") != null ? documentSnapshot.getLong("coins") : 0;
                if (currentCoins >= 10) {
                    long newCoins = currentCoins - 10;
                    userRef.update("coins", newCoins)
                            .addOnSuccessListener(aVoid -> {
                                // Now, send the message
                                Map<String, Object> message = new HashMap<>();
                                message.put("text", messageText);
                                message.put("senderId", userId);
                                message.put("timestamp", FieldValue.serverTimestamp());

                                db.collection("gameSessions").document(currentSessionId)
                                        .collection("messages").add(message)
                                        .addOnSuccessListener(documentReference -> {
                                            Log.d("SendMessage", "Message sent successfully");
                                            updateCoinsDisplay(); // Update the coin display after sending a message
                                        })
                                        .addOnFailureListener(e -> Log.e("SendMessage", "Error sending message", e));
                            })
                            .addOnFailureListener(e -> Toast.makeText(OnevsOnline.this, "Failed to deduct coins", Toast.LENGTH_SHORT).show());
                } else {
                    Toast.makeText(OnevsOnline.this, "Not enough coins to send message", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(e -> Log.e("FetchUserCoins", "Error fetching user coins", e));
    }

    private void updateCoinsDisplay() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                long currentCoins = documentSnapshot.getLong("coins") != null ? documentSnapshot.getLong("coins") : 0;
                binding.tvCurrentCoins.setText("Coins: " + currentCoins);
            }
        }).addOnFailureListener(e -> Log.e("FetchUserCoins", "Error fetching user coins", e));
    }

    private void listenForMessages() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("gameSessions").document(currentSessionId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("ListenForMessages", "Listen failed.", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    String senderId = dc.getDocument().getString("senderId");
                                    String messageText = dc.getDocument().getString("text");
                                    if (!senderId.equals(userId)) { // If the current user is not the sender
                                        runOnUiThread(() -> Toast.makeText(OnevsOnline.this, messageText, Toast.LENGTH_SHORT).show());
                                    }
                                    break;
                            }
                        }
                    }
                });
    }


    private void deleteDocumentFromFirestore() {
        // Assuming 'db' is your Firestore instance and you know the document ID to delete
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Replace "your_collection" with the actual collection name and "document_id" with the document ID to delete
        db.collection("gameSessions").document(currentSessionId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error deleting document", e);
                    }
                });
    }


    @Override
    public void onBackPressed() {
        // Show the alert dialog to confirm if the user really wants to go back.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Want to go back to Main?");
        builder.setMessage("Your game will be deleted");

        // Handle the positive button action.
        builder.setPositiveButton("Go to Main", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the document from Firestore before leaving.
                deleteDocumentFromFirestore();

                // Now that the user has confirmed, it's safe to call super.onBackPressed().
                OnevsOnline.super.onBackPressed();

                // Alternatively, if you want to navigate to MainActivity explicitly instead of just finishing,
                // you could replace the above line with the following intent.
                // Intent intent = new Intent(OnevsOnline.this, MainActivity.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear the back stack.
                // startActivity(intent);
            }
        });

        // Handle the negative button action (optional).
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If "Cancel" is clicked, just dismiss the dialog and do nothing.
                dialog.dismiss();
            }
        });

        // Show the dialog.
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}
