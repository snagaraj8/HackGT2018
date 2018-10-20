package com.image.hackgt.hackgt2018;

import java.util.ArrayList;
import java.util.List;

public class User {
    String username;
    String password;
    String name;
    String dob;
    List<Double> preferences;

    public User(String username, String password, String name, String dob,
                List<Double> preferences) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.preferences = new ArrayList<>(preferences);
    }
}
