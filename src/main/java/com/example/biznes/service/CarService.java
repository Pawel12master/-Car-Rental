package com.example.biznes.service;

import com.example.biznes.exception.CarIsUsedByAnotherEntity;
import com.example.biznes.exception.CarNotFoundException;
import com.example.biznes.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarService {
    List<Car> getAllCars();
    void saveCar(Car car);
    void updateCar(Long id, Car car) throws CarNotFoundException;
    void deleteCar(Car car) throws CarIsUsedByAnotherEntity;
    Car findById(Long id) throws CarNotFoundException;

}
