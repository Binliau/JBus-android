package com.kevinraihanjbusrd.jbus_android.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

public class Payment extends Invoice {
    private int busId;
    public List<String> busSeats;
    public Timestamp departureDate;

    public int getBusId(){
        return busId;
    }
    public String getDepartureTime(){
        SimpleDateFormat FormatCS = new SimpleDateFormat("MMMM dd, yyyy kk:mm:ss");
        return FormatCS.format(departureDate.getTime());
    }


}
