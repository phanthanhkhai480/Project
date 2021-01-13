package com.group12.logintest;

//Model class for Flight object.

public class Flight {
    double minPrice;
    String departureDate;
    String origin;
    String destination;
    int carrierId;
    String CarrierName;
    String returnDate;

    public Flight(double minPrice, String departureDate, String origin, String destination, int carrierId, String carrierName, String returnDate) {
        this.minPrice = minPrice;
        this.departureDate = departureDate;
        this.origin = origin;
        this.destination = destination;
        this.carrierId = carrierId;
        CarrierName = carrierName;
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "minPrice=" + minPrice +
                ", departureDate='" + departureDate + '\'' +
                ", origin='" + origin + '\'' +
                ", destination='" + destination + '\'' +
                ", carrierId=" + carrierId +
                ", CarrierName='" + CarrierName + '\'' +
                '}';
    }
}
