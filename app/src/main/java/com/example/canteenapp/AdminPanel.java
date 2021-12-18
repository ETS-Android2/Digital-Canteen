package com.example.canteenapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.canteenapp.FoodList.Food;
import com.example.canteenapp.History.itemHistory;
import com.example.canteenapp.OrderList.OrderIteam;
import com.example.canteenapp.Registration.Registration;

public class AdminPanel extends AppCompatActivity {
  ImageView addfood,order,adduser,history;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.admin_panel);
    addfood = findViewById(R.id.addfood);
    order = findViewById(R.id.order);
    adduser = findViewById(R.id.adduser);
    history = findViewById(R.id.history);

    addfood.setOnClickListener((v)->{
      Intent intent = new Intent(getApplicationContext(), Food.class);
      startActivity(intent);
    });
    order.setOnClickListener((v)->{
      Intent intent = new Intent(getApplicationContext(), OrderIteam.class);
      startActivity(intent);
    });
    adduser.setOnClickListener((v)->{
      Intent intent = new Intent(getApplicationContext(), Registration.class);
      startActivity(intent);
    });
    history.setOnClickListener((v)->{
      Intent intent = new Intent(getApplicationContext(), itemHistory.class);
      startActivity(intent);
    });
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
  }
}
