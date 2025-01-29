package com.example.simplefoodapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simplefoodapp.adapter.MenuListAdapter;
import com.example.simplefoodapp.model.Menu;
//import com.example.simplefoodapp.model.Order;
import com.example.simplefoodapp.model.SharedPrefManager;
import com.example.simplefoodapp.model.User;
import com.example.simplefoodapp.remote.ApiUtils;
import com.example.simplefoodapp.remote.MenuService;
//import com.example.simplefoodapp.remote.OrderService;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    MenuService menuService;
    Context context;
    RecyclerView menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = SharedPrefManager.getInstance(getApplicationContext()).getUser();

        TextView tvHello = findViewById(R.id.tvHello);

        // Set the text to display the user's name
        if (user != null) {
            String welcomeMessage = "Hello, " + user.getRole() + " " + user.getUsername() + "!";
            tvHello.setText(welcomeMessage);
        }


            context = this; // get current activity context

            // get reference to the RecyclerView bookList
            menuList = findViewById(R.id.menuList);

            //register for context menu
            registerForContextMenu(menuList);

            // get book service instance
            menuService = ApiUtils.getMenuService();

            // execute the call. send the user token when sending the query
            menuService.getAllMenus(user.getToken()).enqueue(new Callback<List<Menu>>() {
                @Override
                public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                    // for debug purpose
                    Log.d("MyApp:", "Response: " + response.raw().toString());

                    // Get list of book object from response
                    List<Menu> menus = response.body();

                    // initialize adapter
                    MenuListAdapter adapter = new MenuListAdapter(context, menus);

                    // set adapter to the RecyclerView
                    menuList.setAdapter(adapter);

                    // set layout to recycler view
                    menuList.setLayoutManager(new LinearLayoutManager(context));

                    // add separator between item in the list
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(menuList.getContext(),
                            DividerItemDecoration.VERTICAL);
                    menuList.addItemDecoration(dividerItemDecoration);
                }

                @Override
                public void onFailure(Call<List<Menu>> call, Throwable t) {
                    Toast.makeText(context, "Error connecting to the server", Toast.LENGTH_LONG).show();
                    Log.e("MyApp:", t.getMessage());
                }
            });




    }

    public void doLogout(View view) {
        // clear the shared preferences
        SharedPrefManager.getInstance(getApplicationContext()).logout();
        // display message
        Toast.makeText(getApplicationContext(),
                "You have successfully logged out.",
                Toast.LENGTH_LONG).show();
        // forward to LoginActivity
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    public void check(View view){

        startActivity(new Intent(getApplicationContext(), CustomerOrderListActivity.class));
    }
}