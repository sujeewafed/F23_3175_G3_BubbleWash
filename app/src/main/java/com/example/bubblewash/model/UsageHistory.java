package com.example.bubblewash.model;

import java.util.Date;

public class UsageHistory {
    private String date;
    private float cost;

    public UsageHistory(String date, float cost) {
        this.date = date;
        this.cost = cost;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }
}
