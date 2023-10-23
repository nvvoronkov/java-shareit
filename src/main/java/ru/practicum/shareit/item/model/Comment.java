package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity (name = "comments")
@Table
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment_text", nullable = false, length = 500)
    private String text;

    @Column(name = "item_id", nullable = false)
    private Long item;

    @Column(name = "author_id", nullable = false)
    private Long author;

    @Column(name = "created", nullable = false)
    private Instant created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (!id.equals(comment.id)) return false;
        if (!text.equals(comment.text)) return false;
        if (!item.equals(comment.item)) return false;
        if (!author.equals(comment.author)) return false;
        return created.equals(comment.created);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + text.hashCode();
        result = 31 * result + item.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + created.hashCode();
        return result;
    }
}

