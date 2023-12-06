### model
-[x] SupplyLocation

-[x] Point
-[x] DirectionInput
-[x] RunnerStatus
-[x] CurrentPosition
-[x] Leg
-[x] PositionInfo

-[x] GpsSimulatorRequest (for each location -> gpsSimulator)
-[x] SimulatorFixture (for the whole running -> fixture)

-[x] FaultCode

### support
- [x] FaultCodeUtils
- [x] NavUtils

### service
- [x] PositionService
  - processPositionInfo, send Position To Distribution Service
- [x] PathService
  - loadDirectionInput
  - loadSimulatorFixture
  - getCoordinatesFromGoogle (return List<Point>)
  - getCoordinatesFromGoogleAsPolyline (return String)
  - getSupplyLocations
- [x] GpsSimulatorFactory
  - prepareGpsSimulator with gpsSimulatorRequest
    - initialize the gpsSimulator
  - prepareGpsSimulator with gpsSimulator and List<Point>
    - Real-Time use the gpsSimulator and use points to create legs

### task
- [x] GpsSimulator
- [x] GpsSimulatorInstance

### rest ("/api)
#### simulate-local
- ("/dc") start GpsSimulator by using the default file
  - simulate the GpsSimulator using the default json file
- ("/status") get the status of the GpsSimulatorInstance task
  -  status of gpsSimulator tasks
- ("/cancel") cancel the GpsSimulatorInstance task
  - stop the gpsSimulator tasks
- ("/directions") load the default directions 
  - load the default directions
- #### simulate-with-api
- ("/supply-locations") return the supply locations
  - get the supply locations through api (by using the white house location)
- ("/fixture") get the fixture
  - with directions and simulators, return the fixture