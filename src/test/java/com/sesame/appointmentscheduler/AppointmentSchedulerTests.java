package com.sesame.appointmentscheduler;

import static com.sesame.appointmentscheduler.entity.AppointmentStatus.BOOKED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sesame.appointmentscheduler.entity.Appointment;
import com.sesame.appointmentscheduler.entity.AppointmentStatus;
import java.io.IOException;
import java.time.LocalDateTime;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class AppointmentSchedulerTests {

    /**
     * TestContainers Postgres
     */
    private static PostgreSQLContainer postgresContainer = new PostgreSQLContainer()
        .withDatabaseName("postgres")
        .withUsername("postgres")
        .withPassword("postgres123");


    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @BeforeAll
    public static void init() throws IOException, InterruptedException {
        postgresContainer.start();
        int port = postgresContainer.getFirstMappedPort();
        System.setProperty("spring.datasource.url",
            String.format("jdbc:postgresql://localhost:%d/postgres",
                postgresContainer.getFirstMappedPort()));
        System.setProperty("spring.datasource.username",
            postgresContainer.getUsername());
        System.setProperty("spring.datasource.password",
            postgresContainer.getPassword());
    }

    @AfterAll
    public static void shutdown() {
        postgresContainer.stop();
    }

    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }


    public void createAppointment() throws Exception {
        Appointment newAppointment = Appointment.builder()
            .appointmentDate(LocalDateTime.of(2019, 06, 20, 10, 1))
            .appointmentDurationInMins(10)
            .doctorName("Bruno Rossi")
            .price(25.00)
            .status(AppointmentStatus.AVAILABLE)
            .build();

        this.mockMvc.perform(post("/api/v1/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(newAppointment)))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Order(1)
    @DisplayName("Test the creation of a new appointment")
    public void testCreateNewAppointment() throws Exception {
        Appointment newAppointment = Appointment.builder()
            .appointmentDate(LocalDateTime.of(2019, 06, 20, 10, 1))
            .appointmentDurationInMins(10)
            .doctorName("Bruno Rossi")
            .price(25.00)
            .status(AppointmentStatus.AVAILABLE)
            .build();

        this.mockMvc.perform(post("/api/v1/appointments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(newAppointment)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Matchers.is(Matchers.notNullValue())))
            .andExpect(jsonPath("$.doctorName", Matchers.is("Bruno Rossi")))
            .andReturn();
    }

    @Test
    @Order(2)
    @DisplayName("Test the search of an appointment by id")
    public void testGetAppointment() throws Exception {
        createAppointment();
        this.mockMvc.perform(get("/api/v1/appointments/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Matchers.is(Matchers.notNullValue())))
            .andExpect(jsonPath("$.doctorName", Matchers.is("Bruno Rossi")))
            .andExpect(jsonPath("$.price", Matchers.is(25.00)))
            .andExpect(jsonPath("$.status", Matchers.is("AVAILABLE")))
            .andReturn();
    }

    @Test
    @Order(3)
    @DisplayName("Test the update of an appointment")
    public void testAlterAppointment() throws Exception {
        createAppointment();
        Appointment status = Appointment.builder().status(BOOKED).build();
        this.mockMvc.perform(put("/api/v1/appointments/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(status)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Matchers.is(Matchers.notNullValue())))
            .andExpect(jsonPath("$.doctorName", Matchers.is("Bruno Rossi")))
            .andExpect(jsonPath("$.price", Matchers.is(25.00)))
            .andExpect(jsonPath("$.status", Matchers.is("BOOKED")))
            .andReturn();
    }

    @Test
    @Order(4)
    @DisplayName("Test the search between dates")
    public void testSearchAppointmentsDate() throws Exception {
        createAppointment();
        this.mockMvc.perform(get("/api/v1/appointments/search?startDate=2019-06-19&endDate=2019-06-22&sortType=ASC"))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    @Order(5)
    @DisplayName("Test the delete of an appointment")
    public void testDeleteAppointment() throws Exception {
        createAppointment();
        this.mockMvc.perform(delete("/api/v1/appointments/1"))
            .andExpect(status().isOk())
            .andReturn();
    }

}
