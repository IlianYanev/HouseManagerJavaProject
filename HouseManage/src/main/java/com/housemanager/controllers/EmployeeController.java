package com.housemanager.controllers;

import com.housemanager.models.Building;
import com.housemanager.models.Company;
import com.housemanager.models.Employee;
import com.housemanager.repositories.BuildingRepository;
import com.housemanager.repositories.CompanyRepository;
import com.housemanager.repositories.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final BuildingRepository buildingRepository;

    public EmployeeController(EmployeeRepository employeeRepository,
                              CompanyRepository companyRepository,
                              BuildingRepository buildingRepository) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.buildingRepository = buildingRepository;
    }

    @GetMapping("/employees")
    public String showEmployees(Model model, @RequestParam(required = false) String sortBy) {
        List<Employee> employees = employeeRepository.findAll();

        if ("name".equals(sortBy)) {
            employees.sort(Comparator.comparing(Employee::getFirstName, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Employee::getLastName, String.CASE_INSENSITIVE_ORDER));
        } else if ("buildings".equals(sortBy)) {
            employees.sort((e1, e2) -> {
                int s1 = (e1.getBuildings() != null) ? e1.getBuildings().size() : 0;
                int s2 = (e2.getBuildings() != null) ? e2.getBuildings().size() : 0;
                return Integer.compare(s2, s1);
            });
        }

        model.addAttribute("employees", employees);
        model.addAttribute("companies", companyRepository.findAll());
        return "employees";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@RequestParam String firstName,
                              @RequestParam String lastName,
                              @RequestParam(required = false) Integer companyId) {

        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);

        if (companyId != null) {
            Company company = companyRepository.findById(companyId).orElse(null);
            employee.setCompany(company);
        }

        employeeRepository.save(employee);
        return "redirect:/employees";
    }

    @PostMapping("/edit-employee")
    public String editEmployee(@RequestParam int id,
                               @RequestParam String firstName,
                               @RequestParam String lastName,
                               @RequestParam(required = false) Integer companyId) {

        Employee employee = employeeRepository.findById(id).orElse(null);
        if (employee != null) {
            employee.setFirstName(firstName);
            employee.setLastName(lastName);

            if (companyId != null) {
                Company company = companyRepository.findById(companyId).orElse(null);
                employee.setCompany(company);
            } else {
                employee.setCompany(null);
            }

            employeeRepository.save(employee);
        }
        return "redirect:/employees";
    }

    @PostMapping("/delete-employee")
    public String deleteEmployee(@RequestParam int id) {
        Employee employee = employeeRepository.findById(id).orElse(null);

        if (employee != null) {
            List<Building> buildings = employee.getBuildings();
            if (buildings != null) {
                for (Building b : buildings) {
                    b.setEmployee(null);
                    buildingRepository.save(b);
                }
            }

            employeeRepository.deleteById(id);
        }
        return "redirect:/employees";
    }
}