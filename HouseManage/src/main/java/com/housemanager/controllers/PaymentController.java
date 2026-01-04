package com.housemanager.controllers;

import com.housemanager.models.Apartment;
import com.housemanager.models.Payment;
import com.housemanager.repositories.ApartmentRepository;
import com.housemanager.repositories.PaymentRepository;
import com.housemanager.services.CalculationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PaymentController {

    private final ApartmentRepository apartmentRepository;
    private final PaymentRepository paymentRepository;
    private final CalculationService calculationService;

    public PaymentController(ApartmentRepository apartmentRepository,
                             PaymentRepository paymentRepository,
                             CalculationService calculationService) {
        this.apartmentRepository = apartmentRepository;
        this.paymentRepository = paymentRepository;
        this.calculationService = calculationService;
    }

    @GetMapping("/payments")
    public String showPayments(Model model) {
        var apartments = apartmentRepository.findAll();

        Map<Integer, BigDecimal> monthlyFees = new HashMap<>();
        Map<Integer, Boolean> paidStatus = new HashMap<>();

        int currentYear = LocalDate.now().getYear();
        int currentMonth = LocalDate.now().getMonthValue();

        for (Apartment apt : apartments) {
            // Изчисляваме таксата
            monthlyFees.put(apt.getId(), calculationService.calculateMonthlyFee(apt));

            // Проверяваме дали вече е платено за този месец
            boolean isPaid = paymentRepository.existsByApartmentAndYearAndMonth(apt, currentYear, currentMonth);
            paidStatus.put(apt.getId(), isPaid);
        }

        model.addAttribute("apartments", apartments);
        model.addAttribute("fees", monthlyFees);
        model.addAttribute("paidStatus", paidStatus);
        model.addAttribute("paymentHistory", paymentRepository.findAll());
        return "payments";
    }

    @PostMapping("/pay")
    public String makePayment(@RequestParam int apartmentId, @RequestParam BigDecimal amount) {
        Apartment apartment = apartmentRepository.findById(apartmentId).orElse(null);

        if (apartment != null) {
            int currentYear = LocalDate.now().getYear();
            int currentMonth = LocalDate.now().getMonthValue();

            // Двойна проверка (за сигурност), да не би някой да е платил преди секунда
            if (paymentRepository.existsByApartmentAndYearAndMonth(apartment, currentYear, currentMonth)) {
                return "redirect:/payments";
            }

            Payment payment = new Payment();
            payment.setApartment(apartment);
            payment.setAmount(amount);
            payment.setYear(currentYear);
            payment.setMonth(currentMonth);

            paymentRepository.save(payment);

            logPaymentToFile(payment);
        }
        return "redirect:/payments";
    }

    private void logPaymentToFile(Payment payment) {
        try (FileWriter fw = new FileWriter("payments_log.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {

            String companyName = (payment.getApartment().getBuilding().getEmployee() != null &&
                    payment.getApartment().getBuilding().getEmployee().getCompany() != null)
                    ? payment.getApartment().getBuilding().getEmployee().getCompany().getName()
                    : "N/A";

            String employeeName = (payment.getApartment().getBuilding().getEmployee() != null)
                    ? payment.getApartment().getBuilding().getEmployee().getFirstName() + " " +
                    payment.getApartment().getBuilding().getEmployee().getLastName()
                    : "N/A";

            String logEntry = String.format("DATE: %s | COMPANY: %s | EMP: %s | BLDG: %s | APT: %d | AMOUNT: %.2f",
                    payment.getPaymentDate(),
                    companyName,
                    employeeName,
                    payment.getApartment().getBuilding().getAddress(),
                    payment.getApartment().getNumber(),
                    payment.getAmount());

            out.println(logEntry);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}