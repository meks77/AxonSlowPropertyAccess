package at.meks.axon.sensors.domain.commands;

import at.meks.axon.sensors.domain.model.ClientId;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record ClientRegistrationCommand(@TargetAggregateIdentifier ClientId clientId) {

}
