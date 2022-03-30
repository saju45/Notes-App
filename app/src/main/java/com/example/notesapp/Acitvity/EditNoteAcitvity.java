package com.example.notesapp.Acitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.notesapp.Model.Notes;
import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityEditNoteAcitvityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditNoteAcitvity extends AppCompatActivity {

    ActivityEditNoteAcitvityBinding binding;
    Intent intent;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditNoteAcitvityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        String uid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("notes").child(uid);


        intent=getIntent();
        String title=intent.getStringExtra("title");
        String body=intent.getStringExtra("body");
        String pushId=intent.getStringExtra("push");

        binding.title.setText(title);
        binding.noteBody.setText(body);

        binding.noteCreateFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title_=binding.title.getText().toString();
                String body_=binding.noteBody.getText().toString();

                Notes notes=new Notes(title_,body_,pushId);

                databaseReference.child("myNotes").child(pushId).setValue(notes).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful())
                        {
                            Toast.makeText(EditNoteAcitvity.this, "notes edit successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditNoteAcitvity.this,MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        });

    }
}