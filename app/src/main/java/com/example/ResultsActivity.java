package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.engine.QuizEngine;
import com.example.util.ScoreStorage;

/**
 * Results Screen displaying the final score, percentage,
 * performance rating, correct/incorrect breakdown, and next steps.
 */
public class ResultsActivity extends AppCompatActivity {

    private QuizEngine quizEngine;

    private TextView tvPerfTitle;
    private TextView tvPerfDescription;
    private TextView tvResultsPercentage;
    private TextView tvResultsFraction;
    private TextView tvResultsBestBadge;
    private TextView tvCorrectCount;
    private TextView tvIncorrectCount;

    private Button btnPlayAgain;
    private Button btnReviewAnswers;
    private Button btnBackHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        if (getIntent() != null && getIntent().hasExtra(QuizActivity.EXTRA_QUIZ_ENGINE)) {
            quizEngine = (QuizEngine) getIntent().getSerializableExtra(QuizActivity.EXTRA_QUIZ_ENGINE);
        }

        initViews();
        displayResults();
        setupListeners();
    }

    private void initViews() {
        tvPerfTitle = findViewById(R.id.tv_perf_title);
        tvPerfDescription = findViewById(R.id.tv_perf_description);
        tvResultsPercentage = findViewById(R.id.tv_results_percentage);
        tvResultsFraction = findViewById(R.id.tv_results_fraction);
        tvResultsBestBadge = findViewById(R.id.tv_results_best_badge);
        tvCorrectCount = findViewById(R.id.tv_correct_count);
        tvIncorrectCount = findViewById(R.id.tv_incorrect_count);

        btnPlayAgain = findViewById(R.id.btn_play_again);
        btnReviewAnswers = findViewById(R.id.btn_review_answers);
        btnBackHome = findViewById(R.id.btn_back_home);
    }

    private void displayResults() {
        if (quizEngine == null) {
            tvResultsFraction.setText(getString(R.string.results_score_format, 0, 0));
            tvResultsPercentage.setText(getString(R.string.results_percent_format, 0));
            return;
        }

        int score = quizEngine.getScore();
        int total = quizEngine.getTotalQuestions();
        int percentage = quizEngine.getPercentage();
        int incorrect = quizEngine.getIncorrectCount();

        // Record in persistent storage
        boolean isNewBest = ScoreStorage.recordQuizResult(this, score, total);

        tvResultsPercentage.setText(getString(R.string.results_percent_format, percentage));
        tvResultsFraction.setText(getString(R.string.results_score_format, score, total) + " " + getString(R.string.results_correct_label));
        tvCorrectCount.setText(String.valueOf(score));
        tvIncorrectCount.setText(String.valueOf(incorrect));

        // Performance Tier texts
        QuizEngine.PerformanceTier tier = quizEngine.getPerformanceTier();
        switch (tier) {
            case LEGENDARY:
                tvPerfTitle.setText(getString(R.string.perf_legendary_title));
                tvPerfDescription.setText(getString(R.string.perf_legendary_desc));
                break;
            case EXCELLENT:
                tvPerfTitle.setText(getString(R.string.perf_excellent_title));
                tvPerfDescription.setText(getString(R.string.perf_excellent_desc));
                break;
            case GOOD:
                tvPerfTitle.setText(getString(R.string.perf_good_title));
                tvPerfDescription.setText(getString(R.string.perf_good_desc));
                break;
            case NEEDS_WORK:
            default:
                tvPerfTitle.setText(getString(R.string.perf_needs_work_title));
                tvPerfDescription.setText(getString(R.string.perf_needs_work_desc));
                break;
        }

        // All time best badge
        int bestScore = ScoreStorage.getBestScore(this);
        int bestTotal = ScoreStorage.getBestTotal(this);
        int bestPct = ScoreStorage.getBestPercentage(this);
        if (isNewBest) {
            tvResultsBestBadge.setText("★ New Personal Best: " + score + "/" + total + " (" + percentage + "%)");
        } else if (bestScore >= 0) {
            String bestStr = bestScore + "/" + bestTotal + " (" + bestPct + "%)";
            tvResultsBestBadge.setText(getString(R.string.results_best_score_label, bestStr));
        } else {
            tvResultsBestBadge.setVisibility(View.GONE);
        }
    }

    private void setupListeners() {
        btnPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, QuizActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnReviewAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, ReviewActivity.class);
                intent.putExtra(QuizActivity.EXTRA_QUIZ_ENGINE, quizEngine);
                startActivity(intent);
            }
        });

        btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
