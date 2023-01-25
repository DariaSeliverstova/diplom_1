package netology.data;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;

public class DBHelper {
    public static String dbUrl = "jdbc:mysql://localhost:3306/app";
    public static String dbUser = "app";
    public static String dbPass = "pass";

    @SneakyThrows
    public static String getPaymentStatus() {
        val sql = "SELECT status FROM payment_entity;";
        val runner = new QueryRunner();
        String paymentStatus;

        try (
                val conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)
                )
         {
            paymentStatus = runner.query(conn, sql, new ScalarHandler<>());
        }
        return paymentStatus;
    }

    @SneakyThrows
    public static String getCreditStatus() {
        val status = "SELECT status FROM credit_request_entity;";
        val runner = new QueryRunner();
        String creditStatus;

        try (
                val conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)
        ) {
            creditStatus = runner.query(conn, status, new ScalarHandler<>());
        }

        return creditStatus;
    }

    @SneakyThrows
    public static long getPaymentId() {
        val sql = "SELECT COUNT(id) as count FROM payment_entity;";
        val runner = new QueryRunner();
        long paymentId;

        try (
                val conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)
        ) {
            paymentId = runner.query(conn, sql, new ScalarHandler<>());
        }
        return paymentId;
    }

    @SneakyThrows
    public static long getCreditId() {
        val sql = "SELECT COUNT(id) as count FROM credit_request_entity;";
        val runner = new QueryRunner();
        long creditId;

        try (
                val conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)
        ) {
            creditId = runner.query(conn, sql, new ScalarHandler<>());
        }
        return creditId;
    }

    @SneakyThrows
    public static long getOrderId() {
        val sql = "SELECT COUNT(id) as count FROM order_entity;";
        val runner = new QueryRunner();
        long orderId;

        try (
                val conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)
        ) {
            orderId = runner.query(conn, sql, new ScalarHandler<>());
        }
        return orderId;
    }


    @SneakyThrows
    public static void clearSUTData() {
        var runner = new QueryRunner();
        var sqlDeleteAllOrders = "DELETE FROM order_entity;";
        var sqlDeleteAllCreditRequest = "DELETE FROM credit_request_entity;";
        var sqlDeleteAllPaymentRequest = "DELETE FROM payment_entity;";

        try (
                val conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)
        ) {
            runner.update(conn, sqlDeleteAllOrders);
            runner.update(conn, sqlDeleteAllCreditRequest);
            runner.update(conn, sqlDeleteAllPaymentRequest);
        }
    }

}
