package com.example.tokokosmetik.Domain;

import java.io.Serializable;

public class PopularDomain implements Serializable {
    private int stock;
    private int id;
    private String title;
    private String description;
    private String picUrl;
    private double score;

    private int review;

    private double price;
    private int numberinCart;
    private char[] Review;

    public PopularDomain(int id, String title, String description, String picUrl, int review, double score, double price, int stock) {
        this.title = title;
        this.description = description;
        this.picUrl = picUrl;
        this.review = review;
        this.score = score;
        this.price = price;
        this.id = id;
        this.stock = stock;
    }

    public double getPrice() {

        return price;
    }

    public void setPrice(double price) {

        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {

        return picUrl;
    }

    public void setPicUrl(String picUrl) {

        this.picUrl = picUrl;
    }

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getScore() {

        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getNumberinCart() {

        return numberinCart;
    }

    public void setNumberinCart(int numberinCart) {

        this.numberinCart = numberinCart;
    }

    public int getReview(){

        return this.review;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }
}
