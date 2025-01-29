package com.example.simplefoodapp.model;

public class Menu {
    private int id;
    private String itemName;
    private String itemDescription;
    private double itemPrice;


    public Menu(int id, String itemName, String itemDescription, double itemPrice) {
        this.id = id;
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrice = itemPrice;

    }

    public int getMenuID() {
        return id;
    }

    public void setMenuID(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }


    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                '}';
    }
}