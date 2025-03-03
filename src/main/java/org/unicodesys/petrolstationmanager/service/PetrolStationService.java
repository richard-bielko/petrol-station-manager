package org.unicodesys.petrolstationmanager.service;

import org.unicodesys.petrolstationmanager.database.PetrolStationDao;
import org.unicodesys.petrolstationmanager.model.PetrolStation;
import org.unicodesys.petrolstationmanager.utils.Logger;

import java.util.List;

public class PetrolStationService {

    private final PetrolStationDao petrolStationDao;
    private final Logger logger;

    public PetrolStationService() {
        this.petrolStationDao = new PetrolStationDao();
        this.logger = new Logger();
    }

    public PetrolStation createPetrolStation(PetrolStation station) {
        if (station == null) {
            throw new IllegalArgumentException("Petrol station cannot be null.");
        }
        return petrolStationDao.create(station);
    }

    public PetrolStation getPetrolStationById(Integer id) {
        PetrolStation station = petrolStationDao.getById(id);
        if (station == null) {
            throw new IllegalArgumentException("Petrol station with ID " + id + " not found.");
        }
        return station;
    }

    public List<PetrolStation> listAllPetrolStations() {
        return petrolStationDao.listAll();
    }

    public PetrolStation updatePetrolStation(PetrolStation station) {
        if (station == null) {
            throw new IllegalArgumentException("Petrol station cannot be null.");
        }

        PetrolStation existingStation = petrolStationDao.getById(station.getId());
        if (existingStation == null) {
            throw new IllegalArgumentException("Cannot update. Petrol station with ID " + station.getId() + " not found.");
        }

        if (existingStation.equals(station)) {
            logger.warning("No changes detected for petrol station ID: " + station.getId());
            return station;
        }

        return petrolStationDao.update(station);
    }

    public PetrolStation deletePetrolStation(Integer id) {
        PetrolStation existingStation = petrolStationDao.getById(id);
        if (existingStation == null) {
            throw new IllegalArgumentException("Cannot delete. Petrol station with ID " + id + " not found.");
        }
        return petrolStationDao.delete(id);
    }
}

