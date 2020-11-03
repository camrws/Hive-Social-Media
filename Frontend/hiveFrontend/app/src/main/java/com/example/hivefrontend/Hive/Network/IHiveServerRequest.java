package com.example.hivefrontend.Hive.Network;

import com.example.hivefrontend.Hive.Logic.IHiveVolleyListener;
import com.example.hivefrontend.ui.profile.ProfileVolleyListener;

public interface IHiveServerRequest {

    public void addVolleyListener(IHiveVolleyListener logic);
    public String getUserId(final String email);
    public void displayScreen(int userId);
}
