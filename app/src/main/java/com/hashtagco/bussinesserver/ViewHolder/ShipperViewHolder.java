package com.hashtagco.bussinesserver.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hashtagco.bussinesserver.Interface.ItemClickListener;
import com.hashtagco.bussinesserver.R;


public class ShipperViewHolder extends RecyclerView.ViewHolder
        //add for update/delete image
{

    public TextView shiperName;
    public TextView shiperPhone;
Button btnEdit , btnRemove;
    private ItemClickListener itemClickListener;

    public ShipperViewHolder(View itemView) {
        super(itemView);

        shiperName = itemView.findViewById(R.id.shipername);
        shiperPhone = itemView.findViewById(R.id.shiperphone);

btnEdit = itemView.findViewById(R.id.btnedit);
btnRemove = itemView.findViewById(R.id.btnremove);



    }



}
