package com.example.user_service.controller;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.example.user_service.UserServiceApplication;
import com.example.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = UserServiceApplication.class)
@TestPropertySource(properties = "testContext=true")
@DirtiesContext
public class UserControllerTest {

    @MockBean
    UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;
    // @Autowired
    // private Filter springSecurityFilterChain;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                // .addFilter(springSecurityFilterChain)
                .build();
    }

    @Test
    public void signup() throws Exception {
        // SignupForm form = new SignupForm("a@b.test","somepassword");

        // doReturn(new UserDto(form.email)).when(userService).createUser(form.email, form.password);

        // mockMvc.perform(
        //         post("/users")
        //         .contentType(MediaType.APPLICATION_JSON)
        //         .content(objectMapper.writeValueAsBytes(form)))
        //         .andExpect(
        //                status().isOk())
        //         .andExpect(
        //                 jsonPath("$.username", is(form.email))
        //         );

        // verify(userService).createUser(form.email, form.password);
        // verifyNoMoreInteractions(userService);
    }

}