package com.example;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.engine.QuizEngine;
import com.example.ui.ReviewAdapter;

/**
 * Answer Review screen showing all questions, user selected options,
 * correct answers, and in-depth explanations.
 */
public class ReviewActivity extends AppCompatActivity {

    private QuizEngine quizEngine;
    private ImageButton btnBack;
    private RecyclerView rvReviewQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        if (getIntent() != null && getIntent().hasExtra(QuizActivity.EXTRA_QUIZ_ENGINE)) {
            quizEngine = (QuizEngine) getIntent().getSerializableExtra(QuizActivity.EXTRA_QUIZ_ENGINE);
        }

        btnBack = findViewById(R.id.btn_review_back);
        rvReviewQuestions = findViewById(R.id.rv_review_questions);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (quizEngine != null) {
            rvReviewQuestions.setLayoutManager(new LinearLayoutManager(this));
            ReviewAdapter adapter = new ReviewAdapter(this, quizEngine);
            rvReviewQuestions.setAdapter(adapter);
        }
    }
}
