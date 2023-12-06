package demo.rest;

import demo.model.*;
import demo.service.GpsSimulatorFactory;
import demo.service.PathService;
import demo.support.FaultCodeUtils;
import demo.task.GpsSimulator;
import demo.task.GpsSimulatorInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api")
public class LocationSimulatorRestApi {

    @Autowired
    private PathService pathService;

    @Autowired
    private GpsSimulatorFactory gpsSimulatorFactory;

    @Autowired
    private AsyncTaskExecutor taskExecutor;

    private Map<Long, GpsSimulatorInstance> taskFutures = new HashMap<>();

    @RequestMapping("/dc")
    public List<GpsSimulatorInstance> dc(HttpServletRequest request) {
        final SimulatorFixture fixture = this.pathService.loadSimulatorFixture();

        final List<GpsSimulatorInstance> instances = new ArrayList<>();
        final List<Point> lookAtPoints = new ArrayList<>();

        final Set<Long> instanceIds = new HashSet<>(taskFutures.keySet());

        for (GpsSimulatorRequest gpsSimulatorRequest : fixture.getGpsSimulatorRequests()) {

            final GpsSimulator gpsSimulator = gpsSimulatorFactory.prepareGpsSimulator(gpsSimulatorRequest);
            lookAtPoints.add(gpsSimulator.getStartPoint());
            instanceIds.add(gpsSimulator.getId());

            final Future<?> future = taskExecutor.submit(gpsSimulator);
            final GpsSimulatorInstance instance = new GpsSimulatorInstance(gpsSimulator.getId(), gpsSimulator, future);
            taskFutures.put(gpsSimulator.getId(), instance);
            instances.add(instance);
        }
        return instances;
    }

    @RequestMapping("/status")
    public Collection<GpsSimulatorInstance> status() {
        return taskFutures.values();
    }

    @RequestMapping("/cancel")
    public int cancel() {
        int numberOfCancelledTasks = 0;
        for (Map.Entry<Long, GpsSimulatorInstance> entry : taskFutures.entrySet()) {
            GpsSimulatorInstance instance = entry.getValue();
            instance.getGpsSimulator().cancel();
            /*
            This check is useful because not all tasks can be cancelled successfully.
            If the task has already completed, was cancelled before,
            or cannot be cancelled for some other reason,
            wasCancelled will be false.
            */
            boolean wasCancelled = instance.getGpsSimulatorTask().cancel(true);
            if (wasCancelled) {
                numberOfCancelledTasks++;
            }
        }
        taskFutures.clear();
        return numberOfCancelledTasks;
    }

    @RequestMapping("/directions")
    public List<DirectionInput> directions() {
        return pathService.loadDirectionInput();
    }

    @RequestMapping("/supply-locations")
    public List<SupplyLocation> supplyLocations() {
        return pathService.getSupplyLocations();
    }

    @RequestMapping("/fixture")
    public SimulatorFixture fixture() {

        final List<DirectionInput> directions = this.pathService.loadDirectionInput();
        final SimulatorFixture fixture = new SimulatorFixture();

        for (DirectionInput directionInput : directions) {

            final GpsSimulatorRequest gpsSimulatorRequest = new GpsSimulatorRequest();
            gpsSimulatorRequest.setExportPositionsToKml(true);
            gpsSimulatorRequest.setExportPositionsToMessaging(true);
            gpsSimulatorRequest.setMove(true);

            String polyline = this.pathService.getCoordinatesFromGoogleAsPolyline(directionInput);
            gpsSimulatorRequest.setPolyline(polyline);
            gpsSimulatorRequest.setReportInterval(1000);
            gpsSimulatorRequest.setSpeedInKph(50d);
            gpsSimulatorRequest.setExportPositionsToMessaging(true);
            gpsSimulatorRequest.setSecondsToError(60);
            gpsSimulatorRequest.setRunnerStatus(RunnerStatus.NONE);
            //@Nike Running
            gpsSimulatorRequest.setFaultCode(FaultCodeUtils.getRandomFaultCode());
            fixture.getGpsSimulatorRequests().add(gpsSimulatorRequest);
        }

        return fixture;
    }

    /***
     * TESTING HERE
     * CHECK CONNECTION TO DISTRIBUTOR
     */
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/sendData")
    public String sendDataToServiceB(@RequestBody TestData testData) {
        String serviceBUrl = "http://localhost:9006/api/locations/receive";
        // restTemplate.postForObject(serviceBUrl, testData, String.class);
        this.restTemplate.postForLocation(serviceBUrl, testData);
        return "Data sent to Service Distribution";
    }
}
