package com.hashtagco.bussinesserver.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Interface.ItemClickListener;
import com.hashtagco.bussinesserver.R;


public class MenuViewHolder extends RecyclerView.ViewHolder implements
        View.OnClickListener,
        View.OnCreateContextMenuListener //add for update/delete image
{

    public TextView textMenuName;
    //public ImageView imageView;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(View itemView) {
        super(itemView);

        textMenuName = itemView.findViewById(R.id.nameca);
     //   imageView = itemView.findViewById(R.id.menu_image);

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

      //  menu.add(0, 0, getAdapterPosition(), Common.UPDATE);
        menu.add(0, 1, getAdapterPosition(), Common.DELETE);
    }
}
