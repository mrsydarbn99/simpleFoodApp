package com.example.simplefoodapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefoodapp.R;
import com.example.simplefoodapp.RestaurantActivity;
import com.example.simplefoodapp.model.Order;
import com.example.simplefoodapp.model.SharedPrefManager;
import com.example.simplefoodapp.model.User;
import com.example.simplefoodapp.remote.ApiUtils;
import com.example.simplefoodapp.remote.OrderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.OrderViewHolder> {

    private final List<Order> rOrderList;
    private final Context context;

    public RestaurantAdapter(Context context, List<Order> rOrderList) {
        this.context = context;
        this.rOrderList = rOrderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurantorderview, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = rOrderList.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return rOrderList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        private final TextView orderIdTextView;
        private final TextView orderPriceTextView;
        private final TextView orderStatusTextView;
        private final Button btnPreparing;
        private final Button btnReady;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdTextView = itemView.findViewById(R.id.tvIdOrder);
            orderPriceTextView = itemView.findViewById(R.id.tvPriceOrder);
            orderStatusTextView = itemView.findViewById(R.id.tvStatusOrder);
            btnPreparing = itemView.findViewById(R.id.btnPreparing);
            btnReady = itemView.findViewById(R.id.btnReady);
            // Set click listeners
            btnPreparing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    preparingButtonClick(getAdapterPosition());
                }
            });

            btnReady.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    readyButtonClick(getAdapterPosition());
                }
            });

        }

        public void bind(Order order) {
            orderIdTextView.setText(String.valueOf(order.getOrderId()));
            orderPriceTextView.setText(String.valueOf(order.getTotalAmount()));

            int status = order.getStatus();
            String orderStatus;
            if (status == 0) {
                orderStatus = "New";
            } else if (status == 1) {
                orderStatus = "Preparing";
            } else {
                orderStatus = "Ready";
            }
            orderStatusTextView.setText(orderStatus);
        }

        private void preparingButtonClick(int position) {
            // get user info from SharedPreferences
            User user = SharedPrefManager.getInstance(context).getUser();
            // prepare REST API call
            OrderService orderService = ApiUtils.getOrderService();
            Order order = rOrderList.get(position); // Assuming mListData is the list of orders

            Call<Order> call = orderService.prepareOrder(user.getToken(), order.getOrderId());

            // execute the call
            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful()) {
                        // 200 means OK
                        Toast.makeText(context, "Order successfully prepared", Toast.LENGTH_SHORT).show();
                        // Update the order in the list with the response
                        Order updatedOrder = response.body();
                        if (updatedOrder != null) {
                            rOrderList.set(position, updatedOrder);
                            // Notify the adapter that the dataset has changed after update
                            notifyItemChanged(position);
                            // Navigate back to RestaurantActivity
                            navigateBackToRestaurantActivity();
                        }
                    } else {
                        Toast.makeText(context, "Order preparation failed", Toast.LENGTH_SHORT).show();
                        Log.e("MyApp:", response.raw().toString());
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast.makeText(context, "Error preparing order: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MyApp:", t.getMessage());
                }
            });
        }

        private void readyButtonClick(int position) {
            // get user info from SharedPreferences
            User user = SharedPrefManager.getInstance(context).getUser();
            // prepare REST API call
            OrderService orderService = ApiUtils.getOrderService();
            Order order = rOrderList.get(position); // Assuming mListData is the list of orders

            Call<Order> call = orderService.readyOrder(user.getToken(), order.getOrderId());

            // execute the call
            call.enqueue(new Callback<Order>() {
                @Override
                public void onResponse(Call<Order> call, Response<Order> response) {
                    if (response.isSuccessful()) {
                        // 200 means OK
                        Toast.makeText(context, "Order successfully ready", Toast.LENGTH_SHORT).show();
                        // Update the order in the list with the response
                        Order updatedOrder = response.body();
                        if (updatedOrder != null) {
                            rOrderList.set(position, updatedOrder);
                            // Notify the adapter that the dataset has changed after update
                            notifyItemChanged(position);
                            // Navigate back to RestaurantActivity
                            navigateBackToRestaurantActivity();
                        }
                    } else {
                        Toast.makeText(context, "Order preparation failed", Toast.LENGTH_SHORT).show();
                        Log.e("MyApp:", response.raw().toString());
                    }
                }

                @Override
                public void onFailure(Call<Order> call, Throwable t) {
                    Toast.makeText(context, "Error preparing order: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("MyApp:", t.getMessage());
                }
            });
        }
        private void navigateBackToRestaurantActivity() {
            Intent intent = new Intent(context, RestaurantActivity.class);
            context.startActivity(intent);
            // If you want to finish the current activity (remove it from the back stack)
            // ((YourCurrentActivity) context).finish();
        }
    }
}
