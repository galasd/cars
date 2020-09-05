package com.carcyclopedia.cars.controller;

import com.carcyclopedia.cars.exception.ResourceNotFoundException;
import com.carcyclopedia.cars.model.Car;
import com.carcyclopedia.cars.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CarController {

    @Autowired
    CarRepository carRepository;

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    @GetMapping("/cars")
    public ResponseEntity<Map<String, Object>> getAllCars(@RequestParam(required = false) String manufacturer,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "3") int size,
                                                          @RequestParam(defaultValue = "id, desc") String[] sort) {
        try {
            List<Sort.Order> orders = new ArrayList<>();
            if(sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] newSort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(newSort[1]), newSort[0]));
                }
            }
            else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

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
        Car carData = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No car found with id = " + id));
        return new ResponseEntity<>(carData, HttpStatus.OK);
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
        Car carData = carRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No car found with id = " + id));
        carData.setManufacturer(car.getManufacturer());
        carData.setModel(car.getModel());
        carData.setYear(car.getYear());
        return new ResponseEntity<>(carRepository.save(carData), HttpStatus.OK);

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