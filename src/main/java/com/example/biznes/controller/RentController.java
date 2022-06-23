package com.example.biznes.controller;

import com.example.biznes.exception.CarNotFoundException;
import com.example.biznes.exception.RentNotFoundException;
import com.example.biznes.exception.ReservationNotFoundException;
import com.example.biznes.model.CarStatus;
import com.example.biznes.model.Rent;
import com.example.biznes.model.Reservation;
import com.example.biznes.service.CarService;
import com.example.biznes.service.EmailService;
import com.example.biznes.service.RentService;
import com.example.biznes.service.ReservationService;
import com.example.biznes.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@Controller
@RequestMapping("/rents")
public class RentController {
    private static final Logger logger = LogManager.getLogger(ReservationController.class);

    private RentService rentService;
    private ReservationService reservationService;
    private UserService userService;
    private CarService carService;
    private EmailService emailService;

    public RentController(RentService rentService, ReservationService reservationService, UserService userService, CarService carService, EmailService emailService) {
        this.rentService = rentService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.carService = carService;
        this.emailService = emailService;
    }

    @RequestMapping("")
    public String getRents(Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Rent> rents = rentService.findByEmail(userDetails.getUsername());
        model.addAttribute("rents", rents);
        return "rent/user-rents";
    }

    @GetMapping( "/{id}/addRent")
    public String makeRent(@PathVariable Long id, Model model) throws ReservationNotFoundException, CarNotFoundException {
        Reservation reservation = reservationService.findById(id);
        Rent rent = new Rent();
        rent.setCar(reservation.getCar());
        rent.setUser(reservation.getUser());
        model.addAttribute("rent", rent);
        reservation.getCar().setCarStatus(CarStatus.RENTED);
        carService.updateCar(reservation.getCar().getId(), reservation.getCar());
        reservationService.delete(reservation);
        return "rent/add-rent";

    }
    @RequestMapping("/create")
    public String addRent(@ModelAttribute("rent") Rent rent, BindingResult result) throws InterruptedException {

        rent.setStartDate(OffsetDateTime.now());
        rent.setActive(true);
        rentService.save(rent);

        emailService.sendSimpleMessage(rent.getUser().getEmail(),"Potwierdzenie wypożyczenia"
                ,"Samochód " + rent.getCar().getName() + " został wypożyczony." );

        logger.debug("Dodano wypożyczenie: Użytkownik: " + rent.getUser().getEmail() + ", Samochód: " + rent.getCar().getName() + " " + rent.getCar().getModel());
        return "redirect:/rents";
    }
    @RequestMapping("/{id}/end")
    public String endRent(@PathVariable Long id) throws RentNotFoundException, CarNotFoundException, InterruptedException {
        Rent rent = rentService.findById(id);
        rent.setEndDate(OffsetDateTime.now());
        long days = Duration.between(rent.getStartDate(), rent.getEndDate()).toSeconds();
        rent.setTotalPrice(rent.getCar().getPrice() + rent.getCar().getPrice() * days);
        rent.setActive(false);
        rent.getCar().setCarStatus(CarStatus.AVAILABLE);
        carService.updateCar(rent.getCar().getId(), rent.getCar());
        rentService.update(id, rent);

        emailService.sendSimpleMessage(rent.getUser().getEmail(),"Zakończenie wypożyczenia"
                ,"Samochód " + rent.getCar().getName() + " został zwrócony. Zapłacono: " + rent.getTotalPrice() + " zł" );

        logger.debug("Zakończono wypożyczenie: Użytkownik: " + rent.getUser().getEmail() + ", Samochód: " + rent.getCar().getName() + " " + rent.getCar().getModel());
        return "redirect:/rents";
    }
}
