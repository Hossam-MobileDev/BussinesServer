package com.hashtagco.bussinesserver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
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


public class SignIn extends AppCompatActivity {

    EditText editemail, editPassword;
    Button buttonSignIn , buttonSignUp;
    public static final int REQUEST_CODE = 1;
     String localPassword;
    String pass;
    Admin admin;
    FirebaseDatabase db;
    DatabaseReference admins;
    FirebaseAuth firebaseAuth;
    CheckBox checkBox;
    private SharedPreferences sharedPref;
    public static final String MyPREFERENCES = "MyPrefs" ;

    ProgressDialog mDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Paper.init(this);

        editemail = findViewById(R.id.edit_email);
        editPassword = findViewById(R.id.edit_Password);
        buttonSignIn = findViewById(R.id.button_signin);
        buttonSignUp=findViewById(R.id.button_signup);
        checkBox=findViewById(R.id.checkboxremeber);

        sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

buttonSignUp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(SignIn.this,SignUpActivity.class);
//intent.putExtra("admin",admin);
startActivityForResult(intent,REQUEST_CODE);
    }
});
        //Init Firebase
        db = FirebaseDatabase.getInstance();
        admins = db.getReference("Admin");
firebaseAuth = FirebaseAuth.getInstance();
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInUser();
            }
        });
    }

    //signInUser() method
    private void signInUser() {
        String mail = editemail.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();
        if (mail.isEmpty()) {
            editemail.setError("برجاء كتابة ايميلك ");
            editemail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            editemail.setError("ايميلك غير صحيح");
            editemail.requestFocus();
            return;
        }
        if (pass.isEmpty() && pass.length()<6) {
            editPassword.setError("الباسورد فارغ");
            editPassword.requestFocus();
            return;
        }
        if (checkBox.isChecked()){
            Paper.book().write(Common.USER_KEY,editemail.getText().toString());
            Paper.book().write(Common.PWD_KEY,editPassword.getText().toString());
        }

        firebaseAuth.signInWithEmailAndPassword(mail, pass).addOnSuccessListener(aVoid ->
                {
                    mDialog = new ProgressDialog(SignIn.this);
                    mDialog.setMessage("انتظر من فضلك ...");
                    mDialog.show();
                    admins.addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Check if user not exist in Database
                        if(dataSnapshot.
                                child(firebaseAuth.getCurrentUser().getUid()).exists()) {
                              //Get user information
                                mDialog.dismiss();
                                Admin admin = dataSnapshot
                                        .child
                                                (firebaseAuth.getCurrentUser().getUid())
                                        .getValue(Admin.class);
                                admin.setEmail(editemail.getText().toString());//set phone
                            admin.setId(admin.getId());
                            admin.setPassword(editPassword.getText().toString());
                                if (admin.getEmail().equals(editemail.getText().toString())) {

                                    //Toast.makeText(SignIn.this, "تم تسجيل!!!", Toast.LENGTH_SHORT).show();
                                    Intent homeIntent =
                                            new Intent(SignIn.this, Home.class);
                                    Common.currentAdmin = admin;
                                    startActivity(homeIntent);
                                    finish();
                                } else {

                                    Toast.makeText(SignIn.this, "Wrong password!!!", Toast.LENGTH_SHORT).show();
                              }
                         }else {
                            mDialog.dismiss();
                               Toast.makeText(SignIn.this, "User not exist !", Toast.LENGTH_SHORT).show();
                           }
                       }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
        ).addOnFailureListener(e -> Toast.makeText(SignIn.this, "Registration Failed", Toast.LENGTH_LONG)
                .show());
        //mDialog.dismiss();
        // loadingProgressBar.setVisibility(View.INVISIBLE);

        // startActivity(new Intent(Sign_up.this, Book.class));
        // finish();
    }
    }

