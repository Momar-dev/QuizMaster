package com.example.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.R;
import com.example.engine.QuizEngine;
import com.example.model.Question;

import java.util.List;

/**
 * RecyclerView Adapter displaying full answer review for every question completed
 * in the current quiz session.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private final Context context;
    private final QuizEngine quizEngine;
    private final List<Question> questions;

    public ReviewAdapter(Context context, QuizEngine quizEngine) {
        this.context = context;
        this.quizEngine = quizEngine;
        this.questions = quizEngine.getQuestions();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_question, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question q = questions.get(position);
        int total = questions.size();
        int userChoiceIndex = quizEngine.getUserAnswerFor(position);
        boolean isCorrect = q.isCorrect(userChoiceIndex);

        holder.tvQuestionNum.setText("Question " + (position + 1) + " of " + total + " • " + q.getCategory());
        holder.tvQuestionText.setText(q.getQuestionText());

        if (isCorrect) {
            holder.layoutStatusBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_option_correct));
            holder.imgStatusIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_check_circle));
            holder.tvStatusText.setText("Correct");
            holder.tvStatusText.setTextColor(ContextCompat.getColor(context, R.color.correct_green));

            holder.layoutUserAnswer.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_option_correct));
            holder.tvUserAnswer.setText(q.getOptions().get(userChoiceIndex));

            holder.layoutCorrectAnswer.setVisibility(View.GONE);
        } else {
            holder.layoutStatusBadge.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_option_incorrect));
            holder.imgStatusIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_close_circle));
            holder.tvStatusText.setText("Incorrect");
            holder.tvStatusText.setTextColor(ContextCompat.getColor(context, R.color.incorrect_red));

            holder.layoutUserAnswer.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_option_incorrect));
            if (userChoiceIndex >= 0 && userChoiceIndex < q.getOptions().size()) {
                holder.tvUserAnswer.setText(q.getOptions().get(userChoiceIndex));
            } else {
                holder.tvUserAnswer.setText("No answer selected");
            }

            holder.layoutCorrectAnswer.setVisibility(View.VISIBLE);
            holder.tvCorrectAnswer.setText(q.getCorrectAnswerText());
        }

        holder.tvExplanation.setText(q.getExplanation());
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvQuestionNum;
        final LinearLayout layoutStatusBadge;
        final ImageView imgStatusIcon;
        final TextView tvStatusText;
        final TextView tvQuestionText;
        final LinearLayout layoutUserAnswer;
        final TextView tvUserAnswer;
        final LinearLayout layoutCorrectAnswer;
        final TextView tvCorrectAnswer;
        final TextView tvExplanation;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNum = itemView.findViewById(R.id.tv_review_question_num);
            layoutStatusBadge = itemView.findViewById(R.id.layout_review_status_badge);
            imgStatusIcon = itemView.findViewById(R.id.img_review_status_icon);
            tvStatusText = itemView.findViewById(R.id.tv_review_status_text);
            tvQuestionText = itemView.findViewById(R.id.tv_review_question_text);
            layoutUserAnswer = itemView.findViewById(R.id.layout_user_answer);
            tvUserAnswer = itemView.findViewById(R.id.tv_user_answer_text);
            layoutCorrectAnswer = itemView.findViewById(R.id.layout_correct_answer);
            tvCorrectAnswer = itemView.findViewById(R.id.tv_correct_answer_text);
            tvExplanation = itemView.findViewById(R.id.tv_review_explanation);
        }
    }
}
