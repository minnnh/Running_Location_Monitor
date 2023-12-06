package demo.service;

import demo.model.DirectionInput;
import demo.model.Point;
import demo.model.SimulatorFixture;
import demo.model.SupplyLocation;

import java.util.List;

public interface PathService {

    List<DirectionInput> loadDirectionInput();
    SimulatorFixture loadSimulatorFixture();

    List<Point> getCoordinatesFromGoogle(DirectionInput directionInput);

    String getCoordinatesFromGoogleAsPolyline(DirectionInput directionInput);

    List<SupplyLocation> getSupplyLocations();
}
