package com.example.bubblewash.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "timeDurations")
public class TimeDuration {
    @PrimaryKey
    @ColumnInfo(name = "starttime")
    private int startTime;

    public TimeDuration() {
    }

    public TimeDuration(int startTime) {
        this.startTime = startTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }
}
