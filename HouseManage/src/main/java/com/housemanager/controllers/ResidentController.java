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

import java.util.Comparator;
import java.util.List;

@Controller
public class ResidentController {

    private final ResidentRepository residentRepository;
    private final ApartmentRepository apartmentRepository;

    public ResidentController(ResidentRepository residentRepository, ApartmentRepository apartmentRepository) {
        this.residentRepository = residentRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @GetMapping("/residents")
    public String showResidents(Model model, @RequestParam(required = false) String sortBy) {
        List<Resident> residents = residentRepository.findAll();

        if ("name".equals(sortBy)) {
            residents.sort(java.util.Comparator.comparing(Resident::getName, String.CASE_INSENSITIVE_ORDER));
        } else if ("age".equals(sortBy)) {
            residents.sort(java.util.Comparator.comparingInt(Resident::getAge));
        }

        model.addAttribute("residents", residents);
        model.addAttribute("apartments", apartmentRepository.findAll());
        return "residents";
    }

    @PostMapping("/add-resident")
    public String addResident(@RequestParam String name,
                              @RequestParam int age,
                              @RequestParam(defaultValue = "false") boolean usesElevator,
                              @RequestParam int apartmentId) {

        Apartment apartment = apartmentRepository.findById(apartmentId).orElse(null);
        if (apartment != null) {
            Resident resident = new Resident();
            resident.setName(name);
            resident.setAge(age);
            resident.setUsesElevator(usesElevator);
            resident.setApartment(apartment);
            residentRepository.save(resident);
        }
        return "redirect:/residents";
    }

    @PostMapping("/delete-resident")
    public String deleteResident(@RequestParam int id) {
        residentRepository.deleteById(id);
        return "redirect:/residents";
    }

    @PostMapping("/edit-resident")
    public String editResident(@RequestParam int id,
                               @RequestParam String name,
                               @RequestParam int age,
                               @RequestParam(defaultValue = "false") boolean usesElevator,
                               @RequestParam int apartmentId) {

        Resident resident = residentRepository.findById(id).orElse(null);
        Apartment apartment = apartmentRepository.findById(apartmentId).orElse(null);

        if (resident != null && apartment != null) {
            resident.setName(name);
            resident.setAge(age);
            resident.setUsesElevator(usesElevator);
            resident.setApartment(apartment);
            residentRepository.save(resident);
        }
        return "redirect:/residents";
    }
}