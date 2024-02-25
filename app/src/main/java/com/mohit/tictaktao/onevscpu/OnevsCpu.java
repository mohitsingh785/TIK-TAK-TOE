package com.mohit.tictaktao.onevscpu;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mohit.tictaktao.MainActivity;
import com.mohit.tictaktao.R;
import com.mohit.tictaktao.databinding.ActivityOnevsCpuBinding;
import com.mohit.tictaktao.onevsone.OnevsOne;

import java.util.Random;

public class OnevsCpu extends AppCompatActivity {

    private ActivityOnevsCpuBinding binding;
    char[][] dp = new char[3][3];
    int human = 0;
    int cpu = 0;
    private String playerIcon;
    private long totalGames=5;
    char humanicon;
    char cpuIcon;
    int human_color;
    int cpu_color;
     String nameplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOnevsCpuBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }

        // Extract extras from the intent
        Intent intent = getIntent();
        playerIcon = intent.getStringExtra("icon"); // "X" or "O"
        String totalstr = intent.getStringExtra("total");
        totalGames = Long.parseLong(totalstr);
        String name = intent.getStringExtra("name");
         nameplayer=name;
             if (playerIcon.equals("X")) {
                 humanicon = 'X';
                 cpuIcon = 'O';
                  human_color= Color.RED;
                 cpu_color=Color.YELLOW;
                 binding.txtOne.setText(name);
                 binding.scoreCpu.setTextColor(Color.YELLOW);
                 binding.scoreHuman.setTextColor(Color.RED);
                 Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.close);
                 binding.humanImg.setImageDrawable(drawable);
                 binding.txttwo.setText("CPU");
                 Drawable drawable1 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cross_24);
                 binding.cpuImg.setImageDrawable(drawable1);
             }
             else {
                 humanicon = 'O';
                 cpuIcon = 'X';
                 human_color= Color.YELLOW;
                 cpu_color=Color.RED;
                 binding.scoreHuman.setTextColor(Color.YELLOW);
                 binding.scoreCpu.setTextColor(Color.RED);
                 binding.txtOne.setText(name);
                 Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.close);
                 binding.cpuImg.setImageDrawable(drawable);
                 binding.txttwo.setText("CPU");
                 Drawable drawable1 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.cross_24);
                 binding.humanImg.setImageDrawable(drawable1);
             }

        System.out.println("lllllllllllllllllll"+humanicon+" "+cpuIcon);
        binding.btn00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn00.getText().toString().equals("")) {
                    binding.btn00.setText(humanicon+"");
                    binding.btn00.setTextColor(human_color);
                    dp[0][0] = humanicon;
                    if (checkforwin(0, 0, humanicon)) {
                        human();
                    } else if (draw()) {
                        Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
                        reset();
                        score();
                    } else {
                        computer();

                    }


                }
            }
        });
        binding.btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn01.getText().toString().equals("")) {
                    binding.btn01.setText(humanicon+"");
                    binding.btn01.setTextColor(human_color);
                    dp[0][1] = humanicon;
                    if (checkforwin(0, 1, humanicon)) {
                        human();


                    } else if (draw()) {
                        Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
                        reset();
                        score();
                    } else {

                        computer();

                    }
                }
            }
        });
        binding.btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn02.getText().toString().equals("")) {
                    binding.btn02.setText(humanicon+"");
                    binding.btn02.setTextColor(human_color);
                    dp[0][2] = humanicon;
                    if (checkforwin(0, 2, humanicon)) {
                        human();


                    } else if (draw()) {
                        Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
                        reset();
                        score();
                    } else {

                        computer();

                    }
                }
            }
        });
        binding.btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn10.getText().toString().equals("")) {
                    binding.btn10.setText(humanicon+"");
                    binding.btn10.setTextColor(human_color);
                    dp[1][0] = humanicon;
                    if (checkforwin(1, 0, humanicon)) {
                        human();


                    } else if (draw()) {
                        Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
                        reset();
                        score();
                    } else {

                        int[] arr = cpu();
                        computer();

                    }
                }
            }
        });
        binding.btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn11.getText().toString().equals("")) {
                    binding.btn11.setText(humanicon+"");
                    binding.btn11.setTextColor(human_color);
                    dp[1][1] = humanicon;
                    if (checkforwin(1, 1, humanicon)) {
                        human();


                    } else if (draw()) {
                        Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
                        reset();
                        score();
                    } else {

                        computer();

                    }
                }
            }
        });
        binding.btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn12.getText().toString().equals("")) {
                    binding.btn12.setText(humanicon+"");
                    binding.btn12.setTextColor(human_color);
                    dp[1][2] = humanicon;
                    if (checkforwin(1, 2, humanicon)) {
                        human();


                    } else if (draw()) {
                        Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
                        reset();
                        score();
                    } else {

                        computer();

                    }
                }
            }
        });
        binding.btn20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn20.getText().toString().equals("")) {
                    binding.btn20.setText(humanicon+"");
                    dp[2][0] = humanicon;
                    binding.btn20.setTextColor(human_color);
                    if (checkforwin(2, 0, humanicon)) {
                        human();


                    } else if (draw()) {
                        Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
                        reset();
                        score();
                    } else {

                        computer();

                    }
                }
            }
        });
        binding.btn21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn21.getText().toString().equals("")) {
                    binding.btn21.setText(humanicon+"");
                    binding.btn21.setTextColor(human_color);
                    dp[2][1] = humanicon;
                    if (checkforwin(2, 1, humanicon)) {
                        human();


                    } else if (draw()) {
                        Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
                        reset();
                        score();
                    } else {

                        computer();

                    }
                }
            }
        });
        binding.btn22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn22.getText().toString().equals("")) {
                    binding.btn22.setText(humanicon+"");
                    binding.btn22.setTextColor(human_color);
                    dp[2][2] = humanicon;
                    if (checkforwin(2, 2, humanicon)) {
                        human();


                    } else if (draw()) {
                        Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
                        reset();
                        score();
                    } else {

                        computer();

                    }
                }
            }
        });

    }


    public boolean checkforwin(int i, int j, char c) {
        // Check row
        boolean win = true;
        for (int k = 0; k < 3; k++) {
            if (dp[i][k] != c) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // Check column
        win = true;
        for (int k = 0; k < 3; k++) {
            if (dp[k][j] != c) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // Check primary diagonal
        if (i == j) { // Only necessary if the move is on a primary diagonal
            win = true;
            for (int k = 0; k < 3; k++) {
                if (dp[k][k] != c) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        // Check secondary diagonal
        if (i + j == 2) { // Only necessary if the move is on a secondary diagonal
            win = true;
            for (int k = 0; k < 3; k++) {
                if (dp[k][2 - k] != c) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        // If none of the conditions are met, then there's no win
        return false;
    }


    public int[] cpu() {
        int bestScore = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Check if the cell is empty
                if (dp[i][j] == '\0') {
                    // Make the move
                    dp[i][j] = cpuIcon;
                    // Compute score for the move using Minimax
                    int score = minimax(dp, 0, false);
                    // Undo the move
                    dp[i][j] = '\0';
                    // If the score of the current move is better than the best score
                    if (score > bestScore) {
                        bestScore = score;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }

        return bestMove;
    }

    private int minimax(char[][] board, int depth, boolean isMaximizing) {
        // First, check if the game is over and return the appropriate score
        if (checkforwin(0, 0, cpuIcon)) { // Adjust checkforwin to work without parameters or implement a new method to evaluate the board state
            return 1;
        } else if (checkforwin(0, 0, humanicon)) { // Adjust checkforwin as above
            return -1;
        } else if (draw()) { // Use your existing draw method to check for a draw
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = cpuIcon;
                        int score = minimax(board, depth + 1, false);
                        board[i][j] = '\0';
                        bestScore = Math.max(score, bestScore);
                    }
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '\0') {
                        board[i][j] = humanicon;
                        int score = minimax(board, depth + 1, true);
                        board[i][j] = '\0';
                        bestScore = Math.min(score, bestScore);
                    }
                }
            }
            return bestScore;
        }
    }


    public boolean draw() {


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (dp[i][j] == '\0') {
                    return false;
                }
            }
        }
        return true;
    }

    public void reset() {


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                dp[i][j] = '\0';
                String btnId = "btn_" + i + j; // Construct the ID string
                int resId = getResources().getIdentifier(btnId, "id", getPackageName()); // Get the resource ID
                Button btn = findViewById(resId);
                btn.setText("");
            }
        }
    }

    public void human() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                human++;
                reset();
                score();
            }
        }, 200);
    }

    public void computer() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] arr = cpu();
                Log.e("cpuuuuuuuuuuuuuuuuu", arr[0] + " " + arr[1]);
                String btnId = "btn_" + arr[0] + arr[1]; // Construct the ID string
                int resId = getResources().getIdentifier(btnId, "id", getPackageName()); // Get the resource ID
                Button btn = findViewById(resId);
                btn.setText(cpuIcon+"");
                btn.setTextColor(cpu_color);
                dp[arr[0]][arr[1]] = cpuIcon;
                if (checkforwin(arr[0], arr[1], cpuIcon)) {

                    cpu++;
                    reset();
                    score();


                } else if (draw()) {

                    reset();
                    score();
                }
            }
        }, 15); // Delay
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
            Intent intent = new Intent(OnevsCpu.this, MainActivity.class);
            startActivity(intent);

            // Delete the document from Firestore
            // Add your Firestore deletion code here

            dialog.dismiss(); // Dismiss the dialog
        });

        dialog.show();
    }
    public void score() {
        binding.scoreHuman.setText(human + "");
        binding.scoreCpu.setText(cpu + "");

        if(human+cpu==totalGames){
            if(human>cpu){
                showWinnerAndCleanup(nameplayer);
            }else if(cpu>human){
                Toast.makeText(OnevsCpu.this, "CPU Win", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(OnevsCpu.this, "Draw", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to exit the game?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OnevsCpu.super.onBackPressed();
                        Intent intent = new Intent(OnevsCpu.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }


}