package com.mohit.tictaktao.onevsone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mohit.tictaktao.R;
import com.mohit.tictaktao.databinding.ActivitySelectPageBinding;

public class select_page extends AppCompatActivity {
    private ActivitySelectPageBinding binding;
    private RadioGroup radioGroupMode;
    private long total=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // Move setContentView here right after binding

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.status));
        }

        // No need to find the radioGroupMode by ID as you can directly use binding
        binding.radioGroupMode.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButton5) {
                total = 5;
            } else if (checkedId == R.id.radioButton3) {
                total = 3;
            } else if (checkedId == R.id.radioButton10) {
                total = 10;
            }
        });

        binding.btn.setOnClickListener(v -> {
            if (binding.edtPlayer1.getText().toString().isEmpty()) {
                binding.edtPlayer1.setError("Enter First player name");
                return;
            }
            if (binding.edtPlayer2.getText().toString().isEmpty()) {
                binding.edtPlayer2.setError("Enter Second player name");
                return;
            }
            if (total == 0) {
                Toast.makeText(select_page.this, "Chose the game mode", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(select_page.this, OnevsOne.class);
            intent.putExtra("playerOne", binding.edtPlayer1.getText().toString());
            intent.putExtra("playerTwo", binding.edtPlayer2.getText().toString()); // Ensure consistency in naming
            intent.putExtra("total", String.valueOf(total)); // Directly pass long as String
            startActivity(intent);
        });
    }

}