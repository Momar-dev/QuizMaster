package com.example.engine;

import com.example.model.Question;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Quiz state and scoring engine.
 * Completely encapsulates question progression, answer validation, score calculation,
 * and state serialization for activity recreation and device rotation.
 */
public class QuizEngine implements Serializable {
    private static final long serialVersionUID = 1L;

    private final List<Question> questions;
    private final int[] userAnswers;
    private int currentIndex;
    private int score;

    public QuizEngine(List<Question> questions) {
        if (questions == null || questions.isEmpty()) {
            this.questions = new ArrayList<>();
            this.userAnswers = new int[0];
        } else {
            this.questions = new ArrayList<>(questions);
            this.userAnswers = new int[questions.size()];
            Arrays.fill(this.userAnswers, -1);
        }
        this.currentIndex = 0;
        this.score = 0;
    }

    /**
     * Attempts to answer the current question with the specified option index (0..3).
     * Once answered, subsequent answer attempts for the current question are ignored.
     * Returns true if the answer was correct, false otherwise.
     */
    public boolean submitAnswer(int selectedIndex) {
        if (isCurrentQuestionAnswered() || isQuizFinished()) {
            return isCurrentAnswerCorrect();
        }
        if (selectedIndex < 0 || selectedIndex >= 4) {
            return false;
        }

        userAnswers[currentIndex] = selectedIndex;
        Question current = getCurrentQuestion();
        boolean correct = current != null && current.isCorrect(selectedIndex);
        if (correct) {
            score++;
        }
        return correct;
    }

    /**
     * Checks if the user has selected an answer for the current question.
     */
    public boolean isCurrentQuestionAnswered() {
        if (currentIndex < 0 || currentIndex >= userAnswers.length) {
            return false;
        }
        return userAnswers[currentIndex] != -1;
    }

    /**
     * Returns the selected option index for the current question (-1 if unanswered).
     */
    public int getCurrentSelectedOption() {
        if (currentIndex < 0 || currentIndex >= userAnswers.length) {
            return -1;
        }
        return userAnswers[currentIndex];
    }

    /**
     * Checks if the selected answer for the current question is correct.
     */
    public boolean isCurrentAnswerCorrect() {
        if (!isCurrentQuestionAnswered()) {
            return false;
        }
        Question q = getCurrentQuestion();
        return q != null && q.isCorrect(userAnswers[currentIndex]);
    }

    /**
     * Checks if the user is currently on the last question of the quiz.
     */
    public boolean isLastQuestion() {
        return currentIndex == questions.size() - 1;
    }

    /**
     * Advances to the next question.
     * Returns true if successfully advanced, false if already at or past the end.
     */
    public boolean advance() {
        if (canAdvance()) {
            currentIndex++;
            return true;
        }
        return false;
    }

    /**
     * Can advance only if the current question has been answered and not already finished.
     */
    public boolean canAdvance() {
        return isCurrentQuestionAnswered() && currentIndex < questions.size() - 1;
    }

    /**
     * Checks if all questions have been completed or if quiz reached end.
     */
    public boolean isQuizFinished() {
        if (questions.isEmpty()) {
            return true;
        }
        return currentIndex >= questions.size() - 1 && isCurrentQuestionAnswered();
    }

    public Question getCurrentQuestion() {
        if (currentIndex >= 0 && currentIndex < questions.size()) {
            return questions.get(currentIndex);
        }
        return null;
    }

    public Question getQuestionAt(int index) {
        if (index >= 0 && index < questions.size()) {
            return questions.get(index);
        }
        return null;
    }

    public int getUserAnswerFor(int index) {
        if (index >= 0 && index < userAnswers.length) {
            return userAnswers[index];
        }
        return -1;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public int getScore() {
        return score;
    }

    public int getIncorrectCount() {
        int answered = 0;
        for (int ans : userAnswers) {
            if (ans != -1) answered++;
        }
        return Math.max(0, answered - score);
    }

    /**
     * Returns percentage of correct answers (0 - 100).
     * Protected against division by zero.
     */
    public int getPercentage() {
        if (questions.isEmpty()) {
            return 0;
        }
        return (int) Math.round(((double) score / (double) questions.size()) * 100.0);
    }

    public List<Question> getQuestions() {
        return Collections.unmodifiableList(questions);
    }

    /**
     * Performance tier rating based on final percentage.
     */
    public PerformanceTier getPerformanceTier() {
        int pct = getPercentage();
        if (pct >= 90) {
            return PerformanceTier.LEGENDARY;
        } else if (pct >= 70) {
            return PerformanceTier.EXCELLENT;
        } else if (pct >= 50) {
            return PerformanceTier.GOOD;
        } else {
            return PerformanceTier.NEEDS_WORK;
        }
    }

    public enum PerformanceTier {
        LEGENDARY,
        EXCELLENT,
        GOOD,
        NEEDS_WORK
    }
}
