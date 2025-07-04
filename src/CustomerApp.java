import java.util.Scanner;

import models.Cart;
import models.CartItem;
import models.ShipmentService;

public class CustomerApp {
    static final Scanner scanner = new Scanner(System.in);
    static Cart cart = new Cart();
    static String customerIdentfier = "";
    static double customerBalance = 0.0;
    static double shippingCost = 65.0;
    static double totalPayment = 0.0;
    static ShipmentService shipmentService = new ShipmentService();

    public static void main(String[] args) throws Exception {
        String choice = "";
        // Initialize the application
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Welcome to FawryShop!");
        utils.DBHelper.initializeDatabase();

        System.out.println("Choose an action:");
        System.out.println("1. Sign Up");
        System.out.println("2. Log In");
        System.out.println("3. Exit (or type 'x' to exit)");

        choice = scanner.nextLine();
        while (!choice.equals("3") && !choice.equals("x") && !choice.equals("")) {
            switch (choice) {
                case "1": // Sign Up
                    signUp();
                    System.out.println("You can now log in with your credentials.");
                    System.out.println("log in to continue.");
                case "2": // Log In
                    logIn();
                    choice = "";
                    break;

                case "3": // Exit
                case "x":
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        if (choice.equals("3") || choice.equals("x")) {
            System.out.println("Exiting the application!");
            scanner.close();
            return;
        }

        while (choice != "x" && !choice.equals("8")) {
            System.out.println("Choose an action:");
            System.out.println("1. View all products");
            System.out.println("2. View cart");
            System.out.println("3. Add a product to cart");
            System.out.println("4. Remove a product from cart");
            System.out.println("5. Checkout");
            System.out.println("6. View your balance");
            System.out.println("7. add money to your balance");
            System.out.println("8. Exit (or type 'x' to exit)");

            choice = scanner.nextLine();
            switch (choice) {
                case "1": // View all products
                    viewAllProducts();
                    break;
                case "2": // View cart
                    viewCart();
                    break;
                case "3": // Add a product to cart
                    addProductToCart();
                    System.out.println("Updated cart:");
                    viewCart();
                    break;
                case "4": // Remove a product from cart
                    removeProductFromCart();
                    System.out.println("Updated cart:");
                    viewCart();
                    break;
                case "5": // Checkout
                    checkOut();
                    break;
                case "6": // View your balance
                    System.out.println("Your current balance is: " + customerBalance);
                    break;
                case "7": // Add money to your balance
                    addBalance();
                    break;
                case "8": // Exit
                case "x":
                    System.out.println("Exiting the application!");
                    scanner.close();
                    return;

            }

        }

    }

    private static void signUp() {
        System.out.println("Please enter your name:");
        String name = scanner.nextLine();
        System.out.println("Please enter your email:");
        String email = scanner.nextLine();
        System.out.println("Please enter your password:");
        String password = scanner.nextLine();
        System.out.println("Please enter your phone number:");
        String phone = scanner.nextLine();
        System.out.println("Please enter your address:");
        String address = scanner.nextLine();

        models.Customer newCustomer = new models.Customer(name, email, password, phone, address, 0.0);
        dao.CustomerDAO.addCustomer(newCustomer);
        System.out.println("Customer registered successfully!");
    }

    private static void logIn() {
        do {
            System.out.println("Please enter your email or phone number:");
            String identifier = scanner.nextLine();
            System.out.println("Please enter your password:");
            String password = scanner.nextLine();
            models.Customer customer = dao.CustomerDAO.getCustomer(identifier, password);
            if (customer != null) {
                System.out.println("Login successful! Welcome " + customer.getName() + "!");
                customerIdentfier = identifier;
                customerBalance = customer.getBalance();
                System.out.println("Your current balance is: " + customerBalance);
                break;

            } else {
                System.out.println("Invalid credentials. Please try again.");
            }
        } while (customerIdentfier.isEmpty());
    }

    private static void viewAllProducts() {
        System.out.println("Available products:");
        java.util.List<models.Product> products = dao.ProductDAO.getAllProducts();

        if (products.isEmpty()) {
            System.out.println("No products available at the moment.");
        } else {
            System.out.println("all available products:");
            for (models.Product product : products) {
                System.out.println(" - " + product.getName() + " | Price: "
                        + product.getPrice());
            }

        }
    }

    private static void viewCart() {
        if (cart.getItems().isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            System.out.println("Your cart contains the following items:");
            for (models.CartItem item : cart.getItems()) {
                System.out.println(" - " + item.getProduct().getName() + " | Quantity: "
                        + item.getQuantity() + " | Price: " + item.getProduct().getPrice() + " | Total: "
                        + item.getTotalPrice());
            }
        }
    }

    private static void addProductToCart() {
        String continueAdding = "yes";
        do {
            System.out.println("Please enter the product name to add to your cart:");
            String productName = scanner.nextLine().toLowerCase();
            models.Product productToAdd = dao.ProductDAO.getProductByName(productName);

            if (productToAdd != null) {
                System.out.println("Please enter the quantity to add:");
                int quantity = Integer.parseInt(scanner.nextLine());

                while (quantity <= 0) {
                    System.out.println("Invalid quantity. Please enter a positive number.");
                    quantity = Integer.parseInt(scanner.nextLine());
                }
                if (!cart.isItemInCart(productToAdd)) {
                    while (quantity > productToAdd.getQuantity()) {
                        System.out.println("Not Enough Quantity in stock, Quantity in stock: "
                                + productToAdd.getQuantity() + " items");
                        System.out.println("Please enter a valid quantity to add:");
                        quantity = Integer.parseInt(scanner.nextLine());
                        while (quantity <= 0) {
                            System.out.println("Invalid quantity. Please enter a positive number.");
                            quantity = Integer.parseInt(scanner.nextLine());
                        }
                    }
                } else {

                    if (productToAdd.getQuantity() == cart.getItem(productName).getQuantity()) {

                        System.out.println("You already have this product in your cart with the maximum quantity.");
                        return;
                    } else {
                        while (quantity > productToAdd.getQuantity() + cart.getItem(productName).getQuantity()) {
                            System.out.println("Not Enough Quantity in stock, Quantity in stock: "
                                    + productToAdd.getQuantity() + " items");
                            System.out.println("Please enter a valid quantity to add:");
                            quantity = Integer.parseInt(scanner.nextLine());
                            while (quantity <= 0) {
                                System.out.println("Invalid quantity. Please enter a positive number.");
                                quantity = Integer.parseInt(scanner.nextLine());
                            }
                        }

                    }
                }
                cart.addItem(productToAdd, quantity);
            } else {
                System.out.println("Product not available. Please try again.");
            }

            System.out.println("Do you want to add another product to your cart? (yes/no)");
            continueAdding = scanner.nextLine().toLowerCase();
        } while (continueAdding.equals("yes"));
    }

    private static void removeProductFromCart() {
        String continueRemoving = "yes";
        CartItem itemToRemove = null;
        do {
            System.out.println("Please enter the product name to remove from your cart:");
            String productName = scanner.nextLine().toLowerCase();
            for (models.CartItem item : cart.getItems()) {
                if (item.getProduct().getName().equalsIgnoreCase(productName)) {
                    itemToRemove = item;
                    break;
                }
            }
            if (itemToRemove != null) {
                System.out.println("Please enter the quantity to remove:");
                int quantity = Integer.parseInt(scanner.nextLine());
                while (quantity <= 0) {
                    System.out.println("Invalid quantity. Please enter a positive number.");
                    quantity = Integer.parseInt(scanner.nextLine());
                }
                while (quantity > itemToRemove.getQuantity()) {
                    System.out
                            .println("Not Enough Quantity in cart, Quantity in cart:" + itemToRemove.getQuantity()
                                    + " items");
                    System.out.println("Please enter a valid quantity to remove:");
                    quantity = Integer.parseInt(scanner.nextLine());

                }
                cart.deduceItem(productName, quantity);
                System.out.println(
                        quantity + " items of " + itemToRemove.getProduct().getName()
                                + " removed from your cart.");
            } else {
                System.out.println("Product not found in your cart. Please try again.");
            }
            if (cart.getItems().isEmpty()) {
                System.out.println("Your cart is now empty.");
                return;
            } else {
                System.out.println("Do you want to remove another product from your cart? (yes/no)");
                continueRemoving = scanner.nextLine().toLowerCase();
            }

        } while (continueRemoving.equals("yes") && !cart.getItems().isEmpty());

    }

    private static void checkOut() {

        if (cart.getItems().isEmpty()) {
            System.out.println("Your cart is empty. Please add items to your cart before checking out");
            return;
        }
        if (cart.ExpiredProducts().size() > 0) {
            System.out.println("You have expired products in your cart, please remove them before checking out");
            System.out.println("Expired Products in your cart:");
            for (models.CartItem item : cart.ExpiredProducts()) {
                System.out.println(item.getProduct().getName() + " | Quantity: " + item.getQuantity());
            }
            return;
        }
        totalPayment = cart.getTotalPrice();
        if (cart.hasShippableProducts()) {
            System.out.println(
                    "You have shippable products in your cart, shipping cost will be added to your total price");
            System.out.println("Shipping cost: " + shippingCost);
            printShipmentNotice();
            totalPayment += shippingCost;
        }
        printCheckoutReceipt();
        System.out.println("Do you want to proceed with checkout? (yes/no)");
        String proceed = scanner.nextLine().toLowerCase();
        if (proceed.equals("yes")) {
            if (customerBalance < totalPayment) {
                System.out.println("You don't have enough balance to complete this transaction.");
                System.out.println("Your current balance is: " + customerBalance);
                System.out.println("Please add money to your balance before checking out");
                return;
            }
            for (models.CartItem item : cart.getItems()) {
                models.Product product = item.getProduct();
                // Deduct the quantity from the product stock
                int newQuantity = product.getQuantity() - item.getQuantity();
                if (newQuantity < 0) {
                    System.out.println("Not enough stock for " + product.getName()
                            + ". Please remove it from cart and then try again.");
                    return;
                }

            }
            for (models.CartItem item : cart.getItems()) {
                models.Product product = item.getProduct();
                // Deduct the quantity from the product stock
                int newQuantity = product.getQuantity() - item.getQuantity();
                product.setQuantity(newQuantity);
                // Update the product quantity in the database
                dao.ProductDAO.updateProduct(product);
            }

            // Deduct the total payment from the customer's balance
            customerBalance -= totalPayment;
            dao.CustomerDAO.updateCustomerBalance(customerIdentfier, customerBalance);
            System.out.println("Checkout successful! Your new balance is: " + customerBalance);
            System.out.println("Thank you for shopping at FawryShop!");
            cart.sendToShipmentService(shipmentService);
            cart.clearCart();

            return;

        } else {
            System.out.println("Checkout cancelled.");
        }

    }

    public static void printShipmentNotice() {
        System.out.println("\n** Shipment Notice **");
        for (models.CartItem item : cart.getShippableProducts()) {
            System.out.println(item.getQuantity() + "x " + item.getProduct().getName() +
                    "\t" + item.getProduct().getWeight() + item.getProduct().getWeightUnit());
        }
        System.out.println("Total Package weight \t" + cart.getTotalShipmentWeight() + "kg");

    }

    public static void printCheckoutReceipt() {
        System.out.println("\n** Checkout receipt **");
        for (models.CartItem item : cart.getItems()) {
            System.out.println(item.getQuantity() + "x " + item.getProduct().getName() +
                    "\t" + item.getProduct().getPrice());
        }
        System.out.println("------------------------------------");
        if (cart.hasShippableProducts()) {
            System.out.println("SubTotal \t" + cart.getTotalPrice());
            System.out.println("Shipping \t" + shippingCost);
            System.out.println("Amount \t" + totalPayment);
        }
    }

    public static void addBalance() {
        System.out.println("Please enter the amount to add to your balance:");
        double amountToAdd = Double.parseDouble(scanner.nextLine());
        while (amountToAdd <= 0) {
            System.out.println("Invalid amount. Please enter a positive number.");
            amountToAdd = Double.parseDouble(scanner.nextLine());

        }
        customerBalance += amountToAdd;
        dao.CustomerDAO.updateCustomerBalance(customerIdentfier, customerBalance);
        System.out.println("Your new balance is: " + customerBalance);
    }

}
