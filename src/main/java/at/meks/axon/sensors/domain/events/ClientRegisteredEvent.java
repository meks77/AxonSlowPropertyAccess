package at.meks.axon.sensors.domain.events;

import at.meks.axon.sensors.domain.model.ClientId;

public record ClientRegisteredEvent(ClientId clientId) {

}
