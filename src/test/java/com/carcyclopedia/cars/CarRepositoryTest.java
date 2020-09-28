package com.carcyclopedia.cars;

import com.carcyclopedia.cars.model.Car;
import com.carcyclopedia.cars.repository.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations="classpath:application-dev.properties")
public class CarRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    CarRepository carRepository;

    @Test
    public void WhenFoundByManufacturer_returnCar() {
        //given
        Car ferrari = new Car("Ferrari");
        testEntityManager.persist(ferrari);
        testEntityManager.flush();
        //when
        Car foundByManu = carRepository.findByManufacturer(ferrari.getManufacturer());
        //then
        assertThat(foundByManu.getManufacturer()).isEqualTo(ferrari.getManufacturer());
    }
}