import java.math.BigDecimal;
import java.sql.*;
import java.util.UUID;

public class Main {

    private static final Integer ITERATIONS = 1;

    private static String sqlInsert, sqlSelect, sqlUpdate, sqlDelete;
    private static BigDecimal timeSelect, timeInsert, timeUpdate, timeDelete;

    public static void main(String[] args) throws SQLException {

        timeSelect = instanceBigDecimal();
        timeInsert = instanceBigDecimal();
        timeUpdate = instanceBigDecimal();
        timeDelete = instanceBigDecimal();

        selectOrderItem();
        insertOrderItem();
        updateOrderItem();
        deleteOrderItem((int) Math.round(Math.random() * 100000));

        //obtendo medias
        timeSelect = timeSelect.divide(new BigDecimal(ITERATIONS.toString()));
        timeSelect = timeSelect.divide(new BigDecimal("1000000"));

        timeInsert = timeInsert.divide(new BigDecimal(ITERATIONS.toString()));
        timeInsert = timeInsert.divide(new BigDecimal("1000000"));

        timeUpdate = timeUpdate.divide(new BigDecimal(ITERATIONS.toString()));
        timeUpdate = timeUpdate.divide(new BigDecimal("1000000"));

        timeDelete = timeDelete.divide(new BigDecimal(ITERATIONS.toString()));
        timeDelete = timeDelete.divide(new BigDecimal("1000000"));

        System.out.println("=============== Execution time averages in milliseconds ===============");
        System.out.println("\nSelect: " + timeSelect +
                "\nInsert: " + timeInsert +
                "\nUpdate: " + timeUpdate +
                "\nDelete: " + timeDelete);
        System.out.println("=======================================================================");
    }

    private static void selectOrderItem() throws SQLException {
        for (int i = 0; i < 10; i++) {
            sqlSelect = "SELECT * FROM order_items AS oi WHERE oi.price > " + Math.random() * 51 + ";";
            accessDatabase(sqlSelect);
        }
        System.out.println("select");
    }

    private static void insertOrderItem() {
        for (int i = 0; i < 1; i++) {
            OrderItem orderItem = createOrderItem();
            sqlInsert = "INSERT INTO order_items VALUES (" + orderItem.getControlId() +
                    ", '" + orderItem.getOrderId() +
                    "', " + orderItem.getOrderItemId() +
                    ", '" + orderItem.getProductId() +
                    "', '" + orderItem.getSellerId() +
                    "', '" + orderItem.getShippingLimitDate() +
                    "', " + orderItem.getPrice() +
                    ", " + orderItem.getFreightValue() + ");";
            accessDatabase(sqlInsert);
        }
        System.out.println("insert");
    }

    private static void updateOrderItem() {
        for (int i = 0; i < 10; i++) {
            sqlUpdate = "UPDATE order_items SET freight_value = " + Math.random() * 101 + "WHERE control_id = " + (int) Math.round(Math.random() * 101) + ";";
            accessDatabase(sqlUpdate);
        }
        System.out.println("update");
    }

    private static void deleteOrderItem(int num) {
        for (int i = 0; i < 10; i++) {
            sqlDelete = "DELETE FROM order_items WHERE control_id = " + num + ";";
            accessDatabase(sqlDelete);
        }
        System.out.println("delete");
    }

    private static BigDecimal instanceBigDecimal() {
        return new BigDecimal("0");
    }

    private static void accessDatabase(String query) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://127.0.0.1:5432/test", "postgres", "root");
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {

            if (conn == null) {
                System.out.println("Failed to make connection!");
            }

            Long startTime = System.nanoTime();
            preparedStatement.execute();
            Long endTime = System.nanoTime();

            Long executionTime = endTime - startTime;

            if(query.startsWith("SELECT")) {
                timeSelect = timeSelect.add(new BigDecimal(executionTime.toString()));
            } else if(query.startsWith("INSERT")) {
                timeInsert = timeInsert.add(new BigDecimal(executionTime.toString()));
            } else if(query.startsWith("UPDATE")) {
                timeUpdate = timeUpdate.add(new BigDecimal(executionTime.toString()));
            } else if(query.startsWith("DELETE")) {
                timeDelete = timeDelete.add(new BigDecimal(executionTime.toString()));
            }


        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static OrderItem createOrderItem() {
        OrderItem orderItem = new OrderItem();
        orderItem.setControlId((int) Math.round(Math.random() * 4));
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
