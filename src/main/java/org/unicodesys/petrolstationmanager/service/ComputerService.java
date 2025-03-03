package org.unicodesys.petrolstationmanager.service;

import org.unicodesys.petrolstationmanager.database.ComputerDao;
import org.unicodesys.petrolstationmanager.model.Computer;
import org.unicodesys.petrolstationmanager.model.PetrolStation;
import org.unicodesys.petrolstationmanager.utils.Logger;

import java.util.List;

public class ComputerService {
    private final PetrolStationService petrolStationService;
    private final ComputerDao computerDao;

    public ComputerService() {
        this.petrolStationService = new PetrolStationService();
        this.computerDao = new ComputerDao();
    }

    public Computer createComputer(Computer computer) {
        if (computer == null) {
            throw new IllegalArgumentException("Computer cannot be null.");
        }

        PetrolStation station = petrolStationService.getPetrolStationById(computer.getPetrolStationId());
        if (station == null) {
            throw new IllegalArgumentException("Petrol station with ID " + computer.getPetrolStationId() + " does not exist.");
        }

        return computerDao.create(computer);
    }

    public Computer getComputerById(Integer id) {
        Computer computer = computerDao.getById(id);
        if (computer == null) {
            throw new IllegalArgumentException("Computer with ID " + id + " not found.");
        }
        return computer;
    }

    public List<Computer> listAllComputers() {
        return computerDao.listAll();
    }

    public List<Computer> listComputersForPetrolStation(Integer petrolStationId) {
        PetrolStation station = petrolStationService.getPetrolStationById(petrolStationId);
        if (station == null) {
            throw new IllegalArgumentException("Petrol station with ID " + petrolStationId + " does not exist.");
        }
        return computerDao.listByPetrolStationId(petrolStationId);
    }

    public Computer updateComputer(Computer computer) {
        if (computer == null) {
            throw new IllegalArgumentException("Computer cannot be null.");
        }

        Computer existingComputer = computerDao.getById(computer.getId());
        if (existingComputer == null) {
            throw new IllegalArgumentException("Cannot update. Computer with ID " + computer.getId() + " not found.");
        }

        PetrolStation station = petrolStationService.getPetrolStationById(computer.getPetrolStationId());
        if (station == null) {
            throw new IllegalArgumentException("Petrol station with ID " + computer.getPetrolStationId() + " does not exist.");
        }

        if (existingComputer.equals(computer)) {
            System.out.println("⚠️ No changes detected for computer ID: " + computer.getId());
            return computer;
        }

        return computerDao.update(computer);
    }

    public Computer deleteComputer(Integer id) {
        Computer existingComputer = computerDao.getById(id);
        if (existingComputer == null) {
            throw new IllegalArgumentException("Cannot delete. Computer with ID " + id + " not found.");
        }
        return computerDao.delete(id);
    }
}

