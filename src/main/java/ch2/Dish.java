package ch2;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Dish {
    private String name;
    private int calories;
    private String type;
}
