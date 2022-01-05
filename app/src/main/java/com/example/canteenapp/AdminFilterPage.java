package com.example.canteenapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.canteenapp.Util.CanteenUtil;
import com.example.canteenapp.constant.FireBaseConstant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminFilterPage extends AppCompatActivity {
  DatabaseReference databaseReferenceMasterRecord;
  FirebaseDatabase firebaseDatabase;
  static List<DataEntry> foodNameAndCount = new ArrayList<>();
  static Map<String, Integer> map = new HashMap<>();
  AnyChartView anyChartView;
  private static int LIST_ID = 01;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.admin_filter_page);
    anyChartView = findViewById(R.id.any_chart_view);

    anyChartView.setProgressBar(findViewById(R.id.progressBarAdminFilter));
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReferenceMasterRecord = firebaseDatabase.getReference().child(FireBaseConstant.MASTER_RECORD);
    databaseReferenceMasterRecord.addListenerForSingleValueEvent(new ValueEventListener() {
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (map.isEmpty()) {
          for (DataSnapshot innerSnapShot : snapshot.getChildren()) {

            if (CanteenUtil.getYearFromMilliSecond(Long.parseLong(innerSnapShot.child("time").getValue().toString())) == CanteenUtil.getYearFromMilliSecond(System.currentTimeMillis())) {
              String[] foodCount = innerSnapShot.child("foodCount").getValue().toString().replaceFirst("\n", "").split("\n");
              String[] foodName = innerSnapShot.child("foodName").getValue().toString().split("\n");
              if (foodCount.length == 1) {
                foodName[0] = foodName[0].trim();
                if (map.containsKey(foodName[0])) {
                  map.put(foodName[0], map.get(foodName[0]) + Integer.parseInt(foodCount[0]));
                }
                if (!map.containsKey(foodName[0])) {
                  map.put(foodName[0], Integer.parseInt(foodCount[0]));
                }
              }
              for (int i = 0; i < foodCount.length - 1; i++) {
                foodName[i] = foodName[i].trim();
                if (map.containsKey(foodName[i])) {
                  map.put(foodName[i], map.get(foodName[i]) + Integer.parseInt(foodCount[i]));
                }
                if (!map.containsKey(foodName[i])) {
                  map.put(foodName[i], Integer.parseInt(foodCount[i]));
                }

              }
            }
          }
        }


        if (!map.isEmpty()) {
          if (LIST_ID == 01) {
            for (Map.Entry<String, Integer> set : map.entrySet()) {
              foodNameAndCount.add(new ValueDataEntry(set.getKey(), set.getValue()));
            }
            LIST_ID = 02;
            setUpAnyChart();
          }
        }

      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });


  }

  private void setUpAnyChart() {
    Pie pie = AnyChart.pie();
    pie.data(foodNameAndCount);
    pie.animation(true, 2000);
    pie.title("Food sold in year " + CanteenUtil.getYearFromMilliSecond(System.currentTimeMillis()));
    pie.background("#fafafa");
    pie.legend()
            .background("#fafafa")
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL)
            .align(Align.CENTER);
    anyChartView.setChart(pie);

  }


  @Override
  public void onBackPressed() {
    super.onBackPressed();
    finishAffinity();
    LIST_ID = 01;
    foodNameAndCount = new ArrayList<>();
    Intent intent = new Intent(getApplicationContext(), AdminPanel.class);
    startActivity(intent);
  }
}