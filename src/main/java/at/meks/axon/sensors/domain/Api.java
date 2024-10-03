package at.meks.axon.sensors.domain;

import at.meks.axon.sensors.domain.model.ClientId;
import at.meks.axon.sensors.domain.model.MeasuredValue;
import at.meks.axon.sensors.domain.model.MeasurementId;
import at.meks.axon.sensors.domain.model.SensorId;
import at.meks.axon.sensors.domain.model.Unit;
import io.quarkiverse.axonframework.extension.runtime.Command;
import io.quarkiverse.axonframework.extension.runtime.Event;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public interface Api {

    interface Commands {

        @Command
        record AddManyMeasurementsCommand(@TargetAggregateIdentifier ClientId clientId,
                                          SensorId sensorId, MeasurementOfSensor[] measurements) {

        }

        @Command
        record AddMeasurementCommand(@TargetAggregateIdentifier ClientId clientId, SensorId sensorId,
                                     MeasurementId measurementId, Unit unit,
                                     MeasuredValue measuredValue) {

        }

        @Command
        public static record AddSensorCommand(@TargetAggregateIdentifier ClientId clientId, SensorId sensorId) {

        }

        @Command
        public static record ClientRegistrationCommand(@TargetAggregateIdentifier ClientId clientId) {

        }

        public static record MeasurementOfSensor(MeasurementId measurementId, Unit unit,
                                                 MeasuredValue measuredValue) {

        }
    }

    class Events {

        @Event
        public static record ClientRegisteredEvent(ClientId clientId) {

        }

        @Event
        public static record SensorAddedEvent(SensorId sensorId) {

        }

        @Event
        public static record ValueMeasuredEvent(SensorId sensorId, MeasurementId measurementId, Unit unit,
                                                MeasuredValue measuredValue) {

        }
    }
}
