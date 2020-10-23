package com.example.hivefrontend.Register.Logic;

import android.content.Context;

import com.example.hivefrontend.Register.IRegisterView;
import com.example.hivefrontend.Register.Network.IRegisterServerRequest;
import com.example.hivefrontend.SharedPrefManager;
import com.example.hivefrontend.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterLogic implements IRegisterVolleyListener {

    IRegisterServerRequest server;
    IRegisterView registerView;

    @Override
    public JSONObject createUser() {
        JSONObject object = new JSONObject();
        try {
            registerView.usernameCheck();
            registerView.passwordCheck();
            registerView.emailAddressCheck();
            registerView.validateEmailAddress();
            object.put("email", registerView.getEmailAddress());
            object.put("userRegistrationIdentity", null);
            object.put("password", registerView.getPassword());
            object.put("birthday", "test birthday");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    @Override
    public void onRegisterUserSuccess(JSONObject response) {
        User user = new User(registerView.getUsername(), registerView.getPassword());
        SharedPrefManager.getInstance(getRegisterContext()).userLogin(user);
    }

    public Context getRegisterContext() {return registerView.getRegisterContext();}
}