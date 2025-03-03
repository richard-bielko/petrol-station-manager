package org.unicodesys.petrolstationmanager.model;

public class PetrolStation {
    private Integer id;
    private String number;
    private String name;
    private String address;
    private String country;

    public PetrolStation(int id, String number, String name, String address, String country) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.address = address;
        this.country = country;
    }

    public PetrolStation(String number, String name, String address, String country) {
        this.number = number;
        this.name = name;
        this.address = address;
        this.country = country;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    @Override
    public String toString() {
        return "PetrolStation{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
