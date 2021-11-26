package com.hashtagco.bussinesserver.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hashtagco.bussinesserver.Interface.ItemClickListener;
import com.hashtagco.bussinesserver.R;


//الViewHoldr لعرض Table ال Foods الاطعمه اللى عندى فى ال RecyclerView بستخدام FirebaseUI مع استخدام CallBack Interface  لاكشن الضغط
public class SubFoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView  textFoodName;
   // public ImageView foodimageView;
    //public ImageView favimage;
    //public ImageView shareImage;
    public TextView textFoodPrice;
    //public ImageView queckCartImage;




    //-------------------------
    private ItemClickListener itemClickListener;// Interface


    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }

    public SubFoodViewHolder(View itemView)
    {
        super(itemView);

        textFoodName=(TextView)itemView.findViewById(R.id.namesubfoo);
     //   foodimageView=(ImageView)itemView.findViewById(R.id.food_image);
        //favimage=(ImageView)itemView.findViewById(R.id.fav_imageFood);
       // shareImage=(ImageView)itemView.findViewById(R.id.share_imageFood);
       // textFoodPrice=(TextView)itemView.findViewById(R.id.food_price_foodList);
     //   queckCartImage=(ImageView)itemView.findViewById(R.id.queck_cart_image);




        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {

        itemClickListener.onClick(view,getAdapterPosition(),false);

    }



}
