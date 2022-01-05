package com.example.canteenapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.canteenapp.FoodList.Food;
import com.example.canteenapp.History.HistoryModel;
import com.example.canteenapp.History.itemHistory;
import com.example.canteenapp.OrderList.OrderIteam;
import com.example.canteenapp.Registration.Registration;
import com.example.canteenapp.Util.CanteenUtil;
import com.example.canteenapp.constant.CanteenConstant;
import com.example.canteenapp.constant.FireBaseConstant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AdminPanel extends AppCompatActivity {
  CardView viewOderCard, addUserCard, addFoodCard, viewLogCard, viewAnalysisCard;
  DatabaseReference databaseReference;
  FirebaseDatabase firebaseDatabase;
  TextView adminPanelTotalAmount;
  int netIncome = 0;
  private static final DecimalFormat df = new DecimalFormat("0.00");

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.admin_panel);
    viewOderCard = findViewById(R.id.viewOrderLogoCard);
    addUserCard = findViewById(R.id.addUserLogoCard);
    addFoodCard = findViewById(R.id.addFoodLogoCard);
    viewLogCard = findViewById(R.id.logLogoCard);
    viewAnalysisCard = findViewById(R.id.analysisLogoCard);
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference(FireBaseConstant.HISTORY);
    adminPanelTotalAmount = findViewById(R.id.adminPanelTotalAmount);


    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.getValue() != null) {
          netIncome = 0;
          for (DataSnapshot s : snapshot.getChildren()) {
            if (CanteenUtil.getYearAndDayFromMilliSecond(System.currentTimeMillis()).equals(CanteenUtil.getYearAndDayFromMilliSecond(Long.parseLong(s.child("time").getValue().toString())))) {
              HistoryModel historyModel = s.getValue(HistoryModel.class);
              if (null != historyModel && historyModel.getComment().contains(CanteenConstant.ADMIN)) {
                netIncome = netIncome + Integer.parseInt(historyModel.getTotal());
              }
            } else {
              databaseReference.child(s.getKey()).removeValue();
            }
          }
          adminPanelTotalAmount.setText("Rs."+NumberFormat.getInstance().format(Float.parseFloat(df.format(netIncome))));
          databaseReference.removeEventListener(this);
        }
        databaseReference.removeEventListener(this);
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {
        databaseReference.removeEventListener(this);
      }
    });

    viewOderCard.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), OrderIteam.class);
      startActivity(intent);
    });
    addUserCard.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), Registration.class);
      startActivity(intent);
    });
    addFoodCard.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), Food.class);
      startActivity(intent);
    });
    viewLogCard.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), itemHistory.class);
      startActivity(intent);
    });
    viewAnalysisCard.setOnClickListener(v -> {
      Intent intent = new Intent(getApplicationContext(), AdminFilterPage.class);
      startActivity(intent);
    });


  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    startActivity(intent);

  }
}
