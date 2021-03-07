package com.jacup101.homework3;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EpisodeFragment extends Fragment {

    private static final String CHANNEL_ID = "episode_frag_id";
    private View view;
    private RecyclerView recyclerView;

    private TextView nameView;
    private TextView airView;

    private Button buttonInfo;
    private List<EpisodeCharacter> characters;
    private EpisodeCharacterAdapter adapter;

    protected RequestQueue queue;

    private String url = "https://www.youtube.com";
    private String notificationTitle = "title";
    private String notificationText = "text";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_episode, container, false);
        //Grab Views
        recyclerView = view.findViewById(R.id.recyclerView_episode);
        buttonInfo = view.findViewById(R.id.button_moreInfo);
        nameView = view.findViewById(R.id.textView_episodeName);
        airView = view.findViewById(R.id.textView_episodeAirdate);

        characters = new ArrayList<EpisodeCharacter>();

        //Get Request Queue
        IEpisodeFragmentActivity activity = (IEpisodeFragmentActivity) getActivity();
        queue = activity.getQueue();

        //Set Button to Do Show Notification
        buttonInfo.setOnClickListener(v -> sendNotification(v));
        String jsonStr = activity.getJson();
        try {
            JSONObject json = new JSONObject(jsonStr);
            nameView.setText(json.getString("episode") + " " + json.getString("name"));
            airView.setText("Aired On: " + json.getString("air_date"));

            notificationTitle = json.getString("episode") + " " + json.getString("name");
            url = "https://rickandmorty.fandom.com/wiki/" + json.getString("name").replace(' ','_');
            notificationText = "To read more information about Episode " + json.getString("episode") + ", please visit: " + url;

            JSONArray residents = json.getJSONArray("characters");
            for(int i = 0; i < residents.length(); i++) {
                requestCharacter(residents.getString(i));
            }
            adapter = new EpisodeCharacterAdapter(characters);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        createNotificationChannel();
        return view;
    }


    private void requestCharacter(String characterUrl) throws JSONException {

        StringRequest characterReq = new StringRequest(Request.Method.GET, characterUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("json_response",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String imgUrl = jsonObject.getString("image");
                            String name = jsonObject.getString("name");
                            EpisodeCharacter character = new EpisodeCharacter(name, imgUrl);
                            if(adapter != null) adapter.addCharacter(character);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        queue.add(characterReq);


    }
    private void sendNotification(View v) {
        //Build the intent
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(notificationText))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());

    // notificationId is a unique int for each notification that you must define
        notificationManager.notify(100, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    


}
