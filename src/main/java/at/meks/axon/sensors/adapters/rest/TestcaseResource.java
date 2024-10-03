package at.meks.axon.sensors.adapters.rest;

import at.meks.axon.sensors.domain.Api;
import at.meks.axon.sensors.domain.model.ClientId;
import at.meks.axon.sensors.domain.model.MeasuredValue;
import at.meks.axon.sensors.domain.model.MeasurementId;
import at.meks.axon.sensors.domain.model.SensorId;
import at.meks.axon.sensors.domain.model.Unit;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.jboss.resteasy.reactive.RestQuery;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Path("/startTestCase")
public class TestcaseResource {

    @Inject
    CommandGateway commandGateway;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void startTestcase(@RestQuery int numberOfClients, @RestQuery int numberOfSensorsPerClient,
                              @RestQuery int numberOfMeasurementsPerSensor) throws InterruptedException {
        var random = new Random();
        int numberOfClientsToCreate = numberOfClients <= 0 ? 1 : numberOfClients;
        int numberOfSensors = numberOfSensorsPerClient <= 0 ? 10 : numberOfSensorsPerClient;
        int numberOfMeasurements = numberOfMeasurementsPerSensor <= 0 ? 1 : numberOfMeasurementsPerSensor;
        try (var executorService = Executors.newFixedThreadPool(30, Thread.ofVirtual().factory())) {
            Log.info("starting testcase");
            for (int i = 0; i < numberOfClientsToCreate; i++) {
                final int index = i;
                executorService.submit(() -> {
                    ClientId clientId = createClient();
                    Set<SensorId> sensorIds = createSensors(numberOfSensors, clientId);
                    createMeasurementsPerSensor(numberOfMeasurements, sensorIds, clientId, random);
                    if (index % 99 == 0) {
                        Log.infof("started testcase for %s clients", index + 1);
                    }
                });
            }
            Log.info("all executors created");
            executorService.shutdown();
            boolean terminated = executorService.awaitTermination(600, TimeUnit.SECONDS);
            if (terminated) {
                Log.info("Testcase finished");
            } else {
                Log.error("Waiting for testcase timed out");
            }
        }
    }

    private ClientId createClient() {
        Log.debug("Creating client");
        ClientId clientId = new ClientId(UUID.randomUUID().toString());
        commandGateway.sendAndWait(new Api.Commands.ClientRegistrationCommand(clientId));
        return clientId;
    }

    private Set<SensorId> createSensors(int numberOfSensors, ClientId clientId) {
        Log.debugf("Creating %s sensors", numberOfSensors);
        Set<SensorId> sensorIds = newSensorIds(numberOfSensors);
        sensorIds.forEach(id -> commandGateway.sendAndWait(new Api.Commands.AddSensorCommand(clientId, id)));
        return sensorIds;
    }

    private void createMeasurementsPerSensor(int numberOfMeasurements, Set<SensorId> sensorIds, ClientId clientId,
                                             Random random) {
        Log.debugf("Creating %s measurements per sensor, for %s sensors", numberOfMeasurements, sensorIds.size());
        try (var executor = Executors.newVirtualThreadPerTaskExecutor())  {
            sensorIds
                    .forEach(id -> {
                        Api.Commands.MeasurementOfSensor[] measurments = IntStream.rangeClosed(0, numberOfMeasurements).boxed()
                                                                                  .map(i -> new Api.Commands.MeasurementOfSensor(newMeasurementId(),
                                                                                                                                 new Unit("°C"),
                                                                                                                                 randomValue(random)))
                                                                                  .toArray(Api.Commands.MeasurementOfSensor[]::new);
                        executor.submit(() ->
                        commandGateway.sendAndWait(new Api.Commands.AddManyMeasurementsCommand(clientId, id, measurments)));
                    });
        }

    }


    private Set<SensorId> newSensorIds(int numberOfSensors) {
        return IntStream.range(0, numberOfSensors).boxed().map(i -> UUID.randomUUID().toString()).map(SensorId::new)
                        .collect(Collectors.toSet());
    }

    private MeasuredValue randomValue(Random random) {
        return new MeasuredValue(
                random.nextDouble(0.5));
    }

    private MeasurementId newMeasurementId() {
        return new MeasurementId(UUID.randomUUID().toString());
    }
}
