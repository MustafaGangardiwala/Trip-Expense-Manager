package com.example.tripexpensemanager;

public class TripItem {
    private int id;
    private String name;
    private String date;
    private String amount;
    private  String note;

    public TripItem(String name, String date) {
        this.name = name;
        this.date = date;
    }
    public TripItem(int id, String name, String amount, String note) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.note = note;
    }

    public TripItem(String note, String amount, String name){
        this.name = name;
        this.amount = amount;
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }
}
