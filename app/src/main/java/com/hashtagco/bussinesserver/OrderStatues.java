package com.hashtagco.bussinesserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Model.DataMessage;
import com.hashtagco.bussinesserver.Model.MyResponse;
import com.hashtagco.bussinesserver.Model.Notification;
import com.hashtagco.bussinesserver.Model.Request;
import com.hashtagco.bussinesserver.Model.Sender;
import com.hashtagco.bussinesserver.Model.Token;
import com.hashtagco.bussinesserver.Remote.ApiService;
import com.hashtagco.bussinesserver.ViewHolder.OrderStatusViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderStatues extends AppCompatActivity {


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private MaterialSpinner spinnerStatuse,spinnerShippers;
    //---------------------------
    private FirebaseDatabase db;
    private DatabaseReference requestsTable;
    //------------Firebase Ui-------------//
    private Query query;
    private FirebaseRecyclerOptions<Request>options;
    private FirebaseRecyclerAdapter<Request, OrderStatusViewHolder>adapter;
    //-------------------------------------
    private ApiService mService;
    //-------------------------------------
    private RelativeLayout rootLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        //-------------------------Id-------------------------------//
        recyclerView=(RecyclerView)findViewById(R.id.recyclerView_OrderStatus);
        rootLayout=(RelativeLayout)findViewById(R.id.rootLayout_OrderStatus);


        //-----------------------Firebase------------------------//
        db=FirebaseDatabase.getInstance();
        requestsTable =db.getReference("Requests");


        //Init "Retrofit" Service  Notification
        mService= Common.getFCMClinet();

        //------------------RecyclerView--------------//
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //-------------------Event-----------------

        loadOrderStatus();//Load All Status


    }


    //Load All Status and show Request into Recylerview
    private void loadOrderStatus()
    {


        //---Using Firebase UI to populate a RecyclerView--------//
        query= FirebaseDatabase.getInstance()
                .getReference()
                .child("Requests");

        //.orderByChild("phone").equalTo(phone)

        query.keepSynced(true);//Load Data OffLine

        options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Request,OrderStatusViewHolder>(options) {
            @Override
            public OrderStatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);

                return new OrderStatusViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final OrderStatusViewHolder holder,
                                            final int position, final Request model) {
                // Bind the Chat object to the ChatHolder
holder.textprodutname.setText(model.getFoods().get(0).getProudactName());
//model.getFoods().get(position).getImage()// // /
holder.textquant.setText(model.getFoods().get(0).getQuentity());
               // holder.textOrderId.setText(adapter.getRef(position).getKey()); //adapter.getRef(position).getKey() ==االمفتاح الرئيسى للصف المعروض فى الفيربيز
               // holder.textOrderStatus.setText(Common.converCodeToStatus(model.getStatus()));
                holder.textname.setText(model.getName());
                holder.textOrderPhone.setText(model.getPhoneClient());
//                holder.textOrderAddress.setText(model.getAddress());
                holder.textOrderDate.setText(model.getDate());
                Common.currentRequest=model;

                // holder.textOrderDate.setText
                     //   (Common.getData(Long.parseLong(adapter.getRef(position).getKey())));


                //New event Button
          /*      holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        showUpdateDialog(adapter.getRef(position).getKey()
                                ,adapter.getItem(position));
                    }
                });*/


                holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        deleteDialogOrderFeromTableRequestsAndOrdersNeedShip(adapter.
                                getRef(position).getKey());
                    }
                });

             /*   holder.btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        Intent orderDetailsActivity= new Intent(OrderStatues.this,OrderDetails.class);
                        Common.currentRequest=model;
                        orderDetailsActivity.putExtra("orderId",adapter.getRef(position).getKey());
                        startActivity(orderDetailsActivity);

                    }
                });*/

                holder.btnDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {

                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + model.getPhoneClient()));
                        startActivity(callIntent);
/*
                        Intent trackingOrderIntent=new Intent(OrderStatues.this,TrackingOrder.class);
                        Common.currentRequest=model;
                        trackingOrderIntent.putExtra("Latlng",model.getLatlng());
                        trackingOrderIntent.putExtra("shipperPhone",model.getPhoneShipper());
                        trackingOrderIntent.putExtra("clientPhone",model.getPhoneClient());
                        startActivity(trackingOrderIntent);*/

                    }
                });
/*
                holder.btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(OrderStatues.this,
                                OrderDetails.class);
                        Common.currentRequest=model;
                        intent.putExtra("orderId",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
*/
              /*  //لما المستخدم يضغط على اى صف
                holder.setItemClickListener(new ItemClickListiner() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick)
                    {
                        //Get CategoryId and send to new Activity

                        if (!isLongClick)
                        {
                                 Intent trackingOrderIntent=new Intent(OrderStatus.this,TrackingOrder.class);
                                            Common.currentRequest=model;
                                            startActivity(trackingOrderIntent);
                        }
                        *//*else
                            {
                                Intent orderDetailsActivity= new Intent(OrderStatus.this,OrderDetails.class);
                                       Common.currentRequest=model;
                                       orderDetailsActivity.putExtra("orderId",adapter.getRef(position).getKey());
                                       startActivity(orderDetailsActivity);
                            }
                          *//*
                    }
                     });*/


            }//end OnBind

        };

        //end Adapter
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }



    //Start Adapter
    @Override
    protected void onStart() {
        super.onStart();

        adapter.startListening();

    }

    //Stop Adapter
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /*
      @Override
      public boolean onContextItemSelected(MenuItem item)
      {

          if (item.getTitle().equals(Common.UPDATE))
          {

              showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
          }
          else if (item.getTitle().equals(Common.DELETE))
          {
              deleteStatusDialog(adapter.getRef(item.getOrder()).getKey());
          }
          return super.onContextItemSelected(item);
      }

    */
    //Follow Context Menu ==> Update Status
    private void deleteDialogOrderFeromTableRequestsAndOrdersNeedShip(final String key)
    {
        //Remove Item From Table Requests
        // requestsTable.child(key).removeValue();
        //  adapter.notifyDataSetChanged();
        //Remove also Table Shipper
        final String keyOrder=key;


        requestsTable.child(key).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Request request=dataSnapshot.getValue(Request.class);


//                if (request.getStatus().equals("0"))
//                {
                    requestsTable.child(keyOrder).removeValue();
                    adapter.notifyDataSetChanged();
/*
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                            "you Cannot Cancel This Order", Snackbar.LENGTH_LONG);*/

                  /*  snackbar.setActionTextColor(Color.RED);
                    snackbar.setText("Successful Remove"+Common.converCodeToStatus(request.getStatus()));
                    snackbar.getView().setBackgroundColor(Color.GREEN);
                    snackbar.show();*/
               /* }else
                {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content)
                    ,"you Cannot Cancel This Order", Snackbar.LENGTH_LONG);

                    snackbar.setActionTextColor(Color.RED);
                    snackbar.setText("you Cannot Delete This Order Because Status "+Common.converCodeToStatus(request.getStatus()));
                    snackbar.getView().setBackgroundColor(Color.RED);
                    snackbar.show();
                }*/


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



   /* private void showUpdateDialog(final String key, final Request item)
    {
        AlertDialog.Builder  alertDialog=new AlertDialog.Builder(this);



        LayoutInflater layoutInflater=this.getLayoutInflater();
        View view=layoutInflater.inflate(R.layout.update_order_status_item,null);

        //--------------------Id-------------------------//
        spinnerStatuse =(MaterialSpinner)view.findViewById(R.id.OrderstatusSpinner_item);
        spinnerStatuse.setItems("الطلب مقدم","الطلب في الطريق","شحن الطلب");


        spinnerShippers=(MaterialSpinner)view.findViewById(R.id.OrderchoseShipperSpinner_item);
        //Load All Shippers phone to shipper
        final List<String>shipperList=new ArrayList<>();

        FirebaseDatabase.getInstance().getReference(Common.SHIPPER_TABLE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        shipperList.clear();
                        for (DataSnapshot snapshotShipper:dataSnapshot.getChildren())
                            shipperList.add(snapshotShipper.getKey());

                        if (shipperList.size()>0)
                            spinnerShippers.setItems(shipperList);
                        else
                            spinnerShippers.setItems("No Shipper");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




        alertDialog.setView(view);

        final String LocalKey=key;

        alertDialog.setPositiveButton("تاكيد", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();

                item.setStatus(String.valueOf(spinnerStatuse.getSelectedIndex()));
                item.setPhoneShipper(spinnerShippers.getItems().get(spinnerShippers.getSelectedIndex())
                        .toString());

                if (item.getStatus().equals("2")) //Shipping
                {
                    //Send Data to Table OrderNeedShip                                 // .getItems().get(index) ==>  return name element to click
                    FirebaseDatabase.getInstance().getReference(Common.ORDER_NEED_SHIP) //getSelectedIndex() ==> return index element to click
                            .child(spinnerShippers.getItems().get(spinnerShippers.getSelectedIndex()).toString())
                            .child(LocalKey) //Key item for Requests Table
                            .setValue(item); //object for Request model

                    requestsTable.child(key).setValue(item);
                    adapter.notifyDataSetChanged(); //add to update item size
Toast.makeText(OrderStatues.this,"تم تعديل حالة الطلب بنجاح !",Toast.LENGTH_SHORT).show();
                    sendOrderStatusToUser(LocalKey, item);
                    //sendOrderShipRequestToShipper(spinnerShippers.getItems().
                       //     get(spinnerShippers.getSelectedIndex()).toString(), item);


                }
                else {
                    requestsTable.child(key).setValue(item);
                    adapter.notifyDataSetChanged(); //add to update item size

                    sendOrderStatusToUser(LocalKey, item);
                }

            }
        });


        alertDialog.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                dialogInterface.dismiss();

            }
        });

        alertDialog.show();

    }*/

    //Send Notification to Shipper
   /* private void sendOrderShipRequestToShipper(String shipperPhone, final Request item)
    {
        final DatabaseReference referenceToken=db.getReference("Tokens");

        referenceToken.child(shipperPhone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        if (dataSnapshot.exists())
                        {
                            Token token=dataSnapshot.getValue(Token.class);


                            Map<String,String> dataSend = new HashMap<>();
                            dataSend.put("title","Food Spotting");
                            dataSend.put("body","New Your Order Shipper");

                            DataMessage dataMessage = new DataMessage(token.getToken(),dataSend);

                            mService.sendNotification(dataMessage)
                                    .enqueue(new Callback<MyResponse>()
                                    {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response)
                                        {
                                            if (response.body().success==1)
                                            {
                                                Toast.makeText(OrderStatues.this, "Send to Shipper", Toast.LENGTH_SHORT).show();
                                            }else
                                            {
                                                Toast.makeText(OrderStatues.this, "failed send Notification !", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t)
                                        {

                                            Log.e("Error", t.getMessage());
                                        }
                                    });
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }*/

    //Send Notification to User
  /*  private void sendOrderStatusToUser(final String key,Request item)
    {
        final DatabaseReference referenceToken=db.getReference("Tokens");


        referenceToken.orderByKey().equalTo(item.getPhoneClient())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                            for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {
                                Token token = postsnapshot.getValue(Token.class);
                                Notification notification = new Notification("hossam",
                                        "your order "+key+"was updated");
                                Sender sender = new Sender(token.getToken(),notification);




                                mService.sendNotification(sender).enqueue(new Callback<MyResponse>()
                                {
                                    @Override
                                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response)
                                    {
                                        //
                                        if (response.code()==200) {
                                            if (response.body().success == 1) {
                                                Toast.makeText(OrderStatues.this,
                                                        "order updated sucess ", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(OrderStatues.this, "Failed !!! ", Toast.LENGTH_SHORT).show();

                                            }

                                        }

                                    }

                                    @Override
                                    public void onFailure(Call<MyResponse> call, Throwable t)
                                    {
                                        Log.e("Error ", t.getMessage() );
                                    }
                                });
                            }





                        }



                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });


    }*/


}