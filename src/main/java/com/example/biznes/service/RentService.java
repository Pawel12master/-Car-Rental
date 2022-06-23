package com.example.biznes.service;

import com.example.biznes.exception.RentNotFoundException;
import com.example.biznes.exception.ReservationNotFoundException;
import com.example.biznes.model.Rent;
import com.example.biznes.model.Reservation;

import java.util.List;

public interface RentService {
    public List<Rent> findAll();

    public Rent findById(Long id) throws RentNotFoundException;

    public void save(Rent rent);

    public void update(Long id, Rent rent) throws RentNotFoundException;

    public void delete(Rent rent);

    public List<Rent> findByEmail(String email);
}
