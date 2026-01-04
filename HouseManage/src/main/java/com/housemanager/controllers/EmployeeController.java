package com.housemanager.controllers;

import com.housemanager.models.Company;
import com.housemanager.models.Employee;
import com.housemanager.repositories.CompanyRepository;
import com.housemanager.repositories.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    public EmployeeController(EmployeeRepository employeeRepository, CompanyRepository companyRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    @GetMapping("/employees")
    public String showEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        model.addAttribute("companies", companyRepository.findAll()); // Трябва ни за падащото меню
        return "employees";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam int companyId) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);

        // Намираме фирмата по ID и я закачаме за служителя
        Company company = companyRepository.findById(companyId).orElse(null);
        if (company != null) {
            employee.setCompany(company);
        }

        employeeRepository.save(employee);
        return "redirect:/employees";
    }
}