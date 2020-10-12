package com.example.hivefrontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextPassword;
    boolean userExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = (EditText) findViewById(R.id.loginUsernameField);
        editTextPassword = (EditText) findViewById(R.id.loginPasswordField);
        final Button signUpButton = (Button) findViewById(R.id.signUpButton);

        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin() {
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Oops! Please enter your username.");
            editTextUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Oops! Please enter your password.");
            editTextPassword.requestFocus();
            return;
        }

        String url ="http://10.24.227.37:8080/userRegistrations";
        JsonArrayRequest arrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for(int i = 0; i < response.length(); i++){
                                JSONObject member = response.getJSONObject(i);
                                if ((username.compareTo(member.getString("email")) == 0) && (password.compareTo(member.getString("password")) == 0)) {
                                      userExists = true;
                                    User user = new User(username, password);
                                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }
                            if (!userExists) {
                                editTextPassword.setError("Oops! The username or password is incorrect.");
                                editTextPassword.requestFocus();
                                return;
                            }

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

                    }
                });
                VolleySingleton.getInstance(this).addToRequestQueue(arrayRequest);
    }
}