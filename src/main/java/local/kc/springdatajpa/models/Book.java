package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer id;

    @Column(name = "book_name", nullable = false)
    private String name;

    @Column(name = "book_image", nullable = false)
    private String image;

    @Column(name = "book_price", nullable = false)
    private Integer price;

    @Column(name = "book_description", columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "book_created_at", nullable = false)
    private Date createAt;

    @Column(name = "book_is_deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @ManyToMany()
    @JoinTable(name = "books_categories",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private Set<Option> options;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private Set<Image> images;

    public Book(Integer id) {
        this.id = id;
    }

    public Book(Integer id, String name, String image, Integer price, String description, Date createAt) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.createAt = createAt;
    }

    public Book(Integer id, String name, String image, Integer price, String description, Date createAt, boolean isDeleted) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.createAt = createAt;
        this.isDeleted = isDeleted;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", price=" + price +
                ", description='" + description + '\'' +
                ", createAt=" + createAt +
                '}';
    }
}
