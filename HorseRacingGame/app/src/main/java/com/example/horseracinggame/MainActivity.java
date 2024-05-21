package com.example.horseracinggame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView horseImage1, horseImage2, horseImage3;
    private EditText etBet1, etBet2, etBet3;
    private Button btnStart, btnReset;
    private TextView tvBalance;
    private int balance = 1000;
    private Random random;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private RelativeLayout raceTrackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        horseImage1 = findViewById(R.id.horseImage1);
        horseImage2 = findViewById(R.id.horseImage2);
        horseImage3 = findViewById(R.id.horseImage3);
        etBet1 = findViewById(R.id.et_bet1);
        etBet2 = findViewById(R.id.et_bet2);
        etBet3 = findViewById(R.id.et_bet3);
        btnStart = findViewById(R.id.btn_start);
        btnReset = findViewById(R.id.btn_reset);
        tvBalance = findViewById(R.id.tv_balance);
        raceTrackLayout = findViewById(R.id.raceTrackLayout);
        random = new Random();
        handler = new Handler();
        mediaPlayer = MediaPlayer.create(this, R.raw.race_sound);

        tvBalance.setText("Balance: $" + balance);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRace();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRace();
            }
        });
    }

    private void startRace() {
        final int bet1, bet2, bet3;
        try {
            bet1 = Integer.parseInt(etBet1.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid bet for Horse 1", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            bet2 = Integer.parseInt(etBet2.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid bet for Horse 2", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            bet3 = Integer.parseInt(etBet3.getText().toString());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid bet for Horse 3", Toast.LENGTH_SHORT).show();
            return;
        }

        final int totalBet = bet1 + bet2 + bet3;

        if (totalBet > balance) {
            Toast.makeText(this, "Insufficient balance!", Toast.LENGTH_SHORT).show();
            return;
        }

        mediaPlayer.start();
        balance -= totalBet;
        tvBalance.setText("Balance: $" + balance);

        final int maxDistance = raceTrackLayout.getWidth() - horseImage1.getWidth();
        horseImage1.setTranslationX(0);
        horseImage2.setTranslationX(0);
        horseImage3.setTranslationX(0);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int progress1 = (int) (horseImage1.getTranslationX() + random.nextInt(10));
                int progress2 = (int) (horseImage2.getTranslationX() + random.nextInt(10));
                int progress3 = (int) (horseImage3.getTranslationX() + random.nextInt(10));

                horseImage1.setTranslationX(progress1);
                horseImage2.setTranslationX(progress2);
                horseImage3.setTranslationX(progress3);

                if (progress1 >= maxDistance || progress2 >= maxDistance || progress3 >= maxDistance) {
                    mediaPlayer.stop();
                    determineWinner(progress1, progress2, progress3, bet1, bet2, bet3);
                } else {
                    handler.postDelayed(this, 100);
                }
            }
        }, 100);
    }

    private void determineWinner(int progress1, int progress2, int progress3, int bet1, int bet2, int bet3) {
        int winner;
        int maxDistance = raceTrackLayout.getWidth() - horseImage1.getWidth();
        if (progress1 >= maxDistance) {
            winner = 1;
        } else if (progress2 >= maxDistance) {
            winner = 2;
        } else {
            winner = 3;
        }

        int winnings = 0;
        if (winner == 1) {
            winnings = bet1 * 2;
        } else if (winner == 2) {
            winnings = bet2 * 2;
        } else if (winner == 3) {
            winnings = bet3 * 2;
        }

        balance += winnings;
        tvBalance.setText("Balance: $" + balance);

        Toast.makeText(this, "Horse " + winner + " wins! You won $" + winnings, Toast.LENGTH_SHORT).show();

        if (balance > 0) {
            Intent intent = new Intent(MainActivity.this, WinActivity.class);
            intent.putExtra("balance", balance);
            startActivity(intent);
        }
    }

    private void resetRace() {
        horseImage1.setTranslationX(0);
        horseImage2.setTranslationX(0);
        horseImage3.setTranslationX(0);
        etBet1.setText("");
        etBet2.setText("");
        etBet3.setText("");
    }
}
