package at.meks.axon.sensors.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import org.axonframework.modelling.command.EntityId;

import java.util.Objects;

@Value
@Accessors(fluent = true)
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Measurement {

    @EntityId
    MeasurementId measurementId;
    Unit unit;
    MeasuredValue value;

}
