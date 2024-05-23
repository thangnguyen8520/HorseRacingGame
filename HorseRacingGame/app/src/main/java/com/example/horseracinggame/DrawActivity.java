package com.example.horseracinggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DrawActivity extends AppCompatActivity {
    private TextView tvDrawMessage, tvBalanceMessage;
    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        tvDrawMessage = findViewById(R.id.tv_draw_message);
        tvBalanceMessage = findViewById(R.id.tv_balance_message);
        btnBackToMain = findViewById(R.id.btn_back_to_main);

        Intent intent = getIntent();
        int balance = intent.getIntExtra("balance", 0);

        tvBalanceMessage.setText("Your balance remains $" + balance);

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(DrawActivity.this, MainActivity.class);
                startActivity(mainIntent);
            }
        });
    }
}
