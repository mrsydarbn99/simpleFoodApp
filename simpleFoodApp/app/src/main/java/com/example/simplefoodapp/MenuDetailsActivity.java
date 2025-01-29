package com.example.simplefoodapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.simplefoodapp.model.Order;
import com.example.simplefoodapp.model.SharedPrefManager;
import com.example.simplefoodapp.model.User;
import com.example.simplefoodapp.remote.ApiUtils;
import com.example.simplefoodapp.remote.MenuOrderService;
import com.example.simplefoodapp.remote.MenuService;
import com.example.simplefoodapp.model.Menu;
import com.example.simplefoodapp.model.MenuOrder;
import com.example.simplefoodapp.remote.OrderService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuDetailsActivity extends AppCompatActivity {

    MenuService menuService;


    private EditText etRemark;
    private EditText etQuantity;
    private TextView tvPriceVal;

    private TextView tvMenuName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);

        // Add this line to initialize etQuantity
        etQuantity = findViewById(R.id.etQuantity);
        etRemark = findViewById(R.id.etRemark);
        tvPriceVal = findViewById(R.id.tvPriceVal);
        tvMenuName = findViewById(R.id.tvMenuName);

        final Intent intent = getIntent();
        final int MenuID = intent.getIntExtra("id", -1);

        // get user info from SharedPreferences
        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        // get book service instance
        menuService = ApiUtils.getMenuService();

        // execute the API query. send the token and book id
        menuService.getMenu(user.getToken(), MenuID).enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                // check if the response is successful and the body is not null
                if (response.isSuccessful() && response.body() != null) {
                    // get the menu object from the response
                    Menu menu = response.body();

                    // get references to the view elements
                    TextView tvItemName = findViewById(R.id.tvMenuName);
                    TextView tvItemPrice = findViewById(R.id.tvPriceVal);
                    TextView tvDesc = findViewById(R.id.tvDescVal);

                    // set values
                    tvItemName.setText(menu.getItemName());
                    tvItemPrice.setText("RM " + menu.getItemPrice());
                    tvDesc.setText(menu.getItemDescription());
                } else {
                    // Handle unsuccessful response here
                    // For example, show an error message or log the issue
                    Log.e("MyApp:", "Unsuccessful response: " + response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });


        // Enable back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Button click event for "Add to Cart"

        Button btnAddToCart = findViewById(R.id.btnAddToCart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get quantity and price values
                String quantityString = etQuantity.getText().toString();
                String priceString = tvPriceVal.getText().toString();
                String remarkString = etRemark.getText().toString();

                // Extract the numerical part of the price string (remove "RM " prefix)
                String numericPriceString = priceString.replace("RM ", "");

                try {
                    // Convert quantity and numeric price to appropriate data types (e.g., int, double)
                    int quantity = Integer.parseInt(quantityString);
                    double price = Double.parseDouble(numericPriceString);
                    double total = quantity*price;
                    User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();


                    OrderService orderService = ApiUtils.getOrderService();
                    Call<Order> call = orderService.addOrder(user.getToken(), user.getId(), total, quantity, remarkString);

// Execute
                    call.enqueue(new Callback<Order>() {
                        @Override
                        public void onResponse(Call<Order> call, Response<Order> response) {
                            Order addedOrder = response.body();
                            if (addedOrder != null) {
                                int orderId = addedOrder.getOrderId();
                                Log.d("MyApp", "Order added successfully. Order ID: " + orderId);
                                addMenuOrder(user.getId(),orderId);
                                // Now you can use the orderId as needed
                            } else {
                                Log.e("MyApp", "Response body is null");
                            }
                        }

                        @Override
                        public void onFailure(Call<Order> call, Throwable t) {
                            Log.e("MyApp", "Error adding menu: " + t.getMessage());
                        }
                    });



                    // Get the menu name
                    String menuName = tvMenuName.getText().toString();

                    Log.d("MyApp", "MenuID: " + MenuID);
                    Log.d("MyApp", "Quantity: " + quantity);
                    Log.d("MyApp", "Price: " + price);
                    Log.d("MyApp", "MenuName: " + menuName);

                    // Create an Intent to start CartActivity
                    Intent intent = new Intent(MenuDetailsActivity.this, CartActivity.class);

                    // Pass data to CartActivity using Intent.putExtra()
                    intent.putExtra("id", MenuID);
                    intent.putExtra("quantity", quantity);
                    intent.putExtra("price", price);
                    intent.putExtra("menuName", menuName);
                    intent.putExtra("remark", remarkString);
                    Log.d("MyApp", "hehe: ada yang betul");

                    // Start CartActivity
                    startActivity(intent);

                } catch (NumberFormatException e) {
                    // Handle the exception, e.g., show an error message
                    Toast.makeText(MenuDetailsActivity.this, "Invalid quantity or price format", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        });
    }

    public void addMenuOrder(int userId, int orderId){

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();
        MenuOrderService menuOrderService = ApiUtils.getMenuOrderService();
        Call<MenuOrder> call = menuOrderService.addMenuOrder(user.getToken(), userId, orderId);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Terminate this Activity and go back to the caller
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
