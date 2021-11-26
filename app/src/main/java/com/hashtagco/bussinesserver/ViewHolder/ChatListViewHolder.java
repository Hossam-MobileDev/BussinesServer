package com.hashtagco.bussinesserver.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hashtagco.bussinesserver.IRecyclerViewListner;
import com.hashtagco.bussinesserver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


//Adabter Home Activity
public class ChatListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    Unbinder unbinder;
   private Context context;
@BindView(R.id.txt_time)
public TextView txt_time;

    @BindView(R.id.txt_email)
    public TextView txt_eemail;
    @BindView(R.id.txt_chat_message)
    public TextView txt_chatt_message;
    @BindView(R.id.profile_image)
   public CircleImageView profile_image;
IRecyclerViewListner listner;
    public ChatListViewHolder(@NonNull View itemView)
    {
        super(itemView);

unbinder = ButterKnife.bind(this,itemView);
itemView.setOnClickListener(this);
    }

public void setListener (IRecyclerViewListner listener){
this.listner = listener;
}


    @Override
    public void onClick(View v) {
        listner.onItenClickListner(v,getAdapterPosition());
    }
}
