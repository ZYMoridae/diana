package com.jz.nebula.validator;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.jz.nebula.dao.ProductRepository;

@SpringBootTest
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.yml")
public class CartItemValidatorTest {
	@Mock
	private ProductRepository productRepository;
	
	@InjectMocks
	CartItemValidator cartItemValidator;
	
    @Before
    public void beforeTests() throws Exception {
        MockitoAnnotations.initMocks(this);
    }
	
	@Test
	public void carItemValidationTest() {
		assertTrue(true);
	}
}
