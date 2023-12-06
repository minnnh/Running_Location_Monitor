package demo.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CurrentPosition {

    private String runningId;
    private Point location;
    private RunnerStatus runningStatus = RunnerStatus.NONE;
    private Double speed;
    private Double heading;

    private FaultCode faultCode;

}
