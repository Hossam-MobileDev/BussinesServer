package com.hashtagco.bussinesserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Model.ChatMessageModel;
import com.hashtagco.bussinesserver.ViewHolder.ChatListViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends AppCompatActivity {

    LinearLayoutManager linearLayoutManager;
    @BindView(R.id.recycler_chat)
    RecyclerView recyclerViewchat;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatRef;

    FirebaseRecyclerAdapter<ChatMessageModel, ChatListViewHolder> adapter;
    FirebaseRecyclerOptions<ChatMessageModel> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        loadListChat();
    }

    private void loadListChat() {
        adapter = new FirebaseRecyclerAdapter<ChatMessageModel, ChatListViewHolder>(options) {


            @Override
            protected void onBindViewHolder(
                                                    ChatListViewHolder holder, int position,
                                              ChatMessageModel model) {
                holder.txt_eemail.setText(new StringBuilder(model.getName()));
                holder.setListener((view,pos)->{
                    Intent intent = new Intent
                            (ChatActivity.this,ChatDetailActivity.class);
                    intent.putExtra("Room_Id",adapter.getRef(position).getKey());

                    startActivity(intent);
                });
            }



            @Override
            public ChatListViewHolder onCreateViewHolder(
                                                                      ViewGroup parent, int viewType) {
                return new ChatListViewHolder(LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.layout_message_item,parent,false));
            }
        };
        recyclerViewchat.setAdapter(adapter);
    }

    private void initViews() {
        ButterKnife.bind(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        chatRef = firebaseDatabase.getReference("ResturantRef")
               // .child(Common.currentoken.getToken()).child("Chat")
                .child(Common.currentoken.getToken())
                /*.child(Common.generateChatRoomId
                        (Common.currentoken.getToken(), Common.currentRequest.getName()))*/;
        Query query = chatRef;
        options = new FirebaseRecyclerOptions.
                Builder<ChatMessageModel>().setQuery
                (query, ChatMessageModel.class).build();
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewchat.setLayoutManager(linearLayoutManager);
        recyclerViewchat.addItemDecoration(new DividerItemDecoration(this,
                linearLayoutManager.getOrientation()));
    }
}