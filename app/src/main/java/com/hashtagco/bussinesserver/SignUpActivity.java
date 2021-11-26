package com.hashtagco.bussinesserver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.CommonNotificationBuilder;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Model.Admin;

public class SignUpActivity extends AppCompatActivity {
private EditText newemail , secretno,oldpassword;
    Admin admin ;
    FirebaseDatabase db;
    FirebaseAuth auth;

    DatabaseReference admins;
Button btnsignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        newemail = findViewById(R.id.ediEmail);
        oldpassword = findViewById(R.id.oldpass);
        secretno = findViewById(R.id.secretno);
        btnsignup=findViewById(R.id.btn_signUp);
        auth = FirebaseAuth.getInstance();

        db = FirebaseDatabase.getInstance();
        admins = db.getReference("Admin");

       //admin= (Admin) getIntent().getSerializableExtra("admin");

/*
        admins.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("1234").exists()){
                  //  mDialog.dismiss();
                    Admin      admin = dataSnapshot.child("1234").getValue(Admin.class);

                    //user.setPhone(localPhone);
                    admin.setPassword(localPassword);
                    admin.setEmail(localemail);
                    // pass =admin.getPassword();
                    // admin.setIsStaff("true");
                    if(admin.getEmail().equals(localemail)){ //isStaff == true
                        if(admin.getPassword().equals(localPassword)){
                            Intent intentHome = new Intent(SignIn.this, Home.class);
                            Common.currentAdmin = admin;
                            startActivity(intentHome);
                            finish();
                        }
                        else{
                            Toast.makeText(SignIn.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(SignIn.this, "Please login with Staff Account!!!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    mDialog.dismiss();
                    //Toast.makeText(SignIn.this, "User not exist in Database!!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

//        String data = getIntent().getExtras().getString("pass","defaultKey");
//Toast.makeText(SignUpActivity.this,"data"+data,Toast.LENGTH_SHORT).show();
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signup();


            }
        });
    }

    private void signup() {

        String email = newemail.getText().toString().trim();
        String password = oldpassword.getText().toString().trim();

        if(email.isEmpty()){
            newemail.setError("برجاء كتابة الايميل");
        }
        if (password.isEmpty()&& password.length()<6) {
            oldpassword.setError("برجاء كتابة الباسورد ولا يقل عن 6 ");
            oldpassword.requestFocus();

        }
        String secre = secretno.getText().toString();
        //  int secree=Integer.parseInt(secre);

        if (secre.isEmpty()) {
            secretno.setError("غير مصرح لك");
            secretno.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            newemail.setError("ايميلك غير صحيح");
            newemail.requestFocus();
            return;
        }

        final ProgressDialog mDialog = new ProgressDialog(SignUpActivity.this);
        mDialog.setMessage("انتظر من فضلك ...");
        mDialog.show();

         auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    //save user to the database
                 Admin  admin = new Admin();
                    admin.setPassword(password);
                    admin.setEmail(email);
                    admin.setSecretno(2021);
                    admin.setId(auth.getCurrentUser().getUid());
                    Common.currentAdmin=admin;

                    if(Integer.parseInt(secretno.getText().toString())!=2021){
                        Toast.makeText(SignUpActivity.this
                                ,"عفوا غير مصرح لك الدخول "
                                ,Toast.LENGTH_SHORT).show();
                    }


                    // user.setPassword(password);
                  //  admin.setIsStaff("false");
                //    user.setUid(auth.getCurrentUser().getUid());
//Common.currentUser=user;
                    //    loadingProgressBar.setVisibility(View.VISIBLE);

                    //use phone as key

               else     admins.child(auth.getCurrentUser().getUid())
                            .setValue(admin).addOnSuccessListener(aVoid -> {

                                    Intent intent =
                                            new Intent(SignUpActivity.this
                                                    , SignIn.class);
                                    //  intent.putExtra("bundle",user);
                                    //  Common.currentUser = user;
                                    startActivity(intent);

                            }     /*  Intent intent = new Intent(SignIn.this,Home.class)
                            startActivity(intent)*/
                    )
                            .addOnFailureListener
                                    (e -> Toast.makeText(SignUpActivity.this,
                                            "غير مصرح لك الدخول", Toast.LENGTH_LONG)
                                    .show());
                    mDialog.dismiss();
                    // loadingProgressBar.setVisibility(View.INVISIBLE);

                    // startActivity(new Intent(Sign_up.this, Book.class));
                    // finish();
                })
                .addOnFailureListener
                        (e -> Toast.makeText(SignUpActivity.this
                                , "عفوا الايميل مستحدم : " +
                                        e.getMessage(),
                        Toast.LENGTH_LONG).show());;


    }
    }


