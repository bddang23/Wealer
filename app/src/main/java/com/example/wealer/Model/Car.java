package com.example.wealer.Model;

public class Car {
   private String make;
   private String model;
   private String description;
   private String imageLink;
   private float price;
   private String address;

    public Car(String make, String model, String description, String imageLink, float price, String address) {
        this.make = make;
        this.model = model;
        this.description = description;
        this.imageLink = imageLink;
        this.price = price;
        this.address = address;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
