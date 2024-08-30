package at.meks.axon.sensors.domain.commands;

import at.meks.axon.sensors.domain.model.ClientId;
import at.meks.axon.sensors.domain.model.SensorId;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.Accessors;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Objects;

@Value
@Accessors(fluent = true)
@NoArgsConstructor(force = true, access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class AddSensorCommand {

    @NonNull
    @TargetAggregateIdentifier
    ClientId clientId;

    @NonNull
    SensorId sensorId;

}
