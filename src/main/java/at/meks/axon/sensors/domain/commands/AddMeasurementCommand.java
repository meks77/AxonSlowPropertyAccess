package at.meks.axon.sensors.domain.commands;

import at.meks.axon.sensors.domain.model.ClientId;
import at.meks.axon.sensors.domain.model.MeasuredValue;
import at.meks.axon.sensors.domain.model.MeasurementId;
import at.meks.axon.sensors.domain.model.SensorId;
import at.meks.axon.sensors.domain.model.Unit;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@Accessors(fluent = true)
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AddMeasurementCommand {

    @NonNull
    @TargetAggregateIdentifier
    ClientId clientId;

    @NonNull
    SensorId sensorId;

    @NonNull
    MeasurementId measurementId;

    @NonNull
    Unit unit;

    @NonNull
    MeasuredValue measuredValue;

}
