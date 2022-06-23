package com.example.biznes;

import com.example.biznes.model.Car;
import com.example.biznes.repository.CarRepository;
import com.example.biznes.service.CarService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class BiznesApplicationTests {

    @Autowired
    CarService carService;


    @Test
    public void test_findAll_success() {
        Car car = new Car("Tesla","Cos",12);
        carService.saveCar(car);
        List<Car> foundCars = carService.getAllCars();
        assertEquals(1, foundCars.size());
        assertEquals("Tesla", foundCars.get(0).getName());


    }

}
