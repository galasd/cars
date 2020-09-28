package com.carcyclopedia.cars;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations="classpath:application-dev.properties")
class CarsApplicationTests {

	@Test
	void contextLoads() {
	}

}
