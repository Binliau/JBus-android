package com.kevinraihanjbusrd.jbus_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kevinraihanjbusrd.jbus_android.model.Schedule;

import java.text.SimpleDateFormat;
import java.util.List;

public class BusScheduleArrayAdapter extends ArrayAdapter<Schedule> {
    // invoke the suitable constructor of the ArrayAdapter class
    public BusScheduleArrayAdapter(@NonNull Context context, List<Schedule> arrayList) {

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
            currentItemView = LayoutInflater.from(getContext()).inflate(R.layout.bus_schedule_view, parent, false);
        }

        // get the position of the view from the ArrayAdapter
        Schedule currentNumberPosition = getItem(position);

        // then according to the position of the view assign the desired image for the same
        assert currentNumberPosition != null;

        // then according to the position of the view assign the desired TextView 1 for the same
        TextView textView1 = currentItemView.findViewById(R.id.your_date);
        textView1.setText(currentNumberPosition.printDate());

        TextView textView2 = currentItemView.findViewById(R.id.your_time);
        textView2.setText(currentNumberPosition.printTime());

        // then return the recyclable view
        return currentItemView;
    }
}

