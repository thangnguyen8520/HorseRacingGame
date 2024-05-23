package com.example.horseracinggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoseActivity extends AppCompatActivity {
    private TextView tvLoseMessage, tvBalanceMessage;
    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lose);

        tvLoseMessage = findViewById(R.id.tv_lose_message);
        tvBalanceMessage = findViewById(R.id.tv_balance_message);
        btnBackToMain = findViewById(R.id.btn_back_to_main);

        Intent intent = getIntent();
        int balance = intent.getIntExtra("balance", 0);
        int losses = intent.getIntExtra("losses", 0);

        tvLoseMessage.setText("You lost $" + losses + ".");
        tvBalanceMessage.setText("Your new balance is $" + balance);

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(LoseActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}
