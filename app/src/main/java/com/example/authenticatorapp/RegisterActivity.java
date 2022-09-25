package com.example.authenticatorapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    //private Button bt;
    EditText mFullName,mPass,mPhone,mEmail;
    Button mRegisterbtn;
    TextView mLoginhere;
    FirebaseAuth fAuth;
    ProgressBar progressbar;
    FirebaseFirestore fStore;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFullName=findViewById(R.id.fullname);
        mPass=findViewById(R.id.pass);
        mEmail=findViewById(R.id.emaillogin);
        mPhone=findViewById(R.id.phno);
        mRegisterbtn=findViewById(R.id.registerbtn);

        fAuth=FirebaseAuth.getInstance();
        fStore=FirebaseFirestore.getInstance();
        progressbar=findViewById(R.id.progressBar2);


        if(fAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        mRegisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
//                startActivity(intent);

                String Email=mEmail.getText().toString();
                String Password = mPass.getText().toString();
                String Phone = mPhone.getText().toString();
                String FullName = mFullName.getText().toString();

                if(TextUtils.isEmpty(Email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(Password)){
                    mPass.setError("Password is Required");
                    return;
                }if(TextUtils.isEmpty(FullName)){
                    mPass.setError("Name is Required");
                    return;
                }if(TextUtils.isEmpty(Phone)){
                    mPass.setError("Phone Number is Required");
                    return;
                }if(TextUtils.isEmpty(Password)){
                    mPass.setError("Password is Required");
                    return;
                }
                if(Password.length()<6){
                    mPass.setError("Password should be mininum 6 characters");
                    return;
                }
                if(Phone.length()!=10){
                    mPhone.setError("Phone Number should be 10 Characters");
                    return;
                }
                progressbar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                            userId=fAuth.getCurrentUser().getUid();
                            DocumentReference docRef = fStore.collection("users").document(userId);
                            Map<String,Object> user = new HashMap<>();
                            user.put("FullName",FullName);
                            user.put("Email",Email);
                            user.put("PhoneNumber",Phone);
                            docRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d(TAG,"OnSuccess : User profile is created for "+userId);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"OnFailure : Error"+e.toString());
                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Error Occured"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressbar.setVisibility(View.GONE);

                        }
                    }
                });
            }
        });
    }

    public void Login(View view) {
        mLoginhere=findViewById(R.id.loginhere);
        mLoginhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }

}