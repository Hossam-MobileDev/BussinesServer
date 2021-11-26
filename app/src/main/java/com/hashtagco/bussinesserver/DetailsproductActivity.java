package com.hashtagco.bussinesserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Model.SubFoods;
import com.hashtagco.bussinesserver.databinding.ActivityDetailsproductBinding;

import java.util.UUID;

public class DetailsproductActivity extends AppCompatActivity {
private ActivityDetailsproductBinding binding;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase db;
    DatabaseReference foodList;
    Uri saveUri;
    SubFoods subFoods;
    String foodid;
Dialog dialogAddNewName , dialogAddType;
Button btnAddName;
    private ImageView btnCloase , btnCloasee;
    private TextInputEditText editName , editType;
     Button btnAddType;
    String type;
    String name;
    public static final int SIZES = 100;
    String smfromm;
    String stoo ;
    String mfroom ;
    String mtoo ;
    String mplusfrom;
    String mplustoo ;
    String lfroom ;
    String ltoo ;
    String lplusfroom ;
    String lplustoo ;
    String xlfroom ;
    String xltoo ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDetailsproductBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        db = FirebaseDatabase.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        foodList = db.getReference("SubFoods");
        foodid = getIntent().getStringExtra("FoodId");
       
        binding.btnAddSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent
                        (DetailsproductActivity.this,SizesActivity.class),SIZES);
            }
        });
binding.btnSanf.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showDialogName();
    }
});
binding.btnType.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showDialogType();
    }
});
        binding.btnSelectAnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        binding.btnUploadAnf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    uploadImage();
                    //Toast.makeText(DetailsproductActivity.this, "تم اضافة التفاصيل بنجاح", Toast.LENGTH_SHORT).show();
                   // Common.subfooood = subFoods;
finish();
                }
            });

    }

    private void uploadImage() {
        if (saveUri != null ) {
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
                       //     mDialog.dismiss();
                           // Toast.makeText(DetailsproductActivity.this, "تم تحميل الصورة بنجاح", Toast.LENGTH_SHORT).show();
                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //set value for newCategory if image upload and we can get download link
                                    subFoods = new SubFoods();
                                    subFoods.setFoodId(foodid);
subFoods.setSmallto(stoo);
//subFoods.setSmallfrom(smfromm);
subFoods.setMeduimfrom(mfroom);
subFoods.setMeduimto(mtoo);
subFoods.setMplusfrom(mplusfrom);
subFoods.setMplusto(mplustoo);
subFoods.setLargeto(ltoo);
subFoods.setLargefrom(lfroom);
subFoods.setLargeplusto(lplustoo);
subFoods.setLargeplusfrom(lplusfroom);
subFoods.setXlargeto(xltoo);
subFoods.setXlargfrom(xlfroom);
                                    subFoods.setImage(uri.toString());
                                    subFoods.setName(name);
                                    subFoods.setType(type);
                                  //  Common.subfooood=subFoods;
                                    foodList.child(foodid).setValue(subFoods);
                                    //  newFood.setName(editName.getText().toString());
                               //     subFoods.setName(binding.name.getText().toString());
/*subFoods.setPrice(binding.price.getText().toString());
subFoods.setType(binding.type.getText().toString());
subFoods.setSmallfrom(binding.sfroom.getText().toString());
subFoods.setSmallto(binding.stoo.getText().toString());
subFoods.setLargeto(binding.ltoo.getText().toString());
subFoods.setLargefrom(binding.lfroom.getText().toString());
subFoods.setLargeplusto(binding.lplustoo.getText().toString());
subFoods.setLargeplusfrom(binding.lplusfroom.getText().toString());
subFoods.setXlargeto(binding.xltoo.getText().toString());
subFoods.setXlargfrom(binding.xlfroom.getText().toString());*/


                                   // finish();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(DetailsproductActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            int progress = (int) (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("جار التحميل : " + progress + "%");
                        }
                    });
        }
         else  if
             (name!=null&&type!=null&&foodid!=null){
            subFoods.setName(name);
            subFoods.setType(type);
            subFoods.setFoodId(foodid);
            subFoods.setSmallto(stoo);
//subFoods.setSmallfrom(smfromm);
            subFoods.setMeduimfrom(mfroom);
            subFoods.setMeduimto(mtoo);
            subFoods.setMplusfrom(mplusfrom);
            subFoods.setMplusto(mplustoo);
            subFoods.setLargeto(ltoo);
            subFoods.setLargefrom(lfroom);
            subFoods.setLargeplusto(lplustoo);
            subFoods.setLargeplusfrom(lplusfroom);
            subFoods.setXlargeto(xltoo);
            subFoods.setXlargfrom(xlfroom);
            //  Common.subfooood=subFoods;
            foodList.child(foodid).setValue(subFoods);
        }
else Toast.makeText(DetailsproductActivity.this,"ادخل صورة من فضلك",Toast.LENGTH_SHORT).show();
    }
//chooseImage() method
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture")
                , Common.PIC_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Common.PIC_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            saveUri = data.getData();
            Toast.makeText(DetailsproductActivity.this,
                    "تم اضافة صورة", Toast.LENGTH_SHORT).show();
            // uploadImage();
            // btnSelect.setText("تم اختيار الصورة");
        } else if (requestCode == SIZES) {
            if (resultCode == RESULT_OK) {
                //smfromm = data.getStringExtra("smfromm");

                Toast.makeText(DetailsproductActivity.this, "تم اضافة المقاسات", Toast.LENGTH_SHORT).show();
                stoo = data.getStringExtra("stoo");
                mfroom = data.getStringExtra("mfroom");
                mtoo = data.getStringExtra("mtoo");
                mplusfrom = data.getStringExtra("mplusfrom");
                mplustoo = data.getStringExtra("mplustoo");
                lfroom = data.getStringExtra("lfroom");
                ltoo = data.getStringExtra("ltoo");
                lplusfroom = data.getStringExtra("lplusfroom");
                lplustoo = data.getStringExtra("lplustoo");
                xlfroom = data.getStringExtra("xlfroom");
                xltoo = data.getStringExtra("xltoo");


            } else Toast.makeText(DetailsproductActivity.this, "فارغة", Toast.LENGTH_SHORT).show();
        }
    }
    private void showDialogName()
    {
        dialogAddNewName=new Dialog(this);
        dialogAddNewName.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialogAddNewName.setContentView(R.layout.add_new_name);
         btnAddName = (Button) dialogAddNewName.findViewById(R.id.btn_Dialog_name);

        btnCloase = (ImageView) dialogAddNewName.findViewById(R.id.close_dialog_category);
        //-----------Id------------------------//
        editName =(TextInputEditText)dialogAddNewName.findViewById(R.id.edit_name_Dialog);


   // Toast.makeText(DetailsproductActivity.this,"تمت اضافة الاسم " ,Toast.LENGTH_SHORT).show();





        btnAddName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadImage();
                subFoods = new SubFoods();
           //     subFoods.setFoodId(foodid);


                name = editName.getText().toString();
                subFoods.setName(name);
              //  Common.subfooood = subFoods;

                Toast.makeText(DetailsproductActivity.this,
                        subFoods.getName() + " تمت اضافة اسم الصنف ",
                        Toast.LENGTH_SHORT).show();
             //   finish();
                dialogAddNewName.dismiss();
            }
        });


        btnCloase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddNewName.dismiss();
            }
        });


        dialogAddNewName.show();
    }
    private void showDialogType()
    {
        dialogAddType=new Dialog(this);
        dialogAddType.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialogAddType.setContentView(R.layout.add_new_type);
        btnAddType = (Button) dialogAddType.findViewById(R.id.btn_Dialog_type);

             btnCloasee = (ImageView) dialogAddType.findViewById(R.id.close_dialog_category);
        //-----------Id------------------------//
        editType =(TextInputEditText)dialogAddType.findViewById(R.id.edit_type_Dialog);


       // Toast.makeText(DetailsproductActivity.this,"تمت اضافة نوع " ,Toast.LENGTH_SHORT).show();





        btnAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadImage();
                subFoods = new SubFoods();
              //  subFoods.setFoodId(foodid);
                 type = editType.getText().toString();
              //  Common.subfooood = subFoods;

                Toast.makeText(DetailsproductActivity.this,
                        type + " تمت اضافة النوع ",
                        Toast.LENGTH_SHORT).show();
dialogAddType.dismiss();
            }
        });


        btnCloasee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddType.dismiss();
            }
        });


        dialogAddType.show();
    }
}