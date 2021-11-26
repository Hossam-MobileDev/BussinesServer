package com.hashtagco.bussinesserver.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hashtagco.bussinesserver.R;


public class OrderStatusViewHolder extends RecyclerView.ViewHolder


                                                                    {


    public TextView textname,textOrderPhone,textOrderAddress,textOrderDate,
            textprodutname,textquant
            ;
    public Button btnEdit,btnRemove,btnDirection;




    public OrderStatusViewHolder(View itemView)
    {
        super(itemView);

        textname=(TextView)itemView.findViewById(R.id.nameclient);

        textOrderPhone=(TextView)itemView.findViewById(R.id.order_phone);
       // textOrderAddress=(TextView)itemView.findViewById(R.id.order_address);
        textOrderDate=(TextView)itemView.findViewById(R.id.order_status_Date);
        textprodutname=(TextView)itemView.findViewById(R.id.producttname);
        textquant=(TextView)itemView.findViewById(R.id.quant);

//        btnEdit=(Button)itemView.findViewById(R.id.edtitorder);
     //  btnDetails=(Button)itemView.findViewById(R.id.detailorder);
        btnDirection=(Button)itemView.findViewById(R.id.directionorder);
        btnRemove=(Button)itemView.findViewById(R.id.removeorder);


        }









}
