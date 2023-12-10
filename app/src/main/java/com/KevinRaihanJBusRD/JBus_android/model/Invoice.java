package com.kevinraihanjbusrd.jbus_android.model;

import java.sql.Timestamp;

public class Invoice extends Serializable {
    public enum BusRating
    {
        NONE,
        NEUTRAL,
        GOOD,
        BAD,
    }
    public enum PaymentStatus
    {
        FAILED,
        WAITING,
        SUCCESS,
    }
    public Timestamp time;
    public int buyerId, renterId;
    public BusRating rating;
    public PaymentStatus status;
}
