package com.example.wealer.Model;

import java.util.List;

public class User {
   private String username;
   private String email;
   private List<Car> Listing;

    public User(String username, String email, List<Car> listing) {
        this.username = username;
        this.email = email;
        Listing = listing;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Car> getListing() {
        return Listing;
    }

    public void setListing(List<Car> listing) {
        Listing = listing;
    }
}
