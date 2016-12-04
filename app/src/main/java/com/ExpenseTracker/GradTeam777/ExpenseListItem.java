package com.ExpenseTracker.GradTeam777;

/**
 * Created by vinya on 12/2/2016.
 */
public class ExpenseListItem {
    private int id;
    private String date;
    private double amount;
    private String imagePath;
    private boolean delete = false;


    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate(){
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getAmount(){return amount;}

    public void setAmount(double amount) {this.amount = amount;}

    public String getImagePath(){
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setForDeletion() {delete=true; }

    public boolean getForDeletion()
    {
        return delete;
    }

}
