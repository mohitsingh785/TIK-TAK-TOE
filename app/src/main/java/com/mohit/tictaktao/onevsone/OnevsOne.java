package com.mohit.tictaktao.onevsone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.os.Handler;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mohit.tictaktao.MainActivity;
import com.mohit.tictaktao.OnevsOnline.OnevsOnline;
import com.mohit.tictaktao.R;
import com.mohit.tictaktao.databinding.ActivityOnevsOneBinding;
import com.mohit.tictaktao.onevscpu.OnevsCpu;

import java.util.Random;

public class OnevsOne extends AppCompatActivity {

    private ActivityOnevsOneBinding binding;
    char[][] dp = new char[3][3];
    int human1 = 0;
    int human2 = 0;

    boolean player1=true;
    String total;
    String playerOne;
    String playerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityOnevsOneBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        Intent intent=getIntent();
         playerOne=intent.getStringExtra("playerOne");
         playerTwo=intent.getStringExtra("playerTwo");
         total=intent.getStringExtra("total");
        playerturn();
//        if(playerOne.length()==0){
//            playerOne="player1";
//        }
//
//        if(playerTwo.length()==0){
//            playerTwo="player2";
//        }
//
// Play twing sound
        // Play default notification sound
//        try {
//            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
//            r.play();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        binding.txtOne.setText(playerOne.length()>0?playerOne:"Player1");
        binding.txttwo.setText(playerTwo.length()>0?playerTwo:"Player2");
        System.out.println("LLL: " + playerOne + " " + playerTwo);




        binding.btn00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn00.getText().toString().equals("")) {

                    if(player1){
                        player1 = false;
                        binding.btn00.setText("X");
                        binding.btn00.setTextColor(Color.RED);
                        binding.btn00.setTextSize(30);
                        playerturn();
                        dp[0][0]='X';
                        if (checkforwin(0,0,'X')){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerOne + " Win", Toast.LENGTH_SHORT).show();
                                    human1++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    }
                    else {
                        player1=true;
                        playerturn();
                        binding.btn00.setText("O");
                        binding.btn00.setTextSize(30);

                        binding.btn00.setTextColor(Color.YELLOW);
                        dp[0][0]='O';
                        if (checkforwin(0,0,'O')){
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(OnevsOne.this, playerTwo + " Win", Toast.LENGTH_SHORT).show();
                                            human2++;
                                            reset();
                                            score();
                                        }
                                    }, 8); // Delay
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    }

                }
            }
        });
        binding.btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.btn01.getText().toString().equals("")) {
                    if (player1) {
                        player1=false;
                        binding.btn01.setText("X");
                        binding.btn01.setTextSize(30);
                        playerturn();
                        binding.btn01.setTextColor(Color.RED);
                        dp[0][1] = 'X';
                        if (checkforwin(0, 1, 'X')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerOne + " Win", Toast.LENGTH_SHORT).show();
                                    human1++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    } else {
                        player1=true;
                        playerturn();
                        binding.btn01.setText("O");
                        binding.btn01.setTextSize(30);

                        binding.btn01.setTextColor(Color.YELLOW);
                        dp[0][1] = 'O';
                        if (checkforwin(0, 1, 'O')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(OnevsOne.this, playerTwo + " Win", Toast.LENGTH_SHORT).show();
                                            human2++;
                                            reset();
                                            score();
                                        }
                                    }, 8); // Delay
                                }
                            }, 8); // Delay

                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    }
                }
            }
        });
        binding.btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn02.getText().toString().equals("")) {
                    if (player1) {
                        player1 = false;
                        playerturn();
                        binding.btn02.setText("X");
                        binding.btn02.setTextSize(30);

                        binding.btn02.setTextColor(Color.RED);
                        dp[0][2] = 'X';
                        if (checkforwin(0, 2, 'X')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerOne + " Win", Toast.LENGTH_SHORT).show();
                                    human1++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay

                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    } else {
                        player1 = true;
                        playerturn();
                        binding.btn02.setText("O");
                        binding.btn02.setTextSize(30);

                        binding.btn02.setTextColor(Color.YELLOW);
                        dp[0][2] = 'O';
                        if (checkforwin(0, 2, 'O')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerTwo + " Win", Toast.LENGTH_SHORT).show();
                                    human2++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    }
                }
            }
        });
        binding.btn10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn10.getText().toString().equals("")) {
                    if (player1) {
                        player1 = false;
                        playerturn();
                        binding.btn10.setText("X");
                        binding.btn10.setTextSize(30);

                        binding.btn10.setTextColor(Color.RED);
                        dp[1][0] = 'X';
                        if (checkforwin(1, 0, 'X')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerOne + " Win", Toast.LENGTH_SHORT).show();
                                    human1++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    } else {
                        player1 = true;
                        playerturn();
                        binding.btn10.setText("O");
                        binding.btn10.setTextSize(30);

                        binding.btn10.setTextColor(Color.YELLOW);
                        dp[1][0] = 'O';
                        if (checkforwin(1, 0, 'O')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerTwo + " Win", Toast.LENGTH_SHORT).show();
                                    human2++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    }
                }
            }
        });
        binding.btn11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn11.getText().toString().equals("")) {
                    if (player1) {
                        player1 = false;
                        playerturn();
                        binding.btn11.setText("X");
                        binding.btn11.setTextSize(30);
                        binding.btn11.setTextColor(Color.RED);
                        dp[1][1] = 'X';
                        if (checkforwin(1, 1, 'X')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerOne + " Win", Toast.LENGTH_SHORT).show();
                                    human1++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    } else {
                        player1 = true;
                        playerturn();
                        binding.btn11.setText("O");
                        binding.btn11.setTextSize(30);
                        binding.btn11.setTextColor(Color.YELLOW);
                        dp[1][1] = 'O';
                        if (checkforwin(1, 1, 'O')) {

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerTwo + " Win", Toast.LENGTH_SHORT).show();
                                    human2++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    }
                }
            }
        });
        binding.btn12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn12.getText().toString().equals("")) {
                    if (player1) {
                        player1 = false;
                        playerturn();
                        binding.btn12.setText("X");
                        binding.btn12.setTextSize(30);
                        binding.btn12.setTextColor(Color.RED);
                        dp[1][2] = 'X';
                        if (checkforwin(1, 2, 'X')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerOne + " Win", Toast.LENGTH_SHORT).show();
                                    human1++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    } else {
                        player1 = true;
                        playerturn();
                        binding.btn12.setText("O");
                        binding.btn12.setTextSize(30);
                        binding.btn12.setTextColor(Color.YELLOW);
                        dp[1][2] = 'O';
                        if (checkforwin(1, 2, 'O')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerTwo + " Win", Toast.LENGTH_SHORT).show();
                                    human2++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    }
                }
            }
        });
        binding.btn20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn20.getText().toString().equals("")) {
                    if (player1) {
                        player1 = false;
                        playerturn();
                        binding.btn20.setText("X");
                        binding.btn20.setTextSize(30);
                        binding.btn20.setTextColor(Color.RED);
                        dp[2][0] = 'X';
                        if (checkforwin(2, 0, 'X')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerOne + " Win", Toast.LENGTH_SHORT).show();
                                    human2++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    } else {
                        player1 = true;
                        playerturn();
                        binding.btn20.setText("O");
                        binding.btn20.setTextSize(30);
                        binding.btn20.setTextColor(Color.YELLOW);
                        dp[2][0] = 'O';
                        if (checkforwin(2, 0, 'O')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerTwo + " Win", Toast.LENGTH_SHORT).show();
                                    human2++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    }
                }
            }
        });
        binding.btn21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn21.getText().toString().equals("")) {
                    if (player1) {
                        player1 = false;
                        playerturn();
                        binding.btn21.setText("X");
                        binding.btn21.setTextSize(30);
                        binding.btn21.setTextColor(Color.RED);
                        dp[2][1] = 'X';
                        if (checkforwin(2, 1, 'X')) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerOne + " Win", Toast.LENGTH_SHORT).show();
                                    human2++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    } else {
                        player1 = true;
                        playerturn();
                        binding.btn21.setText("O");
                        binding.btn21.setTextSize(30);
                        binding.btn21.setTextColor(Color.YELLOW);
                        dp[2][1] = 'O';
                        if (checkforwin(2, 1, 'O')) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(OnevsOne.this, playerTwo + " Win", Toast.LENGTH_SHORT).show();
                                    human2++;
                                    reset();
                                    score();
                                }
                            }, 8); // Delay
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    }
                }
            }
        });
        binding.btn22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.btn22.getText().toString().equals("")) {
                    if (player1) {
                        player1 = false;
                        playerturn();
                        binding.btn22.setText("X");
                        binding.btn22.setTextSize(30);
                        binding.btn22.setTextColor(Color.RED);
                        dp[2][2] = 'X';
                        if (checkforwin(2, 2, 'X')) {
                            Toast.makeText(OnevsOne.this, playerOne+" Win", Toast.LENGTH_SHORT).show();
                            human1++;
                            reset();
                            score();
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
                    } else {
                        player1 = true;
                        playerturn();
                        binding.btn22.setText("O");
                        binding.btn22.setTextSize(30);
                        binding.btn22.setTextColor(Color.YELLOW);
                        dp[2][2] = 'O';
                        if (checkforwin(2, 2, 'O')) {
                            Toast.makeText(OnevsOne.this, playerTwo +" Win", Toast.LENGTH_SHORT).show();
                            human2++;
                            reset();
                            score();
                        }
                        else if(draw()){
                            Toast.makeText(OnevsOne.this, "Draw", Toast.LENGTH_SHORT).show();
                            reset();
                            score();
                        }
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




    public void reset(){


        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                dp[i][j]='\0';
                String btnId = "btn_" + i + j; // Construct the ID string
                int resId = getResources().getIdentifier(btnId, "id", getPackageName()); // Get the resource ID
                Button btn=findViewById(resId);
                btn.setText("");
            }
        }
    }

    public boolean draw(){


        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(dp[i][j]=='\0'){
                    return false;
                }
            }
        }
        return true;
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
            Intent intent = new Intent(OnevsOne.this, MainActivity.class);
            startActivity(intent);

            // Delete the document from Firestore
            // Add your Firestore deletion code here

            dialog.dismiss(); // Dismiss the dialog
        });

        dialog.show();
    }

    public void score(){
        binding.scoreHuman.setText(human1+"");
        binding.scoreCpu.setText(human2+"");

        if(Integer.parseInt(total)==human1+human2){

            if (human2>human1){
                showWinnerAndCleanup(playerTwo);
            }
            else {
                showWinnerAndCleanup(playerOne);
            }

        }
    }

    public void playerturn(){

        if(player1){

           binding.player1chance.setBackgroundResource(R.drawable.bg_slt);
            binding.player2chance.setBackgroundResource(R.drawable.bg_edittext);
        }
        else{
            binding.player1chance.setBackgroundResource(R.drawable.bg_edittext);
            binding.player2chance.setBackgroundResource(R.drawable.bg_slt);
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        OnevsOne.super.onBackPressed();
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}