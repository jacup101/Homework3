package com.jacup101.homework3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CharacterFragment extends Fragment {

    private View view;

    private ImageView imageView;
    private TextView nameView;
    private TextView speciesView;
    private TextView statusView;
    private  TextView genderView;
    private  TextView episodeView;
    private TextView originView;
    private TextView locationView;

    private String name;
    private String status;
    private String species;
    private String gender;
    private String imgUrl = null;
    private String origin;
    private String location;
    private String episodes;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_character, container, false);
        //Grab Views
        imageView = view.findViewById(R.id.imageView_character);
        nameView = view.findViewById(R.id.textView_characterName);
        speciesView = view.findViewById(R.id.textView_characterSpecies);
        statusView = view.findViewById(R.id.textView_characterStatus);
        genderView = view.findViewById(R.id.textView_characterGender);
        episodeView = view.findViewById(R.id.textView_characterEpisode);
        originView = view.findViewById(R.id.textView_characterOrigin);
        locationView = view.findViewById(R.id.textView_characterLocation);
        //Grab Json From Bundle, Parse it

        ICharacterFragmentActivity activity = (ICharacterFragmentActivity) getActivity();
        String jsonStr = activity.getJson();
        try {
            JSONObject json = new JSONObject(jsonStr);
            imgUrl = json.getString("image");
            name = json.getString("name");
            status = "Status: " + json.getString("status");
            species = "Species: " + json.getString("species");
            gender = "Gender: " + json.getString("gender");

            JSONObject originObject = json.getJSONObject("origin");
            origin = "Origin: " + originObject.getString("name");

            JSONObject locationObject = json.getJSONObject("location");
            location = "Location: " + locationObject.getString("name");

            JSONArray episodes = json.getJSONArray("episode");
            parseEpisodeArray(episodes);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(imgUrl != null) {
            Log.d("Loading Image", imgUrl);
            Picasso.get().load(imgUrl).into(imageView);
        }
        if(name != null) nameView.setText(name);
        if(species != null) speciesView.setText(species);
        if(status != null) statusView.setText(status);
        if(gender != null) genderView.setText(gender);
        if(origin != null) originView.setText(origin);
        if(location != null) locationView.setText(location);
        if(episodes != null) episodeView.setText(episodes);
        return view;
    }

    private void parseEpisodeArray(JSONArray array) throws JSONException {
        String episodes = "";
        for(int i = 0; i < array.length(); i++) {
            String curr = array.getString(i);
            curr = curr.replace("https://rickandmortyapi.com/api/episode/","");
            episodes += curr;
            if(i < array.length() -1) episodes += ", ";
        }
        this.episodes = "Episodes: " + episodes;
    }
}
