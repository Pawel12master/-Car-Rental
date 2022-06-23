package com.example.biznes;

import com.example.biznes.exception.CarIsUsedByAnotherEntity;
import com.example.biznes.exception.CarNotFoundException;
import com.example.biznes.exception.UserNotFoundException;
import com.example.biznes.model.Car;
import com.example.biznes.model.Reservation;
import com.example.biznes.service.CarService;
import com.example.biznes.service.ReservationService;
import com.example.biznes.user.User;
import com.example.biznes.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiznesApplication.class)
@ActiveProfiles("test")
@Transactional
public class CarTests {

    @Autowired
    CarService carService;
    @Autowired
    ReservationService reservationService;
    @Autowired
    UserService userService;

    @Before
    public void setup() throws CarIsUsedByAnotherEntity {
        for(Car car: carService.getAllCars()){
            carService.deleteCar(car);
        }
    }

    @Test
    public void test_findAll_success() {
        Car car = new Car("Tesla","Cos",12);
        Car car1 = new Car("Tesla","Cos",12);
        Car car2 = new Car("Tesla","Cos",12);
        Car car3 = new Car("Tesla","Cos",12);
        Car car4 = new Car("Tesla","Cos",12);
        Car car5 = new Car("Tesla","Cos",12);
        carService.saveCar(car);
        carService.saveCar(car1);
        carService.saveCar(car2);
        carService.saveCar(car3);
        carService.saveCar(car4);
        carService.saveCar(car5);
        List<Car> foundCars = carService.getAllCars();
        assertEquals(6, foundCars.size());
        assertEquals("Tesla", foundCars.get(0).getName());


    }
    @Test
    public void test_Add_cars(){
        Car car = new Car("Audi","A4",120000);
        Car car1 = new Car("BMW","M5",120500);
        Car car2 = new Car("Audi","A5",220000);
        carService.saveCar(car);
        carService.saveCar(car1);
        carService.saveCar(car2);
        List<Car> allCars = carService.getAllCars();
        assertEquals(3,allCars.size());
    }
    @Test
    public void test_Delete_cars() throws CarIsUsedByAnotherEntity {
        Car car = new Car("Audi","A4",120000);
        Car car1 = new Car("BMW","M5",120500);
        Car car2 = new Car("Audi","A5",220000);
        carService.saveCar(car);
        carService.saveCar(car1);
        carService.saveCar(car2);
        carService.deleteCar(car1);
        List<Car> allCars = carService.getAllCars();
        assertEquals(2,allCars.size());

    }
    @Test
    public void test_findById_car() throws CarNotFoundException {
        Car car = new Car("Audi","A4",120000);
        Car car1 = new Car("BMW","M5",120500);
        Car car2 = new Car("Audi","A5",220000);
        Car car3 = new Car("Tesla","A5",220000);
        Car car4 = new Car("Honda","A5",220000);
        Car car5 = new Car("Mercedes","A5",220000);
        carService.saveCar(car);
        carService.saveCar(car1);
        carService.saveCar(car2);
        carService.saveCar(car3);
        carService.saveCar(car4);
        carService.saveCar(car5);
        assertEquals(car1, carService.findById(car1.getId()));
        assertEquals(car4, carService.findById(car4.getId()));

    }
    @Test
    public void test_Update_cars() throws CarNotFoundException {
        Car car = new Car("Audi","A4",120000);
        Car car1 = new Car("BMW","M5",120500);
        Car car2 = new Car("Audi","A5",220000);
        Car car4 = new Car("Honda","A5",220000);
        Car car5 = new Car("Mercedes","A5",220000);
        carService.saveCar(car);
        carService.saveCar(car1);
        carService.saveCar(car2);
        carService.saveCar(car4);
        carService.saveCar(car5);
        Car updatedCar = carService.findById(car2.getId());
        updatedCar.setName(car5.getName());
        carService.updateCar(car2.getId(), updatedCar);
        assertEquals(carService.findById(car2.getId()).getName(),carService.findById(car5.getId()).getName());
    }
    @Test(expected = CarNotFoundException.class)
    public void carNotFoundTest() throws CarNotFoundException{
        Car car = new Car("Audi","A4",120000);
        Car car2 = new Car("Ford", "Focus", 200);
        car.setId(1L);
        car2.setId(2L);
        carService.saveCar(car);
        carService.saveCar(car2);
        Car notExistedCar = carService.findById(1000L);
    }
    @Test(expected = CarIsUsedByAnotherEntity.class)
    public void carIsUsedByEntity() throws CarIsUsedByAnotherEntity, UserNotFoundException {
        Car car = new Car("Audi","A4",120000);
        carService.saveCar(car);
        Reservation reservation = new Reservation();
        reservation.setCar(carService.getAllCars().get(0));
        reservation.setId(1L);
        User user = new User();
        userService.register(user);
        reservation.setUser(user);
        reservation.setDate(OffsetDateTime.now());
        reservation.setPayment(false);
        reservationService.save(reservation);
        carService.deleteCar(carService.getAllCars().get(0));
    }





}
