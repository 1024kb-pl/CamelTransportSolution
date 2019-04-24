import lombok.*;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter @ToString
@Builder
@Setter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CamelRide {
    private Timestamp arrivalDate;
    private Timestamp departureDate;
    private Camel camel;
    private City destination;
    private City from;
    private Long id;
}
