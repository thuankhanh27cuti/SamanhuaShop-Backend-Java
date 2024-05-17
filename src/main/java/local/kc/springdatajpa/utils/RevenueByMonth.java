package local.kc.springdatajpa.utils;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueByMonth {
    public int month;
    public int year;
    public int revenue;
}
