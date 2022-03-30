package com.example.notesapp.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.notesapp.Acitvity.EditNoteAcitvity;
import com.example.notesapp.Acitvity.NoteDetailsActivity;
import com.example.notesapp.Model.Notes;
import com.example.notesapp.R;
import com.example.notesapp.databinding.NotesLayoutBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.viewHolder> {

    Context context;
    ArrayList<Notes> list;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    public NotesAdapter(Context context, ArrayList<Notes> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_layout, parent, false);
        return new viewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        Notes notes = list.get(position);

        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        String uid=firebaseUser.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("notes").child(uid);


        int colorCode = getRandomColor();
        holder.binding.note.setBackgroundColor(holder.itemView.getResources().getColor(colorCode, null));


        holder.binding.menuPopUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu=new PopupMenu(context,v);
                popupMenu.setGravity(Gravity.END);
                popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        Intent intent=new Intent(context, EditNoteAcitvity.class);
                        intent.putExtra("title",notes.getTitle());
                        intent.putExtra("body",notes.getBody());
                        intent.putExtra("push",notes.getPushId());
                        context.startActivity(intent);
                        return true;
                    }
                });

                popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        AlertDialog.Builder builder=new AlertDialog.Builder(context);
                        builder.setTitle("Are you sure ??");
                        builder.setMessage("Delete this item");
                        builder.setCancelable(false);

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "Delete this note", Toast.LENGTH_SHORT).show();
                                databaseReference.child("myNotes").child(notes.getPushId()).removeValue();
                            }
                        });

                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();


                        return true;
                    }
                });
                popupMenu.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, NoteDetailsActivity.class);
                intent.putExtra("title",notes.getTitle());
                intent.putExtra("body",notes.getBody());
                intent.putExtra("push",notes.getPushId());
                context.startActivity(intent);
            }
        });
        holder.binding.title.setText(notes.getTitle());
        holder.binding.noteContent.setText(notes.getBody());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        NotesLayoutBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = NotesLayoutBinding.bind(itemView);
        }
    }

    public int getRandomColor() {
        List<Integer> list = new ArrayList<>();

        list.add(R.color.gray);
        list.add(R.color.green);
        list.add(R.color.light_green);
        list.add(R.color.sky_blue);
        list.add(R.color.pink);
        list.add(R.color.color1);
        list.add(R.color.color2);
        list.add(R.color.color3);
        list.add(R.color.color4);
        list.add(R.color.color5);

        Random random = new Random();
        int number = random.nextInt(list.size());

        return list.get(number);

    }
}
