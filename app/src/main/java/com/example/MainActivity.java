package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.util.ScoreStorage;

/**
 * Welcome / Home Screen for Quiz Master.
 * Introduces the quiz, displays past statistics, and launches the quiz.
 */
public class MainActivity extends AppCompatActivity {

    private TextView tvBestScoreValue;
    private TextView tvCompletedCount;
    private Button btnStartQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatistics();
    }

    private void initViews() {
        tvBestScoreValue = findViewById(R.id.tv_best_score_value);
        tvCompletedCount = findViewById(R.id.tv_completed_count);
        btnStartQuiz = findViewById(R.id.btn_start_quiz);
    }

    private void setupListeners() {
        btnStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateStatistics() {
        int bestScore = ScoreStorage.getBestScore(this);
        int bestTotal = ScoreStorage.getBestTotal(this);
        int bestPct = ScoreStorage.getBestPercentage(this);
        int completed = ScoreStorage.getQuizzesCompleted(this);

        if (bestScore >= 0 && bestTotal > 0) {
            String bestText = bestScore + "/" + bestTotal + " (" + bestPct + "%)";
            tvBestScoreValue.setText(bestText);
        } else {
            tvBestScoreValue.setText(getString(R.string.no_previous_score));
        }

        tvCompletedCount.setText(String.valueOf(completed));
    }
}
