package models;

import java.util.Date;

public class Product {
    private String name;
    private double price;
    private int quantity;
    private boolean shippable;
    private boolean expirable;
    private Date expirationDate; // for expirable products
    private double weight; // for shippable products
    private char weightUnit; // for shippable products //g for grams or k for kilograms

    ///Product constructor

    public Product(String name, double price, int quantity, boolean shippable, boolean expirable, double weight,
            char weightUnit, Date expirationDate) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.shippable = shippable;
        this.expirable = expirable;
        this.weight = weight;
        this.weightUnit = weightUnit;
        this.expirationDate = expirationDate;
    }

    // getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    public boolean isShippable() {
        return shippable;
    }

    public boolean isExpirable() {
        return expirable;
    }

    public double getWeight() {
        return weight;
    }

    public char getWeightUnit() {
        return weightUnit;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    // setters
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative ❌");
        }
        this.quantity = quantity;
    }

    public boolean isExpired() {
        if (!expirable || expirationDate == null) {
            return false; // Not expirable or no expiration date set
        }
        Date currentDate = new Date();
        return expirationDate.before(currentDate); // Check if the product is expired
    }
}
