package com.example.notesapp.Acitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityRegistrationAcitvityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RegistrationAcitvity extends AppCompatActivity {

    ActivityRegistrationAcitvityBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistrationAcitvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog=new ProgressDialog(this);
        dialog.setTitle("Creating account");
        dialog.setMessage("Please wait..");

        auth=FirebaseAuth.getInstance();
        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=binding.loginEmail.getText().toString().trim();
                String password=binding.loginPassword.getText().toString().trim();

                if (email.isEmpty())
                {
                    binding.loginEmail.setError("Please enter Your email");
                    binding.loginEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    binding.loginEmail.setError("Enter valid Email");
                    binding.loginEmail.requestFocus();
                }else if (password.isEmpty())
                {
                    binding.loginPassword.setError("please enter your password");
                    binding.loginPassword.requestFocus();
                }else {

                    dialog.show();

                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful()){

                                Toast.makeText(getApplicationContext(), "Account created", Toast.LENGTH_SHORT).show();
                                sendEmailVarification();
                            }else {
                                Toast.makeText(RegistrationAcitvity.this, "Fail to Register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

    }
    public void sendEmailVarification()
    {
        FirebaseUser firebaseUser=auth.getCurrentUser();

        if (firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    Toast.makeText(RegistrationAcitvity.this, "Verification Email is send, Verify and Log In Again ", Toast.LENGTH_SHORT).show();
                    auth.signOut();
                    finish();
                    startActivity(new Intent(RegistrationAcitvity.this,MainActivity.class));
                }
            });
        }else {
            Toast.makeText(this, "Fail to send email Verification", Toast.LENGTH_SHORT).show();
        }
    }
}