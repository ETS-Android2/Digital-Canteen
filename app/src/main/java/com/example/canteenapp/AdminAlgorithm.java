package com.example.canteenapp;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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

public class AdminAlgorithm extends AppCompatActivity {
  FirebaseDatabase firebaseDatabase;
  DatabaseReference databaseReferenceMasterRecord;


  private static List<AlgoData> listOfData = new ArrayList<>();
  private static List<AlgoData> listOfDailyData = new ArrayList<>();
  private static List<AlgoData> listOfWeeklyData = new ArrayList<>();
  private static List<AlgoData> listOfMonthlyData = new ArrayList<>();
  private static List<AlgoData> listOfSpringData = new ArrayList<>();
  private static List<AlgoData> listOfSummerData = new ArrayList<>();
  private static List<AlgoData> listOfFallData = new ArrayList<>();
  private static List<AlgoData> listOfWinterData = new ArrayList<>();
  private static List<AlgoData> listOfYearData = new ArrayList<>();
  private static Map<String, Integer> mapOfDailyData = new HashMap<>();
  private static Map<String, Integer> mapOfWeeklyData = new HashMap<>();
  private static Map<String, Integer> mapOfMonthlyData = new HashMap<>();
  private static Map<String, Integer> mapOfYearlyData = new HashMap<>();
  private static Map<String, Integer> mapOfSummerData = new HashMap<>();
  private static Map<String, Integer> mapOfSpringData = new HashMap<>();
  private static Map<String, Integer> mapOfFallData = new HashMap<>();
  private static Map<String, Integer> mapOfWinterData = new HashMap<>();


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.admin_algorithm);
    firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReferenceMasterRecord = firebaseDatabase.getReference().child(FireBaseConstant.MASTER_RECORD);


  }

  @Override
  protected void onStart() {
    super.onStart();
    databaseReferenceMasterRecord.addListenerForSingleValueEvent(new ValueEventListener() {
      @RequiresApi(api = Build.VERSION_CODES.N)
      @Override
      public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (listOfData.isEmpty()) {
          for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            String[] foodCountArray = dataSnapshot.child("foodCount").getValue().toString().replaceFirst("\n", "").split("\n");
            String[] foodNameArray = dataSnapshot.child("foodName").getValue().toString().split("\n");

            if (foodCountArray.length == 1) {
              foodNameArray[0] = foodNameArray[0].trim();
              AlgoData algoData = new AlgoData();
              algoData.setFoodName(foodNameArray[0]);
              algoData.setFoodCount(Integer.parseInt(foodCountArray[0]));
              algoData.setTime(Long.parseLong(dataSnapshot.child("time").getValue().toString()));
              listOfData.add(algoData);
            }
            for (int i = 0; i < foodCountArray.length; i++) {
              foodNameArray[i] = foodNameArray[i].trim();
              AlgoData algoData = new AlgoData();
              algoData.setFoodName(foodNameArray[i]);
              algoData.setFoodCount(Integer.parseInt(foodCountArray[i]));
              algoData.setTime(Long.parseLong(dataSnapshot.child("time").getValue().toString()));
              listOfData.add(algoData);
            }
          }
        }
        if (!listOfData.isEmpty()) {
          for (AlgoData algoData : listOfData) {
            if (CanteenUtil.isThisDay(algoData.time)) {
              listOfDailyData.add(algoData);
            }
            if (CanteenUtil.isThisWeek(algoData.time)) {
              listOfWeeklyData.add(algoData);
            }
            if (CanteenUtil.isThisMonth(algoData.time)) {
              listOfMonthlyData.add(algoData);
            }
            if (CanteenUtil.isThisYear(algoData.time)) {
              listOfYearData.add(algoData);
            }
            if (CanteenUtil.isThisSpring(algoData.time)) {
              listOfSpringData.add(algoData);
            }
            if (CanteenUtil.isThisSummer(algoData.time)) {
              listOfSummerData.add(algoData);
            }
            if (CanteenUtil.isThisFall(algoData.time)) {
              listOfFallData.add(algoData);
            }
            if (CanteenUtil.isThisWinter(algoData.time)) {
              listOfWinterData.add(algoData);
            }
          }
          //TODO:to implement what to do with data

          if (!listOfDailyData.isEmpty()) {
            for (AlgoData algoData : listOfDailyData) {
              mapOfDailyData.putIfAbsent(algoData.getFoodName(), algoData.getFoodCount());
              if (mapOfDailyData.containsKey(algoData.getFoodName())) {
                mapOfDailyData.put(algoData.foodName, mapOfDailyData.get(algoData.getFoodName()) + algoData.getFoodCount());
              }
            }
          }
          if (!listOfWeeklyData.isEmpty()) {
            for (AlgoData algoData : listOfWeeklyData) {
              mapOfWeeklyData.putIfAbsent(algoData.getFoodName(), algoData.getFoodCount());
              if (mapOfWeeklyData.containsKey(algoData.getFoodName())) {
                mapOfWeeklyData.put(algoData.foodName, mapOfWeeklyData.get(algoData.getFoodName()) + algoData.getFoodCount());
              }
            }
          }
          if (!listOfMonthlyData.isEmpty()) {
            for (AlgoData algoData : listOfMonthlyData) {
              mapOfMonthlyData.putIfAbsent(algoData.getFoodName(), algoData.getFoodCount());
              if (mapOfMonthlyData.containsKey(algoData.getFoodName())) {
                mapOfMonthlyData.put(algoData.foodName, mapOfMonthlyData.get(algoData.getFoodName()) + algoData.getFoodCount());
              }
            }
          }
          if (!listOfYearData.isEmpty()) {
            for (AlgoData algoData : listOfYearData) {
              mapOfYearlyData.putIfAbsent(algoData.getFoodName(), algoData.getFoodCount());
              if (mapOfYearlyData.containsKey(algoData.getFoodName())) {
                mapOfYearlyData.put(algoData.foodName, mapOfYearlyData.get(algoData.getFoodName()) + algoData.getFoodCount());
              }
            }
          }
          if (!listOfSummerData.isEmpty()) {
            for (AlgoData algoData : listOfSummerData) {
              mapOfSummerData.putIfAbsent(algoData.getFoodName(), algoData.getFoodCount());
              if (mapOfSummerData.containsKey(algoData.getFoodName())) {
                mapOfSummerData.put(algoData.foodName, mapOfSummerData.get(algoData.getFoodName()) + algoData.getFoodCount());
              }
            }
          }
          if (!listOfSpringData.isEmpty()) {
            for (AlgoData algoData : listOfSpringData) {
              mapOfSpringData.putIfAbsent(algoData.getFoodName(), algoData.getFoodCount());
              if (mapOfSpringData.containsKey(algoData.getFoodName())) {
                mapOfSpringData.put(algoData.foodName, mapOfSpringData.get(algoData.getFoodName()) + algoData.getFoodCount());
              }
            }
          }
          if (!listOfFallData.isEmpty()) {
            for (AlgoData algoData : listOfFallData) {
              mapOfFallData.putIfAbsent(algoData.getFoodName(), algoData.getFoodCount());
              if (mapOfFallData.containsKey(algoData.getFoodName())) {
                mapOfFallData.put(algoData.foodName, mapOfFallData.get(algoData.getFoodName()) + algoData.getFoodCount());
              }
            }
          }
          if (!listOfWinterData.isEmpty()) {
            for (AlgoData algoData : listOfWinterData) {
              mapOfWinterData.putIfAbsent(algoData.getFoodName(), algoData.getFoodCount());
              if (mapOfWinterData.containsKey(algoData.getFoodName())) {
                mapOfWinterData.put(algoData.foodName, mapOfWinterData.get(algoData.getFoodName()) + algoData.getFoodCount());
              }
            }
          }
        }
      }

      @Override
      public void onCancelled(@NonNull DatabaseError error) {

      }
    });

  }
}
