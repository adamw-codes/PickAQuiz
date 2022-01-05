/*
MainAcitivty

Written by Adam Wajahat for CS4301.002, Quiz Program Part 1, starting Feb 25, 2021
Netid: amw170002

This MainActivity class is the initial screen that displays the quiz names for the user to pick. It then reads in the user's name and selection of quiz. It reads in the
quiz information such as the questions and answers into a Quiz object which is passed to the QuizScreen activity.

 */

package com.utd.pickaquizadamwajahat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.Editable;
import android.text.NoCopySpan;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements recyclerAdapter.OnQuizListener, Serializable, NoCopySpan {
    private ArrayList<Quiz> QuizList = new ArrayList<Quiz>(); // arraylist that holds all quizzes
    private RecyclerView recyclerView;
    private Quiz selectedQuiz; // quiz that is chosen by the user
    TextInputLayout textName; // textview that holds the user's input for name
    recyclerAdapter adapter; // adapter that is used to set the recycler
    EditText textNameEdit;
    Button createQuizButton;
    Button editQuizButton;

    // onCreate sets the quiz names into the quizList array so that they can be passed to the recyclerView to be displayed to the user
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // gets the view of the textView where the user enters their name
        textName = findViewById(R.id.textName);
        textNameEdit = findViewById(R.id.textNameEdit);
        // call function to set Quizzes local
        setLocalQuizzes();
        // get recycler view object to set quiz names
        recyclerView = findViewById(R.id.quizNameList);
        // sets the adapter to be viewed through adapter
        setAdapter();
        // gets ids for buttons to set visibility for keyword check later
        createQuizButton = findViewById(R.id.createQuizButton);
        editQuizButton = findViewById(R.id.editQuizButton);
        // set up text change listener for keyword "professor"
        textNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //nothing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // gets the text of edit text and converts to a string, then it checks if it is equal to keyword
                // without caring about case
                if(textName.getEditText().getText().toString().equalsIgnoreCase("professor")){
                    createQuizButton.setVisibility(View.VISIBLE);
                    editQuizButton.setVisibility(View.VISIBLE);
                }
                // if its been set to visible and then the name is changed then the buttons need to
                // go away
                else{
                    createQuizButton.setVisibility(View.INVISIBLE);
                    editQuizButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    // this sets the quiz names for the recycler view with local quizzes
    private void setLocalQuizzes(){
        // Read quizzes in
        try{
            // Get List of Files with Quiz
            File directory = getFilesDir();
            // Filter for files that only start with Quiz
            File[] myFiles = directory.listFiles(new FilenameFilter(){
                public boolean accept(File directory, String fileName){
                    if(fileName.startsWith("Quiz")){
                        return true;
                    }
                    return false;
                }
            });
            // clears the QuizList in case there we are switching from online to local
            QuizList.clear();
            // Read in each of the Quizzes into object array of Quiz
            for(int i = 0; i < myFiles.length; i++){
                Scanner quizReader = null;
                quizReader = new Scanner(myFiles[i]);
                // Create tempQuiz to hold current quiz value
                Quiz tempQuiz = new Quiz();
                // Read first line which is quiz name
                String quizName = quizReader.nextLine();
                tempQuiz.setName(quizName);
                tempQuiz.setQuizFile(myFiles[i]);
                // Add quiz to object array
                QuizList.add(tempQuiz);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    // this is when the keyword has been set and then either of the editing buttons have been clicked
    public void onEditClick(View view) {
        // gets id of clicked button
        String choice = view.getResources().getResourceEntryName(view.getId());
        // checks which button was clicked
        if(choice.equals("editQuizButton")){
            // let next activity know that this is an edit session through intent
            // check if there has been a selected quiz or not
            if(selectedQuiz != null){
                // block is used to fill the quiz with either the online or local quiz
                try {
                    // calls the class and uses get to ensure that the function does not continue until the thread is done
                    // that is because activity cannot be filled on next screen with a null quiz questions
                    Boolean quizFilled = new getQuestions().execute().get();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                // pass in selected quiz list so that the questions can be modified
                Intent intent = new Intent(this, QuizModifierActivity.class);
                intent.putExtra("modification_type", "edit");
                intent.putExtra("quizIn", selectedQuiz);
                startActivity(intent);
            }
        }
        else if(choice.equals("createQuizButton")){
            // let next activity know that this is a create session through intent
            Intent intent = new Intent(this, QuizModifierActivity.class);
            intent.putExtra("modification_type", "create");
            startActivity(intent);
        }
    }

    // this sets the quiz names for the recycler view with online quizzes
    private class setOnlineQuizzes extends AsyncTask<URL, Integer, Boolean>{
        @Override
        // this function gets the names of each of the quizzes from the site
        protected Boolean doInBackground(URL... urls) {
            HttpURLConnection connection = null;
            URL quizNameUrl;
            // block is used to open connected
            try {
                // gets base URL where quizzes are located and opens connection
                quizNameUrl = new URL(getString(R.string.allQuizUrl));
                connection = (HttpURLConnection) quizNameUrl.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.connect();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            // block is used to get quiz URLs
            try {
                // if the connection is successful
                if(connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    // clears the QuizList in case we are switching from local to online
                    QuizList.clear();
                    String quizURLName;
                    // gets the quiz URL's for each of them
                    while((quizURLName = in.readLine()) != null) {
                        Quiz tempQuiz = new Quiz();
                        // gets the URL and modifies it to the new URL
                        String tempURL = getString(R.string.baseQuizUrl) + quizURLName;
                        // sets the member variable in the Quiz class
                        tempQuiz.setQuizURL(new URL(tempURL));
                        // stores the URL into the QuizList
                        QuizList.add(tempQuiz);
                    }
                    // disconnects from site
                    connection.disconnect();
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            // block is used to get quiz names from each quiz url
            try{
                // goes through each of the quizzes
                for(int i = 0; i < QuizList.size(); i++){
                    // opens each of the quizs' respective url and opens connected
                    URL quizURL = QuizList.get(i).getQuizURL();
                    connection = (HttpURLConnection) quizURL.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    // if connection is successful
                    if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                        try {
                            // read in the first line of the site which would be the quiz name
                            InputStream quizNameStream = connection.getInputStream();
                            InputStreamReader isr = new InputStreamReader(quizNameStream, "UTF-08");
                            BufferedReader in = new BufferedReader(isr);
                            String quizName = in.readLine();
                            // set the quizname to that quiz in the quiz object
                            QuizList.get(i).setName(quizName);
                        } catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
                    else{
                        System.out.println("Bad Connection to quiz connection!");
                    }
                    // disconnect from site
                    connection.disconnect();
                }
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
            return null;
        }

        @Override
        // this function lets the adatper know to update the information
        protected void onPostExecute(Boolean aBoolean) {
            adapter.notifyDataSetChanged();
            super.onPostExecute(aBoolean);
        }
    }

    // adapter which allows communication to adapter view in xml
    private void setAdapter() {
        // Pass in QuizList with quiz names to be displayed in recycler view
        adapter = new recyclerAdapter(QuizList, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // sets the adapter
        recyclerView.setAdapter(adapter);
    }

    // returns the position of the quiz that was clicked
    @Override
    public void onQuizClick(int position) {
        selectedQuiz = QuizList.get(position);
    }

    // this function is called when the user pressed the button to continue to the next screen
    public void onNextClick(View view) {
        // sets the user's name that they entered into the textView
        String userName = textName.getEditText().getText().toString();
        // if the user did not pick a quiz
        if(selectedQuiz == null){
            System.out.println("Please enter a quiz");
        }
        // if the user did not enter a name
        else if(userName.isEmpty()){
            System.out.println("Please enter a name");
        }
        else {
            // block is used to fill the quiz with either the online or local quiz
            try {
                // calls the class and uses get to ensure that the function does not continue until the thread is done
                // that is because activity cannot be filled on next screen with a null quiz questions
                Boolean quizFilled = new getQuestions().execute().get();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            // intent between the MainActivity and QuizScreen which passes the Quiz object and the name of the user
            Intent intent = new Intent(this, QuizScreen.class);
            intent.putExtra("quizIn", selectedQuiz);
            intent.putExtra("name", userName);
            startActivity(intent);
        }
    }

    // this function checks what type of quiz that the user picks
    public void onRadioClick(View view) {
        // get radio button that was clicked or unclicked
        RadioButton radioButton = ((RadioButton) view);
        // check if the button was clicked or unclicked (unclicked would return a false)
        boolean isChecked = radioButton.isChecked();
        if(isChecked){
            // if your radio button is selected the selectedQuiz is restarted
            selectedQuiz = null;
            // if the button clicked was local button
            if(radioButton.getId() == R.id.radioButtonLocal){
                // change the quiz data set and notify the adapter
                setLocalQuizzes();
                adapter.notifyDataSetChanged();
            }
            // if the button clicked was online
            else if(radioButton.getId() == R.id.radioButtonOnline){
                selectedQuiz = null;
                // start async task and execute
                setOnlineQuizzes onlineQuiz = new setOnlineQuizzes();
                onlineQuiz.execute();
            }
        }
    }

    // this class is called after the user selects a quiz and confirms their choice, this is called in this order to help without blogging down the onCreate
    private class getQuestions extends AsyncTask<URL, Integer, Boolean> {

        @Override
        // this function gets the questions from the quiz url and sets them in the object
        protected Boolean doInBackground(URL... urls) {
            // Goes through each of the questions
            try {
                Scanner quizReader = null;
                // if the quiz object has a url member variable it would mean that it is an online quiz
                if(selectedQuiz.getQuizURL() != null) {
                    // gets the url and sets it
                    HttpURLConnection connection = null;
                    URL quizURL = selectedQuiz.getQuizURL();
                    // block opens connection to site of quiz
                    try {
                        connection = (HttpURLConnection) quizURL.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(5000);
                        connection.connect();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    // this function assures that the connection is good and the scanner is set for reading
                    try {
                        // if the connection was successful
                        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            InputStream quizNameStream = connection.getInputStream();
                            quizReader = new Scanner(quizNameStream);
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                // if the object does not have a url member variable then that would mean it is a local quiz
                else{
                    quizReader = new Scanner(selectedQuiz.getQuizFile());
                }
                // skips the first line as that is the quiz name which we already have
                String name = quizReader.nextLine();
                // while the file has not reached to the end
                while (quizReader.hasNext()) {
                    // read in the question
                    String question = quizReader.nextLine();
                    // there are 4 answers so there is a 4 string array to store each of them
                    String[] answers = new String[4];
                    int correctAnswer = 0;
                    // Goes through each of the answers
                    for (int j = 0; j < answers.length; j++) {
                        // reads in the answer
                        String tempAnswer = quizReader.nextLine();
                        // this is to test if the answer is the correct answer
                        if (tempAnswer.charAt(0) == '*') {
                            // get the position of the correct answer
                            correctAnswer = j + 1;
                            // take out the *
                            tempAnswer = tempAnswer.replace("*", "");
                        }
                        // set the answer in the answers array
                        answers[j] = tempAnswer;
                    }
                    // Adds in the question into Quiz
                    selectedQuiz.addQuestion(question, answers[0], answers[1], answers[2], answers[3], correctAnswer);
                }
            } catch(Exception e){
                System.out.println(e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }


}