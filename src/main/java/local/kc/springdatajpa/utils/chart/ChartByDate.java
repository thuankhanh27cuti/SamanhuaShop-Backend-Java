package local.kc.springdatajpa.utils.chart;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChartByDate {
    public Date date;
    public int revenue;
}
