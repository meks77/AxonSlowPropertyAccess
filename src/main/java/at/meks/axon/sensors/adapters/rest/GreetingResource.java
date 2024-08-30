package at.meks.axon.sensors.adapters.rest;

import at.meks.axon.sensors.domain.commands.AddMeasurementCommand;
import at.meks.axon.sensors.domain.commands.AddSensorCommand;
import at.meks.axon.sensors.domain.commands.ClientRegistrationCommand;
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
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Path("/startTestCase")
public class GreetingResource {

    @Inject
    CommandGateway commandGateway;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public void startTestcase(@RestQuery int numberOfClients, @RestQuery int numberOfSensorsPerClient,
                              @RestQuery int numberOfMeasurementsPerSensor) {
        var random = new Random();
        int numberOfClientsToCreate = numberOfClients <= 0 ? 1 : numberOfClients;
        int numberOfSensors = numberOfSensorsPerClient <= 0 ? 10 : numberOfSensorsPerClient;
        int numberOfMeasurements = numberOfMeasurementsPerSensor <= 0 ? 1 : numberOfMeasurementsPerSensor;
        try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < numberOfClientsToCreate; i++) {
                executorService.submit(() -> {
                    ClientId clientId = createClient();
                    Set<SensorId> sensorIds = createSensors(numberOfSensors, clientId);
                    createMeasurementsPerSensor(numberOfMeasurements, sensorIds, clientId, random);
                });
            }
        }
    }

    private ClientId createClient() {
        Log.info("Creating client");
        ClientId clientId = new ClientId(UUID.randomUUID().toString());
        commandGateway.sendAndWait(new ClientRegistrationCommand(clientId));
        return clientId;
    }

    private Set<SensorId> createSensors(int numberOfSensors, ClientId clientId) {
        Log.infof("Creating %s sensors", numberOfSensors);
        Set<SensorId> sensorIds = newSensorIds(numberOfSensors);
        sensorIds.forEach(id -> commandGateway.sendAndWait(new AddSensorCommand(clientId, id)));
        return sensorIds;
    }

    private void createMeasurementsPerSensor(int numberOfMeasurements, Set<SensorId> sensorIds, ClientId clientId,
                                             Random random) {
        Log.infof("Creating %s measurements per sensor, for %s sensors", numberOfMeasurements, sensorIds.size());
        sensorIds.parallelStream()
                 .forEach(id -> IntStream.rangeClosed(0, numberOfMeasurements).boxed().parallel()
                                         .forEach(i -> commandGateway.sendAndWait(
                                                 new AddMeasurementCommand(clientId,
                                                                           id,
                                                                           newMeasurementId(),
                                                                           new Unit("°C"),
                                                                           randomValue(random)))));
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
