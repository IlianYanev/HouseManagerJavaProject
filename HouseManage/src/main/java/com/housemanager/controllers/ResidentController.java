package com.housemanager.controllers;

import com.housemanager.models.Apartment;
import com.housemanager.models.Resident;
import com.housemanager.repositories.ApartmentRepository;
import com.housemanager.repositories.ResidentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResidentController {

    private final ResidentRepository residentRepository;
    private final ApartmentRepository apartmentRepository;

    public ResidentController(ResidentRepository residentRepository, ApartmentRepository apartmentRepository) {
        this.residentRepository = residentRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @GetMapping("/residents")
    public String showResidents(Model model) {
        model.addAttribute("residents", residentRepository.findAll());
        model.addAttribute("apartments", apartmentRepository.findAll());
        return "residents";
    }

    @PostMapping("/add-resident")
    public String addResident(@RequestParam String name,
                              @RequestParam int age,
                              @RequestParam(defaultValue = "false") boolean usesElevator,
                              @RequestParam int apartmentId) {

        Resident resident = new Resident();
        resident.setName(name);
        resident.setAge(age);
        resident.setUsesElevator(usesElevator);

        Apartment apartment = apartmentRepository.findById(apartmentId).orElse(null);
        if (apartment != null) {
            resident.setApartment(apartment);
        }

        residentRepository.save(resident);
        return "redirect:/residents";
    }
}