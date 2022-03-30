package com.example.notesapp.Acitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Toast;

import com.example.notesapp.Model.Notes;
import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityCreateNoteBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateNote extends AppCompatActivity {

    ActivityCreateNoteBinding binding;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String uid;
    DatabaseReference databaseReference;
    String pushId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCreateNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        uid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("notes").child(uid);


        binding.noteCreateFloatingBtn.setColorFilter(R.color.white);

        binding.noteCreateFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title=binding.title.getText().toString();
                String note=binding.noteBody.getText().toString();


                if (title.isEmpty() || note.isEmpty())
                {
                    Toast.makeText(CreateNote.this, "Both field are Require", Toast.LENGTH_SHORT).show();
                }else {

                    pushId=databaseReference.push().getKey();
                    Notes notes=new Notes(title,note,pushId);

                    databaseReference.child("myNotes").child(pushId).setValue(notes).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(CreateNote.this, "Note Created successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(CreateNote.this,MainActivity.class));
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateNote.this, "Failed To Create Note", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}