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
import com.example.notesapp.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog=new ProgressDialog(this);
        dialog.setTitle("Login Account");
        dialog.setMessage("Please Wait..");
        auth=FirebaseAuth.getInstance();

        FirebaseUser firebaseUser=auth.getCurrentUser();

        if (firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }

        binding.login.setOnClickListener(new View.OnClickListener() {
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
                }else{

                    dialog.show();

                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialog.dismiss();
                            if (task.isSuccessful())
                            {
                                checkemailverification();
                            }else {
                                Toast.makeText(LoginActivity.this, "Account doesn't Exists", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });

        binding.gotoSignUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistrationAcitvity.class));
            }
        });

        binding.forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgotActivity.class));
            }
        });
    }
    public void checkemailverification()
    {
        FirebaseUser firebaseUser=auth.getCurrentUser();

        if (firebaseUser.isEmailVerified()==true)
        {

            Toast.makeText(this, "Login is successfully ", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }else {
            Toast.makeText(this, "verify your email first", Toast.LENGTH_SHORT).show();
            auth.signOut();
        }

    }
}