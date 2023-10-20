package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotBlank(message = "Имя не должно быть пустым")
    private String name;

    @Column(name = "description")
    @NotBlank(message = "Описание не должно быть пустым")
    private String description;

    @Column(name = "is_available")
    @NotNull(message = "Должно быть указано доступен ли объект: true/false")
    private Boolean available;

    @Column(name = "owner_id")
    @NotNull(message = "Должен быть указан UserId владельца")
    private Long owner;

    @Transient
    private ItemRequest request;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (!id.equals(item.id)) return false;
        if (!name.equals(item.name)) return false;
        if (!description.equals(item.description)) return false;
        if (!available.equals(item.available)) return false;
        if (!owner.equals(item.owner)) return false;
        return Objects.equals(request, item.request);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + available.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + (request != null ? request.hashCode() : 0);
        return result;
    }
}
