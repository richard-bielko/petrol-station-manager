package org.unicodesys.petrolstationmanager.enums;

public enum ComputerType {
    BOS("BOS", "Back Office Server"),
    POS("POS", "Point of Sale"),
    FCS("FCS", "Forecourt Server");

    private final String value;
    private final String description;

    ComputerType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return value + " - " + description;
    }
}