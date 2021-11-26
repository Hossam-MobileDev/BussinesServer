package com.hashtagco.bussinesserver.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hashtagco.bussinesserver.R;


public class OrderViewHolder extends RecyclerView.ViewHolder {

    public TextView txtOrderId, txtOrderStatus, txtOrderPhone, txtOrderAddress;
public Button btnedit , btnremove , btndetails , btndirection ;



    public OrderViewHolder(View itemView) {
        super(itemView);

       // txtOrderId = itemView.findViewById(R.id.order_id);
       // txtOrderStatus = itemView.findViewById(R.id.order_status);
        txtOrderPhone = itemView.findViewById(R.id.order_phone);
      //  txtOrderAddress = itemView.findViewById(R.id.order_address);
//btnedit = itemView.findViewById(R.id.edtitorder);
        btnremove = itemView.findViewById(R.id.removeorder);

       // btndetails = itemView.findViewById(R.id.detailsorder);

        btndirection = itemView.findViewById(R.id.directionorder);


    }




}
