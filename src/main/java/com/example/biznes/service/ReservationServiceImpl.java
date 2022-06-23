package com.example.biznes.service;

import com.example.biznes.exception.ReservationNotFoundException;
import com.example.biznes.model.Reservation;
import com.example.biznes.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public List<Reservation> findAll(){
        return reservationRepository.findAll();
    }
    @Override
    public Reservation findById(Long id) throws ReservationNotFoundException{
        return reservationRepository.findById(id).orElseThrow(ReservationNotFoundException::new);
    }
    @Override
    public void save(Reservation reservation){
        reservationRepository.save(reservation);
    }
    @Override
    public void update(Long id, Reservation reservation) throws ReservationNotFoundException{
        Reservation newReservation = findById(id);
        newReservation.setId(reservation.getId());
        reservationRepository.save(reservation);
    }
    @Override
    public void delete(Reservation reservation){
        reservationRepository.delete(reservation);
    }
    @Override
    public List<Reservation> findByEmail(String email){
        return findAll().stream().filter(element -> element.getUser().getEmail().equals(email)).collect(Collectors.toList());
    }


}
