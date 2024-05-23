package com.example.horseracinggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WinActivity extends AppCompatActivity {
    private TextView tvWinningHorse, tvWinMessage, tvBalanceMessage;
    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        tvWinningHorse = findViewById(R.id.tv_winning_horse);
        tvWinMessage = findViewById(R.id.tv_win_message);
        tvBalanceMessage = findViewById(R.id.tv_balance_message);
        btnBackToMain = findViewById(R.id.btn_back_to_main);

        Intent intent = getIntent();
        String winningHorse = intent.getStringExtra("winningHorse");
        int balance = intent.getIntExtra("balance", 0);
        int winnings = intent.getIntExtra("winnings", 0);

        tvWinningHorse.setText("Winning Horse: " + winningHorse);
        tvWinMessage.setText("Congratulations! You won $" + winnings + "!");
        tvBalanceMessage.setText("Your new balance is $" + balance);

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("balance", balance);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
