package com.example.bubblewash.model;

import androidx.room.ColumnInfo;

public class MonthCostTuple {
    @ColumnInfo(name = "bookingMonth")
    private String bookingMonth;

    @ColumnInfo(name = "cost")
    private float cost;

    public MonthCostTuple(String bookingMonth, float cost) {
        this.bookingMonth = bookingMonth;
        this.cost = cost;
    }

    public String getBookingMonth() {
        return bookingMonth;
    }

    public void setBookingMonth(String bookingMonth) {
        this.bookingMonth = bookingMonth;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
