package com.example.simplefoodapp.remote;

public class ApiUtils {
    // REST API server URL
    public static final String BASE_URL = "https://itsmelife.000webhostapp.com/prestige/";
    // return UserService instance
    public static UserService getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(UserService.class);
    }

    // return MenuService instance
    public static MenuService getMenuService() {
        return RetrofitClient.getClient(BASE_URL).create(MenuService.class);
    }

    public static OrderService getOrderService() {
        return RetrofitClient.getClient(BASE_URL).create(OrderService.class);
    }

    public static MenuOrderService getMenuOrderService() {
        return RetrofitClient.getClient(BASE_URL).create(MenuOrderService.class);
    }

}