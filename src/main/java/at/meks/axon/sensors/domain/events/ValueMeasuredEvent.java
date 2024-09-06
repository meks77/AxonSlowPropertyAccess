package at.meks.axon.sensors.domain.events;

import at.meks.axon.sensors.domain.model.MeasuredValue;
import at.meks.axon.sensors.domain.model.MeasurementId;
import at.meks.axon.sensors.domain.model.SensorId;
import at.meks.axon.sensors.domain.model.Unit;

public record ValueMeasuredEvent(SensorId sensorId, MeasurementId measurementId, Unit unit,
                                 MeasuredValue measuredValue) {

}
