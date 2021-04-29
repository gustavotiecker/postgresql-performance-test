import java.sql.*;
import java.util.UUID;

public class Main {

    private static String sqlInsert, sqlSelect, sqlUpdate, sqlDelete;
    private static long count = 0;
    private static double timeSelect = 0, timeInsert = 0, timeUpdate = 0, timeDelete = 0;

    public static void main(String[] args) throws SQLException {
        selectOrderItem();
        insertOrderItem();
        updateOrderItem();
        deleteOrderItem((int) Math.random() * 1000000);

        //obtendo medias
        timeSelect = timeSelect / count;
        timeInsert = timeInsert / count;
        timeUpdate = timeUpdate / count;
        timeDelete = timeDelete / count;

        System.out.println("=============== Averages ===============");
        System.out.println("Select: " + timeSelect +
                "Insert: " + timeInsert +
                "Update: " + timeUpdate +
                "Delete: " + timeDelete);
        System.out.println("========================================");
    }

    private static void insertOrderItem() {
        //for
        count = 0;
        OrderItem orderItem = createOrderItem();
        sqlInsert = "INSERT INTO order_items VALUES ('" + orderItem.getOrderId() +
                "', " + orderItem.getOrderItemId() +
                ", '" + orderItem.getProductId() +
                "', '" + orderItem.getSellerId() +
                "', '" + orderItem.getShippingLimitDate() +
                "', " + orderItem.getPrice() +
                ", " + orderItem.getFreightValue() + ");";
                accessDatabase(sqlInsert);
                count ++;
        //endfor

    }

    private static void selectOrderItem() throws SQLException {
        //for
        count = 0;
        sqlSelect = "SELECT * FROM order_items AS oi WHERE oi.price > " + Math.random() * 51 + ";";
        accessDatabase(sqlSelect);
        //endfor


    }

    private static void updateOrderItem() {
        //for
        count = 0;
        sqlUpdate = "UPDATE order_items SET freight_value = freight_value + (freight_value * 0.2) WHERE price > " + Math.random() * 101 + ";";
        accessDatabase(sqlUpdate);
        //endfor


    }

    // TO DO
    private static void deleteOrderItem(int num) {
        //for
        count = 0;
        sqlDelete = "DELETE FROM order_items WHERE price > 300 AND freight_value > 5  AND freight_value < 5 + 5;";
        accessDatabase(sqlDelete);
        //endfor


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

            long startTime = System.nanoTime();
            ResultSet resultSet = preparedStatement.executeQuery();
            long endTime = System.nanoTime();

            long executionTime = endTime - startTime;

            if(query.startsWith("SELECT")) {
                timeSelect += executionTime;
            } else if(query.startsWith("INSERT")) {
                timeInsert += executionTime;
            } else if(query.startsWith("UPDATE")) {
                timeUpdate += executionTime;
            } else if(query.startsWith("DELETE")) {
                timeDelete += executionTime;
            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static OrderItem createOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderId(UUID.randomUUID().toString());
        orderItem.setOrderItemId((int) Math.round(Math.random() * 4));
        orderItem.setProductId(UUID.randomUUID().toString());
        orderItem.setSellerId(UUID.randomUUID().toString());
        orderItem.setShippingLimitDate("2017-02-13 13:57:51");
        orderItem.setPrice(Math.random() * 101);
        orderItem.setFreightValue(Math.random() * 19);
        return orderItem;
    }
}
