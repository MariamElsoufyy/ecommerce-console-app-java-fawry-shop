package models;

public class Customer {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
    private double balance;

    // Customer constructor
    public Customer(String name, String email, String password, String phone, String address, double balance) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.balance = 0.0;

    }

    // Getters
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public double getBalance() {
        return balance;
    }

    // Setters
    public void setBalance(double balance) {
        this.balance = balance;
    }

}
