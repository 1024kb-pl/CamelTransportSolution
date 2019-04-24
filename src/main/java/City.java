import lombok.*;

import java.util.Objects;

@NoArgsConstructor
@Getter @ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class City {
    private Long id;
    private String country;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(id, city.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
