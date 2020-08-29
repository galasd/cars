package com.carcyclopedia.cars.repository;

import com.carcyclopedia.cars.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    public List<Car> findByManufacturer(String manufacturer);
    public List<Car> findByYear(int year);
}