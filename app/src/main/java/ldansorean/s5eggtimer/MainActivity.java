package ldansorean.s5eggtimer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int TOTAL_DURATION_SEC = 10 * 60; //x min * 60 sec
    private static final int DEFAULT_EGG_DURATION_SEC = 30;

    private SeekBar progressBar;
    private Button button;
    private TextView timeLabel;
    private boolean running = false;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);
        timeLabel = findViewById(R.id.timeLabel);

        setupProgressBar();
        setTimeLabel(DEFAULT_EGG_DURATION_SEC);
    }

    private void setupProgressBar() {
        progressBar.setMax(TOTAL_DURATION_SEC);
        progressBar.setProgress(DEFAULT_EGG_DURATION_SEC);

        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setTimeLabel(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                ; //noop
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                ; //noop
            }
        });
    }

    private void setTimeLabel(long durationSec) {
        long min = durationSec / 60;
        long sec = durationSec - min * 60;
        String newText = String.format("%02d:%02d", min, sec);
        timeLabel.setText(newText);
    }

    public void startStop(View view) {
        if (running) {
            countDownTimer.cancel();
            stop();
        } else {
            start();
            countDownTimer = createCountDownTimer(progressBar.getProgress());
            countDownTimer.start();
        }
    }

    private void start() {
        running = true;
        button.setText("Stop");
        progressBar.setEnabled(false);
    }

    private void stop() {
        running = false;
        button.setText("Start");
        progressBar.setEnabled(true);
        progressBar.setProgress(DEFAULT_EGG_DURATION_SEC);
        setTimeLabel(DEFAULT_EGG_DURATION_SEC);
    }

    private CountDownTimer createCountDownTimer(int eggDuration) {
        CountDownTimer countDownTimer = new CountDownTimer(eggDuration * 1000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                setTimeLabel(millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                MediaPlayer.create(getApplicationContext(), R.raw.airhorn).start();
                stop();
            }
        };
        return countDownTimer;
    }
}
