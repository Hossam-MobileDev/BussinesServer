package com.hashtagco.bussinesserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Model.Shipper;
import com.hashtagco.bussinesserver.ViewHolder.ShipperViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

//import org.jetbrains.annotations.NotNull;

public class ShiperMangement extends AppCompatActivity {

   /* FloatingActionButton fab ;
    FirebaseDatabase frdatabase ;
    DatabaseReference dbref ;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Shipper, ShipperViewHolder> adapter;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shiper_mangement);
        /*fab = (FloatingActionButton) findViewById(R.id.shiperadd);
        recycler_menu = (RecyclerView) findViewById(R.id.listSHIPER);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        frdatabase = FirebaseDatabase.getInstance();
        dbref = frdatabase.getReference(Common.SHIPPER_TABLES);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showcreateshipper();
            }
        });
        loadAllShipper();*/
    }

   /* private void loadAllShipper() {



            FirebaseRecyclerOptions<Shipper> options =
                    new FirebaseRecyclerOptions.Builder<Shipper>()
                            .setQuery(dbref,Shipper.class)
                            .build();

            adapter = new FirebaseRecyclerAdapter<Shipper, ShipperViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ShipperViewHolder holder,
                                                int position, @NonNull Shipper model) {
             String key     =  getRef(position).getKey();
             dbref.child(key).addValueEventListener(new ValueEventListener() {
                 @Override
                 public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                     final String retName = snapshot.child("name").getValue().toString();
                     final String refphone = snapshot.child("phone").getValue().toString();
                     holder.shiperName.setText(retName);
                     holder.shiperPhone.setText(refphone);
                 }

                 @Override
                 public void onCancelled(@NonNull @NotNull DatabaseError error) {

                 }
             });
                   // holder.shiperPhone.setText(adapter.getRef(position).getKey());
                    //holder.shiperPhone.setText(model.getPhone());
                   // holder.shiperName.setText( adapter.getRef(position).child(model.getName()).toString());
                }

                @Override
                public ShipperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.shiper_layout, parent, false);

                    return new ShipperViewHolder(view);
                }


            };
            adapter.startListening();
            recycler_menu.setAdapter(adapter);
        }


    private void showcreateshipper() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ShiperMangement.this);
        alertDialog.setTitle("بيانات المندوب");
      //  alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.create_shipper, null);
        EditText edtname = view.findViewById(R.id.edtName);
        EditText edtphone = view.findViewById(R.id.edtPhone);

        EditText edtpassword = view.findViewById(R.id.edtPassword);
        alertDialog.setView(view);
        alertDialog.setPositiveButton("اضافة", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Shipper shipper = new Shipper();
                shipper.setName(edtname.getText().toString());
                shipper.setPhone(edtphone.getText().toString());
                shipper.setPassword(edtpassword.getText().toString());
                dbref.push().setValue(shipper);


            }
        });

        alertDialog.setNegativeButton("الفاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.startListening();*/
    }

