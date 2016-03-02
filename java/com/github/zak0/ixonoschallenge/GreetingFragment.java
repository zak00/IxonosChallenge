package com.github.zak0.ixonoschallenge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

public class GreetingFragment extends Fragment {

    private static final String TAG = "GreetingFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_greeting, null);

        final EditText editTextEmail = (EditText) root.findViewById(R.id.editTextEmail);

        final ImageButton buttonGo = (ImageButton) root.findViewById(R.id.buttonGo);
        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "entered email is valid: " + validateEmail(editTextEmail.getText().toString()));

                //boolean emailWasValid = validateEmail(editTextEmail.getText().toString());
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
