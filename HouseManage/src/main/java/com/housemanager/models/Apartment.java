package com.housemanager.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "apartments")
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int number;

    @Column(nullable = false)
    private int floor;

    @Column(nullable = false)
    private BigDecimal area;

    @Column(name = "owner_name")
    private String ownerName;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.REMOVE)
    private List<Resident> residents;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.REMOVE)
    private List<Pet> pets;

    @OneToMany(mappedBy = "apartment", cascade = CascadeType.REMOVE)
    private List<Payment> payments;

    public Apartment() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getNumber() { return number; }
    public void setNumber(int number) { this.number = number; }
    public int getFloor() { return floor; }
    public void setFloor(int floor) { this.floor = floor; }
    public BigDecimal getArea() { return area; }
    public void setArea(BigDecimal area) { this.area = area; }
    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public Building getBuilding() { return building; }
    public void setBuilding(Building building) { this.building = building; }
    public List<Payment> getPayments() { return payments; }
    public void setPayments(List<Payment> payments) { this.payments = payments; }
    public List<Resident> getResidents() { return residents; }
    public void setResidents(List<Resident> residents) { this.residents = residents; }
    public List<Pet> getPets() { return pets; }
    public void setPets(List<Pet> pets) { this.pets = pets; }
}