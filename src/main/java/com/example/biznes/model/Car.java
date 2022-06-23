package com.example.biznes.model;

import javax.persistence.*;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String model;
    private float price;
    @Enumerated(EnumType.STRING)
    private CarStatus carStatus;

    public Car(String name, String model, float price, CarStatus carStatus) {
        this.name = name;
        this.model = model;
        this.price = price;
        this.carStatus = carStatus;
    }
    public Car(String name, String model, float price){
        this.name = name;
        this.model = model;
        this.price = price;
        this.carStatus = CarStatus.AVAILABLE;
    }

    public Car() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public CarStatus getCarStatus() {
        return carStatus;
    }

    public void setCarStatus(CarStatus carStatus) {
        this.carStatus = carStatus;
    }
}
