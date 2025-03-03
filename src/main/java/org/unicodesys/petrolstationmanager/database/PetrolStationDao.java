package org.unicodesys.petrolstationmanager.database;

import org.unicodesys.petrolstationmanager.model.PetrolStation;
import org.unicodesys.petrolstationmanager.utils.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetrolStationDao {

    private final DatabaseConnection databaseConnection;
    private final Logger logger;

    public PetrolStationDao() {
        this.databaseConnection = new DatabaseConnection();
        this.logger = new Logger();
    }

    public PetrolStation create(PetrolStation station) {
        String sql = "INSERT INTO petrol_station (number, name, address, country) VALUES (?, ?, ?, ?)";
        String generatedColumns[] = {"id"};

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql, generatedColumns)) {

            stmt.setString(1, station.getNumber());
            stmt.setString(2, station.getName());
            stmt.setString(3, station.getAddress());
            stmt.setString(4, station.getCountry());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        station.setId(generatedKeys.getInt(1));
                        logger.success("Petrol station created successfully!");
                        return station;
                    } else {
                        logger.error("Failed to retrieve generated ID.");
                    }
                }
            }
        } catch (SQLException e) {
            logger.error("Failed creation of petrol station failed");
            e.printStackTrace();
        }
        return null;
    }

    public PetrolStation getById(Integer id) {
        PetrolStation station = null;
        String sql = "SELECT * FROM petrol_station WHERE id=?";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    station = new PetrolStation(
                            rs.getInt("id"),
                            rs.getString("number"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("country")
                    );
                }
            }

        } catch (SQLException e) {
            logger.error("Failed retrieving petrol station id: " + id);
            e.printStackTrace();
        }
        return station;
    }

    public List<PetrolStation> listAll() {
        List<PetrolStation> stations = new ArrayList<>();
        String sql = "SELECT * FROM petrol_station";

        try (Connection conn = databaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PetrolStation station = new PetrolStation(
                        rs.getInt("id"),
                        rs.getString("number"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("country")
                );
                stations.add(station);
            }

        } catch (SQLException e) {
            logger.error("Failed retrieving petrol stations");
            e.printStackTrace();
        }
        return stations;
    }

    public PetrolStation update(PetrolStation station) {
        String sql = "UPDATE petrol_station SET number=?, name=?, address=?, country=? WHERE id=?";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, station.getNumber());
            stmt.setString(2, station.getName());
            stmt.setString(3, station.getAddress());
            stmt.setString(4, station.getCountry());
            stmt.setInt(5, station.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                logger.success("Petrol station updated successfully!");
                return station;
            } else {
                logger.warning("No changes made to petrol station ID: " + station.getId());
                return station;
            }

        } catch (SQLException e) {
            logger.error("Error updating petrol station id: " + station.getId());
            e.printStackTrace();
        }

        return null;
    }


    public PetrolStation delete(Integer id) {
        String sql = "DELETE FROM petrol_station WHERE id=? RETURNING id, number, name, address, country";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    PetrolStation deletedStation = new PetrolStation(
                            rs.getInt("id"),
                            rs.getString("number"),
                            rs.getString("name"),
                            rs.getString("address"),
                            rs.getString("country")
                    );
                    logger.success("Petrol station deleted successfully!");
                    return deletedStation;
                }
            }

        } catch (SQLException e) {
            logger.error("Failed deleting petrol station id: " + id);
            e.printStackTrace();
        }

        logger.error("Petrol station with ID " + id + " not found.");
        return null;
    }
}
