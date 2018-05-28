package com.example.diu.firebaseapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.diu.firebaseapp.R;
import com.example.diu.firebaseapp.ModelClass.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {

    private EditText inputName,inputEmail,inputPhone,inputPassword,inputVerificationCode;
    private Button signUp,verify;
    private String user_name,user_email,user_phone,user_password,verificationCode;
    private LinearLayout mCodeLayout,mPhoneLayout;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabase;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        //Get Firebase Database instance
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        //Get Firebase auth instance
        firebaseAuth=FirebaseAuth.getInstance();
        getView();
        phoneAuthCallbacks();
        userRegistration();
        verifyUser();


    }

    public void getView(){
        mCodeLayout=(LinearLayout)findViewById(R.id.verification_layout);
        mPhoneLayout=(LinearLayout)findViewById(R.id.signup_layout);
        inputName=(EditText)findViewById(R.id.user_name);
        inputEmail=(EditText)findViewById(R.id.user_email);
        inputPhone=(EditText)findViewById(R.id.user_phone);
        inputPassword=(EditText)findViewById(R.id.user_password);
        inputVerificationCode=(EditText)findViewById(R.id.verification_code);
        signUp=(Button)findViewById(R.id.sign_up);
        verify=(Button)findViewById(R.id.verify);
    }



    //phone auth callbacks
    public void phoneAuthCallbacks(){
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Toast.makeText(RegistrationActivity.this, "Verification complete", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Toast.makeText(RegistrationActivity.this, "Verification failed"+e, Toast.LENGTH_LONG).show();
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    inputPhone.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded

                }

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Toast.makeText(RegistrationActivity.this, "Code sent check message", Toast.LENGTH_SHORT).show();

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                mPhoneLayout.setVisibility(View.GONE);
                mCodeLayout.setVisibility(View.VISIBLE);


            }
        };
    }

  //send verification code to phone
    public boolean sendVerificationCode(){
        user_phone=inputPhone.getText().toString();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                user_phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);
        return true;
    }


    //verification and match verification code
    private void verifyPhoneNumberWithCode() {
        verificationCode=inputVerificationCode.getText().toString();
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,verificationCode);
        signUpWithCradential(credential);

    }

    //signupwithcradential
    public void signUpWithCradential(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Verify", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
                    startActivity(intent);

                }
                else {
                    Toast.makeText(RegistrationActivity.this, "verification failled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //add users information in realtime database
    public void addUssers(){
        user_name=inputName.getText().toString();
        user_email=inputEmail.getText().toString();
        user_phone=inputPhone.getText().toString();
        user_password=inputPassword.getText().toString();

        Date date=new Date();
        long time=date.getTime();
        if(!TextUtils.isEmpty(user_email) && !TextUtils.isEmpty(user_phone)&&!TextUtils.isEmpty(user_password)){
            String id=mDatabase.push().getKey();
            Users users=new Users(user_name,user_email,user_phone,user_password,time);
            users.setId(id);
            mDatabase.child(id).setValue(users);
        }
        else {
            Toast.makeText(this, "You should fill the field", Toast.LENGTH_SHORT).show();
        }
    }
    //sign up with firebase auth
    public void signUpWithAuth(){
        if (user_password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }


        (firebaseAuth.createUserWithEmailAndPassword(user_email,user_password)).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(RegistrationActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                if(!task.isSuccessful()){
                    Toast.makeText(RegistrationActivity.this, "Sign Up failed", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "SignUp successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //registratiion
    public void userRegistration(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendVerificationCode()){
                    Toast.makeText(RegistrationActivity.this, "Verification Code send", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(RegistrationActivity.this, "not send verification code", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    //verify user
    public void verifyUser(){
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyPhoneNumberWithCode();
                addUssers();
                signUpWithAuth();
            }
        });
    }
}
