package at.meks.axon.sensors.domain.model;

import at.meks.axon.sensors.domain.events.ValueMeasuredEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.modelling.command.EntityId;
import org.axonframework.modelling.command.ForwardMatchingInstances;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Sensor {

    @EntityId
    @JsonProperty
    private final SensorId sensorId;

    @AggregateMember(eventForwardingMode = ForwardMatchingInstances.class)
    private List<Measurement> measurements = new LinkedList<>();

    public Sensor(SensorId sensorId) {
        this.sensorId = sensorId;
    }

    @EventSourcingHandler
    protected void on(ValueMeasuredEvent event) {
        measurements.add(new Measurement(event.measurementId(), event.unit(), event.measuredValue()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sensor sensor = (Sensor) o;
        return Objects.equals(sensorId, sensor.sensorId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sensorId);
    }

    @JsonProperty
    public Stream<Measurement> measurements() {
        return measurements.stream();
    }
}
