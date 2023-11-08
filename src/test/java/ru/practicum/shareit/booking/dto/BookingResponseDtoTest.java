package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.enums.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingResponseDtoTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @Test
    void testBookingResponseDto() throws Exception {
        UserDto user = UserDto.builder().id(1L).name("UserName").email("User@email.com").build();
        ItemDto item = ItemDto.builder().id(2L).name("ItemName").description("Item description")
                .available(true).build();

        BookingDto bookingResponseDto = BookingDto.builder()
                .id(5L)
                .start(LocalDateTime.parse("2023-11-03T14:03:50"))
                .end(LocalDateTime.parse("2024-11-03T14:03:50"))
                .item(item)
                .booker(user)
                .status(BookingStatus.WAITING)
                .build();
        JsonContent<BookingDto> result = json.write(bookingResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(5);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2023-11-03T14:03:50");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-11-03T14:03:50");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("Item description");
//        assertThat(result).extractingJsonPathNumberValue("$.item.owner").isEqualTo(3);
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(true);
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("UserName");
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo("User@email.com");
    }
}