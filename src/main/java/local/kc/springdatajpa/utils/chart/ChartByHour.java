package local.kc.springdatajpa.utils.chart;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartByHour {
    public int hour;
    public int revenue;
}
