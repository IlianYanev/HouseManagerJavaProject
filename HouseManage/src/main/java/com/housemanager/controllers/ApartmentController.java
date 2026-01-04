package com.housemanager.controllers;

import com.housemanager.models.Apartment;
import com.housemanager.models.Building;
import com.housemanager.repositories.ApartmentRepository;
import com.housemanager.repositories.BuildingRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class ApartmentController {

    private final ApartmentRepository apartmentRepository;
    private final BuildingRepository buildingRepository;

    public ApartmentController(ApartmentRepository apartmentRepository, BuildingRepository buildingRepository) {
        this.apartmentRepository = apartmentRepository;
        this.buildingRepository = buildingRepository;
    }

    @GetMapping("/apartments")
    public String showApartments(Model model) {
        model.addAttribute("apartments", apartmentRepository.findAll());
        model.addAttribute("buildings", buildingRepository.findAll()); // За падащото меню
        return "apartments";
    }

    @PostMapping("/add-apartment")
    public String addApartment(@RequestParam int number,
                               @RequestParam int floor,
                               @RequestParam BigDecimal area,
                               @RequestParam String ownerName,
                               @RequestParam int buildingId) {

        Apartment apartment = new Apartment();
        apartment.setNumber(number);
        apartment.setFloor(floor);
        apartment.setArea(area);
        apartment.setOwnerName(ownerName);

        Building building = buildingRepository.findById(buildingId).orElse(null);
        if (building != null) {
            apartment.setBuilding(building);
        }

        apartmentRepository.save(apartment);
        return "redirect:/apartments";
    }
}