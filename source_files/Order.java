import java.sql.Connection;
import java.util.ArrayList;

public class Order {

    private int order_number;
    private String customer, shipping_postal_code, shipping_street_address, current_postal_code, current_street_address, order_status, date_of_sale, warehouse;

    Order(int order_num, String customer, String shipping_postal_code,String shipping_street_address, String current_postal_code,String current_street_address,String order_status, String date_of_sale,String warehouse){
        this.order_number = order_num;
        this.customer = customer;
        this.shipping_postal_code = shipping_postal_code;
        this.shipping_street_address = shipping_street_address;
        this.current_postal_code = current_postal_code;
        this.current_street_address = current_street_address;
        this.order_status = order_status;
        this.date_of_sale = date_of_sale;
        this.warehouse = warehouse;
    }

    public int getOrderNumber(){ return order_number; }
    public String getOrderStatus() { return order_status; }

    public String toString() {
        return "Order #" + order_number + "\n" +
                "-----\n" +
                "Shipping postal code: " + shipping_postal_code + '\n' +
                "Shipping street address: " + shipping_street_address + '\n' +
                "Current postal code: " + current_postal_code + '\n' +
                "Current street address: " + current_street_address + '\n' +
                "Order status: " + order_status + '\n' +
                "Date of sale: " + date_of_sale + '\n' +
                "Warehouse: " + warehouse;
    }

    // May be a helpful function - e.g for reports, can just work with the order objects rather than working in dbms
    // Requires creating order objects using query for orders
    public static ArrayList<Order> get_all_orders(Connection connection) {
        return null;
    }

}