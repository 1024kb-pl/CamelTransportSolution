import java.time.Duration;
import java.util.Date;
import java.util.List;

public interface CamelTransportService {
    List<CamelRide> getAllCamelRides();
    List<CamelRide> getCamelRidesByFromCity(String fromCityName);
    List<CamelRide> getCamelRidesByCityDestionation(String destinationCityName);
    List<CamelRide> getCamelRidesByCamel(Long camelId);
    List<CamelRide> getCamelRidesByDepartureDateRange(Date startDepartureDate, Date endDepartureDate);
    List<CamelRide> getCamelRidesByDepartureDate(Date departureDate);
    List<CamelRide> getCamelRidesByFromCityAndCamelCapacity(String fromCityName, double capacity);

    List<City> getCitiesWhereCamelHadBeen(Long camelId);
    List<City> getCitiesByCountryName(String countryName);

    List<Camel> getCamelsOlderThan(int age);
    List<Camel> getCamelsYoungerThan(int age);
    List<Camel> getCamelsByGender(String gender);

    Camel getCamelByName(String name);
    Duration getCamelWorkingDuration(Long camelId);
}
