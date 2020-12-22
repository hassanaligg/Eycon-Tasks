package com.example.architectureexample.firebaseCRUD;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architectureexample.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;

public class MyFirebaseAdapter extends FirebaseRecyclerAdapter<Users,MyFirebaseAdapter.MyViewHolder> {
    private Context context;
    static String mykey;
    public MyFirebaseAdapter(@NonNull FirebaseRecyclerOptions<Users> options, Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final Users model) {
        holder.name.setText(model.getName());
        holder.age.setText(model.getAge());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialog=DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setContentHolder(new ViewHolder(R.layout.edit_dialog))
                        .setExpanded(false)
                        .create();
                final View HolderView = (LinearLayout)dialog.getHolderView();

                final EditText mName=HolderView.findViewById(R.id.update_name);
                final EditText mAge=HolderView.findViewById(R.id.update_age);
                mName.setText(model.getName());
                mAge.setText(model.getAge());
                Button updateData=HolderView.findViewById(R.id.btn_update);
                updateData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("Name",mName.getText().toString());
                        map.put("Age",mAge.getText().toString());
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position-1).getKey())
                                .updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                Toast.makeText(context, "Sucessfully Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
                dialog.show();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialog=DialogPlus.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(50,0,50,0)
                        .setContentHolder(new ViewHolder(R.layout.alert_dialog))
                        .setExpanded(false)
                        .create();
                dialog.show();
                View HolderView = (CardView) dialog.getHolderView();
                final Button yes=HolderView.findViewById(R.id.btn_yes);
                final Button no=HolderView.findViewById(R.id.btn_No);
                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mykey=getRef(position).getKey();
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(getRef(position).getKey())
                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.dismiss();
                    }
                });
                no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list, parent, false);

        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,age;
        ImageView edit,delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.yName);
            age=itemView.findViewById(R.id.yAge);
            edit=itemView.findViewById(R.id.editIcon);
            delete=itemView.findViewById(R.id.deleteIcon);
        }
    }
}
