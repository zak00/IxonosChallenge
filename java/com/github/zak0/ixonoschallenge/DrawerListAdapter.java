package com.github.zak0.ixonoschallenge;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jaakko on 4.3.2016.
 *
 * Custom adapter for drawer list to enable custom fonts.
 */
public class DrawerListAdapter extends ArrayAdapter<String> {

    public DrawerListAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        TextView textView = (TextView) view.findViewById(R.id.textViewListItem);
        textView.setTypeface(MainActivity.TfTungstenRndMedium);

        return view;
    }
}
