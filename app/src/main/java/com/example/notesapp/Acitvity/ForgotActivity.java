package com.example.notesapp.Acitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityForgotBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {

    ActivityForgotBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityForgotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();

        binding.backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(ForgotActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.passwordRecoverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=binding.forgotEmailEditText.getText().toString();

                if (email.isEmpty())
                {
                    binding.forgotEmailEditText.setError("Enter your valid email");
                    binding.forgotEmailEditText.requestFocus();
                }else {
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), "Email send ,You can recover your password using email", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotActivity.this,MainActivity.class));
                            }else {
                                Toast.makeText(getApplicationContext(), "Email is wrong or Account didn't exits", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}