package com.carcyclopedia.cars.repository;

import com.carcyclopedia.cars.models.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByManufacturer(String manufacturer);
    Page<Car> findByManufacturer(String manufacturer, Pageable pageable);
}