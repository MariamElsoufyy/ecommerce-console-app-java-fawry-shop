package models;

public class CartItem {
    private Product product;
    private int quantity;

    // CartItem constructor
    public CartItem(Product product, int quantity) {
        this.product = product;
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
        this.quantity += quantity;
    }

    public void deduceQuantityFromCart(int quantity) {
        this.quantity -= quantity;
    }

}
