package demo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.Description;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

// Spring Data REST automatically generates the base path for the repository as "/supplyLocations"
@RepositoryRestResource(collectionResourceRel = "locations")
public interface SupplyLocationRepository extends PagingAndSortingRepository<SupplyLocation, Long> {
// public interface SupplyLocationRepository extends MongoRepository<SupplyLocation, Long> {
    @RestResource(rel = "by-location", description = @Description("Find by location, comma separated, e.g. 'lat,long', and distance, e.g. '50km'"))
    SupplyLocation findFirstByLocationNear(@Param("location") Point location);

    // /city?city=xxxxx
    @RestResource(path = "zip", rel = "by-zip")
    // Page<SupplyLocation> findByZip(@Param("zip") String zip, Pageable pageable);
    List<SupplyLocation> findByZip(@Param("zip") String zip);

}
