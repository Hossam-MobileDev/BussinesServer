package com.hashtagco.bussinesserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hashtagco.bussinesserver.Common.Common;
import com.hashtagco.bussinesserver.Model.Admin;
import com.hashtagco.bussinesserver.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    ProgressDialog mDialog;

    //----------------------
    private Typeface typeface;
    //-----------------------------
    //------------------------------

    public static final String MyPREFERENCES = "MyPrefs" ;
    FirebaseDatabase db;
    DatabaseReference admins;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        //--------------------------------Id----------------

        //---------------Init---------------//
        db = FirebaseDatabase.getInstance();
        admins = db.getReference("Admin");
        firebaseAuth = FirebaseAuth.getInstance();


        String email = Paper.book().read(Common.USER_KEY);
        String pass=Paper.book().read(Common.PWD_KEY);
        if(email!=null&&pass!=null){
            if(!email.isEmpty()&&!pass.isEmpty()){
                login(email,pass);
            }
        }
        else {
            new Handler().postDelayed(new Runnable() {

// Using handler with postDelayed called runnable run method

                @Override

                public void run() {

                    Intent i = new Intent(MainActivity.this, SignIn.class);

                    startActivity(i);

                    // close this activity

                    finish();

                }

            }, 5*1000);
        }

        //---------------Custom Font----------------//
        typeface=Typeface.createFromAsset(getAssets(),"fonts/NABILA.TTF");


        //------------------------Action----------------------//










   /* private void login(String email, String pass)
    {
        if (Common.isConnectedToInternet(getBaseContext()))
        {
            progressDialog.setMessage("انتظر من فضلك ...");
            progressDialog.show();

            table_User.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    //Check if user not exist in database بمعنى هتأكد هل المستخدم موجود فى قاعده البيانات ولا لا الاول
                    if (dataSnapshot.child(phone).exists()) {//لو موجود هجيب البيانات الخاصه بيه
                        //Get User Information
                        progressDialog.dismiss();
                        //Get Data From Database by Key
                        User user = dataSnapshot.child(phone).getValue(User.class);//Phone Number Is Primary Key
                        user.setPhone(phone); //set Phone

                        if (user.getPassword().equals(pwd))//I am sure of the password
                        {
                            Toast.makeText(MainActivity.this, "تم دخولك بنجاح !", Toast.LENGTH_SHORT).show();
                            Intent homeIntent=new Intent(MainActivity.this,Home.class);
                            Common.currentUser=user;
                            startActivity(homeIntent);
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "هذا المستخدم غير موجود !", Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, "هذا المستخدم غير موجود !", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else
        {
            Toast.makeText(MainActivity.this, "انت غير متصل بالانترنت !", Toast.LENGTH_SHORT).show();
            return;
        }
    }*/









}

    private void login(String email, String pass) {
        firebaseAuth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(aVoid ->
                {
                    mDialog = new ProgressDialog(MainActivity.this);
                    mDialog.setMessage("انتظر من فضلك ...");
                    mDialog.show();
                    admins.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Check if user not exist in Database
                            if(dataSnapshot.child(firebaseAuth.getCurrentUser().getUid()).
                                    exists()) {
                                //Get user information
                                mDialog.dismiss();
                                Admin admin = dataSnapshot.child(firebaseAuth.
                                        getCurrentUser().
                                        getUid())
                                        .getValue(Admin.class);
                                admin.setEmail(email);//set phone
                                if (admin.getEmail().equals(email)) {

                                    //Toast.makeText(SignIn.this, "تم تسجيل!!!", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent =
                                            new Intent(MainActivity.this,
                                                    Home.class);
                                   // Common.currentUser = user;
Common.currentAdmin=admin;
                                    startActivity(homeIntent);
                                    finish();
                                } else {

                                    Toast.makeText(MainActivity.this, "Wrong password!!!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                mDialog.dismiss();
                                Toast.makeText(MainActivity.this, "User not exist !", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
        ).addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_LONG)
                .show());
    }
    }
