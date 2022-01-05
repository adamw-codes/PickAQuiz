/*
Written by Adam Wajahat for CS4301.002, Quiz Program Part 1, starting Feb 25, 2021
Netid: amw170002

This QuizScreen displays the questions and answers, switches out the answer, correct, and incorrect fragments based on user answer, and
keeps track of the user's name and score to be passed over to the ResultsScreen.

 */

package com.utd.pickaquizadamwajahat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.utd.pickaquizadamwajahat.R;

import java.io.Serializable;
import java.util.ArrayList;

public class QuizScreen extends AppCompatActivity implements Serializable {
    TextView questionView;
    TextView[] answerViews = new TextView[4];
    TextView answerPicked = null;
    String correctAnswer;
    FragmentTransaction ft;
    Quiz quiz; // selected quiz
    ArrayList<Questions> quizQuestions = new ArrayList<Questions>(); // question array
    int numOfCorrect = 0;
    fragmentButton fragButton = new fragmentButton();
    correctFragment fragCorrect = new correctFragment();
    int questionNo = 0;
    String userAnswer; // used for storing user's answer
    String userName; // used for storing user's name

    // onCreate sets the first "Answer" fragment and also gets the values passed through the intent from MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_screen);
        // starts the transaction with the help of the FragmentManager to add in the first "Answer" fragment
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.buttonFragment, fragButton);
        ft.commit();
        // gets the views where the question and answers would be displayed
        questionView = findViewById(R.id.questionView);
        answerViews[0] = findViewById(R.id.answer1View);
        answerViews[1] = findViewById(R.id.answer2View);
        answerViews[2] = findViewById(R.id.answer3View);
        answerViews[3] = findViewById(R.id.answer4View);
        // gets the values in from the MainActivity with the help of the Intent
        Intent intent = getIntent();
        quiz = (Quiz) intent.getSerializableExtra("quizIn");
        userName = (String) intent.getSerializableExtra("name");
        // separates the quiz questions out of the quiz so that they can accessed easier
        quizQuestions = quiz.getQuestionList();
        // sets up the first question
        setUpQuestion();
    }

    // this function sets up the question to be displayed on the screen
    private void setUpQuestion() {
        // sets the question to the questionView
       questionView.setText(quizQuestions.get(questionNo).getQuestion());
       // goes through each of the answers that are stored in the questions as an array
       for (int i = 0; i < answerViews.length; i++) {
            answerViews[i].setText(quizQuestions.get(questionNo).getAnswer(i));
       }
       // gets the correct answer based on the position of the question, this returns a position (int) which needs to be passed to the getAnswer function of the question to get the string answer
       correctAnswer = quizQuestions.get(questionNo).getAnswer(quizQuestions.get(questionNo).getCorrectAnswer()-1);
    }

    // this function checks for when the user clicks on an answer, it changes the background to notify of the selection
    public void onAnswerClick(View view) {
        // this is for when another answer has been picked, that answer must be changed back to the default color before changing the new selection
        if(answerPicked!= null){
            answerPicked.setBackgroundColor(Color.WHITE);
            answerPicked.setTextColor(Color.GRAY);
        }
        // change the selected answer's color and text color
        answerPicked = ((TextView) view);
        view.setBackgroundColor(Color.BLUE);
        ((TextView) view).setTextColor(Color.WHITE);
    }

    // this function checks when the user clicks on the answer fragment
    public void answerClicker(){
        if(!(answerPicked==null)) {
            // gets the answer that the user picked
            String userAnswer = answerPicked.getText().toString();
            // checks if they picked the correct answer
            if (userAnswer == correctAnswer) {
                // increment the correct answer and then switch out the fragment with the "Correct" fragment with the help of FragmentManager
                numOfCorrect++;
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.buttonFragment, fragCorrect);
            }
            // if the answer picked was wrong
            else {
                // switch out the fragment with the "Incorrect" fragment with the help of FragmentManager
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.buttonFragment, incorrectFragment.newInstance(correctAnswer));
            }
            ft.commit();
        }
    }

    // this function checks for when the user pressed the "Correct" or "Incorrect" fragment to move onto the next question
    public void nextQuestionClicker() {
        // increments the question number
        questionNo++;
        // checks if the end of the quiz has been reached
        if(questionNo == quizQuestions.size()){
            // passes over results, total questions, quiz name, and the user's name to the ResultsScreen with help of Intent
            Intent intent = new Intent(this, ResultsScreen.class);
            intent.putExtra("results", numOfCorrect);
            intent.putExtra("total", questionNo);
            intent.putExtra("quizName", quiz.getName());
            intent.putExtra("nameIn", userName);
            startActivity(intent);
        }
        // if there are more questions
        else {
            // set up next question based on the new question number
            setUpQuestion();
            // switch out the fragment with the "Answer" fragment
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.buttonFragment, fragButton);
            ft.commit();
            // reset the color and text color of the answer that was previous picked
            answerPicked.setBackgroundColor(Color.WHITE);
            answerPicked.setTextColor(Color.GRAY);
        }
    }
}