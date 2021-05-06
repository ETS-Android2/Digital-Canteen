package com.example.canteenapp.OrderList;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.canteenapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class OrderViewHolder extends RecyclerView.ViewHolder {

    View view;
    TextView topfoodname, topfoodprice, topfoodcount,username,total,tokenadmin;
    Button done,orderredy;
    CardView cardView;
    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        topfoodname = view.findViewById(R.id.foodnameorser);
       topfoodprice = view.findViewById(R.id.foodprizeorder);
        topfoodcount = view.findViewById(R.id.foodcountorder);
        username = view.findViewById(R.id.usersnameorder);
        total = view.findViewById(R.id.totalprizeorder);
        orderredy = view.findViewById(R.id.orderredy);
        done = view.findViewById(R.id.donebutton);
        cardView = view.findViewById(R.id.cardviewfororderlist);
        tokenadmin = view.findViewById(R.id.tokenadmin);
    }

}
