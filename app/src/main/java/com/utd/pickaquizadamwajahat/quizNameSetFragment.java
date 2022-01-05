/*
This fragment is used when the quiz file and name have already been set and they just need to be displayed.
This fragment contains only text views that hold the values that would be set through the newInstance()
method which passes in the quiz file and name from the class that calls it in the parameters and allows
this fragment to access it in the onCreate method with the help of a bundle
Methods added:
- setText sets the text of the views based on what was passed in
 */

package com.utd.pickaquizadamwajahat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link quizNameSetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class quizNameSetFragment extends Fragment {
    TextView quizFileText;
    TextView quizNameText;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public quizNameSetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment quizNameSetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static quizNameSetFragment newInstance(String quizFile, String quizName) {
        quizNameSetFragment fragment = new quizNameSetFragment();
        Bundle args = new Bundle();
        args.putString("quizFile", quizFile);
        args.putString("quizName", quizName);
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
        View view = inflater.inflate(R.layout.fragment_quiz_name_set, container, false);
        quizFileText = (TextView) view.findViewById(R.id.quizFileSet);
        quizNameText = (TextView) view.findViewById(R.id.quizNameSet);
        // gets the values passed in from the class that called through the bundle
        String quizFile = getArguments().getString("quizFile");
        String quizName = getArguments().getString("quizName");
        // sets the text of the values passed in
        setText(quizFile, quizName);
        return view;
    }

    public void setText(String quizFile, String quizName){
        quizFileText.setText(quizFile);
        quizNameText.setText(quizName);
    }
}