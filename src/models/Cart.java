package models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Cart {

    private ArrayList<CartItem> items;
    private ArrayList<CartItem> shippableItems;
    private double totalPrice;

    // Cart constructor
    public Cart() {
        this.items = new ArrayList<CartItem>();
        this.shippableItems = new ArrayList<CartItem>();
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
        CartItem cartItem = new CartItem(product, quantity);
        items.add(cartItem);
        if (product.isShippable()) {
            shippableItems.add(cartItem);
        }

    }

    public boolean isItemInCart(Product product) {
        for (CartItem item : items) {
            if (item.getProduct().getName().equals(product.getName())) {
                return true; // Product is already in the cart
            }
        }
        return false; // Product is not in the cart
    }

    public CartItem getItem(String productName) {
        for (CartItem item : items) {
            if (item.getProduct().getName().equals(productName)) {
                return item; // Return the CartItem if found
            }
        }
        return null; // Return null if not found
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
        return Math.round(totalPrice * 100.0) / 100.0; // Round to 2 decimal places
    }

    public ArrayList<CartItem> ExpiredProducts() {
        ArrayList<CartItem> expiredItems = new ArrayList<CartItem>();
        for (CartItem item : items) {
            if (item.getProduct().isExpirable() && item.getProduct().isExpired()) {
                expiredItems.add(item);
            }
        }
        return expiredItems;

    }

    public boolean hasShippableProducts() {
        return shippableItems != null && !shippableItems.isEmpty();
    }

    public ArrayList<CartItem> getShippableProducts() {
        return shippableItems;
    }

    public double getTotalShipmentWeight() {
        double totalWeight = 0.0;
        for (CartItem item : shippableItems) {
            Product product = item.getProduct();
            double weight = product.getWeight();
            char weightUnit = product.getWeightUnit();
            if (weightUnit == 'g') {
                totalWeight += (weight / 1000) * item.getQuantity(); // Convert grams to kg

            } else if (weightUnit == 'k') {

                totalWeight += weight * item.getQuantity(); // Multiply by quantity
            }
        }
        return Math.round(totalWeight * 100.0) / 100.0; // Round to 2 decimal places
    }

    public boolean checkStockAvailability() {
        for (CartItem item : items) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                System.out.println("Not enough stock for " + product.getName());
                return false; // Not enough stock for at least one item
            }
        }
        return true; // All items have sufficient stock
    }

    public boolean updateStock() {
        for (CartItem item : items) {
            Product product = item.getProduct();
            int newQuantity = product.getQuantity() - item.getQuantity();
            if (newQuantity < 0) {
                System.out.println("Not enough stock for " + product.getName());
                return false; // Not enough stock for at least one item
            }
            product.setQuantity(newQuantity);
        }
        return true; // All items updated successfully
    }

    public void sendToShipmentService(ShipmentService shipmentService) {
        for (CartItem item : shippableItems) {
            for (int i = 0; i < item.getQuantity(); i++) {
                shipmentService.addShippableItem(item.getProduct());
            }
        }
        ShipmentService.printShipmentNotice();

    }
}
