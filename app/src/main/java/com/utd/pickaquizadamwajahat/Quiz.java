/*
This object was created to assist in the structure of the data that was passed in. Each quiz has an arraylist of questions that are held.
The quiz object also contains the name of the quiz and the file location
 */

package com.utd.pickaquizadamwajahat;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;

// implements serializable to help with passing through Intents
public class Quiz implements Serializable {
    private String name; // name of quiz
    private File quizFile;
    private URL quizURL; // variable only needed for online quizzes
    ArrayList<Questions> QuestionList = new ArrayList<Questions>(); // used to hold all questions of the quiz

    public void setName(String name) {
        this.name = name;
    }

    public void setQuizFile(File quizFile){
        this.quizFile = quizFile;
    }

    public void setQuizURL(URL quizURL) {
        this.quizURL = quizURL;
    }

    // this function adds in an entire question object to the arraylist
    public void addQuestion(String question, String answer1, String answer2, String answer3, String answer4, int correctAnswer){
        String[] answers = {answer1, answer2, answer3, answer4};
        Questions tempQuestion = new Questions();
        tempQuestion.setQuestion(question);
        tempQuestion.setAnswer(answers);
        tempQuestion.setCorrectAnswer(correctAnswer);
        QuestionList.add(tempQuestion);
    }

    // this function edits an existing question
    public void editQuestion(String question, String answer1, String answer2, String answer3, String answer4, int correctAnswer, int index){
        String[] answers = {answer1, answer2, answer3, answer4};
        Questions tempQuestion = new Questions();
        tempQuestion.setQuestion(question);
        tempQuestion.setAnswer(answers);
        tempQuestion.setCorrectAnswer(correctAnswer);
        QuestionList.set(index, tempQuestion);
    }

    public String getName() {
        return name;
    }

    public File getQuizFile(){
        return quizFile;
    }

    public URL getQuizURL() {
        return quizURL;
    }

    public ArrayList<Questions> getQuestionList() {
        return QuestionList;
    }

    public void deleteQuestion(int index){
        QuestionList.remove(index);
    }

}
