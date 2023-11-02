package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controler.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingSaveDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    private final BookingDto bookingForTests = new BookingDto(1L,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(24),
            null,
            null,
            null);

    @SneakyThrows
    @Test
    void saveBooking_whenSuccess() {
        BookingSaveDto bookingSaveDto = new BookingSaveDto(1L, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(6));
        when(bookingService.saveBooking(anyLong(), any(BookingSaveDto.class))).thenReturn(bookingForTests);

        String result = mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", "123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingSaveDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingForTests), result);
    }

    @SneakyThrows
    @Test
    void approveBooking() {
        when(bookingService.approvingBooking(anyLong(), anyBoolean(), anyLong())).thenReturn(bookingForTests);

        String result = mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingForTests), result);
    }

    @SneakyThrows
    @Test
    void getBookingInfo() {
        when(bookingService.getBookingInfo(anyLong(), anyLong())).thenReturn(bookingForTests);

        String result = mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingForTests), result);
    }

    @SneakyThrows
    @Test
    void getBookingInfoList() {
        List<BookingDto> bookingsDto = List.of(bookingForTests);

        when(bookingService.getBookingInfoList(anyLong(), any(String.class), anyInt(), anyInt())).thenReturn(bookingsDto);

        String result = mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingsDto), result);
    }

    @SneakyThrows
    @Test
    void getBookingOwnerInfoList() {

        List<BookingDto> bookingsDto = List.of(bookingForTests);

        when(bookingService.getBookingOwnerInfoList(anyLong(), any(String.class), anyInt(), anyInt())).thenReturn(bookingsDto);

        String result = mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "123"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(bookingsDto), result);
    }
}