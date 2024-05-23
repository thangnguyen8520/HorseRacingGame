package com.example.horseracinggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RechargeActivity extends AppCompatActivity {
    private EditText etRechargeAmount;
    private Button btnConfirmRecharge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        etRechargeAmount = findViewById(R.id.et_recharge_amount);
        btnConfirmRecharge = findViewById(R.id.btn_confirm_recharge);

        btnConfirmRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountStr = etRechargeAmount.getText().toString();
                if (!amountStr.isEmpty()) {
                    int rechargeAmount = Integer.parseInt(amountStr);
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("rechargeAmount", rechargeAmount);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(RechargeActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
