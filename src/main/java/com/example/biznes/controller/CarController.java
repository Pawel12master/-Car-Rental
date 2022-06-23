package com.example.biznes.controller;

import com.example.biznes.exception.CarIsUsedByAnotherEntity;
import com.example.biznes.exception.CarNotFoundException;
import com.example.biznes.exception.UserNotFoundException;
import com.example.biznes.model.Car;
import com.example.biznes.model.CarStatus;
import com.example.biznes.service.CarService;
import com.example.biznes.user.Role;
import com.example.biznes.user.User;
import com.example.biznes.user.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cars")
public class CarController {

    private static final Logger logger = LogManager.getLogger(CarController.class);

    private CarService carService;
    private UserService userService;

    public CarController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @GetMapping("")
    public String viewCars(Model model) throws UserNotFoundException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(userService.findByEmail(userDetails.getUsername()).getRole() == Role.USER){
            model.addAttribute("listCars", carService.getAllCars().stream().filter(
                    car -> car.getCarStatus().equals(CarStatus.AVAILABLE)).collect(Collectors.toList()));
        }else {
            model.addAttribute("listCars", carService.getAllCars());
        }
        return "base/cars";
    }
    @GetMapping("/showNewCarForm")
    public String showNewCarForm(Model model){
        // tworzymy model attribute zeby zwiazac z danymi
        Car car = new Car();
        model.addAttribute("car",car);
        return "base/new_car";
    }
    @PostMapping("/saveCar")
    public String saveCar(@ModelAttribute("car") Car car){
        car.setCarStatus(CarStatus.AVAILABLE);
            // zapis samochodu
        carService.saveCar(car);
        logger.debug("Dodano pojazd do bazy: "+car.getName());
        return "redirect:/cars";
        // tutaj mozna dodac zapis do dziennika
    }
    @GetMapping("/{id}/edit")
    public String getEditCar(@PathVariable Long id, Model model) throws CarNotFoundException{

        Car car = carService.findById(id);
        model.addAttribute("car",car);
        return "base/edit_car";
    }
    @PostMapping("/{id}/edit")
    public String editCar(@PathVariable Long id, @ModelAttribute("car") Car car, RedirectAttributes attributes) throws CarNotFoundException{
        carService.updateCar(id, car);
        logger.debug("Edytowano pojazd: " + car.getId());
        return "redirect:/cars";
    }
    @RequestMapping(value = "/{id}/delete")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) throws CarNotFoundException, CarIsUsedByAnotherEntity {
        Car car = carService.findById(id);
        carService.deleteCar(car);
        logger.debug("Usunieto pojazd nr: " + car.getId());
        redirectAttributes.addFlashAttribute("success", "Car deleted succesfully");
        return "redirect:/cars";
    }
    @ExceptionHandler(value = CarNotFoundException.class)
    public String carNotFound(){
        return "error/404";
    }
    @ExceptionHandler(value = CarIsUsedByAnotherEntity.class)
    public String carIsUsed(){
        return "error/cannot-delete";
    }


}
