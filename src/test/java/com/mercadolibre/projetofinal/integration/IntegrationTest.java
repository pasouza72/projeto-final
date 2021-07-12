package com.mercadolibre.projetofinal.integration;

import com.mercadolibre.projetofinal.util.PopulateDatabase;
import com.mercadolibre.restclient.mock.RequestMockHolder;
import com.mercadolibre.projetofinal.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = { "SCOPE_SUFFIX = integration_test" })
public abstract class IntegrationTest {

	@BeforeAll
	protected static void populateTestDatabase(@Autowired PopulateDatabase populateDatabase){
		try {
		populateDatabase.populate();
		} catch (Exception e){

		}
	}

	@AfterEach
	protected void afterEach() {
		RequestMockHolder.clear();
	}
}
