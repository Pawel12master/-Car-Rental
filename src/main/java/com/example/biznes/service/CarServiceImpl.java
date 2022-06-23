package com.example.biznes.service;

import com.example.biznes.exception.CarIsUsedByAnotherEntity;
import com.example.biznes.exception.CarNotFoundException;
import com.example.biznes.model.Car;
import com.example.biznes.model.Reservation;
import com.example.biznes.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {


    @Autowired
    private CarRepository carRepository;

    private RentService rentService;
    private ReservationService reservationService;

    public CarServiceImpl(CarRepository carRepository, RentService rentService, ReservationService reservationService) {
        this.carRepository = carRepository;
        this.rentService = rentService;
        this.reservationService = reservationService;
    }

    @Override
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @Override
    public void saveCar(Car car) {
        this.carRepository.save(car);
    }

    @Override
    public void updateCar(Long id, Car car) throws CarNotFoundException {
        Car newCar = findById(id);
        newCar.setId(car.getId());
        this.carRepository.save(car);
    }

    @Override
    public void deleteCar(Car car) throws CarIsUsedByAnotherEntity {
        if(rentService.findAll().stream().anyMatch(rent -> rent.getCar().equals(car))
                || reservationService.findAll().stream().anyMatch(reservation -> reservation.getCar().equals(car))){
            throw new CarIsUsedByAnotherEntity();
        }

        this.carRepository.delete(car);
    }

    @Override
    public Car findById(Long id) throws CarNotFoundException {
        return this.carRepository.findById(id).orElseThrow(CarNotFoundException::new);
    }
}
