package com.kevinraihanjbusrd.jbus_android.model;

import androidx.annotation.NonNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;

public class Schedule {
    public Timestamp departureSchedule;
    public String stringSchedule;
    public Map<String, Boolean> seatAvailability;
    public String printDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM d, yyyy");
        String formattedDate = dateFormat.format(departureSchedule.getTime());
        return formattedDate;
    }

    public String printTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedTime = dateFormat.format(departureSchedule.getTime());
        return formattedTime;
    }

    @NonNull
    @Override
    public String toString() {
        SimpleDateFormat stringFormat = new SimpleDateFormat("MMMM d'-' HH:mm");
        String formattedString = stringFormat.format(departureSchedule);
        return formattedString;
    }
}
