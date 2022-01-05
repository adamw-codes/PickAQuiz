/*
This fragment is used to show that the user picked an incorrect answer.
 */

package com.utd.pickaquizadamwajahat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link incorrectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// implements View.OnClickListener to help with determining whether the fragment was clicked
public class incorrectFragment extends Fragment implements View.OnClickListener{
    Button button;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public incorrectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment incorrectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static incorrectFragment newInstance(String param1, String param2) {
        incorrectFragment fragment = new incorrectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_incorrect, container, false);
        // set color of button and onClick listener for the button
        button = (Button) view.findViewById(R.id.fragmentIncorrect);
        button.setBackgroundColor(Color.RED);
        // gets the bundle that was define earlier in the newInstance function
        String answer = getArguments().getString("answer", null);
        setButton(answer);
        button.setOnClickListener(this);
        return view;
    }

    // this function is called when the button is clicked
    @Override
    public void onClick(View v) {
        // creates an instance of the quiz and then calls on the nextQuestionClick to move to the next question
        QuizScreen quizSc = (QuizScreen)getActivity();
        quizSc.nextQuestionClicker();
    }

    // this function sets the value of the textView for the fragment to the correct answer
    public void setButton(String correctAnswer) {
        button.setText(correctAnswer);
    }

    // this function adds the answer as a parameter into a new created instance of incorrectFrag
    // this has to be done because the button is not available until the fragment is inflated
    public static incorrectFragment newInstance(String answer){
        incorrectFragment passFrag = new incorrectFragment();
        // sets a bundle as an argument that can be accessed in the onCreateView
        Bundle args = new Bundle();
        args.putString("answer", answer);
        passFrag.setArguments(args);
        return passFrag;
    }
}