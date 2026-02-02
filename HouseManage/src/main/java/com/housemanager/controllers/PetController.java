package com.housemanager.controllers;

import com.housemanager.models.Apartment;
import com.housemanager.models.Pet;
import com.housemanager.repositories.ApartmentRepository;
import com.housemanager.repositories.PetRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PetController {

    private final PetRepository petRepository;
    private final ApartmentRepository apartmentRepository;

    public PetController(PetRepository petRepository, ApartmentRepository apartmentRepository) {
        this.petRepository = petRepository;
        this.apartmentRepository = apartmentRepository;
    }

    @GetMapping("/pets")
    public String showPets(Model model) {
        model.addAttribute("pets", petRepository.findAll());
        model.addAttribute("apartments", apartmentRepository.findAll());
        return "pets";
    }

    @PostMapping("/add-pet")
    public String addPet(@RequestParam String species,
                         @RequestParam(defaultValue = "false") boolean usesCommonParts,
                         @RequestParam int apartmentId) {

        Pet pet = new Pet();
        pet.setSpecies(species);
        pet.setUsesCommonParts(usesCommonParts);

        Apartment apartment = apartmentRepository.findById(apartmentId).orElse(null);
        if (apartment != null) {
            pet.setApartment(apartment);
        }

        petRepository.save(pet);
        return "redirect:/pets";
    }
}