package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Integer id;

    @Column(name = "image_src", nullable = false)
    private String src;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", src='" + src + '\'' +
                '}';
    }
}
