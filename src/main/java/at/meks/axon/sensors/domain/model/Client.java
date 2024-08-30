package at.meks.axon.sensors.domain.model;


import at.meks.axon.sensors.domain.commands.AddMeasurementCommand;
import at.meks.axon.sensors.domain.commands.AddSensorCommand;
import at.meks.axon.sensors.domain.commands.ClientRegistrationCommand;
import at.meks.axon.sensors.domain.events.ClientRegisteredEvent;
import at.meks.axon.sensors.domain.events.SensorAddedEvent;
import at.meks.axon.sensors.domain.events.ValueMeasuredEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.modelling.command.AggregateMember;
import org.axonframework.modelling.command.ForwardMatchingInstances;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class Client {

    @AggregateIdentifier
    private ClientId id;

    @AggregateMember(eventForwardingMode = ForwardMatchingInstances.class)
    private List<Sensor> sensors = new LinkedList<>();

    @CommandHandler
    public Client(ClientRegistrationCommand command) {
        AggregateLifecycle.apply(new ClientRegisteredEvent(command.clientId()));
    }

    protected Client() {
        // needed by axon
    }

    @EventSourcingHandler
    protected void on(ClientRegisteredEvent event) {
        this.id = event.clientId();
    }

    @CommandHandler
    public void addSensor(AddSensorCommand command) {
        AggregateLifecycle.apply(new SensorAddedEvent(command.sensorId()));
    }

    @EventSourcingHandler
    protected void on(SensorAddedEvent event) {
        sensors.add(new Sensor(event.sensorId()));
    }

    @CommandHandler
    public void addMeassurement(AddMeasurementCommand command) {
        AggregateLifecycle.apply(new ValueMeasuredEvent(command.sensorId(), command.measurementId(), command.unit(), command.measuredValue()));
    }

    public Stream<Sensor> sensors() {
        return sensors.stream();
    }

    public ClientId clientId() {
        return id;
    }
}
