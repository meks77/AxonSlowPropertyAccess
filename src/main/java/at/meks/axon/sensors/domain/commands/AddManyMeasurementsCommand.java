package at.meks.axon.sensors.domain.commands;

import at.meks.axon.sensors.domain.model.ClientId;
import at.meks.axon.sensors.domain.model.SensorId;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record AddManyMeasurementsCommand(@TargetAggregateIdentifier ClientId clientId,
                                         SensorId sensorId, MeasurementOfSensor[] measurements) {

}
