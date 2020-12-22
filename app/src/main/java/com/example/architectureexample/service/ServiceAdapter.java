package com.example.architectureexample.service;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.architectureexample.R;
import com.example.architectureexample.mvvm.NoteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {
    List<LocationModel> locationList;

    public ServiceAdapter(List<LocationModel> locationList) {
        this.locationList = locationList;
    }

    @NonNull
    @Override
    public ServiceAdapter.ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_list_items, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceAdapter.ServiceViewHolder holder, int position) {
        LocationModel locationModel=locationList.get(position);
        holder.mlatitude.setText(""+locationModel.getLatitude());
        holder.mlongitude.setText(""+locationModel.getLongitude());
        holder.mtime.setText(""+locationModel.getTime());
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public class ServiceViewHolder extends RecyclerView.ViewHolder {
        TextView mlatitude,mlongitude,mtime;
        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            mlatitude=itemView.findViewById(R.id.mlatitude);
            mlongitude=itemView.findViewById(R.id.mlongitude);
            mtime=itemView.findViewById(R.id.mTime);

        }
    }
}
