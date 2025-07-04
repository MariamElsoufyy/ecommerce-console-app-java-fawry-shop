package models;

import java.util.ArrayList;

public class Cart {

    private ArrayList<CartItem> items;
    private double totalPrice;

    // Cart constructor
    public Cart() {
        this.items = new ArrayList<CartItem>();
        this.totalPrice = 0.0;
    }

    // Adds a product to the cart with a specified quantity
    public void addItem(Product product, int quantity) {
        // Check if the product is already in the cart
        for (CartItem item : items) {
            if (item.getProduct().getName().equals(product.getName())) {
                item.addQuantitytoCart(quantity);
                return;
            }
        }
        // If not found, add a new CartItem
        items.add(new CartItem(product, quantity));

    }

    public void deduceItem(String productName, int quantity) {
        for (CartItem item : items) {
            if (item.getProduct().getName().equals(productName)) {
                item.deduceQuantityFromCart(quantity);
                if (item.getQuantity() <= 0) {
                    items.remove(item); // Remove item if quantity is zero or less
                }
                return;
            }
        }
    }

    public void clearCart() {
        items.clear();
        totalPrice = 0.0;
    }

    public ArrayList<CartItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        totalPrice = 0.0;
        for (CartItem item : items) {
            totalPrice += item.getTotalPrice();
        }
        return totalPrice;
    }

}
