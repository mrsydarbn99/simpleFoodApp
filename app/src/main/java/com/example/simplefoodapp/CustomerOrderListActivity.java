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

import com.example.simplefoodapp.adapter.OrderListAdapter;
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

public class CustomerOrderListActivity extends AppCompatActivity {

    OrderService orderService;

    Context context;

    RecyclerView orderList;

    OrderListAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_list);

        context = this; // get current activity context

        // get reference to the RecyclerView orderList
        orderList = findViewById(R.id.orderList);

        // register for context menu
        super.registerForContextMenu(orderList);

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get order service instance
        orderService = ApiUtils.getOrderService();
        adapter = new OrderListAdapter(context, new ArrayList<>());


        orderService.getOrderById(user.getToken(), user.getId()).enqueue(new Callback<List<Order>>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());


                List<Order> orders = response.body();

                // initialize an empty adapter
                OrderListAdapter adapter = new OrderListAdapter(context, orders);

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
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
    // call the original method in superclass
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
    // use our XML file for the menu items
        inflater.inflate(R.menu.order_context_menu, menu);
    // set menu title - optional
        menu.setHeaderTitle("Select the action:");
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
                adapter = new OrderListAdapter(context, orders);

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
     *
     */
    private void deleteOrder(Order selectedOrder) {
        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // prepare REST API call
        OrderService orderService = ApiUtils.getOrderService();
        Call<DeleteResponse> call = orderService.deleteOrder(user.getToken(), selectedOrder.getOrderId());

        // execute the call
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.code() == 200) {
                    // 200 means OK
                    displayAlert("Book successfully deleted");
                    // update data in list view
                    updateListView();
                } else {
                    displayAlert("Book failed to delete");
                    Log.e("MyApp:", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
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


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Handle the delete action here
        if (item.getItemId() == R.id.delete) {
            // Get the selected order from the adapter
            Order selectedOrder = adapter.getSelectedItem();

            // Check the status of the selected order
            if (selectedOrder != null && selectedOrder.getStatus() == 0) {
                // Status is 0 (New), allow deletion
                deleteOrder(selectedOrder);
            } else {
                // Status is not 0, show a toast indicating that the order cannot be deleted
                Toast.makeText(this, "Order cannot be deleted.", Toast.LENGTH_SHORT).show();
            }

            return true;
        }
        return super.onContextItemSelected(item);
    }
}

