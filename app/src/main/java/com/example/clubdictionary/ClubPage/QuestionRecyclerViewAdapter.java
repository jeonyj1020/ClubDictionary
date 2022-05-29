package com.example.clubdictionary.ClubPage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clubdictionary.R;

import java.util.ArrayList;

public class QuestionRecyclerViewAdapter extends RecyclerView.Adapter<QuestionRecyclerViewAdapter.ViewHolder> {

    //ArrayList<QuestionItem> questionList = new ArrayList<>();
    ArrayList<sampleText> questionList = new ArrayList<>();

    public QuestionRecyclerViewAdapter(ArrayList<sampleText> questionList) {
        this.questionList = questionList;
    }

    @NonNull
    @Override
    public QuestionRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item, parent, false);
        QuestionRecyclerViewAdapter.ViewHolder viewHolder = new QuestionRecyclerViewAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.answerText.setText("A. " + questionList.get(position).getAnswer());
        holder.questionText.setText("Q. " + questionList.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView questionText, answerText;
        ImageButton menuButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            questionText = itemView.findViewById(R.id.questionText);
            answerText = itemView.findViewById(R.id.answerText);

        }
    }

}
