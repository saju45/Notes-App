package com.example.notesapp.Acitvity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.notesapp.Adapter.NotesAdapter;
import com.example.notesapp.Model.Notes;
import com.example.notesapp.R;
import com.example.notesapp.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    ArrayList<Notes> list;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    String uid;
    String pushId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        uid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("notes").child(uid);

        list=new ArrayList<>();
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               startActivity(new Intent(MainActivity.this,CreateNote.class));
               finish();
            }
        });

        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(layoutManager);

        NotesAdapter adapter=new NotesAdapter(this,list);
        binding.recyclerview.setAdapter(adapter);

        pushId=databaseReference.push().getKey();

        databaseReference.child("myNotes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren())
                {
                    Notes notes=snapshot1.getValue(Notes.class);

                    list.add(notes);

                }adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

      getMenuInflater().inflate(R.menu.menu_layout,menu);
        return true;


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if (item.getItemId()==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
            finish();
            Toast.makeText(this, "Logout Account ", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);


    }
}