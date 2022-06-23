package com.example.biznes.service;

import com.example.biznes.exception.RentNotFoundException;
import com.example.biznes.model.Rent;
import com.example.biznes.model.Reservation;
import com.example.biznes.repository.RentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RentServiceImpl implements RentService{

    @Autowired
    private final RentRepository rentRepository;

    public RentServiceImpl(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }

    @Override
    public List<Rent> findAll() {
        return rentRepository.findAll();
    }

    @Override
    public Rent findById(Long id) throws RentNotFoundException {
        return rentRepository.findById(id).orElseThrow(RentNotFoundException::new);
    }

    @Override
    public void save(Rent rent) {
        rentRepository.save(rent);
    }

    @Override
    public void update(Long id, Rent rent) throws RentNotFoundException {
        Rent newRent = findById(id);
        newRent.setId(rent.getId());
        rentRepository.save(rent);
    }

    @Override
    public void delete(Rent rent) {
        rentRepository.delete(rent);
    }

    @Override
    public List<Rent> findByEmail(String email) {
        List<Rent> activeRents = findAll().stream().filter(rent -> rent.getUser().getEmail().equals(email) && rent.isActive()).collect(Collectors.toList());
        List<Rent> endedRents =  findAll().stream().filter(rent -> rent.getUser().getEmail().equals(email) && !rent.isActive()).collect(Collectors.toList());
        return Stream.concat(activeRents.stream(), endedRents.stream()).collect(Collectors.toList());
    }
}
