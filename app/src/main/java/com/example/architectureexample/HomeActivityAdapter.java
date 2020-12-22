package com.example.architectureexample;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architectureexample.demoUi.Demo_Ui_Activity;
import com.example.architectureexample.firebaseCRUD.FirebaseHomeActivity;
import com.example.architectureexample.getlocation.CheckLocationActivity;
import com.example.architectureexample.memorablePlaces.MemorablePlacesActivity;
import com.example.architectureexample.mvvm.MainActivity;
import com.example.architectureexample.service.ServiceActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivityAdapter extends RecyclerView.Adapter<HomeActivityAdapter.HomeViewHolder> {
    private String [] data;
    private String [] data2;

    public HomeActivityAdapter(String[]data,String[]data2) {
        this.data = data;
        this.data2 =data2;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.home_list_items,parent,false);
        return new HomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        String title =data[position];
        String disc=data2[position];
        holder.textView.setText(title);
        holder.textView2.setText(disc);
     }

    @Override
    public int getItemCount() {
        return data.length;
    }
    public class HomeViewHolder extends RecyclerView.ViewHolder{
        TextView textView,textView2;
        LinearLayout linearLayout;
        private final Context context;
        public HomeViewHolder(@NonNull final View itemView) {
            super(itemView);
            context=itemView.getContext();
            textView=itemView.findViewById(R.id.mytext);
            textView2=itemView.findViewById(R.id.mydisc);
            linearLayout=itemView.findViewById(R.id.mylinear);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    textView.setTextColor(ContextCompat.getColor(textView.getContext(), R.color.colorPrimaryDark));
//                    textView2.setTextColor(ContextCompat.getColor(textView2.getContext(), R.color.colorPrimaryDark));
                    Intent intent = null;
                    if(getAdapterPosition()==0) {
                         intent= new Intent(context, MainActivity.class);
                        
                    }
                    else if(getAdapterPosition()==1) {
                        intent = new Intent(context, ServiceActivity.class);
                        
                    }else if(getAdapterPosition()==2) { 
                        intent = new Intent(context, CheckLocationActivity.class);
                       
                    }else if(getAdapterPosition()==3) {
                        intent = new Intent(context, Demo_Ui_Activity.class);
                        
                    }else if(getAdapterPosition()==4){
                        intent =new Intent(context, MemorablePlacesActivity.class);
                    }else if(getAdapterPosition()==5){
                        intent =new Intent(context, FirebaseHomeActivity.class);
                    }
                    context.startActivity(intent);

                }
            });
        }
    }
}
