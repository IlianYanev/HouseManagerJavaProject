package com.housemanager.controllers;

import com.housemanager.models.*;
import com.housemanager.repositories.CompanyRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; // Важно
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CompanyController {

    private final CompanyRepository companyRepository;

    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @GetMapping("/")
    public String showHome(Model model, @RequestParam(required = false) String sortBy) {
        List<Company> companies = companyRepository.findAll();
        Map<Integer, BigDecimal> revenueMap = new HashMap<>();

        for (Company company : companies) {
            revenueMap.put(company.getId(), calculateRevenue(company));
        }

        if ("revenue".equals(sortBy)) {
            companies.sort((c1, c2) -> {
                BigDecimal rev1 = revenueMap.get(c1.getId());
                BigDecimal rev2 = revenueMap.get(c2.getId());
                return rev2.compareTo(rev1);
            });
        }

        model.addAttribute("companies", companies);
        model.addAttribute("revenueMap", revenueMap);
        return "index";
    }

    private BigDecimal calculateRevenue(Company company) {
        BigDecimal total = BigDecimal.ZERO;

        if (company.getEmployees() != null) {
            for (Employee emp : company.getEmployees()) {
                if (emp.getBuildings() != null) {
                    for (Building b : emp.getBuildings()) {
                        if (b.getApartments() != null) {
                            for (Apartment apt : b.getApartments()) {
                                if (apt.getPayments() != null) {
                                    for (Payment p : apt.getPayments()) {
                                        if (p.getAmount() != null) {
                                            total = total.add(p.getAmount());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return total;
    }

    @PostMapping("/add-company")
    public String addCompany(@RequestParam String name, @RequestParam String address) {
        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        companyRepository.save(company);
        return "redirect:/";
    }

    @PostMapping("/delete-company")
    public String deleteCompany(@RequestParam int id) {
        companyRepository.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/edit-company")
    public String editCompany(@RequestParam int id, @RequestParam String name, @RequestParam String address) {
        Company company = companyRepository.findById(id).orElse(null);
        if (company != null) {
            company.setName(name);
            company.setAddress(address);
            companyRepository.save(company);
        }
        return "redirect:/";
    }
}