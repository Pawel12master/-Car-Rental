package com.example.biznes;

import com.example.biznes.model.Car;
import com.example.biznes.model.CarStatus;
import com.example.biznes.repository.CarRepository;
import com.example.biznes.user.Role;
import com.example.biznes.user.User;
import com.example.biznes.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Seeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final PasswordEncoder passwordEncoder;

    public Seeder(UserRepository userRepository, CarRepository carRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setEmail("admin@admin.pl");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setFirstName("adminek");
        user.setLastName("naziwsko");
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        User user2 = new User();
        user2.setEmail("user@user.pl");
        user2.setPassword(passwordEncoder.encode("user"));
        user2.setFirstName("userek");
        user2.setLastName("nazwisko2");
        user2.setRole(Role.USER);
        userRepository.save(user2);

        Car car = new Car();
        car.setModel("mustang");
        car.setName("ford");
        car.setPrice(15);
        car.setCarStatus(CarStatus.AVAILABLE);
        carRepository.save(car);

        Car car2 = new Car();
        car2.setName("toyota");
        car2.setModel("yaris");
        car2.setPrice(15);
        car2.setCarStatus(CarStatus.AVAILABLE);
        carRepository.save(car2);
    }
}
