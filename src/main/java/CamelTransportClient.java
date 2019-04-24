import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class CamelTransportClient {
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public CamelTransportClient() {
        this.restTemplate = new RestTemplate();
        this.baseUrl = "http://localhost:8090";
    }

    public List<CamelRide> getAllCamelRides() {
        ResponseEntity<List<CamelRide>> response = restTemplate.exchange(
                buildUrl(buildUrl("/camelRide")),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CamelRide>>() {});

        return response.getBody();
    }

    public List<City> getCities() {
        ResponseEntity<List<City>> response = restTemplate.exchange(
                buildUrl("/city"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<City>>() {});

        return response.getBody();
    }

    public List<CamelRide> getCamelRidesByCityDestionation(String destinationCityName) {
        ResponseEntity<List<CamelRide>> response = restTemplate.exchange(
                buildUrl("/camelRide/destination/" + getCityByName(destinationCityName).getId()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CamelRide>>() {});

        return response.getBody();
    }

    public List<CamelRide> getCamelRidesByFromCity(String fromCityName) {
        ResponseEntity<List<CamelRide>> response = restTemplate.exchange(
                buildUrl("/camelRide/from/" + getCityByName(fromCityName).getId()),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<CamelRide>>() {});

        return response.getBody();
    }

    public List<City> getCitiesByCountryName(String countryName) {
        ResponseEntity<List<City>> response = restTemplate.exchange(
                buildUrl("/city/by?country=" + countryName),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<City>>() {});

        return response.getBody();
    }

    public List<Camel> getAllCamels() {
        ResponseEntity<List<Camel>> response = restTemplate.exchange(
                buildUrl("/camel/"),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Camel>>() {});

        return response.getBody();
    }

    public List<Camel> getCamelsByGender(String gender) {
        ResponseEntity<List<Camel>> response = restTemplate.exchange(
                buildUrl("/camel/by?gender="+gender),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Camel>>() {});

        return response.getBody();
    }

    public City getCityByName(String fromCityName) {
        return getCities().stream()
                .filter(city -> city.getName().endsWith(fromCityName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Couldn't find a city with name: " + fromCityName));
    }


    private String buildUrl(String path) {
        return baseUrl + path;
    }
}
