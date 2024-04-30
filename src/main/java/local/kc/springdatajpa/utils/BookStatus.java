package local.kc.springdatajpa.utils;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookStatus {
    public Integer id;
    public String name;
    public Integer optionId;
    public String optionName;
    public Integer quantity;
}
