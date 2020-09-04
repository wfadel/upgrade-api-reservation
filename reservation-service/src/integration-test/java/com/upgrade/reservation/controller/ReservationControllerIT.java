package com.upgrade.reservation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.upgrade.reservation.BaseTestIT;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.SneakyThrows;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ReservationControllerIT extends BaseTestIT {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    public void reserve() {
        String checkInDate = LocalDate.now().plusDays(3).format(DATE_TIME_FORMATTER);
        String checkOutDate = LocalDate.now().plusDays(5).format(DATE_TIME_FORMATTER);
        mockMvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                // TODO use constants and helper methods to avoid duplicate code
                .content("{\"user\": {\"email\": \"john.doe@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}, \"checkInDate\": \""
                        + checkInDate + "\", \"checkOutDate\": \"" + checkOutDate + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reservationId").isNotEmpty())
                .andExpect(jsonPath("$.checkInDate").value(checkInDate))
                .andExpect(jsonPath("$.checkOutDate").value(checkOutDate));
    }

    @SneakyThrows
    @Test
    public void update() {
        String checkInDate = LocalDate.now().plusDays(3).format(DATE_TIME_FORMATTER);
        String checkOutDate = LocalDate.now().plusDays(5).format(DATE_TIME_FORMATTER);
        MvcResult result = mockMvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"user\": {\"email\": \"john.doe@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}, \"checkInDate\": \""
                        + checkInDate + "\", \"checkOutDate\": \"" + checkOutDate + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reservationId").isNotEmpty())
                .andExpect(jsonPath("$.checkInDate").value(checkInDate))
                .andExpect(jsonPath("$.checkOutDate").value(checkOutDate)).andReturn();

        // update
        checkInDate = LocalDate.now().plusDays(5).format(DATE_TIME_FORMATTER);
        checkOutDate = LocalDate.now().plusDays(6).format(DATE_TIME_FORMATTER);
        final ObjectNode node = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ObjectNode.class);

        String reservationId = node.get("reservationId").asText();
        mockMvc.perform(put("/api/v1/reservations/{reservationId}", reservationId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"reservationId\": \"" + reservationId + "\", \"user\": {\"email\": \"john.doe@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}, \"checkInDate\": \""
                        + checkInDate + "\", \"checkOutDate\": \"" + checkOutDate + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reservationId").value(reservationId))
                .andExpect(jsonPath("$.checkInDate").value(checkInDate))
                .andExpect(jsonPath("$.checkOutDate").value(checkOutDate));

        mockMvc.perform(get("/api/v1/reservations/{reservationId}", reservationId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reservationId").value(reservationId))
                .andExpect(jsonPath("$.checkInDate").value(checkInDate))
                .andExpect(jsonPath("$.checkOutDate").value(checkOutDate));
    }

    @SneakyThrows
    @Test
    public void cancel() {
        String checkInDate = LocalDate.now().plusDays(3).format(DATE_TIME_FORMATTER);
        String checkOutDate = LocalDate.now().plusDays(5).format(DATE_TIME_FORMATTER);
        MvcResult result = mockMvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"user\": {\"email\": \"john.doe@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}, \"checkInDate\": \""
                        + checkInDate + "\", \"checkOutDate\": \"" + checkOutDate + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reservationId").isNotEmpty())
                .andExpect(jsonPath("$.checkInDate").value(checkInDate))
                .andExpect(jsonPath("$.checkOutDate").value(checkOutDate)).andReturn();

        // update
        checkInDate = LocalDate.now().plusDays(5).format(DATE_TIME_FORMATTER);
        checkOutDate = LocalDate.now().plusDays(6).format(DATE_TIME_FORMATTER);
        final ObjectNode node = new ObjectMapper().readValue(result.getResponse().getContentAsString(), ObjectNode.class);

        String reservationId = node.get("reservationId").asText();
        mockMvc.perform(delete("/api/v1/reservations/{reservationId}", reservationId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/reservations/{reservationId}", reservationId))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    public void overlapReserve() {
        String checkInDate = LocalDate.now().plusDays(3).format(DATE_TIME_FORMATTER);
        String checkOutDate = LocalDate.now().plusDays(5).format(DATE_TIME_FORMATTER);
        mockMvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"user\": {\"email\": \"john.doe@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}, \"checkInDate\": \""
                        + checkInDate + "\", \"checkOutDate\": \"" + checkOutDate + "\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.reservationId").isNotEmpty());

        mockMvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"user\": {\"email\": \"james.bond@gmail.com\", \"firstName\": \"James\", \"lastName\": \"Bond\"}, \"checkInDate\": \""
                        + checkInDate + "\", \"checkOutDate\": \"" + checkOutDate + "\"}"))
                .andExpect(status().isConflict());
    }

    @SneakyThrows
    @Test
    public void reserveStartDateAfterEndDate() {
        String checkInDate = LocalDate.now().plusDays(5).format(DATE_TIME_FORMATTER);
        String checkOutDate = LocalDate.now().plusDays(3).format(DATE_TIME_FORMATTER);
        mockMvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"user\": {\"email\": \"john.doe@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}, \"checkInDate\": \""
                        + checkInDate + "\", \"checkOutDate\": \"" + checkOutDate + "\"}"))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    public void reserveStartDateIsToday() {
        String checkInDate = LocalDate.now().format(DATE_TIME_FORMATTER);
        String checkOutDate = LocalDate.now().plusDays(3).format(DATE_TIME_FORMATTER);
        mockMvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"user\": {\"email\": \"john.doe@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}, \"checkInDate\": \""
                        + checkInDate + "\", \"checkOutDate\": \"" + checkOutDate + "\"}"))
                .andExpect(status().isBadRequest());
    }


    @SneakyThrows
    @Test
    public void reserveMoreThanThreeDays() {
        String checkInDate = LocalDate.now().plusDays(1).format(DATE_TIME_FORMATTER);
        String checkOutDate = LocalDate.now().plusDays(5).format(DATE_TIME_FORMATTER);
        mockMvc.perform(post("/api/v1/reservations")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("{\"user\": {\"email\": \"john.doe@gmail.com\", \"firstName\": \"John\", \"lastName\": \"Doe\"}, \"checkInDate\": \""
                        + checkInDate + "\", \"checkOutDate\": \"" + checkOutDate + "\"}"))
                .andExpect(status().isBadRequest());
    }
}
