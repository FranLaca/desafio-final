package com.example.desafiofinal.Auth;

import com.example.desafiofinal.Auth.Jwt.JwtService;
import com.example.desafiofinal.model.Roles;
import com.example.desafiofinal.model.User;
import com.example.desafiofinal.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testStatus() throws Exception {
        mockMvc.perform(get("/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("online"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testUser");
        loginRequest.setPassword("testPass");

        User user = User.builder()
                .username("testUser")
                .password("encodedPass")
                .rol(Roles.USER)
                .build();

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("mockToken");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"));
    }

    @Test
    public void testRegisterSuccess() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("newUser");
        registerRequest.setPassword("newPass");

        User user = User.builder()
                .username("newUser")
                .password("encodedPass")
                .rol(Roles.USER)
                .build();

        when(passwordEncoder.encode("newPass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtService.getToken(any(UserDetails.class))).thenReturn("mockToken");

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockToken"));
    }
    @Test
    public void testLoginBadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("invalidUser");
        loginRequest.setPassword("password");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorMessage").value("Invalid username or password"));
    }
}