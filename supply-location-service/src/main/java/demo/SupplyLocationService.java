package demo;

import java.util.List;

public interface SupplyLocationService {
    List<SupplyLocation> saveSupplyLocations(List<SupplyLocation> supplyLocations);

    void deleteAll();

    List<SupplyLocation> saveSupplyLocationsZipContains504(List<SupplyLocation> supplyLocationslocations);

    // Page<SupplyLocation> findByCity(String city, Pageable pageable);
}
