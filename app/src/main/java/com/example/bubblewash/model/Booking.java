package com.example.bubblewash.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.bubblewash.utils.BookingStatus;

@Entity(tableName = "bookings")
public class Booking {
    @NonNull
    @PrimaryKey(autoGenerate = true )
    @ColumnInfo(name = "id")
    private int id;

    @NonNull
    @ColumnInfo(name = "userId")
    private String userId;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "bookingMonth")
    private String month;

    @ColumnInfo(name = "wash")
    private boolean wash;

    @ColumnInfo(name = "dry")
    private boolean dry;

    @ColumnInfo(name = "washCost")
    private float washCost;

    @ColumnInfo(name = "dryCost")
    private float dryCost;

    @ColumnInfo(name = "totalCost")
    private float totalCost;

    @ColumnInfo(name = "washTime")
    private int washTime;

    @ColumnInfo(name = "dryTime")
    private int dryTime;

    @ColumnInfo(name = "pick")
    private boolean pick;

    @ColumnInfo(name = "deliver")
    private boolean deliver;

    @ColumnInfo(name = "pickTime")
    private int pickTime;

    @ColumnInfo(name = "deliverTime")
    private int deliverTime;

    @NonNull
    @ColumnInfo(name = "status")
    private BookingStatus status;

    @ColumnInfo(name = "rating")
    private float rating;

    @ColumnInfo(name = "remarks")
    private String remarks;

    public Booking() {
    }

    public Booking(@NonNull String userId, @NonNull String date, @NonNull String month, boolean wash, boolean dry, float washCost, float dryCost,
                   float totalCost, int washTime, int dryTime, boolean pick, int pickTime,boolean deliver, int deliverTime, BookingStatus status, float rating, String remarks) {
        //this.id = id;
        this.userId = userId;
        this.month = month;
        this.date = date;
        this.wash = wash;
        this.dry = dry;
        this.washCost = washCost;
        this.dryCost = dryCost;
        this.totalCost = totalCost;
        this.washTime = washTime;
        this.dryTime = dryTime;
        this.pick = pick;
        this.pickTime = pickTime;
        this.deliver = deliver;
        this.deliverTime = deliverTime;
        this.status = status;
        this.rating = rating;
        this.remarks = remarks;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    @NonNull
    public String getMonth() {
        return month;
    }

    public void setMonth(@NonNull String month) {
        this.month = month;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public boolean isWash() {
        return wash;
    }

    public void setWash(boolean wash) {
        this.wash = wash;
    }

    public boolean isDry() {
        return dry;
    }

    public void setDry(boolean dry) {
        this.dry = dry;
    }

    public float getWashCost() {
        return washCost;
    }

    public void setWashCost(float washCost) {
        this.washCost = washCost;
    }

    public float getDryCost() {
        return dryCost;
    }

    public void setDryCost(float dryCost) {
        this.dryCost = dryCost;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public int getWashTime() {
        return washTime;
    }

    public void setWashTime(int washTime) {
        this.washTime = washTime;
    }

    public int getDryTime() {
        return dryTime;
    }

    public void setDryTime(int dryTime) {
        this.dryTime = dryTime;
    }

    @NonNull
    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(@NonNull BookingStatus status) {
        this.status = status;
    }

    public int getPickTime() {
        return pickTime;
    }

    public void setPickTime(int pickTime) {
        this.pickTime = pickTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isPick() {
        return pick;
    }

    public void setPick(boolean pick) {
        this.pick = pick;
    }

    public boolean isDeliver() {
        return deliver;
    }

    public void setDeliver(boolean deliver) {
        this.deliver = deliver;
    }

    public int getDeliverTime() {
        return deliverTime;
    }

    public void setDeliverTime(int deliverTime) {
        this.deliverTime = deliverTime;
    }
}

