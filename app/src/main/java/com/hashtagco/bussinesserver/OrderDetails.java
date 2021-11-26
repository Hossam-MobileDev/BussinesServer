package com.hashtagco.bussinesserver;

import android.os.Bundle;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Model.Foods;
import com.hashtagco.bussinesserver.Model.Order;
import com.hashtagco.bussinesserver.ViewHolder.OrderDetailsAdabter;
import com.squareup.picasso.Picasso;

import java.util.List;


public class OrderDetails extends AppCompatActivity
{

    private TextView textTotal,textname,textquantity;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    //--------------------------------
    private OrderDetailsAdabter orderDetailsAdabter;
    private String orderId="";
    Foods currentFood;
/*//Order order1;
private DatabaseReference table_Foods;
    private FirebaseDatabase database;*/
//List<Order> foods =Common.currentRequest.getFoods();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
/*
        database=FirebaseDatabase.getInstance();
        table_Foods=database.getReference("Foods");*/
        //----------------------Id-------------------------------//

        textTotal=(TextView)findViewById(R.id.detailtot);
        textname=(TextView) findViewById(R.id.nameproduc);
     /*   textprice=(TextView)findViewById(R.id.detailprice);
        textquantity =(TextView) findViewById(R.id.detailquant);*/

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_order_details);


        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        if (getIntent()!=null)
        {
            orderId=getIntent().getStringExtra("orderId");
        }

      //  getorderdetails(orderId);
       // Order order=foods.get(orderId);
          //-----------Event----------------//
        textTotal.setText(Common.currentRequest.getTotal());
//        textname.setText(Common.category.getName());
       /* textquantity.setText(order.getQuantity());
        textprice.setText(order.getPrice());*/
        //set image
       /* Picasso.get()
                .load(Common)//Url
                // .networkPolicy(NetworkPolicy.OFFLINE)//تحميل الصوره Offline
                //.placeholder(R.drawable.d)//الصوره الافتراضه اللى هتظهر لحد لما الصوره تتحمل
                .into(foodImage);//Image View*/
      //  textComment.setText(Common.currentRequest.getComment());

        //Adabter
        orderDetailsAdabter=new OrderDetailsAdabter(OrderDetails
                .this,Common.currentRequest.getFoods());
        orderDetailsAdabter.notifyDataSetChanged();
        recyclerView.setAdapter(orderDetailsAdabter);



    }

   /* private void getorderdetails(final String foodId)
    {

        table_Foods.child(foodId).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                currentFood = dataSnapshot.getValue(Foods.class);

                //set image
                Picasso.get()
                        .load(currentFood.getImage())//Url
                        // .networkPolicy(NetworkPolicy.OFFLINE)//تحميل الصوره Offline
                        //.placeholder(R.drawable.d)//الصوره الافتراضه اللى هتظهر لحد لما الصوره تتحمل
                        .into(foodImage);//Image View
                price.setText(String.format(" %s جنيه", currentFood.getPrice()));
                type.setText(currentFood.getDiscount());
                if (currentFood.getDescription() != null) {
                    size.setText(currentFood.getDescription());
                }
                else if (currentFood.getDescription().isEmpty() || currentFood.getDescription() == null ||
                        currentFood.getDescription() == "") {
                    size.setText("______");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }*/
}
