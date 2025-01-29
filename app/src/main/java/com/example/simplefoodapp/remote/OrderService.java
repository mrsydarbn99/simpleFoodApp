package com.example.simplefoodapp.remote;

import com.example.simplefoodapp.model.DeleteResponse;
import com.example.simplefoodapp.model.Order;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {
    @FormUrlEncoded
    @POST("api/orders/addOrder")
    Call<Order> addOrder(@Header("api-key") String apiKey,
                         @Field("id") int id,
                         @Field("totalAmount") double totalAmount,
                         @Field("quantity") int quantity,
                         @Field("remark") String remark);



    @GET("api/orders/allOrder")
    Call<List<Order>> getOrder(@Header("api-key") String apiKey);

    @GET("api/orders/getById")
    Call<List<Order>> getOrderById(@Header("api-key") String apiKey,
                                   @Query("id") int id);
    @POST("api/orders/delete/{id}")
    Call<DeleteResponse> deleteOrder(@Header ("api-key") String apiKey, @Path("id")
    int id);
    @FormUrlEncoded
    @POST("api/orders/prepareOrder")
    Call<Order> prepareOrder(@Header("api-key") String apiKey,
                             @Field("orderId") int orderId);
    @FormUrlEncoded
    @POST("api/orders/readyOrder")
    Call<Order> readyOrder(@Header("api-key") String apiKey,
                           @Field("orderId") int orderId);


}
