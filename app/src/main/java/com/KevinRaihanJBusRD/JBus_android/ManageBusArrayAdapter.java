package com.kevinraihanjbusrd.jbus_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kevinraihanjbusrd.jbus_android.model.Bus;

import java.util.List;

public class ManageBusArrayAdapter extends ArrayAdapter<Bus> {
    // invoke the suitable constructor of the ArrayAdapter class
    public ManageBusArrayAdapter(@NonNull Context context, List<Bus> arrayList) {

        // pass the context and arrayList for the super
        // constructor of the ArrayAdapter class
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // convertView which is recyclable view
        View currentItemView = convertView;

        // of the recyclable view is null then inflate the custom layout for the same
        if (currentItemView == null) {
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.manage_bus_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Bus currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        assert currentNumberPosition != null;

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.textView1);
        textView1.setText(currentNumberPosition.toString());


        // then return the recyclable view
        return currentItemView;
    }
}