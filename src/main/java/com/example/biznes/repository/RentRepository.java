package com.example.biznes.repository;

import com.example.biznes.model.Car;
import com.example.biznes.model.Rent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
}

