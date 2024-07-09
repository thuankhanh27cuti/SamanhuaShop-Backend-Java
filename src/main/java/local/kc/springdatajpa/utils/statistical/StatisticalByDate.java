package local.kc.springdatajpa.utils.statistical;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticalByDate {
    public int id;
    public long sum;
    public Date finishedAt;
}
