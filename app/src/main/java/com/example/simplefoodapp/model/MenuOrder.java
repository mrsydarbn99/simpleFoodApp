package com.example.simplefoodapp.model;

import java.util.Date;

import retrofit2.Call;

public class MenuOrder {
    private int menu_order_id;
    private int orderId;
    private int id;
    private Date date;
    private int status;

    // Constructors
    public MenuOrder(int menu_order_id, int orderId, int id, Date date, int status) {
        this.menu_order_id = menu_order_id;
        this.orderId = orderId;
        this.id = id;
        this.date = date;
        this.status = status;
    }

    public MenuOrder(int orderId, int id) {
        this.orderId = orderId;
        this.id = id;
    }

    // Getters and Setters

    public int getMenuOrderId() {
        return menu_order_id;
    }

    public void setMenuOrderId(int menuOrderId) {
        this.menu_order_id = menuOrderId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // You may also override toString() for debugging purposes
    @Override
    public String toString() {
        return "MenuOrder{" +
                "menuOrderId=" + menu_order_id +
                ", orderId=" + orderId +
                ", id=" + id +
                ", date=" + date +
                ", status='" + status + '\'' +
                '}';
    }
}
