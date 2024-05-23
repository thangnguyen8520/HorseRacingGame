package com.example.horseracinggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InstructionActivity extends AppCompatActivity {

    private Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

        btnBackToMain = findViewById(R.id.btn_back_to_main);
        Intent intent = getIntent();
        int balance = intent.getIntExtra("balance", 0);

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
