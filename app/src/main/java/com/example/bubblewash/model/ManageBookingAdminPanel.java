package com.example.bubblewash.model;

import com.example.bubblewash.utils.BookingStatus;

public class ManageBookingAdminPanel {

    private String date;
    private String month;

    private int pickTime;
    private BookingStatus status;

    public ManageBookingAdminPanel(String date, String month, int pickTime, BookingStatus status) {
        this.date = date;
        this.month = month;
        this.pickTime = pickTime;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


    public int getPickTime() {
        return pickTime;
    }

    public void setPickTime(int pickTime) {
        this.pickTime = pickTime;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }
}
