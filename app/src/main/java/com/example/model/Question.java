package com.example.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single multiple-choice quiz question with 4 options,
 * exactly one correct answer index, a category, and an educational explanation.
 */
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String id;
    private final String questionText;
    private final List<String> options;
    private final int correctAnswerIndex;
    private final String category;
    private final String explanation;

    public Question(String id,
                    String questionText,
                    List<String> options,
                    int correctAnswerIndex,
                    String category,
                    String explanation) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Question ID cannot be null or empty.");
        }
        if (questionText == null || questionText.trim().isEmpty()) {
            throw new IllegalArgumentException("Question text cannot be null or empty.");
        }
        if (options == null || options.size() != 4) {
            throw new IllegalArgumentException("Each question must contain exactly 4 options.");
        }
        if (correctAnswerIndex < 0 || correctAnswerIndex >= 4) {
            throw new IllegalArgumentException("Correct answer index must be between 0 and 3.");
        }
        if (category == null || category.trim().isEmpty()) {
            category = "General";
        }
        if (explanation == null || explanation.trim().isEmpty()) {
            explanation = "No explanation provided.";
        }

        this.id = id;
        this.questionText = questionText;
        this.options = Collections.unmodifiableList(new ArrayList<>(options));
        this.correctAnswerIndex = correctAnswerIndex;
        this.category = category;
        this.explanation = explanation;
    }

    public String getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getOptions() {
        return options;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public String getCorrectAnswerText() {
        return options.get(correctAnswerIndex);
    }

    public String getCategory() {
        return category;
    }

    public String getExplanation() {
        return explanation;
    }

    public boolean isCorrect(int selectedIndex) {
        return selectedIndex == correctAnswerIndex;
    }
}
