package local.kc.springdatajpa.utils;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevenueByDate {
    public Date date;
    public int revenue;
}
