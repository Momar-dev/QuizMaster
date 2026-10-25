package com.example.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Local persistence helper for best score, percentage, and quiz attempts
 * using Android SharedPreferences.
 */
public class ScoreStorage {
    private static final String PREF_NAME = "quiz_master_prefs";
    private static final String KEY_BEST_SCORE = "key_best_score";
    private static final String KEY_BEST_TOTAL = "key_best_total";
    private static final String KEY_BEST_PERCENTAGE = "key_best_percentage";
    private static final String KEY_QUIZZES_COMPLETED = "key_quizzes_completed";

    private static SharedPreferences getPrefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static int getBestScore(Context context) {
        return getPrefs(context).getInt(KEY_BEST_SCORE, -1);
    }

    public static int getBestTotal(Context context) {
        return getPrefs(context).getInt(KEY_BEST_TOTAL, 0);
    }

    public static int getBestPercentage(Context context) {
        return getPrefs(context).getInt(KEY_BEST_PERCENTAGE, -1);
    }

    public static int getQuizzesCompleted(Context context) {
        return getPrefs(context).getInt(KEY_QUIZZES_COMPLETED, 0);
    }

    /**
     * Saves new score if it's the first time or if the percentage is higher than the previous best.
     * Returns true if a new high score was set.
     */
    public static boolean recordQuizResult(Context context, int score, int total) {
        if (context == null || total <= 0) {
            return false;
        }

        SharedPreferences prefs = getPrefs(context);
        int prevBestPct = prefs.getInt(KEY_BEST_PERCENTAGE, -1);
        int currentPct = (int) Math.round(((double) score / (double) total) * 100.0);
        int completed = prefs.getInt(KEY_QUIZZES_COMPLETED, 0) + 1;

        boolean isNewBest = (prevBestPct == -1) || (currentPct > prevBestPct) ||
                (currentPct == prevBestPct && score > prefs.getInt(KEY_BEST_SCORE, 0));

        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_QUIZZES_COMPLETED, completed);
        if (isNewBest) {
            editor.putInt(KEY_BEST_SCORE, score);
            editor.putInt(KEY_BEST_TOTAL, total);
            editor.putInt(KEY_BEST_PERCENTAGE, currentPct);
        }
        editor.apply();

        return isNewBest;
    }
}
