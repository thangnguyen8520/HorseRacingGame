package com.example.horseracinggame;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
    private AnimationDrawable horseAnimation1, horseAnimation2, horseAnimation3;
    private EditText etBet1, etBet2, etBet3;
    private Button btnStart, btnReset, btnInstruction, btnRecharge, btnLogout;
    private TextView tvBalance;
    private int balance = 1000;
    private Random random;
    private Handler handler;
    private MediaPlayer mediaPlayer;
    private RelativeLayout raceTrackLayout;
    private boolean isRaceRunning = false; // To track if the race is running
    private static final int REQUEST_CODE_WIN = 1;
    private static final int REQUEST_CODE_LOSE = 2;
    private static final int REQUEST_CODE_DRAW = 3;
    private static final int REQUEST_CODE_RECHARGE = 4;

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
        btnInstruction = findViewById(R.id.btn_instruction);
        btnRecharge = findViewById(R.id.btn_recharge);
        btnLogout = findViewById(R.id.btn_logout);
        tvBalance = findViewById(R.id.tv_balance);
        raceTrackLayout = findViewById(R.id.raceTrackLayout);
        random = new Random();
        handler = new Handler();
        mediaPlayer = MediaPlayer.create(this, R.raw.race_sound);

        // Set default values of the bet EditTexts to 0
        etBet1.setText("0");
        etBet2.setText("0");
        etBet3.setText("0");

        Log.d("MainActivity", "onCreate: horseImage1 = " + horseImage1);
        Log.d("MainActivity", "onCreate: horseImage2 = " + horseImage2);
        Log.d("MainActivity", "onCreate: horseImage3 = " + horseImage3);

        if (horseImage1 == null || horseImage2 == null || horseImage3 == null) {
            Log.e("MainActivity", "One or more ImageViews are null. Check your XML layout.");
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("balance")) {
            balance = intent.getIntExtra("balance", 1000); // Default balance is 1000
            tvBalance.setText("Balance: $" + balance);
        }


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRaceRunning) {

                    startRace();
                }
            }

        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetRace();
            }
        });

        btnInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InstructionActivity.class);
                startActivity(intent);
            }
        });

        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RechargeActivity.class);
                startActivityForResult(intent, REQUEST_CODE_RECHARGE);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Stop any ongoing race
                if (isRaceRunning) {
                    isRaceRunning = false;
                    handler.removeCallbacks(raceRunnable);
                }

                // Release media player resources
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                // Navigate back to the login screen or main screen
                Intent intent = new Intent(MainActivity.this, LoginActivity.class); // Change LoginActivity to your login activity class
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Finish the current activity
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

        horseImage1.setImageDrawable(null);
        horseImage1.setBackgroundResource(R.drawable.running_horse1);
        horseAnimation1 = (AnimationDrawable) horseImage1.getBackground();
        horseAnimation1.start();

        horseImage2.setImageDrawable(null);
        horseImage2.setBackgroundResource(R.drawable.running_horse2);
        horseAnimation2 = (AnimationDrawable) horseImage2.getBackground();
        horseAnimation2.start();

        horseImage3.setImageDrawable(null);
        horseImage3.setBackgroundResource(R.drawable.running_horse3);
        horseAnimation3 = (AnimationDrawable) horseImage3.getBackground();
        horseAnimation3.start();
        mediaPlayer.start();
        balance -= totalBet;
        tvBalance.setText("Balance: $" + balance);

        final int maxDistance = raceTrackLayout.getWidth() - horseImage1.getWidth();
        horseImage1.setTranslationX(0);
        horseImage2.setTranslationX(0);
        horseImage3.setTranslationX(0);

        isRaceRunning = true;
        setFieldsAndButtonsEnabled(false);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int progress1 = (int) (horseImage1.getTranslationX() + random.nextInt(25));
                int progress2 = (int) (horseImage2.getTranslationX() + random.nextInt(25));
                int progress3 = (int) (horseImage3.getTranslationX() + random.nextInt(25));

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

        handler.post(raceRunnable);
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

        String winningHorse = null;
        int winnings = 0;
        int losings = 0;
        int totalBet = bet1 + bet2 + bet3;

        if (winner == 1) {
            winnings = bet1 * 2;
            losings = bet2 + bet3;
            winningHorse = "Black Horse";
        } else if (winner == 2) {
            winnings = bet2 * 2;
            losings = bet1 + bet3;
            winningHorse = "White Horse";
        } else if (winner == 3) {
            winnings = bet3 * 2;
            losings = bet2 + bet1;
            winningHorse = "Brown Horse";
        }

        boolean isWin = false;
        boolean isDraw = false;

        if (winnings - losings > 0){
            isWin = true;
        } else if (winnings - losings < 0) {
            isWin = false;
        }else if (winnings - losings == 0) {
            isDraw = true;
        }

        if (isWin) {
            winnings -= losings;
            balance += (winnings + losings);
            tvBalance.setText("Balance: $" + balance);
            Intent intent = new Intent(MainActivity.this, WinActivity.class);
            intent.putExtra("winningHorse", winningHorse);
            intent.putExtra("balance", balance);
            intent.putExtra("winnings", winnings);
            startActivityForResult(intent, REQUEST_CODE_WIN);
        } else if(isDraw){
            balance += totalBet;
            tvBalance.setText("Balance: $" + balance);
            Intent intent = new Intent(MainActivity.this, DrawActivity.class);
            intent.putExtra("winningHorse", winningHorse);
            intent.putExtra("balance", balance);
            startActivityForResult(intent, REQUEST_CODE_DRAW);
        }else {
            balance += winnings;
            totalBet -= winnings;
            tvBalance.setText("Balance: $" + balance);
            Intent intent = new Intent(MainActivity.this, LoseActivity.class);
            intent.putExtra("winningHorse", winningHorse);
            intent.putExtra("balance", balance);
            intent.putExtra("losses", totalBet);
            startActivityForResult(intent, REQUEST_CODE_LOSE);
        }

        isRaceRunning = false;
        setFieldsAndButtonsEnabled(true);
    }


    private void resetRace() {
        // Stop the race if it's running
        if (isRaceRunning) {
            isRaceRunning = false; // Set to false first to immediately stop the raceRunnable
            handler.removeCallbacks(raceRunnable);
        }

        // Reset bet values
        etBet1.setText("0");
        etBet2.setText("0");
        etBet3.setText("0");

        // Re-enable the bet fields and start button
        setFieldsAndButtonsEnabled(true);

        // Reset horse positions
        horseImage1.setX(0);
        horseImage2.setX(0);
        horseImage3.setX(0);

        // Stop the animations if they are initialized
        if (horseAnimation1 != null) {
            horseAnimation1.stop();
        }
        if (horseAnimation2 != null) {
            horseAnimation2.stop();
        }
        if (horseAnimation3 != null) {
            horseAnimation3.stop();
        }

        // Optionally, reset the horse images to their static state if needed
        horseImage1.setImageResource(R.drawable.horse1_1);
        horseImage2.setImageResource(R.drawable.horse2_1);
        horseImage3.setImageResource(R.drawable.horse3_1);

    }

    private void setFieldsAndButtonsEnabled(boolean enabled) {
        etBet1.setEnabled(enabled);
        etBet2.setEnabled(enabled);
        etBet3.setEnabled(enabled);
        btnStart.setEnabled(enabled);
        btnReset.setEnabled(enabled);
        btnInstruction.setEnabled(enabled);
        btnRecharge.setEnabled(enabled);
        btnLogout.setEnabled(enabled);
    }

    private Runnable raceRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRaceRunning) return;

            // Example race logic to move horses
            moveHorse(horseImage1);
            moveHorse(horseImage2);
            moveHorse(horseImage3);

            // Check if any horse has won
            if (checkIfHorseWon(horseImage1) || checkIfHorseWon(horseImage2) || checkIfHorseWon(horseImage3)) {
                isRaceRunning = false;
                setFieldsAndButtonsEnabled(true);
            } else {
                handler.postDelayed(this, 100); // Continue running the race
            }
        }
    };

    private void moveHorse(ImageView horse) {
        horse.setX(horse.getX() + random.nextInt(25));
    }

    private boolean checkIfHorseWon(ImageView horse) {
        return horse.getX() >= raceTrackLayout.getWidth() - horse.getWidth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_WIN || requestCode == REQUEST_CODE_LOSE || requestCode == REQUEST_CODE_DRAW) {
                balance = data.getIntExtra("balance", balance);
                tvBalance.setText("Balance: $" + balance);
                resetRace();
                horseImage1.setImageDrawable(null);
                horseImage2.setImageDrawable(null);
                horseImage3.setImageDrawable(null);
            }
            if (requestCode == REQUEST_CODE_RECHARGE) {
                int rechargeAmount = data.getIntExtra("rechargeAmount", 0);
                balance += rechargeAmount;
                tvBalance.setText("Balance: $" + balance);
            }
        }
    }


}
