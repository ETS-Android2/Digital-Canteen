package com.example.canteenapp.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.canteenapp.FoodList.FoodModel;
import com.example.canteenapp.FoodList.FoodVIewHOlder;
import com.example.canteenapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class IteamHistory extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    TextView netincomee;
    DatabaseReference databaseReference,databaseReference2;
    RecyclerView recyclerViewhistory;
    String color=null,content;
    Button resethistory;
    FirebaseDatabase firebaseDatabase1;
    DatabaseReference databaseReference1,getDatabaseReference2;
    int netincome=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteam_history);
        resethistory = findViewById(R.id.resethistory);
        netincomee = findViewById(R.id.netincome);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("History");
        firebaseDatabase1 = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase1.getReference("History");
        recyclerViewhistory =(RecyclerView) findViewById(R.id.recycleviewhistory);
        databaseReference2 =  firebaseDatabase.getReference("Todayshits");
        recyclerViewhistory.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewhistory.setLayoutManager(layoutManager);
        resethistory.setOnClickListener(view -> {
            databaseReference.removeValue();
            databaseReference2.removeValue();
           netincome=0;
           netincomee.setText(String.valueOf(netincome));
        });
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                    String[] b;
                    int count=0;
                    netincome=0;
                    content = snapshot.getValue().toString();
                    String ashish = content.replaceAll("\n",",");
                    String a = ashish.replaceAll("=,","=");
                    Log.d("fssssssssssssssssssss", "onCreate: "+ashish);
                    a = a.replaceAll(",", ",");
                    a = a.replaceAll(" ", "");
                    a = a.replaceAll("\\{", "");
                    a = a.replaceAll(",\"\\{" , "");
                    a = a.replaceAll("\\}\"," , "");
                    a = a.replaceAll("\\}," , ",");
                    a = a.replaceAll("\\[" , "");
                    a = a.replaceAll("\\]" , "");
                    a = a.replaceAll("\\}" , "");
                    b = a.split(",");
                    System.out.println(a);
                    for(String i:b) {
                        if(b[count].contains("ADMIN")) {
                            for(int j=count;j>=0;j--) {
                                if(b[j].contains("total")) {
                                    System.out.println(b[j]);
                                    netincome = netincome+Integer.parseInt(b[j].substring(b[j].indexOf("=")+1));
                                    Log.d("netincomeeeeeeeeeee", "onDataChange: "+netincome);
                                    System.out.println(b[j].substring(b[j].indexOf("=")+1));
                                    break;
                                }
                            }
                        }
                        count++;
                    }

                    netincomee.setText(String.valueOf(netincome));
                    Log.d("fdfdfdfdf", "onDataChange: "+netincome);
                    databaseReference1.removeEventListener(this);
                }
                databaseReference1.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                databaseReference1.removeEventListener(this);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<HistoryModel>().setQuery(databaseReference,HistoryModel.class).build();
        final FirebaseRecyclerAdapter<HistoryModel, HistoryViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<HistoryModel, HistoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryViewHolder historyViewHolder, int i, @NonNull HistoryModel historyModel) {
                historyViewHolder.username.setText(historyModel.getUsername());
                historyViewHolder.foodnamehistory.setText(historyModel.getFoodname());
                historyViewHolder.foodcounthistory.setText(historyModel.getFoodcount());
                historyViewHolder.foodprizehistory.setText(historyModel.getFoodprize());
                historyViewHolder.totalhistory.setText(historyModel.getTotal());
                historyViewHolder.comment.setText(historyModel.getComment());
                if(historyModel.getComment()!=null){
                    if (historyModel.getComment().contains("USER")){
                        Log.d("color", "onBindViewHolder: "+historyModel.getComment());
                        int greenColorValue = Color.parseColor("#D2EC407A");
                        historyViewHolder.cardviewfororderlist.setCardBackgroundColor(greenColorValue);
                    }else {
                        int greenColorValue = Color.parseColor("#29B6F6");
                        historyViewHolder.cardviewfororderlist.setCardBackgroundColor(greenColorValue);
                    }
                }

            }

            @NonNull
            @Override
            public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteamhistorytemp,parent,false);
                return new HistoryViewHolder(view);

            }
        };



        firebaseRecyclerAdapter.startListening();
        firebaseRecyclerAdapter.notifyDataSetChanged();
        recyclerViewhistory.setAdapter(firebaseRecyclerAdapter);

    }


}