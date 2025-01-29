package com.example.simplefoodapp.remote;

import com.example.simplefoodapp.model.Menu;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MenuService {
    /**
     * Add book by sending a single Book JSON
     * @return book object
     */
    @GET("api/menu")
    Call<List<Menu>> getAllMenus(@Header("api-key") String api_key);
    @GET("api/menu/{id}")
    Call<Menu> getMenu(@Header("api-key") String api_key, @Path("id") int id);

}
