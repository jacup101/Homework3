package com.jacup101.homework3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements IEpisodeFragmentActivity, ICharacterFragmentActivity, ILocationFragmentActivity {

    private TabLayout tabLayout_main;
    private ImageView imageView_logo;

    protected RequestQueue queue;
    private String characterUrl = "https://rickandmortyapi.com/api/character/";

    private int numCharacters = 0;

    private String locationUrl = "https://rickandmortyapi.com/api/location";

    private String episodeUrl = "https://rickandmortyapi.com/api/episode/";
    private int numEpisodes = 0;

    private String jsonToPass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout_main = findViewById(R.id.tabLayout_main);
        tabLayout_main.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // called when tab selected
                String str = tab.getText().toString();
                parseClick(str);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // called when tab unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                String str = tab.getText().toString();
                parseClick(str);
                // called when a tab is reselected
            }
        });

        //Load In Logo
        imageView_logo = findViewById(R.id.imageView_logo);
        AssetManager assetManager = getAssets();
        try {
            InputStream ims = assetManager.open("logo.png");
            Drawable d = Drawable.createFromStream(ims, null);
            imageView_logo.setImageDrawable(d);
        } catch (IOException ex) {
            return;
        }

        //Load Android Volley
        queue = Volley.newRequestQueue(this);
        //Load in the number of characters
        StringRequest characterNumReq = new StringRequest(Request.Method.GET, characterUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("json_response",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject info = jsonObject.getJSONObject("info");
                            numCharacters = info.getInt("count");
                            Log.d("nm","" + numCharacters);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        //Load in the number of episodes
        StringRequest episodeNumReq = new StringRequest(Request.Method.GET, episodeUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("json_response",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject info = jsonObject.getJSONObject("info");
                            numEpisodes = info.getInt("count");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        queue.add(characterNumReq);
        queue.add(episodeNumReq);
    }

    private void parseClick(String str) {
        if(str.equals("Character")) {
            int num = (int) (Math.random() * numCharacters) + 1;
            StringRequest characterReq = new StringRequest(Request.Method.GET, String.format(characterUrl + num),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("json_response",response);
                            jsonToPass = response;
                            loadFragment(new CharacterFragment());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });
            queue.add(characterReq);

        } else if(str.equals("Location")) {
            StringRequest locationReq = new StringRequest(Request.Method.GET, locationUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("json_response",response);
                            jsonToPass = response;
                            loadFragment(new LocationFragment());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });
            queue.add(locationReq);

        } else if(str.equals("Episode")) {
            int num = (int) (Math.random() * numEpisodes) + 1;
            StringRequest episodeReq = new StringRequest(Request.Method.GET, String.format(episodeUrl + num),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("json_response",response);
                            jsonToPass = response;
                            loadFragment(new EpisodeFragment());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                }
            });
            queue.add(episodeReq);
        }

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainerView,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public String getJson() {
        return jsonToPass;
    }

    @Override
    public RequestQueue getQueue() {
        return queue;
    }
}