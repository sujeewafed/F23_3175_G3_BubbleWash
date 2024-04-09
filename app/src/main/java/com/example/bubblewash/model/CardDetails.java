package com.example.bubblewash.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "carddetails")
public class CardDetails {
    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "cardnumber")
    private String cardNumber;
    @ColumnInfo(name = "expirydate")
    private String expDate;
    @ColumnInfo(name = "cvc")
    private String cvc;
    @ColumnInfo(name = "cardholdername")
    private String cardHolderName;

    @NonNull
    @ColumnInfo(name = "userId")
    private String userId;

    public CardDetails() {
    }

    public CardDetails(@NotNull String cardNumber, String expDate, String cvc, String cardHolderName, @NonNull String userId) {
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.cvc = cvc;
        this.cardHolderName = cardHolderName;
        this.userId = userId;
    }

    public CardDetails(@NotNull String cardNumber, String expDate, String cvc, String cardHolderName) {
        this.cardNumber = cardNumber;
        this.expDate = expDate;
        this.cvc = cvc;
        this.cardHolderName = cardHolderName;
    }

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NotNull
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(@NotNull String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }
}
