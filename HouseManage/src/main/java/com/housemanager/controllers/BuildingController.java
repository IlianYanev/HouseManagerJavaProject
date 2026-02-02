package com.housemanager.controllers;

import com.housemanager.models.Building;
import com.housemanager.models.Employee;
import com.housemanager.repositories.BuildingRepository;
import com.housemanager.repositories.EmployeeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

@Controller
public class BuildingController {

    private final BuildingRepository buildingRepository;
    private final EmployeeRepository employeeRepository;

    public BuildingController(BuildingRepository buildingRepository, EmployeeRepository employeeRepository) {
        this.buildingRepository = buildingRepository;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/buildings")
    public String showBuildings(Model model) {
        model.addAttribute("buildings", buildingRepository.findAll());
        model.addAttribute("employees", employeeRepository.findAll()); // Нужно за падащото меню при редакция
        return "buildings";
    }

    @PostMapping("/add-building")
    public String addBuilding(@RequestParam String address,
                              @RequestParam int floors,
                              @RequestParam int totalApartments,
                              @RequestParam BigDecimal area,
                              @RequestParam BigDecimal commonPartsArea,
                              @RequestParam BigDecimal feePerSqm,
                              @RequestParam BigDecimal elevatorFee,
                              @RequestParam BigDecimal petFee,
                              @RequestParam(required = false) Integer employeeId) {

        Building building = new Building();
        updateBuildingData(building, address, floors, totalApartments, area, commonPartsArea, feePerSqm, elevatorFee, petFee, employeeId);

        if (employeeId == null && building.getEmployee() == null) {
            List<Employee> allEmployees = employeeRepository.findAll();
            if (!allEmployees.isEmpty()) {
                Employee bestCandidate = allEmployees.stream()
                        .min(Comparator.comparingInt(e -> e.getBuildings().size()))
                        .orElse(allEmployees.get(0));
                building.setEmployee(bestCandidate);
            }
        }

        buildingRepository.save(building);
        return "redirect:/buildings";
    }

    @PostMapping("/delete-building")
    public String deleteBuilding(@RequestParam int id) {
        buildingRepository.deleteById(id);
        return "redirect:/buildings";
    }

    @PostMapping("/edit-building")
    public String editBuilding(@RequestParam int id,
                               @RequestParam String address,
                               @RequestParam int floors,
                               @RequestParam int totalApartments,
                               @RequestParam BigDecimal area,
                               @RequestParam BigDecimal commonPartsArea,
                               @RequestParam BigDecimal feePerSqm,
                               @RequestParam BigDecimal elevatorFee,
                               @RequestParam BigDecimal petFee,
                               @RequestParam(required = false) Integer employeeId) {

        Building building = buildingRepository.findById(id).orElse(null);
        if (building != null) {
            updateBuildingData(building, address, floors, totalApartments, area, commonPartsArea, feePerSqm, elevatorFee, petFee, employeeId);
            buildingRepository.save(building);
        }
        return "redirect:/buildings";
    }

    private void updateBuildingData(Building building, String address, int floors, int totalApartments,
                                    BigDecimal area, BigDecimal commonPartsArea, BigDecimal feePerSqm,
                                    BigDecimal elevatorFee, BigDecimal petFee, Integer employeeId) {
        building.setAddress(address);
        building.setFloors(floors);
        building.setTotalApartments(totalApartments);
        building.setArea(area);
        building.setCommonPartsArea(commonPartsArea);
        building.setFeePerSqm(feePerSqm);
        building.setElevatorFee(elevatorFee);
        building.setPetFee(petFee);

        if (employeeId != null) {
            Employee employee = employeeRepository.findById(employeeId).orElse(null);
            building.setEmployee(employee);
        }
    }
}