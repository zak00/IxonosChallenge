package com.github.zak0.ixonoschallenge;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AboutFragment extends Fragment {

    private static final String TAG = "AboutFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = (ViewGroup) inflater.inflate(R.layout.fragment_about, null);

        TextView textViewAbout = (TextView) root.findViewById(R.id.textViewAbout);
        TextView textViewVersion = (TextView) root.findViewById(R.id.textViewAppVersion);
        TextView textViewAuthor = (TextView) root.findViewById(R.id.textViewAuthor);

        textViewAbout.setTypeface(MainActivity.TfTungstenRndMedium);
        textViewVersion.setTypeface(MainActivity.TfTungstenRndBook);
        textViewAuthor.setTypeface(MainActivity.TfTungstenRndBook);

        return root;
    }

}
