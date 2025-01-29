package com.example.simplefoodapp;


import android.content.Intent;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class CartActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Initialize intent
        intent = getIntent();

        // Retrieve data from Intent
        int menuId = intent.getIntExtra("id", -1);
        int quantity = intent.getIntExtra("quantity", 0);
        double price = intent.getDoubleExtra("price", 0.0);
        String menuName = intent.getStringExtra("menuName");
        double total = quantity * price;
        String remark = intent.getStringExtra("remark");

        // get references to the view elements
        //TextView tvAddMenuID = findViewById(R.id.tvAddMenuID);
        TextView tvAddQty = findViewById(R.id.tvAddQty);
        TextView tvAddPrice = findViewById(R.id.tvAddPrice);
        TextView tvAddMenuName = findViewById(R.id.tvAddMenuName);
        TextView tvAddTotalPrice = findViewById(R.id.tvAddTotalPrice);
        TextView tvRemark = findViewById(R.id.tvRemark);

        //tvAddMenuID.setText(String.valueOf(menuId)); // Convert to String
        tvAddQty.setText(String.valueOf(quantity)); // Convert to String
        tvAddPrice.setText("RM " + String.valueOf(price));
        tvAddMenuName.setText(menuName);

        tvAddTotalPrice.setText("RM " + String.valueOf(total));
        tvRemark.setText(remark);

        // Now you have the menuId, quantity, price, and menuName in CartActivity
        // TODO: Use this data as needed in your CartActivity logic





    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Terminate this Activity and go back to the caller
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void status(View view){

        startActivity(new Intent(getApplicationContext(), CustomerOrderListActivity.class));
    }
}

