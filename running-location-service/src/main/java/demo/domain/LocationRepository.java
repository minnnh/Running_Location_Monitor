package demo.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "locations", collectionResourceRel = "locations")
public interface LocationRepository extends PagingAndSortingRepository<Location, Long> {
    @RestResource(rel = "by-service-type")
    Page<Location> findByServiceType(@Param("type") String type, Pageable pageable);

    // /location/runningId?runningId=xxxxx
    @RestResource(path = "runningId", rel = "by-runningId")
    Page<Location> findByUnitInfoRunningId(@Param("runningId") String runningId, Pageable pageable);

    Page<Location> findByRunnerMovementType(@Param("movementType") Location.RunnerMovementType movementType, Pageable pageable);
}
