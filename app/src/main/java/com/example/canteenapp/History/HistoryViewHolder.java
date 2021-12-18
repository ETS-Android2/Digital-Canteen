package com.example.canteenapp.History;

import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HistoryViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView username,foodnamehistory,foodprizehistory,foodcounthistory,totalhistory,comment;
   CardView cardviewfororderlist;
    public HistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        view=itemView;
        username = view.findViewById(R.id.usersnamehistory);
        foodnamehistory = view.findViewById(R.id.foodnamehistory);
        foodprizehistory= view.findViewById(R.id.foodprizehistory);
        foodcounthistory= view.findViewById(R.id.foodcounthistory);
        totalhistory = view.findViewById(R.id.totalprizehsiory);
        comment= view.findViewById(R.id.comment);
        cardviewfororderlist = view.findViewById(R.id.cardviewfororderlisthistory);
    }
}
