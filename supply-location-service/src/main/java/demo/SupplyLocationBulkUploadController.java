package demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SupplyLocationBulkUploadController {

    private SupplyLocationService service;


    @Autowired
    public SupplyLocationBulkUploadController(SupplyLocationService service) {
        this.service = service;
    }

    @RequestMapping(value = "/bulk/supplyLocations", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void upload(@RequestBody List<SupplyLocation> locations) {
        service.saveSupplyLocations(locations);
    }

    @RequestMapping(value = "/purge", method = RequestMethod.DELETE)
    public void purge() {
        this.service.deleteAll();
    }

    public List<SupplyLocation> uploadFitleredLocations(List<SupplyLocation> locations) {
        return this.service.saveSupplyLocationsZipContains504(locations);
    }

}
