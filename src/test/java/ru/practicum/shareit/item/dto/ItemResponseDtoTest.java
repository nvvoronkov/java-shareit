package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemResponseDtoTest {

    @Autowired
    private JacksonTester<ItemReturnDto> json;

    @Test
    void testItemResponseDto() throws Exception {
        List<CommentReturnDto> comments = List.of(CommentReturnDto.builder().id(11L).text("Text of comment")
                .authorName("AuthorName").created(LocalDateTime.parse("2023-11-03T14:03:50")).build());
        BookingShortDto lastBooking = null;
        BookingShortDto nextBooking = null;
        ItemReturnDto itemResponseDto = ItemReturnDto.builder()
                .id(1L)
                .name("ItemName")
                .description("Item description")
                .available(true)
                .lastBooking(lastBooking)
                .nextBooking(nextBooking)
                .comments(comments)
                .build();

        JsonContent<ItemReturnDto> result = json.write(itemResponseDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(true);
        assertThat(result).extractingJsonPathStringValue("$.lastBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathStringValue("$.nextBooking").isEqualTo(null);
        assertThat(result).extractingJsonPathNumberValue("$.comments.[0].id").isEqualTo(11);
        assertThat(result).extractingJsonPathStringValue("$.comments.[0].text")
                .isEqualTo("Text of comment");
        assertThat(result).extractingJsonPathStringValue("$.comments.[0].authorName")
                .isEqualTo("AuthorName");
        assertThat(result).extractingJsonPathStringValue("$.comments.[0].created")
                .isEqualTo("2023-11-03T14:03:50");
    }
}