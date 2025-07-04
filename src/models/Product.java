package models;

public class Product {
    private String name;
    private double price;
    private int quantity;
    private boolean shippable;
    private boolean expirable;

    ///Product constructor
    public Product(String name, double price, int quantity, boolean shippable, boolean expirable) {
        this.name = name;
        this.price = price;
        this.quantity = quantity; // Default quantity
        this.shippable = shippable; // Default shippable status
        this.expirable = expirable; // Default expirable status
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

    // setters
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative ❌");
        }

        this.quantity = quantity;
    }

    public void reduceQuantity(int quantity) {

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative ❌");
        }
        if (this.quantity < quantity) {
            throw new IllegalArgumentException("Not enough quantity available ❌");
        }
        this.quantity -= quantity;
    }

}
