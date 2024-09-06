package at.meks.axon.sensors.domain.commands;

import at.meks.axon.sensors.domain.model.MeasuredValue;
import at.meks.axon.sensors.domain.model.MeasurementId;
import at.meks.axon.sensors.domain.model.Unit;

public record MeasurementOfSensor(MeasurementId measurementId, Unit unit,
                                  MeasuredValue measuredValue) {

}
