package com.bulka.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String DB_URL = "db.url";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_POOL_SIZE = "db.pool.size";

    private static Properties properties;

    static {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/database.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла database.properties", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(properties.getProperty(DB_URL), properties.getProperty(DB_USER), properties.getProperty(DB_PASSWORD));
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка соединения с базой данных", e);
        }
    }
}
