package com.housemanager.controllers;

import com.housemanager.models.*;
import com.housemanager.repositories.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ReportController {

    private final CompanyRepository companyRepository;
    private final BuildingRepository buildingRepository;
    private final EmployeeRepository employeeRepository;

    public ReportController(CompanyRepository companyRepository,
                            BuildingRepository buildingRepository,
                            EmployeeRepository employeeRepository) {
        this.companyRepository = companyRepository;
        this.buildingRepository = buildingRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/reports")
    public String showReports(Model model) {
        List<Company> companies = companyRepository.findAll();
        List<Employee> employees = employeeRepository.findAll();
        List<Building> buildings = buildingRepository.findAll();

        Map<Integer, BigDecimal> companyDue = new HashMap<>();
        Map<Integer, BigDecimal> companyPaid = new HashMap<>();

        Map<Integer, BigDecimal> employeeDue = new HashMap<>();
        Map<Integer, BigDecimal> employeePaid = new HashMap<>();

        Map<Integer, BigDecimal> buildingDue = new HashMap<>();
        Map<Integer, BigDecimal> buildingPaid = new HashMap<>();

        for (Company c : companies) {
            companyDue.put(c.getId(), BigDecimal.ZERO);
            companyPaid.put(c.getId(), BigDecimal.ZERO);
        }
        for (Employee e : employees) {
            employeeDue.put(e.getId(), BigDecimal.ZERO);
            employeePaid.put(e.getId(), BigDecimal.ZERO);
        }
        for (Building b : buildings) {
            buildingDue.put(b.getId(), BigDecimal.ZERO);
            buildingPaid.put(b.getId(), BigDecimal.ZERO);
        }

        for (Building b : buildings) {
            BigDecimal bDue = BigDecimal.ZERO;
            BigDecimal bPaid = BigDecimal.ZERO;

            if (b.getApartments() != null) {
                for (Apartment apt : b.getApartments()) {
                    BigDecimal aptFee = apt.getArea().multiply(b.getFeePerSqm());

                    if (apt.getResidents() != null) {
                        long elevatorUsers = apt.getResidents().stream().filter(Resident::isUsesElevator).count();
                        aptFee = aptFee.add(b.getElevatorFee().multiply(BigDecimal.valueOf(elevatorUsers)));
                    }

                    if (apt.getPets() != null) {
                        aptFee = aptFee.add(b.getPetFee().multiply(BigDecimal.valueOf(apt.getPets().size())));
                    }

                    bDue = bDue.add(aptFee);

                    if (apt.getPayments() != null) {
                        for (Payment p : apt.getPayments()) {
                            if (p.getAmount() != null) {
                                bPaid = bPaid.add(p.getAmount());
                            }
                        }
                    }
                }
            }

            buildingDue.put(b.getId(), bDue);
            buildingPaid.put(b.getId(), bPaid);

            if (b.getEmployee() != null) {
                int eId = b.getEmployee().getId();
                employeeDue.put(eId, employeeDue.getOrDefault(eId, BigDecimal.ZERO).add(bDue));
                employeePaid.put(eId, employeePaid.getOrDefault(eId, BigDecimal.ZERO).add(bPaid));

                if (b.getEmployee().getCompany() != null) {
                    int cId = b.getEmployee().getCompany().getId();
                    companyDue.put(cId, companyDue.getOrDefault(cId, BigDecimal.ZERO).add(bDue));
                    companyPaid.put(cId, companyPaid.getOrDefault(cId, BigDecimal.ZERO).add(bPaid));
                }
            }
        }

        model.addAttribute("companies", companies);
        model.addAttribute("employees", employees);
        model.addAttribute("buildings", buildings);

        model.addAttribute("companyDue", companyDue);
        model.addAttribute("companyPaid", companyPaid);
        model.addAttribute("employeeDue", employeeDue);
        model.addAttribute("employeePaid", employeePaid);
        model.addAttribute("buildingDue", buildingDue);
        model.addAttribute("buildingPaid", buildingPaid);

        return "reports";
    }
}