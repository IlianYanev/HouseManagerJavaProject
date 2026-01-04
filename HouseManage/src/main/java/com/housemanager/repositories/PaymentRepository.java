package com.housemanager.repositories;

import com.housemanager.models.Apartment;
import com.housemanager.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    boolean existsByApartmentAndYearAndMonth(Apartment apartment, int year, int month);
}