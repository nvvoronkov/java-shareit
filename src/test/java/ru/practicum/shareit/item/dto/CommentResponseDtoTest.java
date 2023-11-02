package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentResponseDtoTest {

    @Autowired
    private JacksonTester<CommentReturnDto> json;

    @Test
    void testCommentResponseDto() throws Exception {
        CommentReturnDto commentResponseDto = CommentReturnDto.builder().id(1L).text("Test text")
                .authorName("Author name").created(LocalDateTime.parse("2023-11-03T14:03:50")).build();

        JsonContent<CommentReturnDto> result = json.write(commentResponseDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo("Test text");
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo("Author name");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2022-10-10 10:10:10");
    }
}