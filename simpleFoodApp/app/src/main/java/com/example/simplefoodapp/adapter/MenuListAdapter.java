package com.example.simplefoodapp.adapter;

import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.simplefoodapp.MenuDetailsActivity;
import com.example.simplefoodapp.R;
import com.example.simplefoodapp.model.Menu;

import java.util.List;
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.ViewHolder> {
    /**
     * Create ViewHolder class to bind list item view
     */
    class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvMenu;
        public TextView tvPrice;
        public TextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            tvMenu = itemView.findViewById(R.id.tvMenu);
            tvDescription = itemView.findViewById(R.id.tvDesc);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }

    private List<Menu> mListData;   // list of book objects
    private Context mContext;       // activity context

    public MenuListAdapter(Context context, List<Menu> listData){
        mListData = listData;
        mContext = context;
    }

    private Context getmContext(){return mContext;}


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the single item layout
        View view = inflater.inflate(R.layout.item_listview, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // bind data to the view holder
        Menu m = mListData.get(position);
        holder.tvMenu.setText(String.valueOf(m.getItemName()));
        holder.tvDescription.setText(String.valueOf(m.getItemDescription()));
        holder.tvPrice.setText(String.valueOf(m.getItemPrice()));

        // Set click listener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle item click here
                Intent intent = new Intent(mContext, MenuDetailsActivity.class);
                intent.putExtra("id", m.getMenuID()); // Replace with your actual method to get book ID
                mContext.startActivity(intent);

                // Debug: Log the clicked MenuID
                Log.d("MyApp", "Clicked MenuID: " + m.getMenuID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData != null ? mListData.size() : 0;
    }
}