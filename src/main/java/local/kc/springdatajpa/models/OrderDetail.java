package local.kc.springdatajpa.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {

    @EmbeddedId
    private OrderDetailId orderDetailId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @MapsId(value = "orderId")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    @MapsId(value = "optionId")
    private Option option;

    @Column(name = "option_quantity", nullable = false)
    private Integer quantity;

    @Column(name = "order_total_price", nullable = false)
    private Float price;

    public OrderDetail(Order order, Option option, Integer quantity, Float price) {
        this.orderDetailId = new OrderDetailId(order.getId(), option.getId());
        this.order = new Order(order.getId());
        Book book = option.getBook();
        this.option = Option.builder()
                .id(option.getId())
                .name(option.getName())
                .quantity(option.getQuantity())
                .image(option.getImage())
                .isDeleted(option.isDeleted())
                .book(Book.builder()
                        .id(book.getId())
                        .name(book.getName())
                        .image(book.getImage())
                        .price(book.getPrice())
                        .description(book.getDescription())
                        .createAt(book.getCreateAt())
                        .isDeleted(book.isDeleted())
                        .build())
                .build();
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "order=" + order.getId() +
                ", option=" + option.getId() +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}