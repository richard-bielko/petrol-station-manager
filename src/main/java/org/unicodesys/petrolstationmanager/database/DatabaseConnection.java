package org.unicodesys.petrolstationmanager.database;

import org.unicodesys.petrolstationmanager.utils.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private final Logger logger;
    private static final String URL = "jdbc:postgresql://localhost:5432/petrol_station_manager";
    private static final String USER = "postgres";
    private static final String PASSWORD = "admin";

    public DatabaseConnection() {
        this.logger = new Logger();
    }

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.success("Connected to PostgreSQL successfully!");
        } catch (SQLException e) {
            logger.error("Database connection failed!");
            e.printStackTrace();
        }
        return conn;
    }
}
