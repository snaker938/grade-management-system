package uk.ac.ucl.comp0010;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

/**
 * Basic integration test to verify that the application context
 * loads successfully and that core components are initialized.
 */
@SpringBootTest
class ApplicationTests {

	@Autowired
	private ApplicationContext context;

	/**
	 * Ensures that the application context loads without errors
	 * and that the ApplicationContext is not null.
	 */
	@Test
	@DisplayName("Context Loads Successfully")
	void contextLoads() {
		assertNotNull(context, "ApplicationContext should be loaded and not null.");
	}
}
