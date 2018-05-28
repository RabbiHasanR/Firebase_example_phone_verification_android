package com.example.diu.firebaseapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.diu.firebaseapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private AppCompatButton signup,signIn;
    private AppCompatEditText inputEamil,inputPassword;
    //private String email,password;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth=FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        getView();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(".RegistrationActivity");
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSginIn();
            }
        });


    }

    public void getView(){
        inputEamil=(AppCompatEditText)findViewById(R.id.email);
        inputPassword=(AppCompatEditText)findViewById(R.id.password);
        signup=(AppCompatButton)findViewById(R.id.signUp);
        signIn=(AppCompatButton)findViewById(R.id.signIn);

    }

    //user sign in with firebase authentication
    public void userSginIn(){
        String email=inputEamil.getText().toString();
        final String password=inputPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }
        (firebaseAuth.signInWithEmailAndPassword(email,password)).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    // there was an error
                    if (password.length() < 6) {
                        inputPassword.setError("Password minimum length is 6");
                    } else {
                        Toast.makeText(LoginActivity.this, "wrong email or password", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(LoginActivity.this, "Sign in Successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error is:"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    //retrive data from firebase real time databas
    /*public void showData(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
                        ArrayList<Users> users=new ArrayList<>();
                        Users users1 = null;
                        for(DataSnapshot issues:dataSnapshot.getChildren()){
                            users1=issues.getValue(Users.class);

                        }
                        if(users1.getEmail().equals(email) && users1.getPassword().equals(password)){
                            Toast.makeText(LoginActivity.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(".ProfileActivity");
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(LoginActivity.this, "Wrong email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "please give email and password", Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                    Toast.makeText(LoginActivity.this, "Database does not exits", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/
}
