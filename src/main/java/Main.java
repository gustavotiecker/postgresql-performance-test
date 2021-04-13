import java.sql.*;
import java.util.UUID;

public class Main {

    private static String sqlInsert, sqlSelect, sqlUpdate, sqlDelete;

    public static void main(String[] args) throws SQLException {
        selectOrderItem();
    }

    private static void insertOrderItem() {
        OrderItem orderItem = createOrderItem();
        sqlInsert = "INSERT INTO order_items VALUES ('" + orderItem.getOrderId() +
                "', " + orderItem.getOrderItemId() +
                ", '" + orderItem.getProductId() +
                "', '" + orderItem.getSellerId() +
                "', '" + orderItem.getShippingLimitDate() +
                "', " + orderItem.getPrice() +
                ", " + orderItem.getFreightValue() + ");";

        accessDatabase(sqlInsert);
    }

    private static void selectOrderItem() throws SQLException {
        sqlSelect = "SELECT product_id, seller_id, shipping_limit_date, price FROM order_items AS oi WHERE oi.price > " + Math.random() * 51 + ";";
        accessDatabase(sqlSelect);
    }

    private static void updateOrderItem() {
        sqlUpdate = "UPDATE order_items SET freight_value = freight_value + (freight_value * 0.2) WHERE price > " + Math.random() * 101 + ";";
        accessDatabase(sqlUpdate);
    }

    // TO DO
    private static void deleteOrderItem(int num) {
        sqlDelete = "DELETE FROM order_items WHERE price > 300 AND freight_value > 5  AND freight_value < 5 + 5;";

        accessDatabase(sqlDelete);
    }

    private static void accessDatabase(String query) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/test", "postgres", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

            //Begin thread - Barbada, 2 minutinhos
            ResultSet resultSet = preparedStatement.executeQuery();
            //End Thread - Feito carreta

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static OrderItem createOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(UUID.randomUUID().toString());
        orderItem.setOrderItemId(Integer.parseInt(String.valueOf(Math.random() * 4)));
        orderItem.setProductId(UUID.randomUUID().toString());
        orderItem.setSellerId(UUID.randomUUID().toString());
        orderItem.setShippingLimitDate("2017-02-13 13:57:51");
        orderItem.setPrice(Math.random() * 101);
        orderItem.setFreightValue(Math.random() * 19);
        return orderItem;
    }
}
