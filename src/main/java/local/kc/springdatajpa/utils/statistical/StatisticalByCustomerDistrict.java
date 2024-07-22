package local.kc.springdatajpa.utils.statistical;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatisticalByCustomerDistrict {
    public int id;
    public String name;
    public int count;
}
