package com.github.zak0.ixonoschallenge;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class GreetingFragment extends Fragment {

    private static final String TAG = "GreetingFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_greeting, null);


        TextView textViewSignUp = (TextView) root.findViewById(R.id.textViewSignUp);
        final EditText editTextEmail = (EditText) root.findViewById(R.id.editTextEmail);
        final EditText editTextFirstName = (EditText) root.findViewById(R.id.editTextFirstName);
        final EditText editTextLastName = (EditText) root.findViewById(R.id.editTextLastName);

        textViewSignUp.setTypeface(MainActivity.TfTungstenRndMedium);
        editTextEmail.setTypeface(MainActivity.TfProximaNovaRegular);
        editTextFirstName.setTypeface(MainActivity.TfProximaNovaRegular);
        editTextLastName.setTypeface(MainActivity.TfProximaNovaRegular);

        final ImageButton buttonGo = (ImageButton) root.findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "entered email is valid: " + validateEmail(editTextEmail.getText().toString()));

                boolean emailWasValid = validateEmail(editTextEmail.getText().toString());

                // Save user if email was valid.
                if(emailWasValid) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

                    ((MainActivity) getActivity()).saveUser(new User(editTextEmail.getText().toString(),
                            editTextFirstName.getText().toString(),
                            editTextLastName.getText().toString()));
                }
                else
                    ((MainActivity) getActivity()).showInvalidEmailBanner();
            }
        });

        return root;
    }


    // Returns true if given email is valid, false otherwise.
    // Uses Androids inbuilt Patterns to validate the email.
    private boolean validateEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
