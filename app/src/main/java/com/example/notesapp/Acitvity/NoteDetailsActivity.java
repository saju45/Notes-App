package com.example.notesapp.Acitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityNoteDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NoteDetailsActivity extends AppCompatActivity {

    ActivityNoteDetailsBinding binding;
    Intent intent;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String uid;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNoteDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        uid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("notes").child(uid);

        intent=getIntent();
        String title=intent.getStringExtra("title");
        String body=intent.getStringExtra("body");
        String pushId=intent.getStringExtra("push");

        binding.title.setText(title);
        binding.noteBody.setText(body);


        binding.noteCreateFloatingBtn.setColorFilter(R.color.green);

        binding.noteCreateFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title_=binding.title.getText().toString();
                String body_=binding.noteBody.getText().toString();

                if (title_.isEmpty() || body_.isEmpty())
                {
                    Toast.makeText(NoteDetailsActivity.this, "Something is empty", Toast.LENGTH_SHORT).show();
                }else {


                    HashMap<String,Object> hashMap=new HashMap<>();

                    hashMap.put("title",title_);
                    hashMap.put("body",body_);

                    databaseReference.child("myNotes").child(pushId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful())
                            {
                                Toast.makeText(NoteDetailsActivity.this, "note updated successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(NoteDetailsActivity.this,MainActivity.class));
                            }

                        }
                    });
                }
            }
        });


    }
}