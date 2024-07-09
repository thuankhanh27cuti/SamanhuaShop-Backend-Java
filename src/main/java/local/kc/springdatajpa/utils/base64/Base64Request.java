package local.kc.springdatajpa.utils.base64;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Base64Request {
    public String base64Data;
    public String type;
}
