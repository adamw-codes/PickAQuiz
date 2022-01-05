/*
The questionAdapter class is used for communication between the recyclerView and the QuizModifierActivity.
It handles setting the values of the recyclerView to the question names, having onClick listeners, and
getting the position of the clicked question
 */

package com.utd.pickaquizadamwajahat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class questionAdapter extends RecyclerView.Adapter<questionAdapter.MyViewHolder>{
    private ArrayList<Questions> questionList = new ArrayList<Questions>();
    private OnQuestionListener localOnQuizListener;

    public questionAdapter(ArrayList<Questions> questionList, OnQuestionListener onQuizListener){
        this.questionList = questionList;
        localOnQuizListener = onQuizListener;
    }

    // this is where the onClick is implemented to test for when the user clicks on a question
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView questionText;
        OnQuestionListener onQuizListener;

        public MyViewHolder(final View view, OnQuestionListener onQuizListener){
            super(view);
            questionText = view.findViewById(R.id.quizEditName);
            // sets the onClick listener
            this.onQuizListener = onQuizListener;
            itemView.setOnClickListener(this);
        }

        // gets the position of the quiz that was click
        @Override
        public void onClick(View v) {
            onQuizListener.onQuestionClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public questionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_list_layout, parent, false);
        return new MyViewHolder(itemView, localOnQuizListener);
    }

    // sets the values of each of the recycler views to the name of the quizzes
    @Override
    public void onBindViewHolder(@NonNull questionAdapter.MyViewHolder holder, int position) {
        String question = questionList.get(position).getQuestion();
        holder.questionText.setText(question);
    }

    @Override
    public int getItemCount() { return questionList.size();    }

    // sets interface for the onClick
    public interface OnQuestionListener{
        void onQuestionClick(int position);
    }
}
