package com.hashtagco.bussinesserver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import android.view.LayoutInflater;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Interface.ItemClickListener;
import com.hashtagco.bussinesserver.Model.Category;
import com.hashtagco.bussinesserver.Model.Foods;
import com.hashtagco.bussinesserver.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.squareup.picasso.Picasso;

import java.util.UUID;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    RelativeLayout rootLayout;

    ExtendedFloatingActionButton fab;
    private Dialog dialogAddNewFood;

    //Firebase
    FirebaseDatabase db;
    DatabaseReference foodList;
    FirebaseStorage storage;
    StorageReference storageReference;
    String categoryId = "";

    FirebaseRecyclerAdapter<Foods, FoodViewHolder> adapter;
ImageView btnCloase;
    //Add new food
    EditText editDescription, editDiscount,name;
    Button btnSelect, btnUpload;
EditText editName;
    Foods newFood;

    Uri saveUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        db = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
foodList = db.getReference(Common.FOODS);
        //Init
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout = findViewById(R.id.root_Layout);

        fab = findViewById(R.id.fab_foodList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(FoodList.this, "FoodList" + this.getClass().getName(), Toast.LENGTH_SHORT).show();
                showDialog();
            }
        });

        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty()) {
            loadFood(categoryId);        }

    }
    private void showDialog()
    {
        dialogAddNewFood=new Dialog(this);
        dialogAddNewFood.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialogAddNewFood.setContentView(R.layout.add_new_menu_dialog_layout);
        btnUpload = (Button) dialogAddNewFood.findViewById(R.id.btn_Dialog_Upload);

        btnCloase = (ImageView) dialogAddNewFood.findViewById(R.id.close_dialog_category);
        //-----------Id------------------------//
        editName =(TextInputEditText)dialogAddNewFood.findViewById(R.id.edit_name_Dialog);


      /*  Snackbar.make(drawer,
                newCategory.getName()+" "
                        + "تمت اضافة", Snackbar.LENGTH_SHORT)
                .show();*/
            //btnSelect=(Button)dialogAddNewFood.findViewById(R.id.btn_Dialog_Select);



            //--------Event Button
     /*   btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                chooseImage();// let user Select Image from Gallery and save Uri this Image
                sharedPreferences.edit().putBoolean("btnSelectUPload",true).apply();
            }
        });*/


            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    uploadImage();
                }
            });


            btnCloase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogAddNewFood.dismiss();
                }
            });


            dialogAddNewFood.show();
        }



    private void uploadImage() {


        if (!editName.getText().toString().isEmpty()) {
            newFood = new Foods();
            newFood.setName(editName.getText().toString());
            newFood.setMenuId(categoryId);
            foodList.push().setValue(newFood);
            Common.food = newFood;
            // mprogressDialog.dismiss();
            dialogAddNewFood.dismiss(); //Finish Dialog

            //SET  Value newCategory if image Uploaded can get download Link
            //final String menuId = editMenuId.getText().toString().trim();
/*
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {
                        //Clear Value From saveUri
                        saveUri=null;
                        Toast.makeText(Home.this, "No data uploaded", Toast.LENGTH_SHORT).show();
                    }
                });
            }else //state someEdit is empty
            {
                Toast.makeText(this, "Please Fill Information", Toast.LENGTH_SHORT).show();
                //  editMenuId.setError("Please Fill Information");
                // editMenuId.setError("Please Fill Information");
                mprogressDialog.dismiss();
            }
        }else // state SaveUri=null
        {
            Toast.makeText(this, "First Select Image Category", Toast.LENGTH_SHORT).show();
        }*/


        }

        //showAddFoodDialog() method
   /* private void showAddFoodDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("اضافة المنتجات ");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout, null);


      *//*  editDescription = add_menu_layout.findViewById(R.id.edit_description_anf);

        editDiscount = add_menu_layout.findViewById(R.id.edit_discount_anf);
name =add_menu_layout.findViewById(R.id.name);
        btnSelect = add_menu_layout.findViewById(R.id.btn_select_anf);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload_anf);*//*

        //Event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage(); //Copy from HomeActivity
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage(); //Copy from HomeActivity
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.shopping_cart);

        //setButton
        alertDialog.setPositiveButton("تاكيد", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // dialog.dismiss();

                //Here just Create new Category

if(newFood!=null) {
    foodList.push().setValue(newFood);
   *//* Snackbar.make(rootLayout, newFood.getName() + " تمت الاضافة  ",
            Snackbar.LENGTH_SHORT).show();*//*
}
else Toast.makeText(FoodList.this, "برجاء اضافة صورة", Toast.LENGTH_SHORT).show();
            }

        });

        alertDialog.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }*/

        //chooseImage() method
   /* private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Common.PIC_IMAGE_REQUEST);
    }

    //uploadImage() method
    private void uploadImage() {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("جار الرفع...");
            mDialog.show();

            //create random string
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "تم تحميل الصورة بنجاح", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for newCategory if image upload and we can get download link
                                    newFood = new Foods();
                                  //  newFood.setName(editName.getText().toString());
                                    newFood.setName(name.getText().toString());
                                    newFood.setDescription(editDescription.getText().toString());
                                    newFood.setDiscount(editDiscount.getText().toString());
                                    newFood.setMenuId(categoryId);
                                    newFood.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded: " + progress + "%");
                        }
                    });
        }
    }*/

        //loadListFood() method
   /* private void loadListFood(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodList.orderByChild("menuId").equalTo(categoryId)
        ) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {
                viewHolder.foodName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(viewHolder.foodImage);

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Code late
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }*/
    }
    private void loadFood(String categoryId) {
        Query query = FirebaseDatabase.getInstance()
                .getReference().child("Foods").orderByChild("menuId").equalTo(categoryId);
                ;
       // foodList = db.getReference().child("MenuId");
        //foodList.orderByChild("MenuId").equalTo(categoryId);
        FirebaseRecyclerOptions<Foods> options =
                new FirebaseRecyclerOptions.Builder<Foods>()
                        .setQuery(query,Foods.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Foods, FoodViewHolder>(options) {
            @Override
            public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);

                return new FoodViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(FoodViewHolder holder,
                                            int position, Foods model) {
 //Picasso.get().load(model.getImage()).into(holder.foodImage);
holder.name.setText(model.getName());
//holder.size.setText(model.getDescription());
//holder.type.setText(model.getDiscount());
                final Foods local = model;
                // final Foods clickItem = model;

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get CategoryId and send to FoodList Activity

                            Intent foodlist = new Intent(FoodList.this,
                                    SubFoodActivity.class);
                            //Because CategoryId is key, so we just get key of this item
                            foodlist.putExtra("FoodId", adapter.getRef(position).getKey());
                            // Log.d(TAG, "HomeActivity Value of Key: " + adapter.getRef(position).getKey());*/
                            //  Toast.makeText(FoodList.this, "Value of Key: " + adapter.getRef(position).getKey(), Toast.LENGTH_SHORT).show();
                            startActivity(foodlist);

                    }
                });
            }

        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
    //Press Ctrl+O

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
/*
        if (requestCode == Common.PIC_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            saveUri = data.getData();
            btnSelect.setText("تم اختيار الصورة");
        }*/
    }


    //Method for delete and update food item
    //Press Ctrl+o
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        /*if (item.getTitle().equals(Common.UPDATE)) {
            //update food
            showUpdateFoodDialog(adapter.getRef(item.getOrder()).getKey(),
                    adapter.getItem(item.getOrder()));
        }
        else */if (item.getTitle().equals(Common.DELETE)) {
            //delete food
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    //deleteFood() method
    private void deleteFood(String key) {
        foodList.child(key).removeValue();
    }

    //showUpdateFoodDialog() method
/*
    private void showUpdateFoodDialog(final String key, final Foods item) {

        //just copy code from showAddFoodDialog() method
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodList.this);
        alertDialog.setTitle("تعديل علي الصنف");
       // alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View add_menu_layout = inflater.inflate(R.layout.add_new_food_layout, null);

        editDescription = add_menu_layout.findViewById(R.id.edit_description_anf);
        editDiscount = add_menu_layout.findViewById(R.id.edit_discount_anf);

        //Set default value for View
        //editName.setText(item.getName());
        editDescription.setText(item.getDescription());
        editDiscount.setText(item.getDiscount());

        btnSelect = add_menu_layout.findViewById(R.id.btn_select_anf);
        btnUpload = add_menu_layout.findViewById(R.id.btn_upload_anf);

        //Event for button
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(item);
            }
        });

        alertDialog.setView(add_menu_layout);
        alertDialog.setIcon(R.drawable.shopping_cart);

        //setButton
        alertDialog.setPositiveButton("تاكيد", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //Update information
                item.setName(name.getText().toString());
                item.setDescription(editDescription.getText().toString());
                item.setDiscount(editDiscount.getText().toString());

                foodList.child(key).setValue(item);

                Snackbar.make(rootLayout, "Category " + item.getName() + " edited Successfully", Snackbar.LENGTH_SHORT).show();

            }
        });

        alertDialog.setNegativeButton("الغاء", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }
*/


    //changeImage() method
/*
    private void changeImage(final Foods item) {
        if (saveUri != null) {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading...");
            mDialog.show();

            //create random string
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Upload Successfully!!!", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for newCategory if image upload and we can get download link
                                    item.setImage(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(FoodList.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("Uploaded: " + progress + "%");
                        }
                    });
        }
    }
*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}