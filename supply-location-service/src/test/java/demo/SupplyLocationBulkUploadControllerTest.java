package demo;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SupplyLocationBulkUploadControllerTest {

    private SupplyLocationRepository repository;
    private SupplyLocationService service;
    private SupplyLocationBulkUploadController controller;
    private List<SupplyLocation> inputLocations;

    @BeforeAll
    public void setupMock() {
        repository = mock(SupplyLocationRepository.class);
        service = mock(SupplyLocationService.class);
        controller = new SupplyLocationBulkUploadController(service);
        inputLocations = new ArrayList<>();
        inputLocations.add(generateSupplyLocations(4, 4, "504"));
        inputLocations.add(generateSupplyLocations(5, 5, "505"));
        inputLocations.add(generateSupplyLocations(6, 6, "506"));
    }

    @Test
    public void whenListFiltered_returnSavedList() {
        List<SupplyLocation> locations = new ArrayList<>();
        locations.add(generateSupplyLocations(4, 4, "504"));
        when(service.saveSupplyLocationsZipContains504(inputLocations)).thenReturn(locations);

        assertThat(controller.uploadFitleredLocations(inputLocations)).size().isEqualTo(1);
        assertThat(controller.uploadFitleredLocations(inputLocations).get(0).getZip()).isEqualTo("504");
    }

    @Test
    public void saveSupplyLocationsTest() {
        List<SupplyLocation> locations = new ArrayList<>();
        locations.add(generateSupplyLocations(1d, 1d, "012"));

        when(repository.saveAll(locations)).thenReturn(locations);
        when(repository.findByZip("012")).thenReturn(locations);

        controller.upload(locations);

        assertThat(repository.findByZip("012").size()).isEqualTo(1);
        assertThat(repository.findByZip("012").get(0).getZip()).isEqualTo("012");

    }


    private SupplyLocation generateSupplyLocations(double latitude, double longitude, String zip) {
        SupplyLocation location = new SupplyLocation(latitude, longitude);
        location.setZip(zip);
        return location;
    }
}
