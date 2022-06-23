package com.example.biznes.exception;

public class CarIsUsedByAnotherEntity extends Throwable{
    public CarIsUsedByAnotherEntity(){
        super("cannot delete car, because it is used");
    }
}
