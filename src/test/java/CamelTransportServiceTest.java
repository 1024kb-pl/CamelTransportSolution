import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CamelTransportServiceTest  {
    private final Camel MALGOSIA_CAMEL =
            Camel.builder()
                    .id(1L)
                    .capacity(10.4f)
                    .gender(Gender.FEMALE)
                    .age(9)
                    .name("Małgosia")
                    .build();

    private final Camel JAS_CAMEL =
            Camel.builder()
                    .id(2L)
                    .age(15)
                    .capacity(100.4f)
                    .gender(Gender.MALE)
                    .name("Jaś")
                    .build();

    private final City WARSAW =
            City.builder()
                    .country("Poland")
                    .name("Warsaw")
                    .id(1L)
                    .build();

    private final City LODZ =
            City.builder()
                    .country("Poland")
                    .name("Łódź")
                    .id(2L)
                    .build();

    private final CamelRide LODZ_WARSAW_RIDE =
            CamelRide.builder()
                    .id(1L)
                    .camel(JAS_CAMEL)
                    .arrivalDate(Timestamp.valueOf("2018-10-10 03:00:00"))
                    .departureDate(Timestamp.valueOf("2018-10-10 01:00:00"))
                    .from(LODZ)
                    .destination(WARSAW)
                    .build();


    private final CamelRide WARSAW_LODZ_RIDE =
            CamelRide.builder()
                    .id(2L)
                    .camel(MALGOSIA_CAMEL)
                    .arrivalDate(Timestamp.valueOf("2018-10-03 03:00:00"))
                    .departureDate(Timestamp.valueOf("2018-10-02 01:00:00"))
                    .from(WARSAW)
                    .destination(LODZ)
                    .build();

    private final CamelRide WARSAW_LODZ_RIDE_2 =
            CamelRide.builder()
                    .id(3L)
                    .camel(JAS_CAMEL)
                    .arrivalDate(Timestamp.valueOf("2018-10-04 03:00:00"))
                    .departureDate(Timestamp.valueOf("2018-10-01 01:00:00"))
                    .from(WARSAW)
                    .destination(LODZ)
                    .build();

    private CamelTransportClient camelTransportClient =  Mockito.mock(CamelTransportClient.class);
    private CamelTransportService camelTransportService = new CamelTransportServiceImpl(camelTransportClient);

    @Test
    public void testGetCamelByName() {
        Camel expectedCamelName = MALGOSIA_CAMEL;
        Mockito.when(camelTransportClient.getAllCamels()).thenReturn(Arrays.asList(MALGOSIA_CAMEL, JAS_CAMEL));

        Camel camel = camelTransportService.getCamelByName(MALGOSIA_CAMEL.getName());

        Assert.assertEquals(expectedCamelName, camel);
    }

    @Test
    public void testGetAllCamels() {
        Mockito.when(camelTransportClient.getAllCamelRides()).thenReturn(Arrays.asList(LODZ_WARSAW_RIDE, WARSAW_LODZ_RIDE));

        List<CamelRide> camelRides = camelTransportService.getAllCamelRides();

        Assert.assertEquals(Arrays.asList(LODZ_WARSAW_RIDE, WARSAW_LODZ_RIDE), camelRides);
    }

    @Test
    public void testGetCamelsRideByFromCity() {
        Mockito.when(camelTransportClient.getCamelRidesByFromCity(WARSAW.getName())).thenReturn(Collections.singletonList(WARSAW_LODZ_RIDE));

        List<CamelRide> camelRides = camelTransportService.getCamelRidesByFromCity(WARSAW.getName());

        Assert.assertEquals(Collections.singletonList(WARSAW_LODZ_RIDE), camelRides);
    }

    @Test
    public void testGetCamelsRideByDestinationCity() {
        Mockito.when(camelTransportClient.getCamelRidesByCityDestionation(WARSAW.getName())).thenReturn(Collections.singletonList(LODZ_WARSAW_RIDE));

        List<CamelRide> camelRides = camelTransportService.getCamelRidesByCityDestionation(WARSAW.getName());

        Assert.assertEquals(Collections.singletonList(LODZ_WARSAW_RIDE), camelRides);
    }

    @Test
    public void testGetCamelsRideByDCamel() {
        Mockito.when(camelTransportClient.getAllCamelRides()).thenReturn(Arrays.asList(LODZ_WARSAW_RIDE, WARSAW_LODZ_RIDE));

        List<CamelRide> camelRides = camelTransportService.getCamelRidesByCamel(JAS_CAMEL.getId());

        Assert.assertEquals(Collections.singletonList(LODZ_WARSAW_RIDE), camelRides);
    }

    @Test
    public void testGetCamelsRideByFromCityAndCapacity() {
        Mockito.when(camelTransportClient.getCamelRidesByFromCity(WARSAW.getName())).thenReturn(Arrays.asList(WARSAW_LODZ_RIDE_2, WARSAW_LODZ_RIDE));

        List<CamelRide> camelRides = camelTransportService.getCamelRidesByFromCityAndCamelCapacity(WARSAW.getName(), JAS_CAMEL.getCapacity() - 10.0f);

        Assert.assertEquals(Collections.singletonList(WARSAW_LODZ_RIDE_2), camelRides);
    }

    @Test
    public void testGetCitiesWhenCamelHadBeen() {
        Mockito.when(camelTransportClient.getAllCamelRides()).thenReturn(Arrays.asList(WARSAW_LODZ_RIDE_2, WARSAW_LODZ_RIDE));

        List<City> cities = camelTransportService.getCitiesWhereCamelHadBeen(JAS_CAMEL.getId());

        Assert.assertEquals(Arrays.asList(WARSAW, LODZ), cities);
    }

    @Test
    public void testGetCitiesByCountryName() {
        Mockito.when(camelTransportClient.getCitiesByCountryName(WARSAW.getCountry())).thenReturn(Arrays.asList(LODZ, WARSAW));

        List<City> cities = camelTransportService.getCitiesByCountryName(WARSAW.getCountry());

        Assert.assertEquals(Arrays.asList(LODZ, WARSAW), cities);
    }

    @Test
    public void testGetCamelsOlderThan() {
        Mockito.when(camelTransportClient.getAllCamels()).thenReturn(Arrays.asList(JAS_CAMEL, MALGOSIA_CAMEL));

        List<Camel> camels = camelTransportService.getCamelsOlderThan(10);

        Assert.assertEquals(Collections.singletonList(JAS_CAMEL), camels);
    }

    @Test
    public void testGetCamelsYoungerThan() {
        Mockito.when(camelTransportClient.getAllCamels()).thenReturn(Arrays.asList(JAS_CAMEL, MALGOSIA_CAMEL));

        List<Camel> camels = camelTransportService.getCamelsYoungerThan(10);

        Assert.assertEquals(Collections.singletonList(MALGOSIA_CAMEL), camels);
    }

    @Test
    public void testGetCamelsByGender() {
        Mockito.when(camelTransportClient.getCamelsByGender(Gender.FEMALE.name())).thenReturn(Collections.singletonList(MALGOSIA_CAMEL));

        List<Camel> camels = camelTransportService.getCamelsByGender(Gender.FEMALE.name());

        Assert.assertEquals(Collections.singletonList(MALGOSIA_CAMEL), camels);
    }



    @Test
    public void testGetCamelsRideByDepartureDate() {
        Date departureDate = Date.from(Timestamp.valueOf("2018-10-01 01:00:00").toInstant());

        Mockito.when(camelTransportClient.getAllCamelRides()).thenReturn(Arrays.asList(LODZ_WARSAW_RIDE, WARSAW_LODZ_RIDE, WARSAW_LODZ_RIDE_2));

        List<CamelRide> camelRides = camelTransportService.getCamelRidesByDepartureDate(departureDate);

        Assert.assertEquals(Collections.singletonList(WARSAW_LODZ_RIDE_2), camelRides);
    }



    @Test
    public void testGetCamelsRideByDateRange() {
        Date departureDate = Date.from(Timestamp.valueOf("2018-10-08 01:00:00").toInstant());
        Date arrivalDate = Date.from(Timestamp.valueOf("2018-10-11 04:00:00").toInstant());

        Mockito.when(camelTransportClient.getAllCamelRides()).thenReturn(Arrays.asList(LODZ_WARSAW_RIDE, WARSAW_LODZ_RIDE, WARSAW_LODZ_RIDE_2));

        List<CamelRide> camelRides = camelTransportService.getCamelRidesByDepartureDateRange(departureDate, arrivalDate);

        Assert.assertEquals(Collections.singletonList(LODZ_WARSAW_RIDE), camelRides);
    }


    @Test
    public void testGetCamelWorkingDuration() {
        Duration expectedDuration = Duration.ofHours(76);

        Mockito.when(camelTransportClient.getAllCamelRides()).thenReturn(Arrays.asList(LODZ_WARSAW_RIDE, WARSAW_LODZ_RIDE, WARSAW_LODZ_RIDE_2));

        Duration duration = camelTransportService.getCamelWorkingDuration(JAS_CAMEL.getId());

        Assert.assertEquals(expectedDuration, duration);
    }


}
