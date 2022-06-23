package com.example.biznes;


import com.example.biznes.exception.CarNotFoundException;
import com.example.biznes.exception.ReservationNotFoundException;
import com.example.biznes.model.Car;
import com.example.biznes.model.Reservation;
import com.example.biznes.service.CarService;
import com.example.biznes.service.ReservationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BiznesApplication.class)
@ActiveProfiles("test")
@Transactional
public class ReservationTests {
    @Autowired
    ReservationService reservationService;
    @Autowired
    CarService carService;

    @Before
    public void setup(){
        for(Reservation reservation: reservationService.findAll()){
            reservationService.delete(reservation);
        }
    }
    @Test
    public void findAllTest(){
        Reservation reservation = new Reservation();
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();

        reservationService.save(reservation);
        reservationService.save(reservation1);
        reservationService.save(reservation2);

        assertEquals(3, reservationService.findAll().size());
    }
    @Test
    public void findByIdTest() throws ReservationNotFoundException {
        Reservation reservation = new Reservation();
        Car car = new Car("Ford", "Focus", 12000);
        carService.saveCar(car);
        reservation.setCar(car);
        reservationService.save(reservation);

        assertEquals(reservation, reservationService.findById(reservation.getId()));
    }
    @Test
    public void saveTest(){
        Reservation reservation = new Reservation();
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();

        reservationService.save(reservation);
        reservationService.save(reservation1);
        reservationService.save(reservation2);

        List<Reservation> reservationList = reservationService.findAll();

        assertEquals(3, reservationList.size());
    }
    @Test
    public void deleteTest(){
       Reservation reservation = new Reservation();

       reservationService.save(reservation);

       assertEquals(1, reservationService.findAll().size());
       reservationService.delete(reservation);
       assertEquals(0, reservationService.findAll().size());
    }
    @Test(expected = ReservationNotFoundException.class)
    public void notFoundTest() throws ReservationNotFoundException{
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        reservationService.save(reservation);
        reservationService.findById(1000L);
    }
}
