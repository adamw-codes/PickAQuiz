/*
Written by Adam Wajahat for CS4301.002, Quiz Program Part 1, starting Feb 25, 2021
Netid: amw170002

This ResultsScreen gets the results, user's name, and quiz name to display to the screen. It also displays a button to return back to the MainActivity.
 */

package com.utd.pickaquizadamwajahat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ResultsScreen extends AppCompatActivity {
    String userIn; // used to store user's name passed in
    String quizName; // used to store quiz name passed in
    int correctAnswers;
    int totalAnswers;

    // onCreate gets in the user's name, quiz name, and results in through Intent and then displays them in the respective textViews
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_screen);
        Intent intent = getIntent();
        // sets values in from Intent
        userIn = (String) intent.getSerializableExtra("nameIn");
        quizName = (String) intent.getSerializableExtra("quizName");
        correctAnswers = (Integer) intent.getSerializableExtra("results");
        totalAnswers = (Integer) intent.getSerializableExtra("total");
        TextView nameDisplay = findViewById(R.id.nameText);
        TextView quizDisplay = findViewById(R.id.quizText);
        TextView scoreDisplay = findViewById(R.id.scoreText);
        // displays out to textViews
        nameDisplay.setText(userIn);
        quizDisplay.setText(quizName);
        String score = Integer.toString(correctAnswers) + "/" + Integer.toString(totalAnswers);
        scoreDisplay.setText(score);
    }

    // this function is a button click listener to move the activity back to the MainActivity
    public void onHomeClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}