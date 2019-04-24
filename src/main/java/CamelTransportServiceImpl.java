import java.sql.Timestamp;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class CamelTransportServiceImpl implements CamelTransportService {
    private final CamelTransportClient camelTransportClient;

    public CamelTransportServiceImpl(CamelTransportClient camelTransportClient) {
        this.camelTransportClient = camelTransportClient;
    }

    @Override
    public List<CamelRide> getAllCamelRides() {
        return camelTransportClient.getAllCamelRides();
    }

    @Override
    public List<CamelRide> getCamelRidesByFromCity(String fromCityName) {
        return camelTransportClient.getCamelRidesByFromCity(fromCityName);
    }

    @Override
    public List<CamelRide> getCamelRidesByCityDestionation(String destinationCityName) {
        return camelTransportClient.getCamelRidesByCityDestionation(destinationCityName);
    }

    @Override
    public List<CamelRide> getCamelRidesByCamel(Long camelId) {
        return getAllCamelRides().stream()
                .filter(camelRide -> camelRide.getCamel().getId().equals(camelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<CamelRide> getCamelRidesByDepartureDateRange(Date startDepartureDate, Date endDepartureDate) {
        return getAllCamelRides().stream()
                .filter(camelRide -> isBetweenRange(camelRide.getDepartureDate(), startDepartureDate, endDepartureDate))
                .collect(Collectors.toList());
    }

    private boolean isBetweenRange(Timestamp camelRide, Date startDepartureDate, Date endDepartureDate) {
        return camelRide.after(startDepartureDate) && camelRide.before(endDepartureDate);
    }

    @Override
    public List<CamelRide> getCamelRidesByDepartureDate(Date departureDate) {
        return getAllCamelRides().stream()
                .filter(camelRide -> camelRide.getDepartureDate().compareTo(departureDate) == 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<CamelRide> getCamelRidesByFromCityAndCamelCapacity(String fromCityName, double capacity) {
        return getCamelRidesByFromCity(fromCityName).stream()
                .filter(camelRide -> camelRide.getCamel().getCapacity() >= capacity)
                .collect(Collectors.toList());
    }

    @Override
    public List<City> getCitiesWhereCamelHadBeen(Long camelId) {
        Set<City> cities = new HashSet<>();

        getCamelRidesByCamel(camelId).forEach(camelRide -> cities.addAll(Arrays.asList(camelRide.getDestination(), camelRide.getFrom())));

        return new LinkedList<>(cities);
    }

    @Override
    public List<City> getCitiesByCountryName(String countryName) {
        return camelTransportClient.getCitiesByCountryName(countryName);
    }

    private List<Camel> getAllCamels() {
        return camelTransportClient.getAllCamels();
    }

    @Override
    public List<Camel> getCamelsOlderThan(int age) {
        return getAllCamels().stream()
                .filter(camel -> camel.getAge() > age)
                .collect(Collectors.toList());
    }

    @Override
    public List<Camel> getCamelsYoungerThan(int age) {
        return getAllCamels().stream()
                .filter(camel -> camel.getAge() < age)
                .collect(Collectors.toList());
    }

    @Override
    public List<Camel> getCamelsByGender(String gender) {
        return camelTransportClient.getCamelsByGender(gender);
    }

    @Override
    public Camel getCamelByName(String name) {
        return getAllCamels().stream()
                .filter(camel -> camel.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Couldn't find a camel with name: " + name));
    }

    @Override
    public Duration getCamelWorkingDuration(Long camelId) {
        Duration duration = Duration.ZERO;

        List<Duration> durations = getCamelRidesByCamel(camelId).stream()
                .map(this::calculateDuration)
                .collect(Collectors.toList());

        for (Duration dur : durations) {
            duration = duration.plus(dur);
        }

        return duration;
    }

    private Duration calculateDuration(CamelRide camelRide) {
        return Duration.between(camelRide.getDepartureDate().toInstant(), camelRide.getArrivalDate().toInstant());
    }


}
