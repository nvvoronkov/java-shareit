package ru.practicum.shareit.request.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestRequesterDtoTest {

    @Autowired
    private JacksonTester<ItemRequestRequesterDto> json;

    @Test
    void testOutputItemRequestDto() throws Exception {
        List<ItemDto> items = List.of(ItemDto.builder().id(2L).name("ItemName").description("Item description")
                .available(true).requestId(4L).build());

        ItemRequestRequesterDto outputItemRequestDto = ItemRequestRequesterDto.builder()
                .id(1L)
                .description("Test description")
                .created(LocalDateTime.parse("2023-11-03T14:03:50"))
                .items(items)
                .build();

        JsonContent<ItemRequestRequesterDto> result = json.write(outputItemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("Test description");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo("2023-11-03T14:03:50");
        assertThat(result).extractingJsonPathNumberValue("$.items.[0].id").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.items.[0].name").isEqualTo("ItemName");
        assertThat(result).extractingJsonPathStringValue("$.items.[0].description")
                .isEqualTo("Item description");
        assertThat(result).extractingJsonPathBooleanValue("$.items.[0].available").isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.items.[0].requestId").isEqualTo(4);
    }
}