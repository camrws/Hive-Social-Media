package com.example.hivefrontend.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.hivefrontend.R;

import com.example.hivefrontend.PostDetailsActivity;
import com.example.hivefrontend.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private RequestQueue queue;
    private Context context;
    private List<JSONObject> notifications;


    NotificationsAdapter(Context context, List<JSONObject> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    // inflates the row layout from xml when needed
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_recycler_row, parent, false);
        NotificationsAdapter.ViewHolder viewHolder = new NotificationsAdapter.ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(NotificationsAdapter.ViewHolder holder, final int position) {
        try {
            holder.cv.setTag(position);
            holder.notiText.setText(notifications.get(position).getString("notiText"));
            String date = notifications.get(position).getString("dateCreated");
            holder.notiDateTime.setText(date);
            holder.notiIsNew.setText("isNew: " + notifications.get(position).getString("isNew"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return notifications.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView notiText;
        public TextView notiDateTime;
        public TextView notiIsNew;
        public CardView cv;

        ConstraintLayout constraintLayout;

        ViewHolder(View itemView) {
            super(itemView);
            notiText = itemView.findViewById(R.id.noti_text);
            notiDateTime = itemView.findViewById(R.id.noti_datetime);
            notiIsNew = itemView.findViewById(R.id.noti_isnew);
            constraintLayout=itemView.findViewById(R.id.notiViewLayout);
            cv = itemView.findViewById(R.id.cardView);
            cv.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //for find item that hold in list
            int position = (Integer) v.getTag();
            Intent intent = new Intent(v.getContext(), PostDetailsActivity.class);
            try {
                //start new activity and pass the post ID to it
                int entityId = notifications.get(position).getInt("entityId");
                int notiId = notifications.get(position).getInt("notiId");
                Boolean isNew = notifications.get(position).getBoolean("isNew");
                if(isNew) {
                    readNotification(notiId);
                }
                String notiType = notifications.get(position).getString("notiType");
                if(notiType.equals("like") || notiType.equals("comment") || notiType.equals("post-mention")) {
                    Log.i("OK", "notiType:" + notiType);
                    intent.putExtra("postId", entityId);
                    v.getContext().startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public void readNotification(int notiId) {
        queue = Volley.newRequestQueue(context);
        String url ="http://10.24.227.37:8080/readNotification/byNotiId/" + notiId;
        // Server name http://coms-309-tc-03.cs.iastate.edu:8080
        final JSONObject putObject = new JSONObject();
        Log.i("request","OK!" + url);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url,
                putObject, new Response.Listener<JSONObject>(){
            public void onResponse(JSONObject response) {
                Log.i("request","success!");
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error){
                Log.i("request","fail!");
            }
        });
        queue.add(jsonObjectRequest);
    }

    // convenience method for getting data at click position
    String getItem(int id) throws JSONException {
        return notifications.get(id).getString("textContent");
    }

}