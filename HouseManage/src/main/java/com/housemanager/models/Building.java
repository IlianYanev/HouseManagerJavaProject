package com.housemanager.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "buildings")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String address;

    private Integer floors;

    @Column(name = "total_apartments")
    private Integer totalApartments;

    private BigDecimal area;

    @Column(name = "common_parts_area")
    private BigDecimal commonPartsArea;

    @Column(name = "fee_per_sqm")
    private BigDecimal feePerSqm;

    @Column(name = "elevator_fee")
    private BigDecimal elevatorFee;

    @Column(name = "pet_fee")
    private BigDecimal petFee;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "building", cascade = CascadeType.REMOVE)
    private List<Apartment> apartments;

    public Building() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public Integer getFloors() { return floors; }
    public void setFloors(Integer floors) { this.floors = floors; }
    public Integer getTotalApartments() { return totalApartments; }
    public void setTotalApartments(Integer totalApartments) { this.totalApartments = totalApartments; }
    public BigDecimal getArea() { return area; }
    public void setArea(BigDecimal area) { this.area = area; }
    public BigDecimal getCommonPartsArea() { return commonPartsArea; }
    public void setCommonPartsArea(BigDecimal commonPartsArea) { this.commonPartsArea = commonPartsArea; }
    public BigDecimal getFeePerSqm() { return feePerSqm; }
    public void setFeePerSqm(BigDecimal feePerSqm) { this.feePerSqm = feePerSqm; }
    public BigDecimal getElevatorFee() { return elevatorFee; }
    public void setElevatorFee(BigDecimal elevatorFee) { this.elevatorFee = elevatorFee; }
    public BigDecimal getPetFee() { return petFee; }
    public void setPetFee(BigDecimal petFee) { this.petFee = petFee; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public List<Apartment> getApartments() { return apartments; }
    public void setApartments(List<Apartment> apartments) { this.apartments = apartments; }
}