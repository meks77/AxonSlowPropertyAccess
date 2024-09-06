package at.meks.axon.sensors.infrastructure.axon;

import at.meks.axon.sensors.domain.events.ValueMeasuredEvent;
import at.meks.axon.sensors.domain.model.MeasuredValue;
import at.meks.axon.sensors.domain.model.MeasurementId;
import at.meks.axon.sensors.domain.model.SensorId;
import at.meks.axon.sensors.domain.model.Unit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

class ObjectSerializerTest {

    private final ObjectSerializer serializer = new ObjectSerializer();
    private final Random random = new Random();
    private final Logger logger = LoggerFactory.getLogger(ObjectSerializerTest.class);

    @Test
    void serializeToJson() {
        measureTime(() -> serializer.serializeToJson(new ValueMeasuredEvent(new SensorId(UUID.randomUUID().toString()),
                                                              new MeasurementId(UUID.randomUUID().toString()),
                                                              new Unit("metre"), new MeasuredValue(random.nextDouble() * 100.0))),
                    10000);
    }

    @Test
    void serializeToXml() {
        measureTime(() ->
            serializer.serializeToXml(new ValueMeasuredEvent(new SensorId(UUID.randomUUID().toString()),
                                                              new MeasurementId(UUID.randomUUID().toString()),
                                                              new Unit("metre"), new MeasuredValue(random.nextDouble() * 100.0))),
                    10000);
    }

    private void measureTime(Supplier<String> runnable, int numberOfSerializations) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < numberOfSerializations; i++) {
            Assertions.assertThat(runnable.get()).isNotBlank().hasSizeGreaterThan(20);
        }
        long end = System.currentTimeMillis();
        logger.info("Serialization time: {} ms", (end - start));
        logger.info("Serializations/s: {} ", numberOfSerializations / (double)(end - start) * 1000.0);
    }
}