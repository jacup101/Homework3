package com.jacup101.homework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class EpisodeCharacterAdapter extends RecyclerView.Adapter<EpisodeCharacterAdapter.ViewHolder>{

    private List<EpisodeCharacter> characters;

    public EpisodeCharacterAdapter(List<EpisodeCharacter> characters) {
        this.characters = characters;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View characterView = inflater.inflate(R.layout.item_episodecharacter,parent,false);
        ViewHolder viewHolder = new ViewHolder(characterView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EpisodeCharacter character = characters.get(position);
        holder.nameText.setText(character.getName());
        Picasso.get().load(character.getUrl()).into(holder.characterView);


    }

    @Override
    public int getItemCount() {
        return characters.size();
    }

    public void addCharacter(EpisodeCharacter character) {
        this.characters.add(character);
        notifyDataSetChanged();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText;
        ImageView characterView;
        public ViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textView_episodeCharacter);
            characterView = itemView.findViewById(R.id.imageView_episodeCharacter);
        }

    }
}