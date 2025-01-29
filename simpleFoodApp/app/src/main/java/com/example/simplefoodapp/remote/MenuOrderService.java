package com.example.simplefoodapp.remote;

import com.example.simplefoodapp.model.Menu;
import com.example.simplefoodapp.model.MenuOrder;
import com.example.simplefoodapp.model.Order;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MenuOrderService {

    @FormUrlEncoded
    @POST("api/menuOrder/addMenuOrder")
    Call<MenuOrder> addMenuOrder(@Header("api-key") String apiKey,
                         @Field("orderId") int orderId,
                         @Field("id") int id);

    @GET("api/menu_order/{menu_order_id}")
    Call<Order>getMenuOrderId(@Header("api-key") String api_key, @Path("menu_order_id") int
            menu_order_id);
}
