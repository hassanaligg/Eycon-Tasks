package com.example.architectureexample.firebaseCRUD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.architectureexample.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.ObservableSnapshotArray;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;

public class FirebaseHomeActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference mRef;
    RecyclerView recyclerView;
    private MyFirebaseAdapter myFirebaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_home);
        database=FirebaseDatabase.getInstance();
        mRef=database.getReference("users");
        recyclerView=findViewById(R.id.mrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("users"), Users.class)
                        .build();
        myFirebaseAdapter=new MyFirebaseAdapter(options,this);
        recyclerView.scheduleLayoutAnimation();
        recyclerView.setAdapter(myFirebaseAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition(); // this is how you can get the position
                DatabaseReference key = myFirebaseAdapter.getRef(position); // You will have your own class ofcourse.

                // then you can delete the object
               // root.child("Object").child(object.getId()).setValue(null);// setting the value to null will just delete it from the database.

                FirebaseDatabase.getInstance().getReference().child("users")
                        .child(key.getKey())
                        .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(FirebaseHomeActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FirebaseHomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);
    }



    public void AddnewUser(View view) {

        final DialogPlus dialog = DialogPlus.newDialog(FirebaseHomeActivity.this)
                .setGravity(Gravity.CENTER)
                .setMargin(50,0,50,0)
                .setContentHolder(new ViewHolder(R.layout.add_user_dialog))
                .setExpanded(false)
                .create();

        View HolderView = (LinearLayout)dialog.getHolderView();

        final EditText name=HolderView.findViewById(R.id.mName);
        final EditText age=HolderView.findViewById(R.id.mAge);

        Button addData=HolderView.findViewById(R.id.enterRecord);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map=new HashMap<>();
                map.put("Name",name.getText().toString());
                map.put("Age",age.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("users").push()
                        .setValue(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(FirebaseHomeActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FirebaseHomeActivity.this, "Insertion Failed -- "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
//                String key=mRef.push().getKey();
//                String UserName=name.getText().toString();
//                int UserAge= Integer.parseInt(age.getText().toString());
//                mRef.child(key).child("Name").setValue(UserName);
//                mRef.child(key).child("Age").setValue(UserAge);
//                Toast.makeText(FirebaseHomeActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    protected void onStart() {
        super.onStart();
        myFirebaseAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        myFirebaseAdapter.stopListening();
    }
}
