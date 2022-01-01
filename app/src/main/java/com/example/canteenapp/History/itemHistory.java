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
import com.example.canteenapp.Util.CanteenUtil;
import com.example.canteenapp.constant.CanteenConstant;
import com.example.canteenapp.constant.FireBaseConstant;
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
    FirebaseDatabase firebaseDatabase1;
    DatabaseReference databaseReference1;
    int netIncome =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteam_history);
        netIncomeText = findViewById(R.id.netincome);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference(FireBaseConstant.HISTORY);
        firebaseDatabase1 = FirebaseDatabase.getInstance();
        databaseReference1 = firebaseDatabase1.getReference(FireBaseConstant.HISTORY);
        recyclerViewHistory =(RecyclerView) findViewById(R.id.recycleviewhistory);
        databaseReference2 =  firebaseDatabase.getReference(FireBaseConstant.TODAYS_HITS);
        recyclerViewHistory.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerViewHistory.setLayoutManager(layoutManager);

        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue()!=null){
                  netIncome =0;
                  for (DataSnapshot s: snapshot.getChildren()) {
                    HistoryModel historyModel = s.getValue(HistoryModel.class);
                    if (null!=historyModel&& historyModel.getComment().contains(CanteenConstant.ADMIN)){
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
                historyViewHolder.time.setText(CanteenUtil.ConvertMilliSecondsToFormattedDate(historyModel.getTime()));
                if(historyModel.getComment()!=null){
                    int colorValue;
                    if (historyModel.getComment().contains(CanteenConstant.USER)){
                        colorValue = Color.parseColor("#D2EC407A");
                    }else {
                        colorValue = Color.parseColor("#29B6F6");
                    }
                    historyViewHolder.cardViewForOrderList.setCardBackgroundColor(colorValue);
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