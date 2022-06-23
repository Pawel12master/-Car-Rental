package com.example.biznes.service;

import com.example.biznes.exception.ReservationNotFoundException;
import com.example.biznes.model.Reservation;

import java.util.List;
import java.util.stream.Collectors;

public interface ReservationService {
    public List<Reservation> findAll();

    public Reservation findById(Long id) throws ReservationNotFoundException;

    public void save(Reservation reservation);

    public void update(Long id, Reservation reservation) throws ReservationNotFoundException;

    public void delete(Reservation reservation);

    public List<Reservation> findByEmail(String email);
}
