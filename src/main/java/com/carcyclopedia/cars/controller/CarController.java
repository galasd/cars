package com.carcyclopedia.cars.controller;

import com.carcyclopedia.cars.model.Car;
import com.carcyclopedia.cars.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class CarController {

    @Autowired
    CarRepository carRepository;

    @GetMapping("/cars")
    public ResponseEntity<Map<String, Object>> getAllCars(@RequestParam(required = false) String manufacturer,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "3") int size) {
        try {
            List<Car> cars;
            Pageable paging = PageRequest.of(page, size);
            Page<Car> pageCars;
            if (manufacturer == null) {
                pageCars = carRepository.findAll(paging);
            } else {
                pageCars = carRepository.findByManufacturer(manufacturer, paging);
            }
            cars = pageCars.getContent();
            if (cars.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("cars", cars);
            response.put("currentPage", pageCars.getNumber());
            response.put("totalItems", pageCars.getTotalElements());
            response.put("totalPages", pageCars.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id") long id) {
        Optional<Car> carData = carRepository.findById(id);
        if (carData.isPresent()) {
            return new ResponseEntity<>(carData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/cars")
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        try {
            Car newCar = carRepository.save(new Car(car.getManufacturer(), car.getModel(), car.getYear()));
            return new ResponseEntity<>(newCar, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/cars/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable("id") long id, @RequestBody Car car) {
        Optional<Car> carData = carRepository.findById(id);
        if (carData.isPresent()) {
            Car newCar = carData.get();
            newCar.setManufacturer(car.getManufacturer());
            newCar.setModel(car.getModel());
            newCar.setYear(car.getYear());
            return new ResponseEntity<>(carRepository.save(newCar), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Car> deleteCar(@PathVariable("id") long id) {
        try {
            carRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cars/")
    public ResponseEntity<Car> deleteAllCars() {
        try {
            carRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}