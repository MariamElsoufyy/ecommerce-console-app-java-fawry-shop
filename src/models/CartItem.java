package models;

public class CartItem {
    private Product product;
    private int quantity;

    // CartItem constructor
    public CartItem(Product product, int quantity) {
        this.product = product;
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        this.quantity = quantity;
    }

    // getters
    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }

    // setters

    public void addQuantitytoCart(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (quantity > product.getQuantity()) {
            throw new IllegalArgumentException("Not Enough Quantity in stock");
        }
        product.setQuantity(product.getQuantity() - quantity);
        this.quantity += quantity;
    }

    public void deduceQuantityFromCart(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("Not Enough Quantity in cart");
        }
        product.setQuantity(product.getQuantity() + quantity);
        this.quantity -= quantity;
    }

}
