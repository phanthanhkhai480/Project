package com.group12.logintest;


import android.util.Log;

//Model class for Hotel object.
public class Hotel {

    int id;
    String hotelName;
    Double price;
    int rating;
    String address;
    String imgurl;
    int adults;
    String date;
    String checkIn;
    String checkOut;


    public Hotel(int id, String name, Double price, int starRating, String address, String url, int adults, String date) {
        this.id = id;
        this.hotelName = name;
        this.price = price;
        this.rating = starRating;
        this.address = address;
        this.imgurl = url;
        this.adults = adults;
        this.date = date;
        checkIn = date.substring(5, 15);
        checkOut = date.substring(19, 29);
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getAdults() {
        return adults;
    }

    public void setAdults(int adults) {
        this.adults = adults;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", hotelName='" + hotelName + '\'' +
                ", price=" + price +
                ", rating=" + rating +
                ", address='" + address + '\'' +
                ", imgurl='" + imgurl + '\'' +
                '}';
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }
}
