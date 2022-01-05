/*
This fragment is used when the user needs to enter in a file and quiz name for the quiz.
This fragment contains edit text boxes that can then be read for input.
Methods that are added
- getQuizFile returns whatever the user entered in the quizFileEnter text box
- getQuizName returns whatever the user entered in the quizNameEnter text box
- clearFragment clears both of the text box
 */

package com.utd.pickaquizadamwajahat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link quizNameEditFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class quizNameEditFragment extends Fragment {
    TextInputLayout quizName, quizFile;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public quizNameEditFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QuizNameEdit.
     */
    // TODO: Rename and change types and number of parameters
    public static quizNameEditFragment newInstance(String param1, String param2) {
        quizNameEditFragment fragment = new quizNameEditFragment();
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
        View view = inflater.inflate(R.layout.fragment_quiz_name_edit, container, false);
        quizFile = view.findViewById(R.id.quizFileInput);
        quizName = view.findViewById(R.id.quizNameInput);
        return view;
    }

    public String getQuizFile(){
        return quizFile.getEditText().getText().toString();
    }

    public String getQuizName(){
        return quizName.getEditText().getText().toString();
    }

    public void clearFragment(){
        quizFile.getEditText().getText().clear();
        quizName.getEditText().getText().clear();
    }
}