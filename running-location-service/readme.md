### domain
- ```Location```
  - create database
- ```LocationRepository (interface)```
  - create rest repository
- ```LocationRestRepository (interface)```
  - create rest repository
  - find the page in database of the content
- ```MedicalInfo```
  - medical info
- ```UnitInfo```
  - unit info
### rest
- ```RunningBulkUploadController```
  - map the request for rest api
  - ("running") post the locations
  - ("/purge") delete the locations data
  - ("/running/{movementType}") find the data by movementType
### service
- impl
  - ```LocationServiceImpl```
    - implements the methods in ```LocationService```
  - ```LocationService (interface)```
    - create the methods
- ```RunningLocationServiceApplication```
  - run the application


