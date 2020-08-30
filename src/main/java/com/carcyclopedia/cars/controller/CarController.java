package com.carcyclopedia.cars.controller;

import com.carcyclopedia.cars.model.Car;
import com.carcyclopedia.cars.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CarController {

    @Autowired
    CarRepository carRepository;

    @GetMapping("/cars")
    public ResponseEntity<List<Car>> getAllCars(@RequestParam(required = false) String manufacturer) {
        try {
            List<Car> cars = new ArrayList<>();
            if (manufacturer == null) {
                carRepository.findAll().forEach(cars::add);
            } else {
                carRepository.findByManufacturer(manufacturer).forEach(cars::add);
            }
            if (cars.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(cars, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
