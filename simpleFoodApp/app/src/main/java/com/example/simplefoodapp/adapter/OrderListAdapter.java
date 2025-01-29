package com.example.simplefoodapp.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefoodapp.MenuDetailsActivity;
import com.example.simplefoodapp.R;
import com.example.simplefoodapp.model.DeleteResponse;
import com.example.simplefoodapp.model.Order;
import com.example.simplefoodapp.model.SharedPrefManager;
import com.example.simplefoodapp.model.User;
import com.example.simplefoodapp.remote.ApiUtils;
import com.example.simplefoodapp.remote.OrderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderListAdapter extends RecyclerView.Adapter<com.example.simplefoodapp.adapter.OrderListAdapter.ViewHolder> {
    /**
     * Create ViewHolder class to bind list item view
     */

    private Order selectedItem;
    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvOrderId;
        public TextView tvOrderStatus;

        public TextView tvOrderPrice;
        public TextView tvUserId;

        public Button btnDelete;



        public ViewHolder(View itemView) {
            super(itemView);

            tvOrderId = itemView.findViewById(R.id.tvIdOrder);
            tvOrderStatus = itemView.findViewById(R.id.tvStatusOrder);
            tvOrderPrice = itemView.findViewById(R.id.tvPriceOrder);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void setSelectedItem(Order order) {
        selectedItem = order;
    }

    public Order getSelectedItem() {
        return selectedItem;
    }

    private List<Order> mListData;   // list of book objects
    private Context mContext;       // activity context

    public OrderListAdapter(Context context, List<Order> listData){
        mListData = listData;
        mContext = context;
    }


    private Context getmContext(){return mContext;}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the single item layout
        View view = inflater.inflate(R.layout.item_orderview, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // bind data to the view holder
        Order order = mListData.get(position);
        holder.tvOrderId.setText(String.valueOf(order.getOrderId()));
        holder.tvOrderPrice.setText(String.valueOf(order.getTotalAmount()));


        int status = order.getStatus();
        String orderStatus = null;
        if (status == 0) {
            orderStatus = "New";
        } else if (status == 1) {
            orderStatus = "Preparing";
        } else orderStatus = "Ready";


        holder.tvOrderStatus.setText(orderStatus);

        // Set long press listener
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle the button click here
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Order order = mListData.get(position);
                    deleteOrder(order, position);
                }
            }
        });
    }


    private void deleteOrder(Order order, int position) {
        // Check if the order status is 0 (New) before allowing deletion
        if (order.getStatus() == 0) {
            // get user info from SharedPreferences
            User user = SharedPrefManager.getInstance(mContext).getUser();

            // prepare REST API call
            OrderService orderService = ApiUtils.getOrderService();
            Call<DeleteResponse> call = orderService.deleteOrder(user.getToken(), order.getOrderId());

            // execute the call
            call.enqueue(new Callback<DeleteResponse>() {
                @Override
                public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                    if (response.code() == 200) {
                        // 200 means OK
                        Toast.makeText(mContext, "Order successfully deleted", Toast.LENGTH_SHORT).show();
                        // Remove the deleted order from the list
                        mListData.remove(position);
                        // Notify the adapter that the dataset has changed after deletion
                        notifyItemRemoved(position);
                    } else {
                        Toast.makeText(mContext, "Order failed to delete", Toast.LENGTH_SHORT).show();
                        Log.e("MyApp:", response.raw().toString());
                    }
                }

                @Override
                public void onFailure(Call<DeleteResponse> call, Throwable t) {
                    // Handle failure, e.g., display an error message
                    Log.e("MyApp:", t.getMessage());
                }
            });
        } else {
            // Status is not 0, show a toast indicating that the order cannot be deleted
            Toast.makeText(mContext, "Order cannot be canceled", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public int getItemCount() {
        return mListData.size();
    }

}
