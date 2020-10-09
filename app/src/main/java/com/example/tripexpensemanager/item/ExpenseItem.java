package com.example.tripexpensemanager.item;

public class ExpenseItem {
    private String name;
    private String amount;
    private  String note;

    public ExpenseItem(String name, String note, String amount) {
        this.name = name;
        this.note = note;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
