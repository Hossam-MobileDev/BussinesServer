package com.hashtagco.bussinesserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Interface.ItemClickListener;
import com.hashtagco.bussinesserver.R;


public class FoodViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener //add for update/delete image
{

    public TextView type,size,name;
    //public ImageView foodImage;

    private ItemClickListener itemClickListener;

    public FoodViewHolder(View itemView) {
        super(itemView);

      //  foodName = itemView.findViewById(R.id.food_name);
       /* foodImage = itemView.findViewById(R.id.food_image);
type=itemView.findViewById(R.id.type);
size=itemView.findViewById(R.id.size);*/
name=itemView.findViewById(R.id.namefoo);
        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    //add for update/delete image
    //Override method for View.OnCreateContextMenuListener
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderIcon(R.drawable.firebase);
       // menu.setHeaderTitle("Select an Action");

        //menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
        menu.add(0, 1, getAdapterPosition(), Common.DELETE);
    }
}
