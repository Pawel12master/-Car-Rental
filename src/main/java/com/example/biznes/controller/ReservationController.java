package com.example.biznes.controller;

import com.example.biznes.exception.CarNotFoundException;
import com.example.biznes.exception.ReservationWithCarExists;
import com.example.biznes.exception.UserNotFoundException;
import com.example.biznes.model.Car;
import com.example.biznes.model.CarStatus;
import com.example.biznes.model.Reservation;
import com.example.biznes.service.CarService;
import com.example.biznes.service.EmailService;
import com.example.biznes.service.ReservationService;
import com.example.biznes.service.ReservationServiceImpl;
import com.example.biznes.user.User;
import com.example.biznes.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@Controller
@RequestMapping("/reservations")
public class ReservationController {
    private static final Logger logger = LogManager.getLogger(ReservationController.class);

    private final UserService userService;
    private final CarService carService;
    private final ReservationService reservationService;
    private final EmailService emailService;

    public ReservationController(UserService userService, CarService carService, ReservationService reservationService, EmailService emailService) {
        this.userService = userService;
        this.carService = carService;
        this.reservationService = reservationService;
        this.emailService = emailService;
    }
    @Scheduled(fixedDelay = 1000000)
    public void deleteReservations() throws CarNotFoundException, InterruptedException {
        for(Reservation reservation : reservationService.findAll()){
            if(reservation.getDate().plusSeconds(10).compareTo(OffsetDateTime.now()) < 0){      // czas do usunięcia rezerwacji edytujemy za pomoca dodania wartosci do daty rezerwacji
                reservation.getCar().setCarStatus(CarStatus.AVAILABLE);
                emailService.sendSimpleMessage(reservation.getUser().getEmail(),"Anulowanie rezerwacji"
                        ,"Rezerwacja na samochód " + reservation.getCar().getName() + " została anulowana" );
                logger.debug("Usunięto rezerwację nr: " + reservation.getId() + " z powodu wygaśnięcia daty");
                carService.updateCar(reservation.getCar().getId(), reservation.getCar());
                reservationService.delete(reservation);
            }
        }
    }


    @RequestMapping("/{id}/add")
    public String makeReservation(@PathVariable Long id) throws CarNotFoundException, UserNotFoundException, ReservationWithCarExists, InterruptedException {
        Car car = carService.findById(id);
        if(reservationService.findAll().stream().anyMatch(reservation -> reservation.getCar().getId().equals(car.getId()))){
            throw new ReservationWithCarExists();
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        car.setCarStatus(CarStatus.RESERVED);
        carService.updateCar(id,car);

        Reservation reservation = new Reservation();
        reservation.setCar(car);
        User user = userService.findByEmail(userDetails.getUsername());
        reservation.setUser(user);

        reservation.setDate(OffsetDateTime.now());
        reservation.setPayment(false);
        reservationService.save(reservation);

        emailService.sendSimpleMessage(reservation.getUser().getEmail(),"Potwierdzenie rezerwacji"
                ,"Samochód " + reservation.getCar().getName() + " został zarezerwowany. W przypadku braku płatności po trzech dniach rezerwacja zostanie anulowana" );

        logger.debug("Dodano rezerwację: Użytkownik: " + user.getEmail() + " Samochód: " + car.getId());

        return "redirect:/cars";
    }
    @RequestMapping("/myReservations")
    public String userReservations(Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Reservation> reservationList = reservationService.findByEmail(userDetails.getUsername());
        model.addAttribute("reservations", reservationList);
        return "reservations/user-reservations";
    }
}
