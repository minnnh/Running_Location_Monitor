package demo;

import demo.domain.Location;
import demo.domain.LocationRepository;
import demo.domain.UnitInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class LocationRepositoryTest {

    @Autowired
    LocationRepository repository;

    @Test
    public void saveLocation() {
        this.repository.save(new Location(new UnitInfo("test-runningId-1")));
        assertThat(this.repository.findByUnitInfoRunningId("test-runningId-1", PageRequest.of(0, 1)).getContent()
                .get(0).getRunningId()).isEqualTo("test-runningId-1");

    }

    @Test
    public void queryByUnitInfoRunningId() {
        final String runningId = "test-runningId-2";
        this.repository.save(new Location(new UnitInfo(runningId)));
        assertTrue(this.repository.findByUnitInfoRunningId(runningId, PageRequest.of(0, 10)).getTotalElements() == 1);
        assertTrue(this.repository.findByUnitInfoRunningId(runningId, PageRequest.of(0, 10)).getContent().get(0)
                .getRunningId().equals(runningId));
    }
}
