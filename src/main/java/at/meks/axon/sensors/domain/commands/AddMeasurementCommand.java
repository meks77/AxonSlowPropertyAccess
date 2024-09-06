package at.meks.axon.sensors.domain.commands;

import at.meks.axon.sensors.domain.model.ClientId;
import at.meks.axon.sensors.domain.model.MeasuredValue;
import at.meks.axon.sensors.domain.model.MeasurementId;
import at.meks.axon.sensors.domain.model.SensorId;
import at.meks.axon.sensors.domain.model.Unit;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record AddMeasurementCommand(@TargetAggregateIdentifier ClientId clientId, SensorId sensorId,
                                    MeasurementId measurementId, Unit unit,
                                    MeasuredValue measuredValue) {

}
