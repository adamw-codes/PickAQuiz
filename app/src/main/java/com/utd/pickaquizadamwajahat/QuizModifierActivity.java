/*
Written by Adam Wajahat for CS4301.002, Quiz Program Part 1, starting Feb 25, 2021
Netid: amw170002

This class takes care of all quiz modification; creating, editing, or deleting.

Reset - clears the fields
Save - saves the inputs to the quiz object to be written (If you are editing a quiz and then would
like to add a new question, click the last question in the recycler view and then press save)
Done - saves the current quiz to local storage and goes back to the main screen
Delete - only comes up for editing quizzes, this deletes the question

The class can then modify the files back in the directory. This class works for both local and
online quizzes. Online quizzes are saved locally. If all questions are deleted from a quiz then the
quiz is deleted from the local storage.
 */

package com.utd.pickaquizadamwajahat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class QuizModifierActivity extends AppCompatActivity implements questionAdapter.OnQuestionListener{
    TextView questionText, radioAnswerHeader;
    EditText questionEnter;
    EditText[] answerChoices = new EditText[4];
    RadioGroup radioAnswerGroup;
    Button deleteButton;
    FragmentTransaction ft;
    quizNameEditFragment editFragment = new quizNameEditFragment();
    quizNameSetFragment setFragment = new quizNameSetFragment();
    Quiz editQuiz = new Quiz();
    String choice;
    RecyclerView recyclerView;
    questionAdapter adapter;
    int questionNo = 0;
    int answer = 0;
    int correctAnswer = 0;
    boolean deleteFlag = false; // flag to tell if the delete button has been pressed
    int questionLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_modifier);
        // get the choice of edit or create option from the main
        Intent intent = getIntent();
        choice = (String) intent.getSerializableExtra("modification_type");
        // gets ids of relevant views from the xml
        questionText = findViewById(R.id.questionText);
        radioAnswerHeader = findViewById(R.id.answerRadioHeader);
        radioAnswerGroup = findViewById(R.id.answerRadioGroup);
        questionEnter = findViewById(R.id.questionEnter);
        answerChoices[0] = findViewById(R.id.answerChoice1Enter);
        answerChoices[1] = findViewById(R.id.answerChoice2Enter);
        answerChoices[2] = findViewById(R.id.answerChoice3Enter);
        answerChoices[3] = findViewById(R.id.answerChoice4Enter);
        deleteButton = findViewById(R.id.deleteQuestionButton);
        recyclerView = findViewById(R.id.questionsRecyclerView);
        // if user wants to edit an existing quiz
        if(choice.equals("edit")){
            // gets the quiz that wants to be edited
            editQuiz = (Quiz) intent.getSerializableExtra("quizIn");
            ft = getSupportFragmentManager().beginTransaction();
            // if the quiz is online then there will not be a quizFile member
            if(editQuiz.getQuizFile() == null){
                // replace with quizFile name from the URL
                ft.replace(R.id.editQuizNameFragment, quizNameSetFragment.newInstance(editQuiz.getQuizURL().toString().substring(46), editQuiz.getName()));
            }
            // if the file is a local quiz
            else {
                // replace with quizFile name from the member variable
                ft.replace(R.id.editQuizNameFragment, quizNameSetFragment.newInstance(editQuiz.getQuizFile().toString().substring(48), editQuiz.getName()));
            }
            ft.commit();
            // set the first set of questions
            setQuestionsFormat();
        }
        // if user wants to create a new quiz
        else if(choice.equals("create")){
            ft = getSupportFragmentManager().beginTransaction();
            // replace with fragment that can take in input for file  and quiz name
            ft.replace(R.id.editQuizNameFragment, editFragment);
            ft.commit();
            // sets the rest of the views to invisible to allow user to first pick a file and quiz name
            setVisible("invisible");
        }
        setAdapter();
        // if the quiz has a length then it needs to be saved to make sure that we reach the end of the
        // quiz to change the type of mode to create
        questionLength = editQuiz.getQuestionList().size();
    }

    // sets the adapter with the question names
    private void setAdapter() {
        adapter = new questionAdapter(editQuiz.getQuestionList(), this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    // method that sets questions up for edit mode
    private void setQuestionsFormat(){
        // if in edit mode add in the delete bottom
        if(choice.equals("edit")){
            deleteButton.setVisibility(View.VISIBLE);
        }
        // sets the text based on the quiz values
        questionEnter.setText(editQuiz.getQuestionList().get(questionNo).getQuestion());
        answer = editQuiz.getQuestionList().get(questionNo).getCorrectAnswer();
        // goes through each of the answers and sets them to their respective answer views
        for(int i = 0; i < answerChoices.length; i++){
            answerChoices[i].setText(editQuiz.getQuestionList().get(questionNo).getAnswer(i));
        }
        correctAnswer = editQuiz.getQuestionList().get(questionNo).getCorrectAnswer();
        // sets the correct answer for the respective radio group so that the user knows which is the correct
        // answer
        if(correctAnswer==1){
            radioAnswerGroup.check(R.id.answerChoice1);
        }
        else if(correctAnswer==2){
            radioAnswerGroup.check(R.id.answerChoice2);
        }
        else if(correctAnswer==3){
            radioAnswerGroup.check(R.id.answerChoice3);
        }
        else if(correctAnswer==4){
            radioAnswerGroup.check(R.id.answerChoice4);
        }
    }

    // sets the visibility of everything except the fragment
    private void setVisible(String visibility){
        // if the visibility needs to be set to invisible it would set each of the views to invisible
        if(visibility.equals("invisible")){
            questionText.setVisibility(View.INVISIBLE);
            questionEnter.setVisibility(View.INVISIBLE);
            radioAnswerHeader.setVisibility(View.INVISIBLE);
            for(int i = 0; i < answerChoices.length; i++){
                answerChoices[i].setVisibility(View.INVISIBLE);
            }
            radioAnswerGroup.setVisibility(View.INVISIBLE);
        }
        // if the visibility needs to be set to visible it would set each of the views to visible
        else if(visibility.equals("visible")){
            questionText.setVisibility(View.VISIBLE);
            questionEnter.setVisibility(View.VISIBLE);
            radioAnswerHeader.setVisibility(View.VISIBLE);
            for(int i = 0; i < answerChoices.length; i++){
                answerChoices[i].setVisibility(View.VISIBLE);
            }
            radioAnswerGroup.setVisibility(View.VISIBLE);
        }
    }

    // when user picks an answer
    public void onAnswerRadioClick(View view) {
        RadioButton radioButton = ((RadioButton) view);
        // ensures that the radio button was clicked not unclicked
        boolean isChecked = radioButton.isChecked();
        if(isChecked){
            // create substring of answer
            char answerChoice = view.getResources().getResourceEntryName(view.getId()).charAt(12);
            // convert that substring into a int to save as answer position
            answer = Character.getNumericValue(answerChoice);
        }
    }

    // user wants to clear their choices
    public void onResetClick(View view) {
        clearForm();
    }

    // user wants to save their choices to the quiz
    public void onSaveClick(View view) {
        // when there is no quiz name is when the user is creating a quiz and the name has not been set
        if(editQuiz.getName() == null){
            // get user input from the fragment
            String quizFileTemp = editFragment.getQuizFile();
            // modify string to allow for better save later in directory
            String quizFile = "Quiz" + quizFileTemp + ".txt";
            String quizName = editFragment.getQuizName();
            // validate input so that it is not blank
            if(!(quizFile.equals("") || quizName.equals(""))){
                ft = getSupportFragmentManager().beginTransaction();
                // sets the fragment with the new file and quiz name
                ft.replace(R.id.editQuizNameFragment, quizNameSetFragment.newInstance(quizFile, quizName));
                ft.commit();
                // set the rest of the views to visible so that the user can enter questions and answers
                setVisible("visible");
                // sets the file and name to the member variables in the quiz object
                File directory = getFilesDir();
                File quizFileDir = new File(directory, quizFile);
                editQuiz.setQuizFile(quizFileDir);
                editQuiz.setName(quizName);
            }
        }
        // when the quiz name has already been set either because user is editing quiz or already set it
        else{
            // gets the entered values for question answer and correct answer
            String question = questionEnter.getText().toString();
            String answers[] = new String[4];
            for(int i = 0; i < answerChoices.length; i++){
                answers[i] = answerChoices[i].getText().toString();
            }
            // validates input to make sure no fields were left black
            if(!(question == null || answers[0] == null || answers[1] == null || answers[2] == null || answers[3] == null || answer == 0)) {
                // checks if the quiz needs to be edited or created
                if (choice.equals("edit")) {
                    // checks if the user picked deleting the question
                    if(deleteFlag) {
                        editQuiz.deleteQuestion(questionNo);
                    }
                    // if the user did not delete the question but edited it
                    else{
                        editQuiz.editQuestion(question, answers[0], answers[1], answers[2], answers[3], answer, questionNo);
                        // move to next question
                        questionNo++;
                    }
                    // if the end of the quiz list has been reached then start creating questions
                    if(questionNo==editQuiz.getQuestionList().size()){
                        // set up the form, clear it and then change mode to create and make the delete
                        // button invisible
                        clearForm();
                        choice= "create";
                        deleteButton.setVisibility(View.INVISIBLE);
                    }
                    // if there are still questions left in the quiz
                    else {
                        // set up the next question
                        setQuestionsFormat();
                    }
                }
                // if the mode is to create
                else if (choice.equals("create")) {
                    // add the question to the quiz object increment the question and clear the form
                    // reset answer back so that it is ready to receive new answer
                    editQuiz.addQuestion(question, answers[0], answers[1], answers[2], answers[3], answer);
                    questionNo++;
                    clearForm();
                    answer = 0;
                }
            }
            // updates the adapter of any changes
            adapter.notifyDataSetChanged();
        }
    }

    // goes through each of the views and clears them
    private void clearForm() {
        if(radioAnswerHeader.getVisibility() == View.INVISIBLE){
            editFragment.clearFragment();
        }
        else {
            radioAnswerGroup.clearCheck();
            questionEnter.getText().clear();
            for (int i = 0; i < answerChoices.length; i++) {
                answerChoices[i].getText().clear();
            }
        }
    }

    // user is done editing or creating
    public void onDoneClick(View view) {
        // checks to ensure that the button is not pressed after no name has been set
        if(!(editQuiz.getName() == null)){
            try {
                // moves the writing the file to new thread
                Boolean quizSaved = new writeQuizFile().execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // toast to show location and let user know that quiz has been saved
            Toast.makeText(this, "Saved to " + editQuiz.getQuizFile(), Toast.LENGTH_LONG).show();
            // returns back to main activity
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    // user wants to delete a question during edit mode
    public void onDeleteClick(View view) {
        // sets deleteFlag to true and calls onSaveClick which handles the editing
        // once deleted the deleteFlag is set back to false
        deleteFlag = true;
        onSaveClick(view);
        deleteFlag = false;
    }

    // when a question is clicked in the recycler view
    @Override
    public void onQuestionClick(int position) {
        // if going back to a previous question
        if(position<=questionNo){
            choice = "edit";
        }
        // set the questionNo to whatever position you are at
        questionNo = position;
        // call method to show that question to the screen
        setQuestionsFormat();
    }

    // method writes the quiz object to local memory
    private class writeQuizFile extends AsyncTask<Quiz, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Quiz... quizzes) {
            // if its an online quiz then it will have a quizUrl member variable
            // online quizzes do not have the file member set so they need to be set
            if (editQuiz.getQuizURL() != null) {
                // gets the name of the quiz
                String quizFile = editQuiz.getName();
                File directory = getFilesDir();
                // sets name based on directory and the quiz name
                File quizFileDir = new File(directory, "Quiz" + quizFile + ".txt");
                editQuiz.setQuizFile(quizFileDir);
            }
            // if the quiz is not blank, it may be blank if the user deleted all the questions
            if(editQuiz.QuestionList.size() != 0) {
                FileOutputStream stream = null;
                try {
                    // opens fileoutputstream with directory from the editQuiz quizFile member variable
                    stream = new FileOutputStream(editQuiz.getQuizFile());
                    // sets name in file
                    String tempName = editQuiz.getName() + "\n";
                    stream.write(tempName.getBytes());
                    // goes through each of the questions
                    for (int i = 0; i < editQuiz.getQuestionList().size(); i++) {
                        // sets the question
                        String tempQuestion = editQuiz.getQuestionList().get(i).getQuestion() + "\n";
                        stream.write(tempQuestion.getBytes());
                        /// gets the position of the correct answer
                        int answerPos = editQuiz.getQuestionList().get(i).getCorrectAnswer();
                        // goes through each of the answers for each question
                        for (int j = 0; j < 4; j++) {
                            String tempAnswer;
                            // if the correct answer's position has been reached add an * at the beginning of the answer
                            if (answerPos == j + 1) {
                                tempAnswer = "*" + editQuiz.getQuestionList().get(i).getAnswer(j) + "\n";
                            }
                            // if the last answer of the last question has been reached be sure not to add a new line
                            else if (i == editQuiz.getQuestionList().size() - 1 && j == 3) {
                                tempAnswer = editQuiz.getQuestionList().get(i).getAnswer(j);
                            }
                            // add in the answer
                            else {
                                tempAnswer = editQuiz.getQuestionList().get(i).getAnswer(j) + "\n";
                            }
                            // write answer to file
                            stream.write(tempAnswer.getBytes());
                        }
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // close the stream after the file has been written
                    if (stream != null) {
                        try {
                            stream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            // in the case that the editQuiz object has no questions
            else{
                // get the directory to delete the file if it exits or just dont write anything
                File temp = editQuiz.getQuizFile();
                if(temp.exists()){
                    temp.delete();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}