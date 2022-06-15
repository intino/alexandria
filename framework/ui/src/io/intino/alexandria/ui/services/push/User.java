package io.intino.alexandria.ui.services.push;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User {
    private String username;
    private String fullName;
    private String email;
    private URL photo;
    private String language;
    private Map<String, String> preferences = new HashMap<>();
    private List<String> roles = new ArrayList<>();

    public String username() {
        return username;
    }

    public User username(String username) {
        this.username = username;
        return this;
    }

    public String fullName() {
        return fullName;
    }

    public User fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public String email() {
        return email;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public URL photo() {
        return photo;
    }

    public User photo(URL photo) {
        this.photo = photo;
        return this;
    }

    public String language() {
        return language;
    }

    public User language(String language) {
        this.language = language;
        return this;
    }

    public List<String> preferences() {
        return new ArrayList<>(preferences.values());
    }

    public String preference(String name) {
        return preferences.get(name);
    }

    public User add(String preference, String value) {
        preferences.put(preference, value);
        return this;
    }

    public List<String> roles() {
        return roles;
    }

    public void roles(List<String> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
    }

}