package com.jacup101.homework3;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder>{

    private List<Location> locations;

    public LocationAdapter(List<Location> locations) {
        this.locations = locations;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View locationView = inflater.inflate(R.layout.item_location,parent,false);
        ViewHolder viewHolder = new ViewHolder(locationView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Location loc = locations.get(position);
        holder.nameText.setText(loc.getName());
        holder.dimensionPlace.setText("Dimension: " + loc.getDimension());
        holder.typePlace.setText("Type: " + loc.getType());


    }

    @Override
    public int getItemCount() {
        return locations.size();
    }





    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView dimensionPlace;
        TextView typePlace;
        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textView_locationName);
            dimensionPlace = itemView.findViewById(R.id.textView_locationDimension);
            typePlace = itemView.findViewById(R.id.textView_locationType);


        }

    }
}
