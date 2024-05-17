package local.kc.springdatajpa.utils;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueByHour {
    public int hour;
    public int revenue;
}
