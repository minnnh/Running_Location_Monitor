package demo.task;

import demo.model.*;
import demo.service.PositionService;
import demo.support.NavUtils;
import lombok.Getter;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class GpsSimulator implements Runnable {
    private long id;

    private PositionService positionInfoService;

    private AtomicBoolean cancel = new AtomicBoolean();

    @Getter
    private Double speedInMps; // In meters/sec
    private boolean shouldMove;
    private boolean exportPositionsToKml = false;
    private boolean exportPositionsToMessaging = true;

    private Integer reportInterval = 500; // millisecs at which to send position reports
    private PositionInfo positionInfo = null;
    private List<Leg> legs;
    private RunnerStatus runnerStatus = RunnerStatus.NONE;
    private String vin;

    private Integer secondsToError = 45;
    private Point startPoint;
    private Date executionStartTime;
    //@Nike Running
    private FaultCode faultCode;

    public GpsSimulator(GpsSimulatorRequest gpsSimulatorRequest) {
        this.shouldMove = gpsSimulatorRequest.isMove();
        this.exportPositionsToKml = gpsSimulatorRequest.isExportPositionsToKml();
        this.exportPositionsToMessaging = gpsSimulatorRequest.isExportPositionsToMessaging();
        this.setSpeedInKph(gpsSimulatorRequest.getSpeedInKph());
        this.reportInterval = gpsSimulatorRequest.getReportInterval();

        this.secondsToError = gpsSimulatorRequest.getSecondsToError();
        this.vin = gpsSimulatorRequest.getRunningId();
        this.runnerStatus = gpsSimulatorRequest.getRunnerStatus();
        //@Nike Running
        this.faultCode = gpsSimulatorRequest.getFaultCode();
    }

    @Override
    public void run() {
        try {
            executionStartTime = new Date();
            if (cancel.get()) {
                destroy();
                return;
            }
            while (!Thread.interrupted()) {
                long startTime = new Date().getTime();
                if (positionInfo != null) {
                    if (shouldMove) {
                        moveRunningLocation();
                        positionInfo.setSpeed(speedInMps);
                    } else {
                        positionInfo.setSpeed(0d);
                    }

                    if (this.secondsToError > 0 && startTime - executionStartTime
                            .getTime() >= this.secondsToError * 1000) {
                        this.runnerStatus = RunnerStatus.SERVICE_NOW;
                    }

                    positionInfo.setRunnerStatus(this.runnerStatus);

                    final FaultCode faultCodeToUse;

                    switch (this.runnerStatus) {
                        case SERVICE_INFO:
                        case SERVICE_SOON:
                        case STOP_NOW:
                            faultCodeToUse = this.faultCode;
                            break;
                        default:
                            faultCodeToUse = null;
                            break;
                    }

                    final CurrentPosition currentPosition = new CurrentPosition(
                            positionInfo.getRunningId(),
                            new Point(positionInfo.getPosition().getLatitude(), positionInfo.getPosition()
                                    .getLongitude()),
                            positionInfo.getRunnerStatus(),
                            positionInfo.getSpeed(),
                            positionInfo.getLeg().getHeading(),
                            faultCodeToUse
                    );
                    positionInfoService
                            .processPositionInfo(id, currentPosition, this.exportPositionsToKml, this.exportPositionsToMessaging);

                }

                // wait till next position report is due
                sleep(startTime);
            }
        } catch (InterruptedException ie) {
            destroy();
            return;
        }

        destroy();
    }

    /**
     * On thread interrupt. Send null position to all consumers to indicate that
     * sim has closed.
     */
    void destroy() {
        positionInfo = null;
    }

    // Sleep till next position report is due.
    private void sleep(long startTime) throws InterruptedException {
        long endTime = new Date().getTime();
        long elapsedTime = endTime - startTime;
        long sleepTime = reportInterval - elapsedTime > 0 ? reportInterval - elapsedTime : 0;
        Thread.sleep(sleepTime);
    }

    /**
     * Set new position of running location based on current position and running speed.
     */
    void moveRunningLocation() {
        Double distance = speedInMps * reportInterval / 1000.0;
        Double distanceFromStart = positionInfo.getDistanceFromStart() + distance;
        Double excess = 0.0; // amount by which next postion will exceed end
        // point of present leg

        for (int i = positionInfo.getLeg().getId(); i < legs.size(); i++) {
            Leg currentLeg = legs.get(i);
            excess = distanceFromStart > currentLeg.getLength() ? distanceFromStart - currentLeg.getLength() : 0.0;

            if (Double.doubleToRawLongBits(excess) == 0) {
                // this means new position falls within current leg
                positionInfo.setDistanceFromStart(distanceFromStart);
                positionInfo.setLeg(currentLeg);
                Point newPosition = NavUtils.getPosition(currentLeg.getStartPosition(), distanceFromStart,
                        currentLeg.getHeading());
                positionInfo.setPosition(newPosition);
                return;
            }
            distanceFromStart = excess;
        }

        // if we've reached here it means running location has moved beyond end of path
        // so go back to start of path
        setStartPosition();
    }

    /**
     * Position running location at start of path.
     */
    public void setStartPosition() {
        positionInfo = new PositionInfo();
        positionInfo.setRunningId(this.vin);
        Leg leg = legs.get(0);
        positionInfo.setLeg(leg);
        positionInfo.setPosition(leg.getStartPosition());
        positionInfo.setDistanceFromStart(0.0);
    }

    /**
     * @return the speed
     */
    public Double getSpeedInMps() {
        return speedInMps;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeedInMps(Double speed) {
        this.speedInMps = speed;
    }

    public void setSpeedInKph(Double speed) {
        this.speedInMps = speed / 3.6;
    }

    public Double getSpeedInKph() {
        return this.speedInMps * 3.6;
    }

    /**
     * @return the shouldMove
     */
    public Boolean getShouldMove() {
        return shouldMove;
    }

    /**
     * @param shouldMove the shouldMove to set
     */
    public void setShouldMove(Boolean shouldMove) {
        this.shouldMove = shouldMove;
    }

    public synchronized void cancel() {
        this.cancel.set(true);
    }

    public void setExportPositionsToKml(boolean exportPositionsToKml) {
        this.exportPositionsToKml = exportPositionsToKml;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public PositionInfo getCurrentPosition() {
        return positionInfo;
    }

    public void setCurrentPosition(PositionInfo currentPosition) {
        this.positionInfo = currentPosition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public RunnerStatus getRunnerStatus() {
        return runnerStatus;
    }

    public void setRunnerStatus(RunnerStatus runnerStatus) {
        this.runnerStatus = runnerStatus;
    }

    public Integer getSecondsToError() {
        return secondsToError;
    }

    public void setSecondsToError(Integer secondsToError) {
        this.secondsToError = secondsToError;
    }

    public void setPositionInfoService(PositionService positionInfoService) {
        this.positionInfoService = positionInfoService;
    }

    @Override
    public String toString() {
        return "GpsSimulator [id=" + id + ", speedInMps=" + speedInMps + ", shouldMove=" + shouldMove
                + ", exportPositionsToKml=" + exportPositionsToKml + ", exportPositionsToMessaging="
                + exportPositionsToMessaging + ", reportInterval=" + reportInterval + ", currentPosition="
                + positionInfo + "]";
    }
}
