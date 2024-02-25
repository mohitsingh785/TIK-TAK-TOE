package com.mohit.tictaktao.onevscpu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.mohit.tictaktao.OnevsOnline.OnevsOnline;
import com.mohit.tictaktao.OnevsOnline.creategame;
import com.mohit.tictaktao.R;
import com.mohit.tictaktao.databinding.ActivityChosecharacterBinding;

public class chosecharacter extends AppCompatActivity {
   private ActivityChosecharacterBinding binding;
    private RadioGroup radioGroupMode;
    private String selectedOption = "X";
    private EditText editTextUserName;

    private LinearLayout favX, favO;

    private long total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChosecharacterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Change the status bar color.
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }
        initUI();

    }
    private void selectOption(LinearLayout selected, LinearLayout other) {
        // Here, add logic to change the appearance based on selection
        // For simplicity, let's assume it changes the background
        selected.setBackgroundResource(R.drawable.bg_slt); // Make sure this drawable exists
        other.setBackgroundResource(R.drawable.bg_edittext); // Default background
    }
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

        binding.buttonStartNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(chosecharacter.this, OnevsCpu.class);

                intent.putExtra("name", editTextUserName.getText().toString());
                intent.putExtra("icon", selectedOption);
                intent.putExtra("total", total+"");
                startActivity(intent);

            }
        });
    }
}