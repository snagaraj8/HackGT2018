package com.image.hackgt.hackgt2018;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String name;
    private String dob;
    private List<Double> preferences;

    public User(String username, String password, String name, String dob,
                List<Double> preferences) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.preferences = new ArrayList<>(preferences);
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public List<Double> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Double> preferences) {
        this.preferences = preferences;
    }
}
