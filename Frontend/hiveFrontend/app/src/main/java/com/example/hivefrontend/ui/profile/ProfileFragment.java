package com.example.hivefrontend.ui.profile;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.hivefrontend.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    //private RecyclerView recyclerView;
    //private RecyclerView.Adapter mAdapter;
    //private RecyclerView.LayoutManager layoutManager;
    private ProfileViewModel mViewModel;
    private ArrayList<Integer> hiveIds;
    private ArrayList<String> hiveOptions;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        hiveIds= new ArrayList<>();
        hiveOptions = new ArrayList<>();
        final TextView textView = (TextView) rootView.findViewById(R.id.displayName);
        final TextView userName = (TextView) rootView.findViewById(R.id.userName);
        final TextView hiveListHeading = (TextView) rootView.findViewById(R.id.hiveListHeading);
        final TextView bio = (TextView) rootView.findViewById(R.id.bio);
        final TextView hiveList = (TextView) rootView.findViewById(R.id.hiveList);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        String url ="http://10.24.227.37:8080/users";

        // Server name http://coms-309-tc-03.cs.iastate.edu:8080/posts

        //first request: user information
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            JSONObject user1 = response.getJSONObject(3);
                            // Get the current user (json object) data
                            String name = user1.getString("displayName");

                            // Gets first name out of whole name, but checks if a space exists.
                            String firstName = "";
                            if (name.contains(" ")) {
                                firstName = name.substring(0, name.indexOf(" "));
                            }

                            String uName = user1.getString("userName");
                            // Formats username and concatenates an '@' before.
                            uName = uName.toLowerCase();
                            uName = "@" + uName;
                            // Display the formatted json data in text view
                            textView.setText(name);
                            userName.setText(uName);
                            bio.setText(user1.getString("biography"));
                            // TODO: replace '(4)' with actual count.
                            if (firstName.length() == 0)
                            {
                                hiveListHeading.setText("Their Public Hives:");
                            }
                            else {
                                hiveListHeading.setText((firstName + "'s Public Hives:"));
                            }
                            //dateJoined.setText(user1.getString("dateCreated"));
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Log.i("jsonAppError",e.toString());
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.i("volleyAppError","Error: " + error.getMessage());
                        Log.i("volleyAppError","VolleyError: "+ error);

                        textView.setText("Error.");

                    }
                });
        //second request: hive information
        url ="http://10.24.227.37:8080/members/byUserId/1"; //for now, getting this user's hive information until we have login functionality

        JsonArrayRequest hiveRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try{
                            for(int i = 0; i < response.length(); i++){
                                JSONObject member = response.getJSONObject(i); //should return user,hive pair
                                Integer hiveId = (Integer) member.getJSONObject("hive").getInt("hiveId");
                                hiveIds.add(hiveId);
                                String hiveName =  member.getJSONObject("hive").getString("name");
                                hiveOptions.add(hiveName);
                            }
                            //here the hives' ids and names have been set appropriately
                            String hiveString = "";
                            for(int i = 0; i<hiveOptions.size(); i++){
                                if(i==hiveOptions.size()-1){
                                    hiveString = hiveString + "" + hiveOptions.get(i);
                                }
                                else{
                                    hiveString = hiveString + "" + hiveOptions.get(i) + ", ";
                                }
                            }
                            hiveList.setText(hiveString);

                            // set up the RecyclerView
                           // RecyclerView recyclerView = rootView.findViewById(R.id.hiveList);
                            //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                            //mAdapter = new MyAdapter(getActivity().getApplicationContext(), hiveOptions);
                            //recyclerView.setAdapter(mAdapter);
                            //mAdapter.notifyDataSetChanged();

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            Log.i("jsonAppError",e.toString());
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Log.i("volleyAppError","Error: " + error.getMessage());
                        Log.i("volleyAppError","VolleyError: "+ error);

                        textView.setText("Error.");

                    }
                });



// Add the request to the RequestQueue.
        queue.add(jsonArrayRequest);
        queue.add(hiveRequest);

        return rootView;
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel


    }



}