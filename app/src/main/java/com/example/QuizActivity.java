package com.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.data.QuizRepository;
import com.example.engine.QuizEngine;
import com.example.model.Question;

import java.util.List;

/**
 * Main Quiz screen handling question display, option interaction,
 * answer feedback, and progression.
 */
public class QuizActivity extends AppCompatActivity {

    public static final String EXTRA_QUIZ_ENGINE = "extra_quiz_engine";
    private static final String KEY_QUIZ_ENGINE = "key_quiz_engine";

    private QuizEngine quizEngine;

    private ScrollView quizScrollView;
    private TextView tvQuestionCounter;
    private TextView tvQuizScore;
    private ProgressBar progressQuiz;
    private TextView tvQuestionCategory;
    private TextView tvQuestionText;

    private LinearLayout[] optionLayouts = new LinearLayout[4];
    private TextView[] optionBadges = new TextView[4];
    private TextView[] optionTexts = new TextView[4];
    private ImageView[] optionStatusIcons = new ImageView[4];

    private LinearLayout layoutExplanationCard;
    private ImageView imgFeedbackResult;
    private TextView tvFeedbackTitle;
    private TextView tvExplanationText;

    private Button btnNextQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        initViews();
        setupOptionClickListeners();
        setupNextButton();

        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_QUIZ_ENGINE)) {
            quizEngine = (QuizEngine) savedInstanceState.getSerializable(KEY_QUIZ_ENGINE);
        }

        if (quizEngine == null) {
            List<Question> questions = QuizRepository.getQuizQuestions(QuizRepository.DEFAULT_QUIZ_SIZE, true);
            quizEngine = new QuizEngine(questions);
        }

        renderCurrentQuestion();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_QUIZ_ENGINE, quizEngine);
    }

    private void initViews() {
        quizScrollView = findViewById(R.id.quiz_scroll_view);
        tvQuestionCounter = findViewById(R.id.tv_question_counter);
        tvQuizScore = findViewById(R.id.tv_quiz_score);
        progressQuiz = findViewById(R.id.progress_quiz);
        tvQuestionCategory = findViewById(R.id.tv_question_category);
        tvQuestionText = findViewById(R.id.tv_question_text);

        optionLayouts[0] = findViewById(R.id.layout_option_0);
        optionLayouts[1] = findViewById(R.id.layout_option_1);
        optionLayouts[2] = findViewById(R.id.layout_option_2);
        optionLayouts[3] = findViewById(R.id.layout_option_3);

        optionBadges[0] = findViewById(R.id.tv_option_badge_0);
        optionBadges[1] = findViewById(R.id.tv_option_badge_1);
        optionBadges[2] = findViewById(R.id.tv_option_badge_2);
        optionBadges[3] = findViewById(R.id.tv_option_badge_3);

        optionTexts[0] = findViewById(R.id.tv_option_text_0);
        optionTexts[1] = findViewById(R.id.tv_option_text_1);
        optionTexts[2] = findViewById(R.id.tv_option_text_2);
        optionTexts[3] = findViewById(R.id.tv_option_text_3);

        optionStatusIcons[0] = findViewById(R.id.img_option_status_0);
        optionStatusIcons[1] = findViewById(R.id.img_option_status_1);
        optionStatusIcons[2] = findViewById(R.id.img_option_status_2);
        optionStatusIcons[3] = findViewById(R.id.img_option_status_3);

        layoutExplanationCard = findViewById(R.id.layout_explanation_card);
        imgFeedbackResult = findViewById(R.id.img_feedback_result);
        tvFeedbackTitle = findViewById(R.id.tv_feedback_title);
        tvExplanationText = findViewById(R.id.tv_explanation_text);

        btnNextQuestion = findViewById(R.id.btn_next_question);
    }

    private void setupOptionClickListeners() {
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            optionLayouts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOptionSelected(optionIndex);
                }
            });
        }
    }

    private void setupNextButton() {
        btnNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextClicked();
            }
        });
    }

    private void renderCurrentQuestion() {
        Question current = quizEngine.getCurrentQuestion();
        if (current == null) {
            return;
        }

        int currentNum = quizEngine.getCurrentIndex() + 1;
        int total = quizEngine.getTotalQuestions();

        tvQuestionCounter.setText(getString(R.string.quiz_question_counter, currentNum, total));
        tvQuizScore.setText(getString(R.string.quiz_score_counter, quizEngine.getScore()));

        progressQuiz.setMax(total);
        progressQuiz.setProgress(currentNum);

        tvQuestionCategory.setText(current.getCategory());
        tvQuestionText.setText(current.getQuestionText());

        List<String> options = current.getOptions();
        for (int i = 0; i < 4; i++) {
            optionTexts[i].setText(options.get(i));
            optionLayouts[i].setBackground(ContextCompat.getDrawable(this, R.drawable.bg_option_normal));
            optionStatusIcons[i].setVisibility(View.GONE);
            optionLayouts[i].setEnabled(true);
            optionLayouts[i].setClickable(true);
        }

        if (quizEngine.isCurrentQuestionAnswered()) {
            displayAnswerFeedback(quizEngine.getCurrentSelectedOption());
        } else {
            layoutExplanationCard.setVisibility(View.GONE);
            btnNextQuestion.setEnabled(false);
            btnNextQuestion.setAlpha(0.5f);
            if (quizEngine.isLastQuestion()) {
                btnNextQuestion.setText(getString(R.string.quiz_btn_view_results));
            } else {
                btnNextQuestion.setText(getString(R.string.quiz_btn_next));
            }
        }
    }

    private void onOptionSelected(int optionIndex) {
        if (quizEngine.isCurrentQuestionAnswered()) {
            // Already answered, prevent double tapping or modifying answer
            return;
        }

        quizEngine.submitAnswer(optionIndex);
        tvQuizScore.setText(getString(R.string.quiz_score_counter, quizEngine.getScore()));
        displayAnswerFeedback(optionIndex);
    }

    private void displayAnswerFeedback(int selectedOptionIndex) {
        Question current = quizEngine.getCurrentQuestion();
        if (current == null) {
            return;
        }

        int correctIndex = current.getCorrectAnswerIndex();
        boolean isCorrect = (selectedOptionIndex == correctIndex);

        // Lock options
        for (int i = 0; i < 4; i++) {
            optionLayouts[i].setEnabled(false);
            optionLayouts[i].setClickable(false);
        }

        // Highlight correct option in green
        optionLayouts[correctIndex].setBackground(ContextCompat.getDrawable(this, R.drawable.bg_option_correct));
        optionStatusIcons[correctIndex].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle));
        optionStatusIcons[correctIndex].setVisibility(View.VISIBLE);

        // If user picked wrong option, highlight it in red
        if (!isCorrect && selectedOptionIndex >= 0 && selectedOptionIndex < 4) {
            optionLayouts[selectedOptionIndex].setBackground(ContextCompat.getDrawable(this, R.drawable.bg_option_incorrect));
            optionStatusIcons[selectedOptionIndex].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_circle));
            optionStatusIcons[selectedOptionIndex].setVisibility(View.VISIBLE);
        }

        // Show feedback card & explanation
        layoutExplanationCard.setVisibility(View.VISIBLE);
        if (isCorrect) {
            imgFeedbackResult.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_check_circle));
            tvFeedbackTitle.setText(getString(R.string.quiz_feedback_correct));
            tvFeedbackTitle.setTextColor(ContextCompat.getColor(this, R.color.correct_green));
        } else {
            imgFeedbackResult.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_close_circle));
            tvFeedbackTitle.setText(getString(R.string.quiz_feedback_incorrect));
            tvFeedbackTitle.setTextColor(ContextCompat.getColor(this, R.color.incorrect_red));
        }
        tvExplanationText.setText(current.getExplanation());

        // Configure next button
        btnNextQuestion.setEnabled(true);
        btnNextQuestion.setAlpha(1.0f);
        if (quizEngine.isLastQuestion()) {
            btnNextQuestion.setText(getString(R.string.quiz_btn_view_results));
        } else {
            btnNextQuestion.setText(getString(R.string.quiz_btn_next));
        }

        // Scroll to reveal explanation smoothly
        quizScrollView.post(new Runnable() {
            @Override
            public void run() {
                quizScrollView.smoothScrollTo(0, layoutExplanationCard.getTop());
            }
        });
    }

    private void onNextClicked() {
        if (!quizEngine.isCurrentQuestionAnswered()) {
            return;
        }

        if (quizEngine.isLastQuestion()) {
            // Open Results Screen
            Intent intent = new Intent(QuizActivity.this, ResultsActivity.class);
            intent.putExtra(EXTRA_QUIZ_ENGINE, quizEngine);
            startActivity(intent);
            finish();
        } else {
            quizEngine.advance();
            renderCurrentQuestion();
            quizScrollView.post(new Runnable() {
                @Override
                public void run() {
                    quizScrollView.smoothScrollTo(0, 0);
                }
            });
        }
    }
}
