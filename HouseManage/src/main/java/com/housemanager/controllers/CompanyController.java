package com.housemanager.controllers;

import com.housemanager.models.Company;
import com.housemanager.repositories.CompanyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/")
    public String showHome(Model model) {
        model.addAttribute("companies", companyRepository.findAll());
        return "index";
    }

    @PostMapping("/add-company")
    public String addCompany(@RequestParam String name, @RequestParam String address) {
        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        companyRepository.save(company);
        return "redirect:/";
    }
}