package com.housemanager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String species;

    @Column(name = "uses_common_parts")
    private boolean usesCommonParts;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;

    public Pet() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    public boolean isUsesCommonParts() { return usesCommonParts; }
    public void setUsesCommonParts(boolean usesCommonParts) { this.usesCommonParts = usesCommonParts; }
    public Apartment getApartment() { return apartment; }
    public void setApartment(Apartment apartment) { this.apartment = apartment; }
}