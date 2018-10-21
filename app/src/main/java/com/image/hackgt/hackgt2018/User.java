package com.image.hackgt.hackgt2018;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String username;
    private String password;
    private String name;
    private String dob;
    private Map<String, Integer> preferences;

    public User(String username, String password, String name, String dob,
                Map<String, Integer> preferences) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.dob = dob;
        if (preferences == null) {
            // implies the user is new and has no set of preferences associated with it
            String[] features = new String[]{"camping", "car", "dance", "music", "running",
                        "sports", "swim", "vacation"};
            Map<String, Integer> unipref = new HashMap<>();
            for (int i = 0; i < features.length; i++) {
                unipref.put(features[i], 1);
            }
            this.preferences = new HashMap<>(unipref);
        } else {
            this.preferences = new HashMap<>(preferences);
        }
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

    public Map<String, Integer> getPreferences() {
        return this.preferences;
    }

    public void setPreferences(Map<String, Integer> preferences) {
        this.preferences = new HashMap<>(preferences);
    }

    @Override
    public String toString() {
        return "Username: " + username + "\nPassword: " + password + "\nName: " + name +
                "\nDOB: " + dob;
    }
}
