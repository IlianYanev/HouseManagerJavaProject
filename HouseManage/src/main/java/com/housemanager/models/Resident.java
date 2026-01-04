package com.housemanager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "residents")
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int age;

    @Column(name = "uses_elevator")
    private boolean usesElevator;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    public Resident() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public boolean isUsesElevator() { return usesElevator; }
    public void setUsesElevator(boolean usesElevator) { this.usesElevator = usesElevator; }
    public Apartment getApartment() { return apartment; }
    public void setApartment(Apartment apartment) { this.apartment = apartment; }
}