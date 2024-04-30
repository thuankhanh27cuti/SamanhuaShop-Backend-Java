package local.kc.springdatajpa.utils;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TopSellerBook {
    public Integer id;
    public String name;
    public String image;
    public Long quantity;
    public Long revenue;

    public TopSellerBook(Integer id, String name, String image, Long quantity, int revenue) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.quantity = quantity;
        this.revenue = (long) revenue;
    }
}
