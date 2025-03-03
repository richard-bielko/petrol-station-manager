package org.unicodesys.petrolstationmanager.model;

import org.unicodesys.petrolstationmanager.enums.ComputerType;

import java.net.InetAddress;

public class Computer {
    private Integer id;
    private String number;
    private InetAddress ipAddress;
    private ComputerType type;
    private Integer petrolStationId;

    public Computer(int id, String number, InetAddress ipAddress, ComputerType type, int petrolStationId) {
        this.id = id;
        this.number = number;
        this.ipAddress = ipAddress;
        this.type = type;
        this.petrolStationId = petrolStationId;
    }

    public Computer(String number, InetAddress ipAddress, ComputerType type, int petrolStationId) {
        this.number = number;
        this.ipAddress = ipAddress;
        this.type = type;
        this.petrolStationId = petrolStationId;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public InetAddress getIpAddress() { return ipAddress; }
    public void setIpAddress(InetAddress ipAddress) { this.ipAddress = ipAddress; }

    public ComputerType getType() { return type; }
    public void setType(ComputerType type) { this.type = type; }

    public Integer getPetrolStationId() { return petrolStationId; }
    public void setPetrolStationId(Integer petrolStationId) { this.petrolStationId = petrolStationId; }

    @Override
    public String toString() {
        return "Computer{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", ipAddress='" + ipAddress + '\'' +
                ", type=" + type +
                ", petrolStationId=" + petrolStationId +
                '}';
    }
}

