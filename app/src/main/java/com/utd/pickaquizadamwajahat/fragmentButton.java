/*
This fragment is used to let the user confirm their choice.
*/

package com.utd.pickaquizadamwajahat;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmentButton#newInstance} factory method to
 * create an instance of this fragment.
 */

// implements View.OnClickListener to help with determining whether the fragment was clicked
public class fragmentButton extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmentButton() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmentButton.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmentButton newInstance(String param1, String param2) {
        fragmentButton fragment = new fragmentButton();
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
        View view = inflater.inflate(R.layout.fragment_button, container, false);
        // set onClick listener for the button
        Button button = (Button) view.findViewById(R.id.fragmentButton);
        button.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    // this function is called when the button is clicked
    @Override
    public void onClick(View v) {
        // creates an instance of the quiz and then calls on the nextQuestionClick to check if the answer is correct
        QuizScreen quizSc = (QuizScreen)getActivity();
        quizSc.answerClicker();
    }
}