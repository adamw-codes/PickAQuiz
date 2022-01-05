/*
This object was created as a result of holding the each of the questions for the quiz object. This object
contains the question, a 4 length string array for answers, and an int for the position of the correct answer.
 */

package com.utd.pickaquizadamwajahat;

import java.io.Serializable;

// implements serializable to help with passing through Intents
public class Questions implements Serializable {
    private String question; // the question string
    private String[] answer; // an array of answers
    private int correctAnswer; // the position of the correct answer

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String[] answer) {
        this.answer = answer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    // this function returns answer based on passed in position
    public String getAnswer(int i) {
        return answer[i];
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }
}
