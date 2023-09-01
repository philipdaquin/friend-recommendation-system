package com.example.user_service.domains;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.validateMockitoUsage;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserDetailsTest {

    private Validator validator;

    @Before
    void setUp() { 
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void equalsVerifier() throws Exception { 
        
        User userOne = new User();
        User userTwo = new User();

        userOne.setId(1L);
        userTwo.setId(2L);
        assertNotEquals(userOne.getId(), userTwo.getId());

        userOne.setFirstName("john");
        userTwo.setFirstName("philip");
        assertNotEquals(userOne.getFirstName(), userTwo.getFirstName());
        
        userTwo.setEmail("philip");
        assertNull(userOne.getEmail());
        assertNotNull(userTwo.getEmail());


        userTwo.setCreatedBy("john");
        assertEquals(userTwo.getCreatedBy(), "john");
        assertEquals(null, userTwo.getLastModifiedBy());
    
    }    
}
