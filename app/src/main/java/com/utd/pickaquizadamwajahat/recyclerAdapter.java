/*
The recyclerAdapter class is used for communication between the recyclerView and the MainActivity.
It handles setting the values of the recyclerView to the quiz names, having onClick listeners, and getting the position of the clicked quiz

 */

package com.utd.pickaquizadamwajahat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> {
    private ArrayList<Quiz> quizList;
    private OnQuizListener localOnQuizListener;

    // sets the quizList that is passed in for reading and displaying
    public recyclerAdapter(ArrayList<Quiz> quizList, OnQuizListener onQuizListener){
        this.quizList = quizList;
        localOnQuizListener = onQuizListener;
    }

    // this is where the onClick is implemented to test for when the user clicks on a quiz
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView quizText;
        OnQuizListener onQuizListener;

        public MyViewHolder(final View view, OnQuizListener onQuizListener){
            super(view);
            quizText = view.findViewById(R.id.quizEditName);
            // sets the onClick listener
            this.onQuizListener = onQuizListener;
            itemView.setOnClickListener(this);
        }

        // gets the position of the quiz that was click
        @Override
        public void onClick(View v) {
            onQuizListener.onQuizClick(getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_list_layout, parent, false);
        return new MyViewHolder(itemView, localOnQuizListener);
    }

    // sets the values of each of the recycler views to the name of the quizzes
    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String name = quizList.get(position).getName();
        holder.quizText.setText(name);
    }

    @Override
    public int getItemCount() {
        return quizList.size();
    }

    // sets interface for the onClick
    public interface OnQuizListener{
        void onQuizClick(int position);
    }

}
