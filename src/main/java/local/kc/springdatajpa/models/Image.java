package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public Image(Integer id, String src) {
        this.id = id;
        this.src = src;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", src='" + src + '\'' +
                '}';
    }
}
