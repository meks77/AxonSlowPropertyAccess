package at.meks.axon.sensors.domain.model;

import at.meks.axon.sensors.domain.commands.AddMeasurementCommand;
import at.meks.axon.sensors.domain.events.ClientRegisteredEvent;
import at.meks.axon.sensors.domain.events.SensorAddedEvent;
import at.meks.axon.sensors.domain.events.ValueMeasuredEvent;
import org.assertj.core.api.Assertions;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.axonframework.test.aggregate.StubAggregateLifecycleExtension;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

class ClientTest {

    private final FixtureConfiguration<Client> fixture = new AggregateTestFixture<>(Client.class);
    private final Logger logger = LoggerFactory.getLogger(ClientTest.class);

    @Test
    void testMeasurementComand() {
        logger.info("Start test");
        ClientId clientId = new ClientId(UUID.randomUUID().toString());
        SensorId sensorId = new SensorId(UUID.randomUUID().toString());

        MeasurementId measurementId = new MeasurementId(UUID.randomUUID().toString());
        var expectedMeasurement = new Measurement(measurementId, new Unit("°C"), new MeasuredValue(24.6));
        fixture.given(new ClientRegisteredEvent(clientId), new SensorAddedEvent(sensorId))
               .when(new AddMeasurementCommand(clientId, sensorId, expectedMeasurement.measurementId(), new Unit("°C"), new MeasuredValue(24.6)))
               .expectEvents(new ValueMeasuredEvent(sensorId, expectedMeasurement.measurementId(), expectedMeasurement.unit(), expectedMeasurement.value()))
               .expectState(client -> Assertions.assertThat(client.sensors().findFirst().orElseThrow().measurements()).containsExactly(expectedMeasurement));
        ;

    }

}