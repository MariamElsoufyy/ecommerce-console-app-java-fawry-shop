import java.util.Scanner;

public class SellerApp {

    static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        String choice = "";
        // Initialize the application
        System.out.println("-----------------------------------------------------------------");
        System.out.println("Welcome to the Seller Application!");
        System.out.println("Initializing database...");
        utils.DBHelper.initializeDatabase();

        while (!choice.equals("x") && !choice.equals("8")) {
            System.out.println("Choose an action:");
            System.out.println("1. Add a new product");
            System.out.println("2. Update an existing product's Quantity");
            System.out.println("3. Delete a product");
            System.out.println("4. Check expired products");
            System.out.println("5. View all products");
            System.out.println("6. Clear all products");
            System.out.println("7. Show all customers");
            System.out.println("8. Exit (or type 'x' to exit)");

            choice = scanner.nextLine();
            switch (choice) {
                case "1": // Add a new product
                    addNewProduct();
                    break;
                case "2": // Update an existing product
                    updateProductQuantity();
                    break;

                case "3": // Delete a product
                    deleteProduct();
                    break;

                case "4":
                    // Check expired products
                    checkExpiredProducts();
                    break;
                case "5": // View all products
                    viewAllProducts();
                    break;
                case "6": // Clear all products
                    System.out.println("Clearing all products...");
                    dao.ProductDAO.clearAllProducts();
                    break;
                case "7": // Show all customers
                    System.out.println("Showing all customers...");
                    java.util.ArrayList<models.Customer> customers = dao.CustomerDAO.getAllCustomers();
                    if (customers.isEmpty()) {
                        System.out.println("No customers found.");
                    } else {
                        System.out.println("Customers:");
                        for (models.Customer customer : customers) {
                            System.out.println(" - " + customer.getName() + " | Email: "
                                    + customer.getEmail() + " | Phone: " + customer.getPhone()
                                    + " | Address: " + customer.getAddress() + " | Balance: "
                                    + customer.getBalance());
                        }
                    }
                    break;
                case "8":
                case "x":
                    System.out.println("Exiting the application!");

                    break;

                default:
                    System.out.println("Invalid choice, please try again.");
            }

        }
        scanner.close();
        return;
    }

    private static void addNewProduct() {
        System.out.println("Adding a new product...");
        System.out.println("Please enter the product name:");
        String productName = scanner.nextLine().toLowerCase();
        models.Product existingProduct = dao.ProductDAO.getProductByName(productName);

        if (existingProduct != null) {
            System.out.println(
                    "Product with the same name exists, Updating inventory instead");
            System.out.println("Please enter the quantity:");
            int productQuantity = Integer.parseInt(scanner.nextLine());
            // Update the existing product's quantity
            existingProduct.setQuantity(existingProduct.getQuantity() + productQuantity);
            dao.ProductDAO.updateProduct(existingProduct);
            return;
        }

        System.out.println("Please enter the product price:");
        double productPrice = Double.parseDouble(scanner.nextLine());
        System.out.println("Please enter the product quantity:");
        int productQuantity = Integer.parseInt(scanner.nextLine());
        while (productQuantity <= 0) {
            System.out.println("Quantity must be greater than zero. Please enter a valid quantity:");
            productQuantity = Integer.parseInt(scanner.nextLine());
        }
        System.out.println("Is the product shippable? (yes/no):");
        boolean isShippable = scanner.nextLine().equalsIgnoreCase("yes");
        double productWeight = 0.0;
        char weightUnit = ' ';
        if (isShippable) {
            System.out.println("Please enter the product weight:");
            productWeight = Double.parseDouble(scanner.nextLine());
            System.out.println("Please enter the weight unit (g for grams, k for kilograms):");
            String weightUnitStr = scanner.nextLine();
            if (weightUnitStr.length() > 0) {
                weightUnit = weightUnitStr.charAt(0);
            }
        }
        System.out.println("Is the product expirable? (yes/no):");
        boolean isExpirable = scanner.nextLine().equalsIgnoreCase("yes");

        if (isExpirable) {
            System.out.println("Please enter the expiration date (yyyy-MM-dd):");
            String expDateStr = scanner.nextLine();
            java.util.Date expirationDate = null;
            while (expirationDate == null) {
                try {
                    expirationDate = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(expDateStr);
                } catch (java.text.ParseException e) {
                    System.out.println("Invalid date format, please use yyyy-MM-dd.");
                    expirationDate = null; // Reset expirationDate to null to retry
                    continue; // Skip to the next iteration
                }
            }

            // Create a new product with expiration date
            models.Product newProduct = new models.Product(productName, productPrice, productQuantity,
                    isShippable, isExpirable, productWeight, weightUnit, expirationDate);
            dao.ProductDAO.addProduct(newProduct);
        } else {
            // Create a new product without expiration date
            models.Product newProduct = new models.Product(productName, productPrice, productQuantity,
                    isShippable, isExpirable, productWeight, weightUnit, null);
            dao.ProductDAO.addProduct(newProduct);
        }

    }

    private static void updateProductQuantity() {
        System.out.println("Updating an existing product...");
        System.out.println("Please enter the product name to update:");
        String updatedProductName = scanner.nextLine().toLowerCase();
        models.Product existingProduct = dao.ProductDAO.getProductByName(updatedProductName);
        if (existingProduct == null) {
            System.out.println("Product not found. Please check the name and try again.");
            return;
        }
        System.out.println("Current quantity: " + existingProduct.getQuantity());
        System.out.println("Please enter the new quantity:");
        int newQuantity = Integer.parseInt(scanner.nextLine());
        while (newQuantity <= 0) {
            System.out.println("Quantity must be greater than zero. Please enter a valid quantity:");
            newQuantity = Integer.parseInt(scanner.nextLine());
        }
        existingProduct.setQuantity(newQuantity);
        dao.ProductDAO.updateProduct(existingProduct);
    }

    private static void deleteProduct() {
        System.out.println("Deleting a product...");
        dao.ProductDAO.getAllProducts();
        System.out.println("Please enter the product name to delete:");
        String deleteProductName = scanner.nextLine();
        models.Product productToDelete = dao.ProductDAO.getProductByName(deleteProductName);
        if (productToDelete == null) {
            System.out.println("Product not found. Please check the name and try again.");
            return;
        }
        dao.ProductDAO.deleteProductByName(deleteProductName);
        System.out.println("Product deleted successfully.");
    }

    private static void checkExpiredProducts() {
        System.out.println("Checking expired products...");
        java.util.ArrayList<models.Product> expiredProducts = dao.ProductDAO.getExpiredProducts();
        if (expiredProducts.isEmpty()) {
            System.out.println("No expired products found.");
        } else {
            System.out.println("Expired Products:");
            for (models.Product product : expiredProducts) {
                System.out.println(" - " + product.getName() + " (Expired on: "
                        + new java.text.SimpleDateFormat("yyyy-MM-dd")
                                .format(product.getExpirationDate())
                        + ")");
            }
        }
        if (!expiredProducts.isEmpty()) {
            System.out.println("Do you want to delete expired products? (yes/no)");
            String deleteChoice = scanner.nextLine().toLowerCase();
            if (deleteChoice.equals("yes")) {
                for (models.Product expiredProduct : expiredProducts) {
                    dao.ProductDAO.deleteProductByName(expiredProduct.getName());
                }
                System.out.println("Expired products deleted successfully.");
            } else {
                System.out.println("Expired products not deleted.");
            }
        }

    }

    private static void viewAllProducts() {
        System.out.println("Viewing all products...");
        java.util.ArrayList<models.Product> allProducts = dao.ProductDAO.getAllProducts();
        if (allProducts.isEmpty()) {
            System.out.println("No products available.");
        } else {
            System.out.println("All Products:");
            for (models.Product product : allProducts) {
                String expDateStr = product.getExpirationDate() != null
                        ? new java.text.SimpleDateFormat("yyyy-MM-dd")
                                .format(product.getExpirationDate())
                        : "N/A";
                System.out.println(" - " + product.getName() + " | Price: "
                        + product.getPrice() + " | Quantity: " + product.getQuantity()
                        + " | Shippable: " + product.isShippable() + " | Expirable: "
                        + product.isExpirable() + " | Weight: " + product.getWeight()
                        + " " + product.getWeightUnit() + " | Expiration Date: " + expDateStr);
            }

        }

    }
}
