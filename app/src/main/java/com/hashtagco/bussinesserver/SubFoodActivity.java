package com.hashtagco.bussinesserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Interface.ItemClickListener;
import com.hashtagco.bussinesserver.Model.Foods;
import com.hashtagco.bussinesserver.Model.SubFoods;
import com.hashtagco.bussinesserver.ViewHolder.FoodViewHolder;
import com.hashtagco.bussinesserver.ViewHolder.SubFoodViewHolder;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SubFoodActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;
    FirebaseStorage storage;
    StorageReference storageReference;
    ExtendedFloatingActionButton fab;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference foodList;

    String foodid;

ImageView imageDetail;
TextView sanfName , sanfType , sanfSizes;
    SubFoods newsubFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_food);
        //Firebase
        db = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        foodList = db.getReference("SubFoods");
        //Init
        MaterialToolbar toolbar = (MaterialToolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
imageDetail = findViewById(R.id.imageDetail);
sanfName = findViewById(R.id.sanfName);
sanfType = findViewById(R.id.sanfType);
//sanfSizes = findViewById(R.id.sanfSizes);
        rootLayout = findViewById(R.id.root_Layout);

        fab = findViewById(R.id.fab_foodList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(FoodList.this, "FoodList" + this.getClass().getName(), Toast.LENGTH_SHORT).show();
                //showDialog();
                Intent intent = new Intent
                        (SubFoodActivity.this,DetailsproductActivity.class);
                intent.putExtra("FoodId",foodid);
             //   startActivityForResult(intent,constant);
                startActivity(intent);
            }
        });

        if (getIntent() != null) {
            foodid = getIntent().getStringExtra("FoodId");
        }
        if (!foodid.isEmpty()) {
           // loadSubFood(foodid);        }
            getFoodDetails(foodid);
    }




}

    private void getFoodDetails(String foodid)
    {

        foodList.child(foodid).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                newsubFood = dataSnapshot.getValue(SubFoods.class);

                //set image
                if (newsubFood != null) {
                    Picasso.get()
                            .load(newsubFood.getImage())//Url
                            .into(imageDetail);//Image View

                    sanfName.setText(newsubFood.getName());
                    if(newsubFood.getType()!=null){
                        sanfType.setText(newsubFood.getType());

                    }else {
                        sanfType.setText("__________");
                    }
//                    sanfSizes.setText(newsubFood.getSize());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle item selection
        switch (item.getItemId()) {
            case R.id.i1:
                deleteFood(foodid);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }




    //deleteFood() method
    private void deleteFood(String key) {
        foodList.child(key).removeValue();
imageDetail.setImageResource(R.drawable.frs);
        sanfName.setText("");
        sanfType.setText("");
        sanfSizes.setText("");
    }

}
