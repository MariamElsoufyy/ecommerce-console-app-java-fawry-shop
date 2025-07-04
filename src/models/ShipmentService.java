package models;

import java.util.ArrayList;

public class ShipmentService {
    static ArrayList<Shippable> ShippedOrders;

    public ShipmentService() {
        ShipmentService.ShippedOrders = new ArrayList<Shippable>();

    }

    public void addShippableItem(Shippable item) {
        ShippedOrders.add(item);
    }

    public static void printShipmentNotice() {
        System.out.println("** Shipment Notice **");
        for (models.Shippable product : ShippedOrders) {
            System.out.println(product.getName() + "\t" + product.getWeight());
        }
    }

}