package com.example.canteenapp.History;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.canteenapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class itemHistory extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    TextView netIncomeText;
    DatabaseReference databaseReference,databaseReference2;
    RecyclerView recyclerViewHistory;
    Button resetHistory;
    FirebaseDatabase firebaseDatabase1;
    DatabaseReference databaseReference1;
    int netIncome =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteam_history);
        resetHistory = findViewById(R.id.resethistory);
        netIncomeText = findViewById(R.id.netincome);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("History");
        firebaseDatabase1 = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase1.getReference("History");
        recyclerViewHistory =(RecyclerView) findViewById(R.id.recycleviewhistory);
        databaseReference2 =  firebaseDatabase.getReference("Todayshits");
        recyclerViewHistory.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewHistory.setLayoutManager(layoutManager);
        resetHistory.setOnClickListener(view -> {
            databaseReference.removeValue();
            databaseReference2.removeValue();
           netIncome =0;
           netIncomeText.setText(String.valueOf(netIncome));
        });
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                  netIncome =0;
                  for (DataSnapshot s: snapshot.getChildren()) {
                    HistoryModel historyModel = s.getValue(HistoryModel.class);
                    if (null!=historyModel&& historyModel.getComment().contains("ADMIN")){
                      netIncome = netIncome +Integer.parseInt(historyModel.getTotal());
                    }
                  }
                    netIncomeText.setText(String.valueOf(netIncome));
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
                historyViewHolder.username.setText(historyModel.getUserName());
                historyViewHolder.foodNameHistory.setText(historyModel.getFoodName());
                historyViewHolder.foodCountHistory.setText(historyModel.getFoodCount());
                historyViewHolder.foodPrizeHistory.setText(historyModel.getFoodPrize());
                historyViewHolder.totalHistory.setText(historyModel.getTotal());
                historyViewHolder.comment.setText(historyModel.getComment());
                if(historyModel.getComment()!=null){
                    int greenColorValue;
                    if (historyModel.getComment().contains("USER")){
                        greenColorValue = Color.parseColor("#D2EC407A");
                    }else {
                        greenColorValue = Color.parseColor("#29B6F6");
                    }
                    historyViewHolder.cardViewForOrderList.setCardBackgroundColor(greenColorValue);
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
        recyclerViewHistory.setAdapter(firebaseRecyclerAdapter);
    }


}