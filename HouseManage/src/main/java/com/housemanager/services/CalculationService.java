package com.housemanager.services;

import com.housemanager.models.*;
import com.housemanager.repositories.PetRepository;
import com.housemanager.repositories.ResidentRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CalculationService {

    private final ResidentRepository residentRepository;
    private final PetRepository petRepository;

    public CalculationService(ResidentRepository residentRepository, PetRepository petRepository) {
        this.residentRepository = residentRepository;
        this.petRepository = petRepository;
    }

    public BigDecimal calculateMonthlyFee(Apartment apartment) {
        Building building = apartment.getBuilding();
        if (building == null) return BigDecimal.ZERO;


        BigDecimal baseFee = apartment.getArea().multiply(building.getFeePerSqm());


        List<Resident> residents = residentRepository.findAll();
        long elevatorUsers = residents.stream()
                .filter(r -> r.getApartment().getId() == apartment.getId())
                .filter(r -> r.getAge() > 7 && r.isUsesElevator())
                .count();
        BigDecimal elevatorFee = building.getElevatorFee().multiply(BigDecimal.valueOf(elevatorUsers));


        List<Pet> pets = petRepository.findAll();
        long petUsers = pets.stream()
                .filter(p -> p.getApartment().getId() == apartment.getId())
                .filter(Pet::isUsesCommonParts)
                .count();
        BigDecimal petFee = building.getPetFee().multiply(BigDecimal.valueOf(petUsers));

        return baseFee.add(elevatorFee).add(petFee);
    }
}