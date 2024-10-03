package at.meks.axon.sensors.domain.model;


import at.meks.axon.sensors.domain.Api;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.modelling.command.ForwardMatchingInstances;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Client {

    @AggregateIdentifier
    private ClientId id;

    @AggregateMember(eventForwardingMode = ForwardMatchingInstances.class)
    private List<Sensor> sensors = new LinkedList<>();

    @CommandHandler
    public Client(Api.Commands.ClientRegistrationCommand command) {
        AggregateLifecycle.apply(new Api.Events.ClientRegisteredEvent(command.clientId()));
    }

    protected Client() {
        // needed by axon
    }

    @EventSourcingHandler
    protected void on(Api.Events.ClientRegisteredEvent event) {
        this.id = event.clientId();
    }

    @CommandHandler
    public void addSensor(Api.Commands.AddSensorCommand command) {
        AggregateLifecycle.apply(new Api.Events.SensorAddedEvent(command.sensorId()));
    }

    @EventSourcingHandler
    protected void on(Api.Events.SensorAddedEvent event) {
        sensors.add(new Sensor(event.sensorId()));
    }

    @CommandHandler
    public void addMeassurement(Api.Commands.AddMeasurementCommand command) {
        AggregateLifecycle.apply(new Api.Events.ValueMeasuredEvent(command.sensorId(),
                                                                   command.measurementId(),
                                                                   command.unit(),
                                                                   command.measuredValue()));
    }

    @CommandHandler
    public void addMeasurements(Api.Commands.AddManyMeasurementsCommand command) {
        Arrays.stream(command.measurements())
              .map(m -> new Api.Events.ValueMeasuredEvent(command.sensorId(), m.measurementId(), m.unit(), m.measuredValue()))
              .forEach(AggregateLifecycle::apply);
    }

    @JsonProperty
    public Stream<Sensor> sensors() {
        return sensors.stream();
    }

    @JsonProperty
    public ClientId clientId() {
        return id;
    }
}
