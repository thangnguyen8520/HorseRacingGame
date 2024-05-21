package com.example.horseracinggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WinActivity extends AppCompatActivity {

    private TextView tvWinMessage;
    private TextView tvBalanceMessage;
    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        tvWinMessage = findViewById(R.id.tv_win_message);
        tvBalanceMessage = findViewById(R.id.tv_balance_message);
        btnBackToMain = findViewById(R.id.btn_back_to_main);

        int balance = getIntent().getIntExtra("balance", 0);
        tvBalanceMessage.setText("Your new balance is $" + balance);

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}