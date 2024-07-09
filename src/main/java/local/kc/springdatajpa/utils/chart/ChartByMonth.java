package local.kc.springdatajpa.utils.chart;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartByMonth {
    public int month;
    public int year;
    public int revenue;
}
