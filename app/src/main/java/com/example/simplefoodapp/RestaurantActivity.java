package com.example.simplefoodapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.example.simplefoodapp.adapter.RestaurantAdapter;
import com.example.simplefoodapp.model.DeleteResponse;
import com.example.simplefoodapp.model.Order;
import com.example.simplefoodapp.model.SharedPrefManager;
import com.example.simplefoodapp.model.User;
import com.example.simplefoodapp.remote.ApiUtils;
import com.example.simplefoodapp.remote.OrderService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantActivity extends AppCompatActivity {

    OrderService orderService;

    Context context;

    RecyclerView orderList;

    RestaurantAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_order_list);

        context = this; // get current activity context

        // get reference to the RecyclerView orderList
        orderList = findViewById(R.id.rOrderList);

        // register for context menu
        super.registerForContextMenu(orderList);

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get order service instance
        orderService = ApiUtils.getOrderService();
        adapter = new RestaurantAdapter(context, new ArrayList<>());

        orderService.getOrder(user.getToken()).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());


                List<Order> orders = response.body();

                // initialize an empty adapter
                RestaurantAdapter adapter = new RestaurantAdapter(context, orders);

                // Set adapter to the RecyclerView
                orderList.setAdapter(adapter);

                // set layout to recycler view
                orderList.setLayoutManager(new LinearLayoutManager(context));

                // add separator between item in the list
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderList.getContext(),
                        DividerItemDecoration.VERTICAL);
                orderList.addItemDecoration(dividerItemDecoration);


            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Log.d("MyApp:", t.getMessage());
                Toast.makeText(context, "Error connecting to the server", Toast.LENGTH_LONG).show();

            }
        });
        // Locate the logout button in the layout
        Button btnLogout = findViewById(R.id.btnLogout);

        // Set a click listener for the logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle logout here
                logout();
            }
        });

    }

    private void logout() {
        // Implement your logout logic here
        // For example, clearing user data from SharedPreferences and navigating to the login screen
        SharedPrefManager.getInstance(getApplicationContext()).logout();
        Intent intent = new Intent(RestaurantActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish the current activity to remove it from the back stack
    }



    /**
     * Fetch data for ListView
     */
    private void updateListView() {
        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get book service instance
        orderService = ApiUtils.getOrderService();

        // execute the call. send the user token when sending the query
        orderService.getOrder(user.getToken()).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // token is not valid/expired
                if (response.code() == 401) {
                    displayAlert("Session Invalid");
                }

                // Get list of book object from response
                List<Order> orders = response.body();

                // initialize adapter
                adapter = new RestaurantAdapter(context, orders);

                // set adapter to the RecyclerView
                orderList.setAdapter(adapter);

                // set layout to recycler view
                orderList.setLayoutManager(new LinearLayoutManager(context));

                // add separator between item in the list
                DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderList.getContext(),
                        DividerItemDecoration.VERTICAL);
                orderList.addItemDecoration(dividerItemDecoration);
            }



            @Override
            public void onFailure(Call<List<Order>> call, Throwable t) {
                Toast.makeText(context, "Error connecting to the server", Toast.LENGTH_LONG).show();
                displayAlert("Error [" + t.getMessage() + "]");
                Log.e("MyApp:", t.getMessage());
            }
        });
    }

    /**
     * Displaying an alert dialog with a single button
     * @param message - message to be displayed
     */
    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

