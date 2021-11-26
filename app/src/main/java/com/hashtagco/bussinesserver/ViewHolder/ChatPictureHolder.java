package com.hashtagco.bussinesserver.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.hashtagco.bussinesserver.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


//Adabter Home Activity
public class ChatPictureHolder extends RecyclerView.ViewHolder
{
    Unbinder unbinder;
   private Context context;
@BindView(R.id.txt_time)
public TextView txt_time;

    @BindView(R.id.txt_email)
    public TextView txt_email;

    @BindView(R.id.txt_chat_message)
   public TextView txt_chat_message;
    @BindView(R.id.profile_image)
   public CircleImageView profile_image;

    @BindView(R.id.image_preview)
   public ImageView image_preview;
    public ChatPictureHolder(@NonNull View itemView)
    {
        super(itemView);

unbinder = ButterKnife.bind(this,itemView);
    }





}
