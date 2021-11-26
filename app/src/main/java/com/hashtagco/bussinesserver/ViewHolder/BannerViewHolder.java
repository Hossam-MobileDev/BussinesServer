package com.hashtagco.bussinesserver.ViewHolder;


import android.text.SpannableString;
import android.text.Spanned;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Interface.ItemClickListener;
import com.hashtagco.bussinesserver.R;


public class BannerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        ,View.OnCreateContextMenuListener
{

    public ImageView imageViewBanner;
    public TextView textViewBanner,price;
    public ItemClickListener itemClickListiner;


    public BannerViewHolder(View itemView)
    {
        super(itemView);

        imageViewBanner=(ImageView)itemView.findViewById(R.id.imagefood_banner);
        textViewBanner=(TextView)itemView.findViewById(R.id.namefood_banner);
price=(TextView) itemView.findViewById(R.id.price);

        itemView.setOnCreateContextMenuListener(this); //Context Menu
        itemView.setOnClickListener(this);
    }


    public void setItemClickListiner(ItemClickListener itemClickListiner) {
        this.itemClickListiner = itemClickListiner;
    }

    @Override
    public void onClick(View view)
    {

     itemClickListiner.onClick(view,getAdapterPosition(),false);
    }


    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view,
                                    ContextMenu.ContextMenuInfo contextMenuInfo)
    {

      //  contextMenu.setHeaderTitle("Select this action ");

       // contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(1,0,getAdapterPosition(), Common.DELETE);


    }

}
