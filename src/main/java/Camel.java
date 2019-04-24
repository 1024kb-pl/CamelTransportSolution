import lombok.*;

@NoArgsConstructor
@Getter @ToString
@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Camel {
    private int age;
    private double capacity;
    private Gender gender;
    private Long id;
    private String name;


}
