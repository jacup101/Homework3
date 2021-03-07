package com.jacup101.homework3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private List<Location> locations;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_location, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_location);
        locations = new ArrayList<Location>();

        ILocationFragmentActivity activity = (ILocationFragmentActivity) getActivity();
        String jsonStr = activity.getJson();
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray array = json.getJSONArray("results");
            for(int i = 0; i < array.length(); i++) {
                JSONObject locObj = array.getJSONObject(i);
                Location location = new Location(locObj.getString("name"),locObj.getString("type"),locObj.getString("dimension"));
                locations.add(location);
            }

            LocationAdapter adapter = new LocationAdapter(locations);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return view;
    }
}
