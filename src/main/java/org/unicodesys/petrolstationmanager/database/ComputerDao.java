package org.unicodesys.petrolstationmanager.database;

import org.unicodesys.petrolstationmanager.enums.ComputerType;
import org.unicodesys.petrolstationmanager.model.Computer;
import org.unicodesys.petrolstationmanager.utils.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComputerDao {

    private final DatabaseConnection databaseConnection;
    private final Logger logger;

    public ComputerDao() {
        this.databaseConnection = new DatabaseConnection();
        this.logger = new Logger();
    }

    public Computer create(Computer computer) {
        String sql = "INSERT INTO computer (number, ip_address, type, petrol_station_id) VALUES (?, ?::INET, ?, ?)";
        String generatedColumns[] = {"id"};

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, generatedColumns)) {

            stmt.setString(1, computer.getNumber());
            stmt.setString(2, computer.getIpAddress().getHostAddress());
            stmt.setString(3, computer.getType().getValue());
            stmt.setInt(4, computer.getPetrolStationId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        computer.setId(generatedKeys.getInt(1));
                        logger.success("Computer created successfully!");
                        return computer;
                    } else {
                        logger.error("Failed to retrieve generated ID.");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Failed creating computer");
            e.printStackTrace();
        }
        return null;
    }

    public Computer getById(Integer id) {
        Computer computer = null;
        String sql = "SELECT * FROM computer WHERE id=?";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    computer = new Computer(
                            rs.getInt("id"),
                            rs.getString("number"),
                            InetAddress.getByName(rs.getString("ip_address")),
                            ComputerType.valueOf(rs.getString("type")),
                            rs.getInt("petrol_station_id")
                    );
                }
            }

        } catch (SQLException | UnknownHostException e) {
            logger.error("Failed retrieving computer ID: " + id);
            e.printStackTrace();
        }
        return computer;
    }

    public List<Computer> listAll() {
        List<Computer> computers = new ArrayList<>();
        String sql = "SELECT * FROM computer";

        try (Connection conn = databaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Computer computer = new Computer(
                        rs.getInt("id"),
                        rs.getString("number"),
                        InetAddress.getByName(rs.getString("ip_address")),
                        ComputerType.valueOf(rs.getString("type")),
                        rs.getInt("petrol_station_id")
                );
                computers.add(computer);
            }

        } catch (SQLException | UnknownHostException e) {
            logger.error("Failed retrieving computers");
            e.printStackTrace();
        }
        return computers;
    }

    public List<Computer> listByPetrolStationId(Integer id) {
        List<Computer> computers = new ArrayList<>();
        String sql = "SELECT * FROM computer WHERE petrol_station_id=?";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Computer computer = new Computer(
                            rs.getInt("id"),
                            rs.getString("number"),
                            InetAddress.getByName(rs.getString("ip_address")),
                            ComputerType.valueOf(rs.getString("type")),
                            rs.getInt("petrol_station_id")
                    );
                    computers.add(computer);
                }
            }

        } catch (SQLException | UnknownHostException e) {
            logger.error("Failed retrieving computers for petrol station ID: " + id);
            e.printStackTrace();
        }

        return computers;
    }

    public Computer update(Computer computer) {
        String sql = "UPDATE computer SET number=?, ip_address=?::INET, type=?, petrol_station_id=? WHERE id=?";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, computer.getNumber());
            stmt.setString(2, computer.getIpAddress().getHostAddress());
            stmt.setString(3, computer.getType().getValue());
            stmt.setInt(4, computer.getPetrolStationId());
            stmt.setInt(5, computer.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.success("Computer updated successfully!");
                return computer;
            } else {
                logger.warning("No changes made to computer ID: " + computer.getId());
                return computer;
            }

        } catch (SQLException e) {
            logger.error("Failed updating computer ID: " + computer.getId());
            e.printStackTrace();
        }

        return null;
    }

    public Computer delete(Integer id) {
        String sql = "DELETE FROM computer WHERE id=? RETURNING id, number, ip_address, type, petrol_station_id";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Computer deletedComputer = new Computer(
                            rs.getInt("id"),
                            rs.getString("number"),
                            InetAddress.getByName(rs.getString("ip_address")),
                            ComputerType.valueOf(rs.getString("type")),
                            rs.getInt("petrol_station_id")
                    );
                    logger.success("Computer deleted successfully!");
                    return deletedComputer;
                }
            }

        } catch (SQLException | UnknownHostException e) {
            logger.error("Failed deleting computer ID: " + id);
            e.printStackTrace();
        }

        logger.error("Computer with ID " + id + " not found.");
        return null;
    }
}
