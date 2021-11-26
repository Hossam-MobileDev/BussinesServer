package com.hashtagco.bussinesserver;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Interface.ItemClickListener;
import com.hashtagco.bussinesserver.Model.MyResponse;
import com.hashtagco.bussinesserver.Model.Notification;
import com.hashtagco.bussinesserver.Model.Sender;
import com.hashtagco.bussinesserver.Model.Token;
import com.hashtagco.bussinesserver.Model.Category;
import com.hashtagco.bussinesserver.Remote.ApiService;
import com.hashtagco.bussinesserver.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.iceteck.silicompressorr.FileUtils;
import com.iceteck.silicompressorr.SiliCompressor;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//import id.zelory.compressor.Compressor;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
Context context;
    private TextView fullName;
    private RecyclerView recyclerViewMenu;
    private RecyclerView.LayoutManager layoutManager;
    //--------Add new Menu use Dialog--------
    private TextInputEditText editName;
    private TextInputEditText editMenuId;
    private Button btnSelect;
    private Button btnUpload;
    private Dialog dialogAddNewFood;
    //--------Update-------------//
    private TextInputEditText editTextNameUpdate;
    private TextView textMenuIdUpdate;
    private Button btnSelectUPdate;
    private Button btnUploadUpdate;
    private Dialog dialogUpdateItem;
    //----------------------
    private FirebaseDatabase database;
    private DatabaseReference categories;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    //--------Firebase UI--------//
    private Query query;
    private FirebaseRecyclerOptions<Category> options;
    private FirebaseRecyclerAdapter<Category, MenuViewHolder> adapter;
    //---------------------------
    private Category newCategory;
    private Uri saveUri;
    private Bitmap thmbBitmap=null;
    private ProgressDialog mprogressDialog;
    private File thump_bitmap=null;
    //------------------------------------
    private DrawerLayout drawer;
    //------------------------------
    private SharedPreferences sharedPreferences;
    private SharedPreferences sharedPref;
    ImageView btnCloase;
    private ApiService mApiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mApiService=Common.getFCMClinet();

        //-----------------------Id------------------//
        ExtendedFloatingActionButton fab = (ExtendedFloatingActionButton) findViewById(R.id.fab_home);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        Paper.init(this);


        recyclerViewMenu=(RecyclerView)findViewById(R.id.recyclerView_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("الأقسام الرئيسية");
        toolbar.setTitleTextColor(getResources().getColor(R.color.toolbarIconColor));
        setSupportActionBar(toolbar);

        //-------------RecyclerView-------------------//
        recyclerViewMenu.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerViewMenu.setLayoutManager(layoutManager);
        mprogressDialog=new ProgressDialog(this);

        //---------------------Firebase-----------------------//
        database=FirebaseDatabase.getInstance();
        categories=database.getReference(Common.CATEGORY);//Reference Database for TABLE Name Category
        //storage=FirebaseStorage.getInstance();
        //storageReference=storage.getReference();


       // sharedPreferences=getSharedPreferences("myPrefs",MODE_PRIVATE);
       // sharedPref=getSharedPreferences(Common.USER_DATA,MODE_PRIVATE);

        //---Action Navigation Drawer---------//
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColorPrimary));
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //set Full Name User

        View headerView=navigationView.getHeaderView(0);
        fullName=(TextView)headerView.findViewById(R.id.text_Full_Name_User);
//        fullName.setText(Common.currentUser.getName());

        //-------------------------Event--------------------------------//

        if (Common.isConnectedToInternet(this))
        {
            loadMenu();
        }else
        {
            Toast.makeText(this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if (Common.isConnectedToInternet(getBaseContext()))
                {
                    showDialog();

                }else
                {
                    Toast.makeText(Home.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
      /* FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Home.this,
                new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String newToken = instanceIdResult.getToken();
                        Log.e("newToken",newToken);
                        Token token = new Token();
                        Common.currentoken = token;
                        token.setToken(newToken);
                        updateToken(newToken);
                    }
                });*/
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                         //   Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String newToken = task.getResult();
                        updateToken(newToken);
                    }
                });
        //Storage your Token app to FirebaseDatabse
       //updateToken(FirebaseInstanceId.getInstance().getToken());

    }


    //Update Token
    private void updateToken(String token)
    {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference("Tokens");

        Token token1=new Token(token,true);
        reference.child(Common.currentAdmin.getPassword()).setValue(token1);
    }
    private void sendNotificationOrder()
    {
        final DatabaseReference referenceTokens=FirebaseDatabase.getInstance().
                getReference("Tokens");

        Query queryData =referenceTokens.orderByChild("serverToken")
                .equalTo(false);// get All node isServerToken is True


        queryData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    Token serverToken=snapshot.getValue(Token.class);
                    Notification notification =
                            new Notification("ميديكال فريست",
                            "اضاف منتج جديد");
                    Sender sender = new Sender(serverToken.getToken(),notification);




                    mApiService.sendNotification(sender).enqueue(new Callback<MyResponse>()
                    {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response)
                        {
                            //
                            if (response.code()==200) {
                                if (response.body().success == 1) {
                                   // Toast.makeText(Cart.this, "Than k you " +
                                           // ",Order Place ", Toast.LENGTH_SHORT).show();
                                   // finish();
                                } else {
                                   // Toast.makeText(Cart.this, "Failed !!! ", Toast.LENGTH_SHORT).show();

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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    //Method Load Data From FirebaseDatabse AND send Data to RecyclerView
    private void loadMenu()
    {
        //---Using Firebase UI to populate a RecyclerView--------//
        query= FirebaseDatabase.getInstance()
                .getReference()
                .child("Category");

        query.keepSynced(true);//Load Data OffLine

        options = new FirebaseRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Category, MenuViewHolder>(options) {
            @Override
            public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_item, parent, false);

                return new MenuViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(final MenuViewHolder holder, final int position, final Category model) {
                // Bind the Chat object to the ChatHolder

                //Send Image Name to Recyclerview
                holder.textMenuName.setText(model.getName());

                //Send Image  to Recyclerview
            /*    Picasso.get()
                        .load(model.getImage())//Url
                        //  .networkPolicy(NetworkPolicy.OFFLINE)//تحميل الصوره Offline
                        // .placeholder(R.drawable.d)//الصوره الافتراضه اللى هتظهر لحد لما الصوره تتحمل
                        .into(holder.imageView);*/

                final Category clickItem=model;

                //لما المستخدم يضغط على اى صف
                holder.setItemClickListener(new ItemClickListener()
                {

                    @Override
                    public void onClick(View view, int position, boolean isLongClick)
                    {
                        //sen Category Id and Start new Activity

                        Intent foodsListIntent=new Intent(Home.this,FoodList.class);

                        foodsListIntent.putExtra("CategoryId",
                                adapter.getRef(position).getKey());//Just Get Key Of item
                        startActivity(foodsListIntent);


                    }
                });



            }//end OnBind


        };//end Adapter

        //هيعمل تحديث للبيانات لو حصل تغيرفيها
        adapter.notifyDataSetChanged(); //Refresh Data if data changed

        recyclerViewMenu.setAdapter(adapter);
    }

    //---------------------------------------------
    //Dialog Shaw Add New Category
    private void showDialog()
    {
        dialogAddNewFood=new Dialog(this);
        dialogAddNewFood.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialogAddNewFood.setContentView(R.layout.add_new_menu_dialog_layout);

        //-----------Id------------------------//
        editName =(TextInputEditText)dialogAddNewFood.findViewById(R.id.edit_name_Dialog);
//        btnSelect=(Button)dialogAddNewFood.findViewById(R.id.btn_Dialog_Select);
        btnUpload=(Button)dialogAddNewFood.findViewById(R.id.btn_Dialog_Upload);
         btnCloase=(ImageView)dialogAddNewFood.findViewById(R.id.close_dialog_category);


        //--------Event Button
/*
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                chooseImage();// let user Select Image from Gallery and save Uri this Image
                sharedPreferences.edit().putBoolean("btnSelectUPload",true).apply();
            }
        });
*/


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                uploadImage();

            }
        });


        btnCloase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialogAddNewFood.dismiss();
            }
        });



        dialogAddNewFood.show();


    }
  /*  private void  chooseImage()
    {

      *//* Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture"),Common.PICK_IMAGE_REQUESTO);
*//*

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Pilih Gambar"),Common.PICK_IMAGE_REQUEST);
    }

    private void  chooseImageItem()
    {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent,"Update picture"),14556);
        // start picker to get image for cropping and then use the image in cropping activity
      *//*  CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setActivityTitle("Update Image")
                .start(this);*//*



    }*/

    //Upload New Category
    private void uploadImage() {


            mprogressDialog.setMessage("جار الرفع ...");
            mprogressDialog.show();

            //SET  Value newCategory if image Uploaded can get download Link
            //final String menuId = editMenuId.getText().toString().trim();
            if (!editName.getText().toString().isEmpty() ) {
                newCategory = new Category(editName.getText().toString())
                        /*uri.toString()*/;
                categories.push().setValue(newCategory);
                Common.category=newCategory;
                mprogressDialog.dismiss();
                dialogAddNewFood.dismiss(); //Finish Dialog

                Snackbar.make(drawer,
                        newCategory.getName()+" "
                                + "تمت اضافة", Snackbar.LENGTH_SHORT)
                        .show();
              /*  categories.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {

                       *//* if (dataSnapshot.exists()) //State In Menu  Id Exist
                        {
                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                if (data.getKey().equals(menuId))
                                {
                                    //do ur stuff
                                    Toast.makeText(Home.this, "Menu Id is Exist !", Toast.LENGTH_LONG).show();
                                    editMenuId.setError("Menu Id is Exist Please Change ");
                                    mprogressDialog.dismiss();


                                } else {
                                    Toast.makeText(Home.this, "  مش موجود ", Toast.LENGTH_LONG).show();
                                }
                            }
                        }else //  in State in Menu Id Not Exist
                        {
                            Toast.makeText(Home.this, "Please Wait !", Toast.LENGTH_SHORT).show();*/


          /*      String imageName = UUID.randomUUID().toString();

                final StorageReference imageFoldar = storageReference.child("images/" + imageName);*/


                //-------------------------------Compress Thumb Image--------------------------------------------//
                //1-فى البدايه انا هضغط الصوره بمكتبه ضغط الصور وهحط الناتج فى الBitmap
                //2-ثم هضغط الصوره كا Bitmap وهاخد الناتج كا Byte فابالتالى هحتاج حاجه اخزن فيها عمليه التحويل فاهستخدم ByteArrayOutPUTStream
                //3-هخزن الByteArrayOutPutStream فى مصفوفه بايت عشان ابعتها للFirebasestorage
                //ملحوظه هيا بتتحول لByte ليه لان عشان انقل حاجه عن طريق الانترنت فا لازم تنقل كا بايت
                //هنحط الصوره فى File عشان نضغطها عن طريق مكتبه لضغط الصور لتقليل حجمها
                          // final File thump_filepathUri=new File(saveUri.getPath());
//             /**/ File file = new File(SiliCompressor.with(this).compress(FileUtils.getPath(this,saveUri)
//              ,new File(this.getCacheDir(),"temp")));
//              Uri uri = Uri.fromFile(file);
                         /*   try
                            {
                                String newfile=null;
                                byte[] thumb_byte_data;

                                thump_bitmap= new Compressor(this )
                                          .setMaxWidth(200)
                                       .setMaxHeight(200)
                                        .setQuality(50)

                                        .compressToFile(thump_filepathUri, String.valueOf(folder));//بديله مسار الصوره الحقيقيه اللى هيضغطها


                            }catch (IOException e)
                            {
                                e.printStackTrace();
                            }

                            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
////
//                         thump_bitmap.(Bitmap.CompressFormat.JPEG,50
//                                 ,byteArrayOutputStream);

                            final byte[] thump_byte=byteArrayOutputStream.toByteArray();*////كده معايا الصوره الحقيقيه مضغوطه والدقه بتاعتها 50%

                //--------------------------------------------------------------------

               /* imageFoldar.putFile(saveUri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mprogressDialog.dismiss();
                                //Toast.makeText(Home.this, "تم رفع صورة منتجك بنجاح", Toast.LENGTH_SHORT).show();

                                imageFoldar.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(final Uri uri) {

                                        newCategory = new Category(editName.getText().toString(),
                                                uri.toString());
                                        categories.push().setValue(newCategory);
                                        Common.category=newCategory;
                                        dialogAddNewFood.dismiss(); //Finish Dialog

                                        Snackbar.make(drawer,
                                                newCategory.getName()+" "
                                                        + "تمت اضافة", Snackbar.LENGTH_SHORT)
                                                .show();
                                       // sendNotificationOrder();
                                    }
                                });

                            } // Failure
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        mprogressDialog.dismiss();
                        Toast.makeText(Home.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        //Do not worry about this error
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                        mprogressDialog.setMessage("Uploaded" + progress + "%");
                        //Clear Value From saveUri
                        saveUri = null;
                    }
                });*/

                //------------------------------------------------------
            }
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

    //----------------Context Menu----------------------
    //Main Method select item from Context Menu
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {

/*
        if(item.getTitle().equals(Common.UPDATE))
        {
            //Method Update Product
            //showUpdateDialog(adapter.getRef(item.getOrder()),adapter.getRef(item.getOrder()).getKey());

          //   Toast.makeText(this, "getOrder+="+item.getOrder()+"---adapter.getItem"+adapter.getItem(item.getOrder()), Toast.LENGTH_LONG).show();
        }
        else*/ if (item.getTitle().equals(Common.DELETE))
        {
            //Method Delete  Product
            deleteCategoryDialog(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }
    // Delete Item Category
    private void deleteCategoryDialog(final String key)
    {
        //Delete Category and All Food in Category by menuId
        //حذف الصف من الفئات وكمان يتحذف معاه اللى واخد رقم الId بتاعه من قائمه الطعام فى الاكتيفيتى التانيه
        // first we need get all Foods
        DatabaseReference foods=database.getReference(Common.FOODS);
        DatabaseReference categoriesDelete=database.getReference(Common.CATEGORY);
       // DatabaseReference banner=database.getReference(Common.BANNER);

        //هيرجع بكل عمود ال menuId بتاعته بتساوى الkey
        Query foodInCategory=foods.orderByChild("menuId").equalTo(key);//ارجع ب الصف اللى الMenuId بتاعه بيساوى الKey
      //  Query foodInBanner= banner.orderByChild("menuId").equalTo(key);
/*

        foodInBanner.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    //Remove From Data equal Sam MenuId
                    snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(Home.this, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        //Table "Food"
        foodInCategory.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {


                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    //Remove From Data equal Sam MenuId
                    snapshot.getRef().removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(Home.this, "Success Remove From DB", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        categoriesDelete.orderByKey().equalTo(key).addListenerForSingleValueEvent
                (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot snapshot:dataSnapshot.getChildren())
                {
                    mprogressDialog.setMessage("انتظر من فضلك");
                    mprogressDialog.show();

                    Category category=new Category();
                    category=snapshot.getValue(Category.class);

                    categories.child(key).removeValue();
                    mprogressDialog.dismiss();

                   /* //---Split Name from Link-------//
                    String [] linkNameImage=category.getImage().split("images%2F");
                    String [] towString=linkNameImage[1].split("\\?alt");
                    String nameImage=towString[0];

                    //------------------Delete Image From Firebase----------//
                    // Create a storage reference from our app
                    StorageReference storageRef = storage.getReference();
                    // Create a reference to the file to delete
                    StorageReference desertRef = storageRef.child("images/"+nameImage);
                    // Delete the file
                    desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid)
                        {
                            Toast.makeText(Home.this, "تم الحذف بنجاح", Toast.LENGTH_SHORT).show();
                            //Table "Category"
                            categories.child(key).removeValue(); //Delete Item From Firebase
                            mprogressDialog.dismiss();
                           // Toast.makeText(getApplicationContext(), "Item Deleted DB!!!", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception)
                        {
                            Toast.makeText(Home.this, "Failure"+exception.getMessage(), Toast.LENGTH_SHORT).show();
                            mprogressDialog.dismiss();
                        }
                    });*/

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                mprogressDialog.dismiss();
            }
        });



    }

    //Dialog Shaw Update Item Category
   /* private void showUpdateDialog(final DatabaseReference item, String key)
    {

        dialogUpdateItem=new Dialog(this);
        dialogUpdateItem.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialogUpdateItem.setContentView(R.layout.update_category_dialog);

        //-----------Id--------------//
        editTextNameUpdate =(TextInputEditText)dialogUpdateItem.findViewById(R.id.productName);
        btnSelectUPdate=(Button)dialogUpdateItem.findViewById(R.id.btnupdateproduct);
        btnUploadUpdate=(Button)dialogUpdateItem.findViewById(R.id.addbuttonproduct);
        ImageView btnClose=(ImageView)dialogUpdateItem.findViewById(R.id.closeimage);
DatabaseReference category = item.child(key);
        //set Defaulet Name
        editTextNameUpdate.setText(item.getName());
//        textMenuIdUpdate.setText(key);

        //--------Event Button-------------------//
        btnSelectUPdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                chooseImageItem();// let user Select Image from Gallery and save Uri this Image
                sharedPreferences.edit().putBoolean("btnSelectUPdate",true).apply();
            }
        });

        //-----Event Button-----------------//
        btnUploadUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                changeImageItem(item); //The intended item

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialogUpdateItem.dismiss();
            }
        });


        dialogUpdateItem.show();
    }*/

    //Upload "Update Category"
   /* private void changeImageItem(final Category item)
    {

        if (saveUri !=null)
        {


            mprogressDialog.setMessage("Uploading...");
            mprogressDialog.show();

            if (!editTextNameUpdate.getText().toString().isEmpty())
            {

                String imageName= UUID.randomUUID().toString(); //Generate Random Name

                final StorageReference imageFoldar=storageReference.child("images/"+imageName);// Create Name For Folder in Firebase Storage

                //-------------------------------Compress Thumb Image--------------------------------------------//
                //1-فى البدايه انا هضغط الصوره بمكتبه ضغط الصور وهحط الناتج فى الBitmap
                //2-ثم هضغط الصوره كا Bitmap وهاخد الناتج كا Byte فابالتالى هحتاج حاجه اخزن فيها عمليه التحويل فاهستخدم ByteArrayOutPUTStream
                //3-هخزن الByteArrayOutPutStream فى مصفوفه بايت عشان ابعتها للFirebasestorage
                //ملحوظه هيا بتتحول لByte ليه لان عشان انقل حاجه عن طريق الانترنت فا لازم تنقل كا بايت
                //هنحط الصوره فى File عشان نضغطها عن طريق مكتبه لضغط الصور لتقليل حجمها
                final File thump_filepathUri=new File(saveUri.getPath());

              *//*  try
                {
                    thump_bitmap= new Compressor(Home.this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(50)
                            .compressToBitmap(thump_filepathUri);//بديله مسار الصوره الحقيقيه اللى هيضغطها

                }catch (IOException e)
                {
                    e.printStackTrace();
                }

                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

                thump_bitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArrayOutputStream);*//*

                //final byte[] thump_byte=byteArrayOutputStream.toByteArray();//كده معايا الصوره الحقيقيه مضغوطه والدقه بتاعتها 50%

                //---------------------------------------------------------------------//
                imageFoldar.putFile(saveUri)//Save Image IN Firebase Storage
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>()
                        {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                            {
                                mprogressDialog.dismiss();
                                Toast.makeText(Home.this, "Uploaded !!!", Toast.LENGTH_SHORT).show();

                                imageFoldar.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() // Download  Uri Image From Firebase Storage
                                {
                                    @Override
                                    public void onSuccess(Uri uri)
                                    {
                                        //SET  Value newCategory if image Uploaded can get download Link
                                        Category category=new Category();
                                    category.setImage(uri.toString()); // send Uri for Model Category
                                   // category.setMenuId(textMenuIdUpdat)e.getText().toString());
                                    category.setName(editTextNameUpdate.getText().toString().trim());//get new name item send into  name for Model Category

                                        Map updateData=new HashMap();
                                        updateData.put("image",uri.toString());
                                        //updateData.put("menuId",textMenuIdUpdate.getText().toString());
                                        updateData.put("name",editTextNameUpdate.getText().toString().trim());

                                        //Update Information
                                        categories
                                                .updateChildren(updateData)
                                                .addOnSuccessListener(new OnSuccessListener() {
                                                    @Override
                                                    public void onSuccess(Object o)
                                                    {
                                                        Toast.makeText(Home.this, "Success Update", Toast.LENGTH_SHORT).show();
                                                        btnSelectUPdate.setText("Select Image");
                                                        dialogUpdateItem.dismiss();
                                                        saveUri=null;

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e)
                                                    {
                                                        Toast.makeText(Home.this, "Failure Update", Toast.LENGTH_LONG).show();
                                                        btnSelectUPdate.setText("select Image");
                                                        saveUri=null;
                                                        mprogressDialog.dismiss();
                                                    }
                                                }); //send model referance  into Firebase database
                                    }
                                });


                            } // Failure
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        mprogressDialog.dismiss();
                        //Clear Value From saveUri
                        saveUri=null;
                        btnSelectUPdate.setText("Select Image");
                        Toast.makeText(Home.this, "No data Update !", Toast.LENGTH_SHORT).show();

                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot)
                    {
                        //Do not worry about this error
                        double progress=(100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());

                        mprogressDialog.setMessage("Uploaded"+progress+"%");
                        //Clear Value From saveUri
                        saveUri=null;

                    }
                });

            }else
            {
                editTextNameUpdate.setError("Please Enter Name Category !");
                mprogressDialog.dismiss();
            }

        }else
        {
            Toast.makeText(this, "Please Select First Image", Toast.LENGTH_SHORT).show();
        }

    }*/


    //------------------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

      /*  if (requestCode == Common.PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            saveUri = data.getData();
          btnSelect.setText("تم اختيار الصورة");
        }*/

    }





    //----------------------------------
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       if (id == R.id.nav_orders)
        {

            Intent order=new Intent(Home.this,OrderStatues.class);
            startActivity(order);


        }  else if (id == R.id.nav_logout)
        {
            Paper.book().destroy();

            Intent intentMainActivity=new Intent(Home.this,MainActivity.class);
            startActivity(intentMainActivity);
            finish();

        }
      /* else if (id==R.id.chat)
       {
           Intent intent=new Intent(Home.this,ChatActivity.class);
           startActivity(intent);
       }*/
       /*else if (id == R.id.nav_ShippersManagement) {

            Intent shippersManagement=new Intent(Home.this,ShiperMangement.class);
            startActivity(shippersManagement);
        }else if (id == R.id.nav_success_request) {

            Intent successfulShipperRequest=new Intent(Home.this,
                    SuccessfulDeliveryRequest.class);
            startActivity(successfulShipperRequest);
        }*/



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
   /*     if (id == R.id.Private_police)
        {


            Intent intent;
            Toast.makeText(Home.this, "privacy-policy", Toast.LENGTH_LONG).show();

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wonderful-goldberg-f3ff4e.netlify.com/"));
            startActivity(intent);
        }*/

        return super.onOptionsItemSelected(item);
    }

}
