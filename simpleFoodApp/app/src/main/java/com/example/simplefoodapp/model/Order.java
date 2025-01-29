package com.example.simplefoodapp.model;


public class Order {
    private int orderId;
    private int id;
    private double totalAmount;

    private int quantity;

    private int status;
    private String remark;
    private User user;

//    public Order(int orderId, int id, double totalAmount, int quantity, User user) {
//        this.orderId = orderId;
//        this.id = id;
//        this.totalAmount = totalAmount;
//        this.quantity = quantity;
//        this.user = user;
//
//    }

    public Order(int id, double totalAmount, int quantity, String remark) {
        this.id = id;
        this.totalAmount = totalAmount;
        this.quantity = quantity;
        this.remark = remark;

    }

    public int getOrderId() {
        return orderId;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus(){
        return status;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Get user id from the associated User object
    public int getUserId() {
        return user != null ? user.getId() : -1; // Return -1 if user is null (handle null scenario)
    }

    public double calculateTotal() {
        return getTotalAmount() * getQuantity();
    }

    @Override
    public String toString() {
        return "Order{" +
                "Order Id=" + orderId +
                ", ID='" + id + '\'' +
                ", Price='" + totalAmount + '\'' +
                ", Quantity='" + quantity + '\'' +
                '}';
    }

}
