package com.carcyclopedia.cars.repository;

import com.carcyclopedia.cars.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    public Page<Car> findByManufacturer(String manufacturer, Pageable pageable);
    public Page<Car> findByManufacturer(String manufacturer, Sort sort);
}