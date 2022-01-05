/*
This fragment is used to show that the user picked an correct answer.
 */

package com.utd.pickaquizadamwajahat;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link correctFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class correctFragment extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public correctFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment correctFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static correctFragment newInstance(String param1, String param2) {
        correctFragment fragment = new correctFragment();
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
        View view = inflater.inflate(R.layout.fragment_correct, container, false);
        // set color of button and onClick listener for the button
        Button button = (Button) view.findViewById(R.id.fragmentCorrect);
        button.setBackgroundColor(Color.GREEN);
        button.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }

    // this function is called when the button is clicked
    @Override
    public void onClick(View v) {
        // creates an instance of the quiz and then calls on the nextQuestionClick to move to the next question
        QuizScreen quizSc = (QuizScreen)getActivity();
        quizSc.nextQuestionClicker();
    }
}